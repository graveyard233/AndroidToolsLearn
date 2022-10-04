package com.lyd.tooltest.UI.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebViewClient;
import com.lyd.tooltest.Base.BaseFragment;
import com.lyd.tooltest.Entity.YingDi.Comment.CommentItem;
import com.lyd.tooltest.Entity.YingDi.Comment.CommentList;
import com.lyd.tooltest.Entity.YingDi.Comment.CommentsNode;
import com.lyd.tooltest.Entity.YingDi.Comment.CommentsNodeBuilder;
import com.lyd.tooltest.JsFile.MyJs;
import com.lyd.tooltest.R;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RxJavaFragment extends BaseFragment {

    private static final int GET_COMMENT_DELAY = 100;

    private AgentWeb webView;
    private LinearLayout linear;

    private int flag_html = 0;

    @Override
    protected void initViews() {
        linear = find(R.id.rxj_linearLayout);

        setWebView("https://iyingdi.com/tz/post/5221599");

    }

    private WebViewClient webViewClient = new WebViewClient(){

        @Override
        public void onLoadResource(WebView view, String url) {

            super.onLoadResource(view, url);
            view.evaluateJavascript(MyJs.removeFloatBar,
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                Log.e("TAG", "finish: "+ value);
                            }
                        });

        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            if (request.getMethod().equals("POST")){
                // TODO: 2022/9/25 这里获取时，有小概率会拿不到数据，需要反复异步请求
                if (request.getUrl().toString().startsWith("https://www.google")){
                    Log.i("TAG", "shouldInterceptRequest: " + request.getUrl().toString());
                    Message message = new Message();
                    message.what = GET_COMMENT_DELAY;
                    handler.sendMessage(message);

                }

            }


            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {


            super.onPageFinished(view, url);
//            view.loadUrl(MyJs.getHtml);//加载js
            //注入js，但没调用
            view.evaluateJavascript(MyJs.getHtml, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {

                }
            });

        }
    };

    private void setWebView(String url){
        webView = AgentWeb.with(this)
                .setAgentWebParent(linear,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()
                .setWebViewClient(webViewClient)
                .createAgentWeb()
                .ready()
                .go(url);

    }

    private void getComments(AgentWeb webView){
        webView.getJsAccessEntrace().quickCallJs("getHtmlCode", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                if (value != null && !value.equals("null")){
                    String trueHtml = StringEscapeUtils.unescapeEcmaScript(value);
                    Document doc = Jsoup.parse(trueHtml);

                    try {
                        Element comments = doc.getElementById("viewComments");
//                                    Log.i("TAG", "comments: " + comments.toString());
                        Elements boxes = comments.select("div.comments-box");
//                                    Log.i("TAG", "boxes size: " + boxes.size());
                        if (boxes.size() == 1){// 等于1的话就有可能 1.没评论 2.网页端还没异步加载出来 3.没热评
                            if (boxes.get(0).select("span.select").get(0).text().equals("全部评论 0条")){
                                Log.e("TAG", "这个帖子没有评论");
                            } else {
                                //有评论，分为两个情况来讨论，
                                // 一是没有热评，只有普通评论，也只有一个box，所以要看评论量，也可以直接找
                                // 二是但还没加载出来，所以要再等一下再获取
                                Elements div_empty = comments.select("div.m-40");
                                if (div_empty.size() == 0){//不存在div.m-40，评论已经加载好了，而且是没有热评的，这一块可以获取评论数据
                                    Log.i("TAG", "有数据了 空评论被移除了，可以拿数据，这个评论没有热评");
                                    CommentList<CommentsNode<CommentItem>> commentList = new CommentList<>(null,0,
                                            toEntity(getNormalCommentsList(comments)));
                                    commentList.printNormalComments(commentList.getNormal_comments());
                                } else if (div_empty.size() == 1){//div.m-40还存在，有评论但数据没有加载好，应该等一下再获取一次
                                    Log.i("TAG", "有评论但数据没有加载好，应该等一下再获取一次");
                                    Message message = new Message();
                                    message.what = GET_COMMENT_DELAY;
                                    //注释掉，省的反复获取
                                handler.sendMessageDelayed(message,1000);
                                }

                            }
                        } else { //box 的size 不等于1，即这个帖子有评论了，可以拿其中的数据
                            Log.d("TAG", "带热评，有两个box数据来了");

                            CommentList<CommentsNode<CommentItem>> commentList = new CommentList<>(toEntity(getHotCommentsList(boxes)),//先拿热评，热评在第一个box里面
                                    toEntity(getHotCommentsList(boxes)).size(),
                                    toEntity(getNormalCommentsList(comments)));
                            commentList.printHotComments(commentList.getHot_comments());

                            Log.i("TAG", "  ");

                            commentList.printNormalComments(commentList.getNormal_comments());

                        }

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }


            }
        });
    }

    private Elements getHotCommentsList(Elements boxes){
        return boxes.get(0).select("div.px-30")
                .get(0).select("div.comment-item-component");
    }

    private Elements getNormalCommentsList(Element div){
        return div.select("div.comments-box").get(div.select("div.comments-box").size() == 2 ? 1 : 0)
        .nextElementSibling()
                .child(0).select("div.comment-item-component");
    }

    /**
     * 需要传入可靠的comment-item-component的list
     * */
    private List<CommentsNode<CommentItem>> toEntity(Elements comment_item_list){
        List<CommentsNode<CommentItem>> tempList = new ArrayList<>();

        for (Element e :
                comment_item_list) {
            tempList.add(CommentsNodeBuilder.getInstance()
                    .initBaseNode()
                    .buildMain(e)
                    .buildReply(e)
                    .getNode());
        }

        return tempList;
    }

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == GET_COMMENT_DELAY){
                getComments(webView);
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_rxjava;
    }
}
