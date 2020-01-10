package com.duanlu.webview.plugin;

import com.duanlu.webview.MonkeyWebView;

/********************************
 * @name IBridge
 * @author 段露
 * @createDate 2019/09/12 11:27
 * @updateDate 2019/09/12 11:27
 * @version V1.0.0
 * @describe 注射器基类.
 ********************************/
public interface IBridge extends LifecycleProxy {

    default String getInterfaceName() {
        return "AppBridge";
    }

    void initialize(MonkeyWebView webView);

    void release(MonkeyWebView webView);

}
