package com.duanlu.webview.interceptor;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.duanlu.webview.utils.MonkeyUtils;

/********************************
 * @name BaseMonkeyUrlInterceptor
 * @author 段露
 * @createDate 2019/09/16 11:48
 * @updateDate 2019/09/16 11:48
 * @version V1.0.0
 * @describe MonkeyUrlInterceptor
 ********************************/
public abstract class BaseMonkeyUrlInterceptor implements MonkeyUrlInterceptor {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return tryInterceptor(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return tryInterceptor(view, request.getUrl().toString());
        }
        return false;
    }

    protected boolean tryInterceptor(WebView webView, String url) {
        if (url.contains("tel")) {//tel:400-888-8888
            onTelUrl(webView, url);
            return true;
        } else if (!url.startsWith("http")
                && !url.startsWith("https")
                && !url.startsWith("ftp")
                && !url.startsWith("file:///android_asset")) {
            onOtherUrl(webView, url);
            return true;
        }
        return false;
    }

    protected void onTelUrl(WebView webView, String url) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + url));
        if (MonkeyUtils.isInstance(webView.getContext(), intent)) {
            webView.getContext().startActivity(intent);
        }
    }

    protected void onOtherUrl(WebView webView, String url) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(url));
        if (MonkeyUtils.isInstance(webView.getContext(), intent)) {
            webView.getContext().startActivity(intent);
        }
    }

}
