package com.lyd.tooltest.UI.Fragment;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.EncodeUtils;
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
        setWebView("https://iyingdi.com/tz/post/5218038");
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
        public void onPageFinished(WebView view, String url) {


            super.onPageFinished(view, url);
//            view.loadUrl(MyJs.getHtml);//加载js


//            webView.getJsAccessEntrace().quickCallJs("getHtmlCode", new ValueCallback<String>() {
//                @Override
//                public void onReceiveValue(String value) {
//                    flag_html++;
//                    if (flag_html % 2 == 0){
//
//                        String trueHtml = StringEscapeUtils.unescapeEcmaScript(value);
////                    Log.d("TAG", "onReceiveValue: " + trueHtml);//先做解码
//
//
//
//                        Document doc = Jsoup.parse(trueHtml);
//
//                        Elements body = doc.select("div.down-load-app-container-post");
//                        Log.d("TAG", "body: " + body.toString());
//                    }
//
//                }
//            });


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
