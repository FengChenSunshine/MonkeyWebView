package com.duanlu.webview.core;

import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.duanlu.webview.interceptor.MonkeyUrlInterceptor;

import java.util.ArrayList;
import java.util.List;

/********************************
 * @name MonkeyUrlInterceptorComposition
 * @author 段露
 * @createDate 2019/09/16 10:54
 * @updateDate 2019/09/16 10:54
 * @version V1.0.0
 * @describe MonkeyUrlInterceptorComposition聚合分发.
 ********************************/
class MonkeyUrlInterceptorComposition implements MonkeyUrlInterceptor {

    private List<MonkeyUrlInterceptor> mUrlInterceptors;

    public MonkeyUrlInterceptorComposition() {
        mUrlInterceptors = new ArrayList<>(2);
    }

    public void addInterceptor(MonkeyUrlInterceptor interceptor) {
        mUrlInterceptors.add(interceptor);
    }

    public List<MonkeyUrlInterceptor> getInterceptors() {
        return mUrlInterceptors;
    }

    public boolean removeInterceptor(MonkeyUrlInterceptor interceptor) {
        return mUrlInterceptors.remove(interceptor);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        List<MonkeyUrlInterceptor> list = mUrlInterceptors;
        for (MonkeyUrlInterceptor interceptor : list) {
            if (interceptor.shouldOverrideUrlLoading(view, url)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        List<MonkeyUrlInterceptor> list = mUrlInterceptors;
        for (MonkeyUrlInterceptor interceptor : list) {
            if (interceptor.shouldOverrideUrlLoading(view, request)) {
                return true;
            }
        }
        return false;
    }

}
