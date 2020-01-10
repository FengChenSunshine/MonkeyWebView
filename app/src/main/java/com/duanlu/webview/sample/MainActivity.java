package com.duanlu.webview.sample;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.duanlu.webview.MonkeyWebView;
import com.duanlu.webview.plugin.LifecycleProxyComposition;
import com.duanlu.webview.sample.theme.ThemeConfig;

public class MainActivity extends AppCompatActivity {

    private MonkeyWebView mWebView;
    private LifecycleProxyComposition mLifecycleProxyComposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = findViewById(R.id.web_view);
        mWebView.useDefaultSetting();
        mLifecycleProxyComposition = mWebView.getProxyComposition();

        mLifecycleProxyComposition.addBridge(new CommonBridge(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLifecycleProxyComposition.onActivityResult(requestCode, resultCode, data);
    }

    public void themeConfig(MonkeyWebView webView, ThemeConfig themeConfig) {

    }

    public void navigation(MonkeyWebView webView, String url) {

    }

}
