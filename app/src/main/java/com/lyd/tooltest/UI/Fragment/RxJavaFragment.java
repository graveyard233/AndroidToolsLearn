package com.lyd.tooltest.UI.Fragment;

import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebViewClient;
import com.lyd.tooltest.Base.BaseFragment;
import com.lyd.tooltest.R;

public class RxJavaFragment extends BaseFragment {

    private AgentWeb webView;
    private LinearLayout linear;

    @Override
    protected void initViews() {
        linear = find(R.id.rxj_linearLayout);
        setWebView("https://iyingdi.com/tz/post/5217677");
    }

    private WebViewClient webViewClient = new WebViewClient(){

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webView.getJsAccessEntrace().callJs("alert('hello')");
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
