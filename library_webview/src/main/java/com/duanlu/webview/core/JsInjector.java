package com.duanlu.webview.core;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.duanlu.webview.MonkeyWebView;
import com.duanlu.webview.annotation.MonkeyParam;
import com.duanlu.webview.callback.MonkeyCallback;
import com.duanlu.webview.exception.MonkeyException;
import com.duanlu.webview.plugin.BridgeMessage;
import com.duanlu.webview.plugin.IBridge;
import com.duanlu.webview.utils.MonkeyUtils;
import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/********************************
 * @name JsInjector
 * @author 段露
 * @createDate 2019/09/12 11:21
 * @updateDate 2019/09/12 11:21
 * @version V1.0.0
 * @describe Js代码注入器.
 ********************************/
public class JsInjector {

    private static final String TAG = "JsInjector";

    private static final String JS_TEMPLATE = "(function(win) {\n" +
            "\tconsole.log(\"native inject start %s\");\n" +
            "\tvar call = {\n" +
            "\t\tcacheCallbacks: [],\n" +
            "\t\tcallback: function() {\n" +
            "\t\t\tvar d = Array.prototype.slice.call(arguments, 0);\n" +
            "\t\t\tvar c = d.shift();\n" +
            "\t\t\tvar e = d.shift();\n" +
            "\t\t\tthis.cacheCallbacks[c].apply(this, d);\n" +
            "\t\t\tif (!e) {\n" +
            "\t\t\t\tdelete this.cacheCallbacks[c];\n" +
            "\t\t\t}\n" +
            "\t\t}\n" +
            "\t};\n" +
            "\t%s\n" +
            "\t\tfunction() {\n" +
            "\t\t\tvar f = Array.prototype.slice.call(arguments, 0);\n" +
            "\t\t\tif (f.length < 1) {\n" +
            "\t\t\t\tthrow \"%s call error, message:miss method name\"\n" +
            "\t\t\t}\n" +
            "\t\t\tvar e = [];\n" +
            "\t\t\tfor (var h = 1; h < f.length; h++) {\n" +
            "\t\t\t\tvar c = f[h];\n" +
            "\t\t\t\tvar j = typeof c;\n" +
            "\t\t\t\te[e.length] = j;\n" +
            "\t\t\t\tif (j == \"function\") {\n" +
            "\t\t\t\t\tvar d = call.cacheCallbacks.length;\n" +
            "\t\t\t\t\tcall.cacheCallbacks[d] = c;\n" +
            "\t\t\t\t\tf[h] = d\n" +
            "\t\t\t\t}\n" +
            "\t\t\t}\n" +
            "\t\t\tvar uri =\"%s:\"+JSON.stringify({\n" +
            "\t\t\t\tmethod: f.shift(),\n" +
            "\t\t\t\ttypes: e,\n" +
            "\t\t\t\targs: f\n" +
            "\t\t\t});\n" +
            "\t\t\tconsole.log(\"准备执行native方法：uri=\"+uri);\n" +
            "\t\t\tvar g = JSON.parse(prompt(uri));\n" +
            "\t\t\tconsole.log(\"native执行返回结果：{code:\"+g.code+\",message:\"+g.message+\",data:\"+g.data+\"}\");\n" +
            "\t\t\treturn g\n" +
            "\t\t};\n" +
            "\tObject.getOwnPropertyNames(call).forEach(function(d) {\n" +
            "\t\tvar c = call[d];\n" +
            "\t\tif (typeof c === \"function\" && d !== \"callback\") {\n" +
            "\t\t\tcall[d] = function() {\n" +
            "\t\t\t\treturn c.apply(call,[d].concat(Array.prototype.slice.call(arguments, 0)))\n" +
            "\t\t\t}\n" +
            "\t\t}\n" +
            "\t});\n" +
            "\twin.%s = call;\n" +
            "\tconsole.log(\"native inject end %s\")\n" +
            "})(window);\n";
    private static final String RETURN_TEMPLATE = "{\"code\":%d,\"message\":\"%s\",\"data\":%s}";

    private String mInterfaceName;
    private Map<String, Method> mNativeMethodCache;
    private Class<? extends IBridge> mBridgeClass;
    private IBridge mBridge;

    public JsInjector(MonkeyWebView webView, IBridge bridge) {
        mBridge = bridge;
        mInterfaceName = bridge.getInterfaceName();
        this.mBridgeClass = bridge.getClass();
        mNativeMethodCache = new HashMap<>();

        if (TextUtils.isEmpty(mInterfaceName)) {
            throw new MonkeyException("interfaceName can not null");
        }

        bridge.initialize(webView);
    }

    public IBridge getBridge() {
        return mBridge;
    }

    public String getInjectCode() {
        return generateJsCode();
    }

    private String generateJsCode() {
        List<Method> methods = MonkeyUtils.extractMethods(mBridgeClass);
        String methodSign;
        Set<String> statementJsVariable = new HashSet<>();
        for (Method method : methods) {
            if (Modifier.PUBLIC == method.getModifiers() && null != method.getAnnotation(JavascriptInterface.class)) {
                methodSign = generateMethodSign(method);
                mNativeMethodCache.put(methodSign, method);
                statementJsVariable.add(method.getName());
            }
        }

        StringBuilder sb = new StringBuilder();
        for (String s : statementJsVariable) {
            sb.append("call.").append(s).append("=");
        }

        String jsCode = String.format(JS_TEMPLATE, mInterfaceName, sb.toString(), mInterfaceName, mInterfaceName, mInterfaceName, mInterfaceName);
        Log.i(TAG, jsCode);
        return jsCode;
    }

    private String generateMethodSign(Method method) {
        Class[] parameterTypes = method.getParameterTypes();
        Annotation[][] annotations = method.getParameterAnnotations();

        StringBuilder sb = new StringBuilder();
        sb.append(method.getName());
        Class parameterType;
        int N = parameterTypes.length;
        for (int i = 0; i < N; i++) {
            parameterType = parameterTypes[i];
            if (annotations.length > 0 && isBridgeParam(annotations[i])) {
                sb.append("&BP");
            } else if (parameterType == String.class) {
                sb.append("&S");
            } else if (parameterType == boolean.class) {
                sb.append("&B");
            } else if (parameterType == int.class
                    || parameterType == double.class
                    || parameterType == long.class
                    || parameterType == float.class) {
                sb.append("&N");
            } else if (parameterType == MonkeyCallback.class) {
                sb.append("&F");//function
            } else if (WebView.class.isAssignableFrom(parameterType)) {
                //nothing.
            } else {
                sb.append("&OTHER");
            }
        }
        return sb.toString();
    }

    private boolean isBridgeParam(Annotation[] annotations) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Stream.of(annotations).anyMatch(ann -> ann instanceof MonkeyParam);
        }
        for (Annotation ann : annotations) {
            if (ann instanceof MonkeyParam) return true;
        }
        return false;
    }

    public boolean isCallJava(WebView view, String message) {
        return view instanceof MonkeyWebView && !TextUtils.isEmpty(message) && message.startsWith(mInterfaceName + ":");
    }

    public String callJava(MonkeyWebView webView, String message) {
        message = message.substring(mInterfaceName.length() + 1);
        BridgeMessage bridgeMessage = new Gson().fromJson(message, BridgeMessage.class);

        int N = bridgeMessage.getTypes().length;

        Object[] params = new Object[N + 1];

        params[0] = webView;

        StringBuilder sb = new StringBuilder();
        sb.append(bridgeMessage.getMethod());
        SparseArray<String> cacheObjectArray = null;
        for (int i = 0; i < N; i++) {
            switch (bridgeMessage.getTypes()[i]) {
                case "string":
                    sb.append("&S");
                    params[i + 1] = bridgeMessage.getArgs()[i];
                    break;
                case "number":
                    sb.append("&N");
                    params[i + 1] = bridgeMessage.getArgs()[i];
                    break;
                case "boolean":
                    sb.append("&B");
                    params[i + 1] = bridgeMessage.getArgs()[i];
                    break;
                case "object":
                    sb.append("&BP");
                    params[i + 1] = bridgeMessage.getArgs()[i].toString();

                    if (null == cacheObjectArray) cacheObjectArray = new SparseArray<>();
                    cacheObjectArray.put(i + 1, bridgeMessage.getArgs()[i].toString());
                    break;
                case "function":
                    sb.append("&F");
                    //使用Gson解析时将Number解析为了Double.
                    int jsCacheMethodFlag = ((Double) bridgeMessage.getArgs()[i]).intValue();
                    params[i + 1] = new MonkeyCallback(webView, mInterfaceName, jsCacheMethodFlag);
                    break;
                default:
                    sb.append("&OTHER");
                    break;
            }
        }

        Method method = mNativeMethodCache.get(sb.toString());

        if (null == method) {
            return generateReturn(404, "方法未找到", "Not found method " + bridgeMessage.getMethod() + ". Or the parameters don't match");
        }

        if (null != cacheObjectArray && cacheObjectArray.size() > 0) {
            Class<?>[] types = method.getParameterTypes();
            Gson gson = new Gson();
            int key;
            for (int i = 0; i < cacheObjectArray.size(); i++) {
                key = cacheObjectArray.keyAt(i);
                params[key] = gson.fromJson(cacheObjectArray.valueAt(i), types[key]);
            }
        }

        try {
            Object executeMethodReturn = method.invoke(mBridge, params);
            return generateReturn(200, "成功", executeMethodReturn);
        } catch (IllegalAccessException | InvocationTargetException e) {
            String errorMsg = null == e.getCause() ? e.getMessage() : e.getCause().getMessage();
            Log.e(TAG, "Method execute error:" + errorMsg);
            return generateReturn(500, "方法执行失败", "Method execute error:" + errorMsg);
        }
    }

    private String generateReturn(int code, String message, Object data) {
        String strData;
        if (null == data) {
            strData = "null";
        } else if (data instanceof String) {
            strData = "\"" + data + "\"";
        } else if (data == boolean.class
                || data == int.class
                || data == double.class
                || data == long.class
                || data == float.class) {
            strData = String.valueOf(data);
        } else {
            strData = new Gson().toJson(data);
        }
        return String.format(Locale.getDefault(), RETURN_TEMPLATE, code, message, strData);
    }

}