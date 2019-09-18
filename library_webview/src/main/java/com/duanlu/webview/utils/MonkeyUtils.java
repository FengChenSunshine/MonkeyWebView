package com.duanlu.webview.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

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

}
