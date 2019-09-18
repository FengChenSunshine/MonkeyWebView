package com.duanlu.webview.interceptor;

import android.webkit.WebResourceRequest;
import android.webkit.WebView;

/********************************
 * @name MonkeyUrlInterceptor
 * @author 段露
 * @createDate 2019/09/16 10:51
 * @updateDate 2019/09/16 10:51
 * @version V1.0.0
 * @describe 请求拦截器.
 ********************************/
public interface MonkeyUrlInterceptor {

    @Deprecated
    default boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    default boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return false;
    }

}
