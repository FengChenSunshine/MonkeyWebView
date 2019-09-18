package com.duanlu.webview.callback;

import android.util.Log;

import com.duanlu.webview.MonkeyWebView;

import java.lang.ref.WeakReference;
import java.util.Locale;

/********************************
 * @name MonkeyCallback
 * @author 段露
 * @createDate 2019/09/12 12:56
 * @updateDate 2019/09/12 12:56
 * @version V1.0.0
 * @describe JsCallJava回调接口.
 ********************************/
public class MonkeyCallback {

    private static final String TAG = "MonkeyCallback";

    private static final String TEMPLATE = "javascript:%s.callback(%d,%d %s);";
    private WeakReference<MonkeyWebView> mWebView;
    private String mInterfaceName;
    private int mJsMethodFlag;
    private boolean isKeep;

    public MonkeyCallback(MonkeyWebView webView, String interfaceName, int jsMethodFlag) {
        mWebView = new WeakReference<>(webView);
        mInterfaceName = interfaceName;
        this.mJsMethodFlag = jsMethodFlag;
    }

    public void execCallback(Object... args) {
        //FIXME 线程.
        execJs(appendArgs(args));
    }

    public void execJsonObjectCallback(String jsonObject) {
        //FIXME 线程.
        execJs("," + jsonObject);
    }

    private String appendArgs(Object... args) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : args) {
            sb.append(",");
            if (obj instanceof String) {
                sb.append("\"");
            }
            sb.append(obj);
            if (obj instanceof String) {
                sb.append("\"");
            }
        }
        return sb.toString();
    }

    private void execJs(String args) {
        String execJs = String.format(Locale.getDefault(), TEMPLATE, mInterfaceName, mJsMethodFlag, isKeep ? 1 : 0, args);

        MonkeyWebView webView = mWebView.get();
        if (null != webView) {
            Log.i(TAG, "Native执行完成,准备执行回调,执行Javascript方法：" + execJs);
            mWebView.get().loadUrl(execJs);
        } else {
            Log.w(TAG, "The WebView related to the callback has been recycled!");
        }
    }

    public MonkeyWebView getWebView() {
        return mWebView.get();
    }

}
