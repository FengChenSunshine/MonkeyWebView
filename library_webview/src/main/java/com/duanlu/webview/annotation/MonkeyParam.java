package com.duanlu.webview.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/********************************
 * @name MonkeyParam
 * @author 段露
 * @createDate 2019/09/12 12:58
 * @updateDate 2019/09/12 12:58
 * @version V1.0.0
 * @describe 标记一个方法参数为Json数据.
 ********************************/
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface MonkeyParam {

}
