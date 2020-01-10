package com.duanlu.webview.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.webkit.JavascriptInterface;

import androidx.annotation.NonNull;

import com.duanlu.webview.plugin.IBridge;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/********************************
 * @name MonkeyUtils
 * @author 段露
 * @createDate 2019/09/16 14:23
 * @updateDate 2019/09/16 14:23
 * @version V1.0.0
 * @describe 工具类.
 ********************************/
public class MonkeyUtils {

    public static boolean isInstance(Context context, Intent intent) {
        return null != intent && context.getApplicationContext()
                .getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    /**
     * 获取对象及其父类中被JavascriptInterface注解标注的方法.
     *
     * @param clazz Class对象.
     * @return 方法集合.
     */
    public static List<Method> extractMethods(@NonNull Class clazz) {
        return extractMethods(clazz, new HashMap<>());
    }

    private static List<Method> extractMethods(@NonNull Class clazz, @NonNull HashMap<String, Method> result) {
        Method[] methods = clazz.getDeclaredMethods();
        String methodSign;
        for (Method method : methods) {
            if (Modifier.PUBLIC == method.getModifiers() && null != method.getAnnotation(JavascriptInterface.class)) {
                methodSign = generateMethodSign(method);
                if (!result.containsKey(methodSign)) {
                    result.put(methodSign, method);
                }
            }
        }

        Class<?> parentClazz = clazz.getSuperclass();
        if (null != parentClazz && IBridge.class.isAssignableFrom(parentClazz)) {
            return extractMethods(parentClazz, result);
        } else {
            List<Method> methodsList = new ArrayList<>();
            for (String key : result.keySet()) {
                methodsList.add(result.get(key));
            }
            return methodsList;
        }
    }

    /**
     * 生成方法临时签名.
     *
     * @param method 方法.
     * @return 签名.
     */
    private static String generateMethodSign(Method method) {
        StringBuilder sb = new StringBuilder(method.getName())
                .append("@");
        for (Class clazz : method.getParameterTypes()) {
            sb.append(clazz.getSimpleName()).append("_");
        }
        if (sb.lastIndexOf("_") == sb.length() - 1) {
            return sb.substring(0, sb.length() - 1);
        } else {
            return sb.toString();
        }
    }

}
