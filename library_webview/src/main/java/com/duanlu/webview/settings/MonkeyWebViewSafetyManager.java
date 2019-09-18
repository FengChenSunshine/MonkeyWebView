package com.duanlu.webview.settings;

import android.webkit.WebView;

import androidx.annotation.NonNull;

/********************************
 * @name MonkeyWebViewSafetyManager
 * @author 段露
 * @createDate 2019/09/16 11:10
 * @updateDate 2019/09/16 11:10
 * @version V1.0.0
 * @describe WebView安全相关.
 ********************************/
public class MonkeyWebViewSafetyManager {

    /**
     * 开启JavaScript后防止远程执行漏洞.
     */
    public static void removeJavascriptInterfaces(@NonNull WebView webView) {
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.removeJavascriptInterface("accessibility");
        webView.removeJavascriptInterface("accessibilityTraversal");
    }

}
