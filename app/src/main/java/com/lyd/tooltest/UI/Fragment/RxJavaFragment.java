package com.lyd.tooltest.UI.Fragment;

import android.util.Log;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.widget.LinearLayout;

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

public class RxJavaFragment extends BaseFragment {

    private AgentWeb webView;
    private LinearLayout linear;

    private int flag_html = 0;

    @Override
    protected void initViews() {
        linear = find(R.id.rxj_linearLayout);

        setWebView("https://iyingdi.com/tz/post/5218587");
    }

    private WebViewClient webViewClient = new WebViewClient(){

        @Override
        public void onLoadResource(WebView view, String url) {

            super.onLoadResource(view, url);

            flag_html++;
            if (flag_html %2 == 1){

                view.evaluateJavascript("javascript: document.getElementsByClassName('down-load-app-container-post')[0].style.display = 'none';",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                Log.e("TAG", "finish: "+ value);
                            }
                        });
            }
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            if (request.getMethod().equals("POST")){
                Log.i("TAG", "shouldInterceptRequest: " + request.getUrl().toString());
            }

            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {


            super.onPageFinished(view, url);
            view.loadUrl(MyJs.getHtml);//加载js


            webView.getJsAccessEntrace().quickCallJs("getHtmlCode", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    flag_html++;
                    if (flag_html % 2 == 0){

                        String trueHtml = StringEscapeUtils.unescapeEcmaScript(value);
//                    Log.d("TAG", "onReceiveValue: " + trueHtml);//先做解码


                        //获取整个html的代码
                        Document doc = Jsoup.parse(trueHtml);

                        //这个是去找悬浮窗，但是已经被我干掉了，所以这两行是废的
//                        Elements body = doc.select("div.down-load-app-container-post");
//                        Log.d("TAG", "body: " + body.toString());

                        //获取评论的div，是最外面的一层，能显示的评论数据都在这里
                        //有个问题，就是有可能网络不好或评论数据过大，网页加载好的时候评论div还是空的
                        Element comments = doc.getElementById("viewComments");
                        Elements boxes = comments.select("div.comments-box");
                        for (int i = 0; i < boxes.size(); i++) {
                            Log.i("TAG", "boxes size: " + boxes.get(i).toString());
                        }

//                        Log.i("TAG", "viewComments: " + comments.toString());
                    }

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

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_rxjava;
    }
}
