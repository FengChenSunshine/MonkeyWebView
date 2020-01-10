package com.duanlu.webview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;

import com.duanlu.webview.core.MonkeyWebChromeClient;
import com.duanlu.webview.core.MonkeyWebViewClient;
import com.duanlu.webview.exception.MonkeyException;
import com.duanlu.webview.interceptor.MonkeyUrlInterceptor;
import com.duanlu.webview.plugin.LifecycleProxyComposition;
import com.duanlu.webview.settings.MonkeyWebSettings;
import com.duanlu.webview.settings.MonkeyWebViewSafetyManager;

import java.util.Map;

/********************************
 * @name CompatWebView
 * @author 段露
 * @createDate 2019/09/11 10:38
 * @updateDate 2019/09/11 10:38
 * @version V1.0.0
 * @describe WebView.
 ********************************/
public class MonkeyWebView extends WebView {

    private static final boolean DEBUG = true;//true打开调试日志.

    private LifecycleProxyComposition mProxyComposition;
    private MonkeyWebViewClient mMonkeyWebViewClient;

    public MonkeyWebView(Context context) {
        super(context);
        this.initialize(context, null, 0, 0);
    }

    public MonkeyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize(context, attrs, 0, 0);
    }

    public MonkeyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MonkeyWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initialize(context, attrs, defStyleAttr, defStyleRes);
    }

    public MonkeyWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        this.initialize(context, attrs, defStyleAttr, 0);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this.mProxyComposition = new LifecycleProxyComposition();

        this.setWebChromeClient(new MonkeyWebChromeClient());
        mMonkeyWebViewClient = new MonkeyWebViewClient();
        this.setWebViewClient(mMonkeyWebViewClient);
    }

    public void useDefaultSetting() {
        //开启or关闭调试模式.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(DEBUG);
        }
        MonkeyWebSettings.settings(this);
        MonkeyWebViewSafetyManager.removeJavascriptInterfaces(this);
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        super.loadUrl(url, additionalHttpHeaders);
    }

    @Override
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    @Override
    public void loadData(String data, String mimeType, String encoding) {
        super.loadData(data, mimeType, encoding);
    }

    public LifecycleProxyComposition getProxyComposition() {
        return mProxyComposition;
    }

    @Override
    public void setWebChromeClient(WebChromeClient client) {
        if (!(client instanceof MonkeyWebChromeClient)) {
            throw new MonkeyException("WebChromeClient must be MonkeyWebChromeClient or a subclass thereof");
        }
        super.setWebChromeClient(client);
    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        if (!(client instanceof MonkeyWebViewClient)) {
            throw new MonkeyException("WebViewClient must be MonkeyWebViewClient or a subclass thereof");
        }
        super.setWebViewClient(client);
        mMonkeyWebViewClient = (MonkeyWebViewClient) client;
    }

    public void addUrlInterceptor(MonkeyUrlInterceptor interceptor) {
        mMonkeyWebViewClient.addInterceptor(interceptor);
    }

    public boolean removeUrlInterceptor(MonkeyUrlInterceptor interceptor) {
        return mMonkeyWebViewClient.removeInterceptor(interceptor);
    }

}
