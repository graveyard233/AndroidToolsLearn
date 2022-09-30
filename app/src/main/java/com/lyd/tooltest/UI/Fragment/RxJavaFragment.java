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
import com.lyd.tooltest.JsFile.MyJs;
import com.lyd.tooltest.R;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

        setWebView("https://iyingdi.com/tz/post/5219116");//5219071

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
//                    handler.sendMessage(message);

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


            Message message = new Message();
            message.what = GET_COMMENT_DELAY;
            handler.sendMessage(message);
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
                        if (boxes.size() == 1){// 等于1的话就有可能没评论或者现在网页端还没异步加载出来
                            if (boxes.get(0).select("span.select").get(0).text().equals("全部评论 0条")){
                                Log.e("TAG", "这个帖子没有评论");
                            } else {
                                //有评论，分为两个情况来讨论，
                                // 一是没有热评，只有普通评论，也只有一个box，所以要看评论量，也可以直接找
                                // 二是但还没加载出来，所以要再等一下再获取
                                Log.e("TAG", "有评论，但还没加载出来，等一下再拿 ");
                                Log.i("TAG", boxes.get(0).toString());
                                Message message = new Message();
                                message.what = GET_COMMENT_DELAY;
//                                handler.sendMessageDelayed(message,1000);
                            }
                        } else { //box 的size 不等于1，即这个帖子有评论了，可以拿其中的数据
                            Log.d("TAG", "数据来了");
                            for (int i = 0; i < boxes.size(); i++) {
                                Log.i("TAG", "boxes size: " + boxes.get(i).toString());
                            }
                        }

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }


            }
        });
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
