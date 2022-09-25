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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RxJavaFragment extends BaseFragment {

    private AgentWeb webView;
    private LinearLayout linear;

    private int flag_html = 0;

    @Override
    protected void initViews() {
        linear = find(R.id.rxj_linearLayout);

//        setWebView("https://iyingdi.com/tz/post/5218768");

        Observable.just("132")
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(String s) throws Throwable {
                        if (s.equals("132"))
                            return true;
                        else return false;
                    }
                })

                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Boolean aBoolean) {
                        if (aBoolean)
                            Log.i("TAG", "onNext: yes" );
                        else
                            Log.e("TAG", "onNext: error");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private WebViewClient webViewClient = new WebViewClient(){

        @Override
        public void onLoadResource(WebView view, String url) {

            super.onLoadResource(view, url);
            view.evaluateJavascript("javascript: document.getElementsByClassName('down-load-app-container-post')[0].style.display = 'none';",
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
                                    Log.i("TAG", "boxes size: " + boxes.size());
//                                    for (int i = 0; i < boxes.size(); i++) {
//                                        Log.i("TAG", "boxes size: " + boxes.get(i).toString());
//                                    }
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                            }


                        }
                    });

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

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_rxjava;
    }
}
