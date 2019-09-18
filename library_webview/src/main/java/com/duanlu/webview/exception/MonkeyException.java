package com.duanlu.webview.exception;

/********************************
 * @name MonkeyException
 * @author 段露
 * @createDate 2019/09/12 16:51
 * @updateDate 2019/09/12 16:51
 * @version V1.0.0
 * @describe 异常.
 ********************************/
public class MonkeyException extends RuntimeException {

    public MonkeyException() {
    }

    public MonkeyException(String message) {
        super(message);
    }

    public MonkeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MonkeyException(Throwable cause) {
        super(cause);
    }

}
