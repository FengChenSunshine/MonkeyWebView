package com.duanlu.webview.sample.theme;

import com.google.gson.annotations.SerializedName;

/********************************
 * @name ThemeConfig
 * @author 段露
 * @createDate 2019/09/17 08:59
 * @updateDate 2019/09/17 08:59
 * @version V1.0.0
 * @describe 主题配置.
 ********************************/
public class ThemeConfig {

    @SerializedName("title")
    private TitleConfig titleConfig;
    @SerializedName("background_color")
    private String backgroundColor;
    private double orientation;
    private boolean keepScreenOn;

    public TitleConfig getTitleConfig() {
        return titleConfig;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public double getOrientation() {
        return orientation;
    }

    public boolean isKeepScreenOn() {
        return keepScreenOn;
    }
}
