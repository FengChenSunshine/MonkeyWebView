package com.duanlu.webview.settings;

import android.content.Context;
import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;

/********************************
 * @name MonkeyWebSettings
 * @author 段露
 * @createDate 2019/09/16 11:11
 * @updateDate 2019/09/16 11:11
 * @version V1.0.0
 * @describe WebSettings相关.
 ********************************/
public class MonkeyWebSettings {

    public static void settings(WebView webView) {

        WebSettings settings = webView.getSettings();

        //如果访问的页面需要和Javascript交互，则必须设置为true.
        //如果加载的页面中Js有执行动画等其它操作，
        // 在onPause和onResume方法里分别设置为false和true会避免造成资源(CPU、电量)浪费.
        settings.setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            //这样设置可以解决https和http混用导致图片无法加载等问题.
            //但是这个操作是不安全的,尽量不要使用.
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //两者结合使用实现自适应屏幕.
        //true时表示支持meta下的viewport属性设置<meta name="viewport" content="width=device-width" />.
        settings.setUseWideViewPort(true);//将图片调整到适合WebView的大小.
        settings.setLoadWithOverviewMode(true);//缩放至屏幕大小.

        //虽然Android默认值是100，但是发现在有的三星(SM-G8850 1080*2094 420dpi)手机上默认值是110导致页面左右滑动.
        //所以还是设置下100吧.
        settings.setTextZoom(100);

        //缩放操作.
        settings.setSupportZoom(false);//支持缩放，默认为true，是下面方法的前提.
        settings.setBuiltInZoomControls(true);//设置内置的缩放控件，若为false，则该WebView不支持缩放.
        settings.setDisplayZoomControls(false);//隐藏原生的缩放控件.

        /**
         * 缓存模式.
         * WebSettings.LOAD_CACHE_ELSE_NETWORK 只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据.
         * WebSettings.LOAD_DEFAULT 默认，根据cache-control决定是否从网络上取数据.
         * WebSettings.LOAD_NO_CACHE 不使用缓存，只从网络上获取数据.
         * WebSettings.LOAD_CACHE_ONLY 不使用网络，只读取本地缓存数据.
         */
//        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//缓存模式.
        settings.setAllowFileAccess(true);//设置可以访问文件.
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过Js打开新窗口.
        settings.setLoadsImagesAutomatically(true);//支持自动加载图片.
        settings.setDefaultTextEncodingName("utf-8");//设置编码格式.

        //允许使用位置信息.
        settings.setGeolocationEnabled(true);

        //设置缓存目录(APP内只需调用一次).
        Context context = webView.getContext().getApplicationContext();
        String appCachePath = context.getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        //开启 Application Caches 功能.
        settings.setAppCacheEnabled(true);

        //Uncaught TypeError: Cannot read property ‘getItem’ of null”,resource：“http://xxx”
        //在JS运行的时候你的页面还没有加载完成，所以你的JS代码找不到你的页面元素，就会抛出这个问题
        settings.setDomStorageEnabled(true);

        settings.setAppCacheMaxSize(1024 * 1024 * 8);

        //设置允许跨域.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
    }

}
