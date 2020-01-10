package com.duanlu.webview.core;

import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.duanlu.webview.MonkeyWebView;
import com.duanlu.webview.plugin.IBridge;

import java.util.ArrayList;
import java.util.List;

/********************************
 * @name MonkeyWebChromeClient
 * @author 段露
 * @createDate 2019/09/10 10:46
 * @updateDate 2019/09/10 10:46
 * @version V1.0.0
 * @describe WebChromeClient.
 ********************************/
public class MonkeyWebChromeClient extends WebChromeClient {

    private static final String TAG = "MonkeyWebChromeClient";
    private static final int DEFAULT_INJECTION_THRESHOLD = 25;

    private List<JsInjector> mJsInjectors;
    private int mInjectionThreshold;
    private boolean isInjectionComplete;

    public MonkeyWebChromeClient() {
        this(DEFAULT_INJECTION_THRESHOLD);
    }

    public MonkeyWebChromeClient(int injectionThreshold) {
        mInjectionThreshold = injectionThreshold;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (!isInjectionComplete && newProgress > mInjectionThreshold) {
            injection(view);
        }
    }

    protected void injection(WebView view) {
        if (view instanceof MonkeyWebView) {
            MonkeyWebView webView = (MonkeyWebView) view;

            if (null == mJsInjectors) {
                mJsInjectors = new ArrayList<>();
            } else {
                for (JsInjector jsInjector : mJsInjectors) {
                    jsInjector.getBridge().release(webView);
                }
                mJsInjectors.clear();
            }

            List<IBridge> bridges = webView.getProxyComposition().getBridges();
            JsInjector jsInjector;
            for (IBridge bridge : bridges) {
                jsInjector = new JsInjector(webView, bridge);
                webView.loadUrl("javascript:" + jsInjector.getInjectCode());
                mJsInjectors.add(jsInjector);
            }
        }
        isInjectionComplete = true;
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        return tryCallJava(view, url, message, defaultValue, result);
    }

    protected boolean tryCallJava(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        if (view instanceof MonkeyWebView) {
            for (JsInjector jsInjector : mJsInjectors) {
                if (jsInjector.isCallJava(view, message)) {
                    result.confirm(jsInjector.callJava((MonkeyWebView) view, message));
                    return true;
                }
            }
        }
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        Log.i(TAG, consoleMessage.message());
        return super.onConsoleMessage(consoleMessage);
    }

}
