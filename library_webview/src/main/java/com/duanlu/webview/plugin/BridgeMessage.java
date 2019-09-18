package com.duanlu.webview.plugin;

/********************************
 * @name BridgeMessage
 * @author 段露
 * @createDate 2019/09/12 14:34
 * @updateDate 2019/09/12 14:34
 * @version V1.0.0
 * @describe 消息.
 ********************************/
public class BridgeMessage {

    private String method;
    private String[] types;
    private Object[] args;

    public String getMethod() {
        return method;
    }

    public String[] getTypes() {
        return types;
    }

    public Object[] getArgs() {
        return args;
    }

}
