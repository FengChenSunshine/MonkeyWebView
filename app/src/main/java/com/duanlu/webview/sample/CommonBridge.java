package com.duanlu.webview.sample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.duanlu.utils.AppFolderManager;
import com.duanlu.utils.DateUtils;
import com.duanlu.utils.DeviceInfoUtils;
import com.duanlu.utils.FileUtils;
import com.duanlu.utils.IntentUtils;
import com.duanlu.webview.MonkeyWebView;
import com.duanlu.webview.annotation.MonkeyParam;
import com.duanlu.webview.callback.MonkeyCallback;
import com.duanlu.webview.plugin.IBridge;
import com.duanlu.webview.sample.theme.ThemeConfig;
import com.google.gson.JsonObject;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.Date;

import io.reactivex.disposables.Disposable;

/********************************
 * @name CommonBridge
 * @author 段露
 * @company 浙江托普云农科技股份有限公司
 * @createDate 2019/09/16 13:41
 * @updateDate 2019/09/16 13:41
 * @version V1.0.0
 * @describe TODO
 ********************************/
public class CommonBridge implements IBridge {

    private static final int REQUEST_CODE_PICK_PHOTO = 10001;//选择图片.
    private static final int REQUEST_CODE_PICK_VIDEO = 10002;//选择视频.
    private static final int REQUEST_CODE_TAKE_PHOTO = 10003;//拍照.
    private static final int REQUEST_CODE_RECORD_VIDEO = 10004;//录制视频.
    private static final int REQUEST_CODE_DISCERN_QR_CODE = 10005;//扫描二维码.
    private static final int REQUEST_CODE_PICK_FILE = 10006;//选择文件.

    private Fragment mFragment;
    private AppCompatActivity mActivity;
    private Context mContext;

    private MonkeyCallback mMonkeyCallback;
    private File mFile;
    private boolean returnPortrait;

    public CommonBridge(Fragment fragment) {
        mFragment = fragment;
    }

    public CommonBridge(AppCompatActivity activity) {
        mActivity = activity;
    }

    public CommonBridge(Context context) {
        mContext = context;
    }

    @Override
    public void initialize(MonkeyWebView monkeyWebView) {

    }

    @Override
    public void release(MonkeyWebView monkeyWebView) {

    }

    @JavascriptInterface
    public void deviceId(final MonkeyWebView webView, final MonkeyCallback callback) {
        Disposable disposable = new RxPermissions((Activity) webView.getContext())
                .request(Manifest.permission.READ_PHONE_STATE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        callback.execCallback(DeviceInfoUtils.getDeviceId(webView.getContext()));
                    } else {
                        callback.execCallback("");
                    }
                });
    }

    @JavascriptInterface
    public String deviceBrand(MonkeyWebView webView) {
        return Build.BRAND;
    }

    @JavascriptInterface
    public String deviceModel(MonkeyWebView webView) {
        return Build.MODEL;
    }

    @JavascriptInterface
    public String deviceRelease(MonkeyWebView webView) {
        return Build.VERSION.RELEASE;
    }

    @JavascriptInterface
    public String deviceDisplay(MonkeyWebView webView) {
        return Build.DISPLAY;
    }

    @JavascriptInterface
    public String deviceResolution(MonkeyWebView webView) {
        DisplayMetrics dm = webView.getContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        return screenWidth + "*" + screenHeight;
    }

    /**
     * 配置页面主题.
     */
    @JavascriptInterface
    public void themeConfig(MonkeyWebView webView, @MonkeyParam ThemeConfig themeConfig) {
        if (mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).themeConfig(webView, themeConfig);
        }
    }

    @JavascriptInterface
    public void changeOrientation(MonkeyWebView webView, double orientation, boolean returnPortrait) {
        this.returnPortrait = returnPortrait;

        ((Activity) webView.getContext()).setRequestedOrientation((int) orientation);
    }

    /**
     * 屏幕常亮.
     */
    @JavascriptInterface
    public void keepScreenOn(MonkeyWebView webView, boolean keepScreenOn) {
        Window window = ((Activity) webView.getContext()).getWindow();
        if (keepScreenOn) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @JavascriptInterface
    public void navigation(MonkeyWebView webView, String url) {
        if (mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).navigation(webView, url);
        }
    }

    @JavascriptInterface
    public void toast(MonkeyWebView webView, String message) {
        Toast.makeText(webView.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public boolean saveData(MonkeyWebView webView, String key, String value) {
        Context context = webView.getContext().getApplicationContext();
        SharedPreferences sp = context.getSharedPreferences("SHARE_DATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        if (edit != null) {
            edit.putString(key, value);
            return edit.commit();
        }
        return false;
    }

    @JavascriptInterface
    public String getData(MonkeyWebView webView, String key, String defaultValue) {
        Context context = webView.getContext().getApplicationContext();
        SharedPreferences sp = context.getSharedPreferences("SHARE_DATA", Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    @JavascriptInterface
    public void dialPhone(MonkeyWebView webView, String phoneNumber) {
        Intent intent = IntentUtils.getDialPhoneIntent(phoneNumber);
        IntentUtils.startActivity(webView.getContext(), intent);
    }

    @JavascriptInterface
    public void sendSMS(MonkeyWebView webView, String phoneNumber, String content) {
        Intent intent = IntentUtils.getSendSmsIntent(phoneNumber, content);
        IntentUtils.startActivity(webView.getContext(), intent);
    }

    @JavascriptInterface
    public void sendEmail(MonkeyWebView webView, @MonkeyParam String[] receive, String subject, String content) {
        Intent intent = IntentUtils.getSendEmailIntent(receive, null, null, subject, content, null);
        IntentUtils.startActivity(webView.getContext(), intent);
    }

    @JavascriptInterface
    public void pickPhoto(MonkeyWebView webView, MonkeyCallback callback) {
        this.mMonkeyCallback = callback;

        Intent intent = IntentUtils.getPickIntent(IntentUtils.SYSTEM_PICK_IMAGE);
        startActivityForResult(webView, intent, REQUEST_CODE_PICK_PHOTO);
    }

    @JavascriptInterface
    public void pickVideo(MonkeyWebView webView, MonkeyCallback callback) {
        this.mMonkeyCallback = callback;

        Intent intent = IntentUtils.getPickIntent(IntentUtils.SYSTEM_PICK_VIDEO);
        startActivityForResult(webView, intent, REQUEST_CODE_PICK_VIDEO);
    }

    @JavascriptInterface
    public void takePhoto(final MonkeyWebView webView, MonkeyCallback callback) {
        this.mMonkeyCallback = callback;

        Disposable disposable = new RxPermissions((Activity) webView.getContext())
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .subscribe(aBoolean -> {
                    mFile = makeTakeFile(IMG_PATH);
                    Intent intent = makeTakeIntent(webView.getContext(), IMG_PATH, mFile);
                    startActivityForResult(webView, intent, REQUEST_CODE_TAKE_PHOTO);
                });
    }

    @JavascriptInterface
    public void recordVideo(final MonkeyWebView webView, MonkeyCallback callback) {
        this.mMonkeyCallback = callback;

        Disposable disposable = new RxPermissions((Activity) webView.getContext())
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .subscribe(aBoolean -> {
                    mFile = makeTakeFile(VIDEO_PATH);
                    Intent intent = makeTakeIntent(webView.getContext(), VIDEO_PATH, mFile);
                    startActivityForResult(webView, intent, REQUEST_CODE_RECORD_VIDEO);
                });
    }

    @JavascriptInterface
    public void pickFile(MonkeyWebView webView, MonkeyCallback callback) {
        this.mMonkeyCallback = callback;

        Intent intent = new Intent(Intent.ACTION_PICK);
        startActivityForResult(webView, intent, REQUEST_CODE_PICK_FILE);
    }

    /**
     * 单次定位.
     */
    @JavascriptInterface
    public void geolocation(final MonkeyWebView webView, final MonkeyCallback callback) {
        geolocation(webView, 0, 0, callback);
    }

    /**
     * 定位.
     *
     * @param second      多次定位时间间隔.
     * @param minDistance 多次定位最小距离.
     */
    @JavascriptInterface
    public void geolocation(final MonkeyWebView webView, final int second, final int minDistance, final MonkeyCallback callback) {
        Disposable disposable = new RxPermissions((Activity) webView.getContext())
                .request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(aBoolean -> internalGeolocation(webView.getContext(), second, minDistance, callback));
    }

    /**
     * 停止定位.
     */
    @SuppressLint("MissingPermission")
    @JavascriptInterface
    public void stopGeolocation(MonkeyWebView webView) {
        if (null != mLocationManager && null != mLocationListener) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }

    private LocationListener mLocationListener;
    private LocationManager mLocationManager;

    @SuppressLint("MissingPermission")
    private void internalGeolocation(Context context, final int second, int minDistance, final MonkeyCallback callback) {
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = mLocationManager.getBestProvider(criteria, true);
        if (null == mLocationListener) {
            mLocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("longitude", location.getLongitude());//经度.
                    jsonObject.addProperty("latitude", location.getLatitude());//纬度.

                    callback.execJsonObjectCallback(jsonObject.toString());

                    if (second <= 0) {
                        if (null != mLocationManager) {
                            mLocationManager.removeUpdates(this);
                        }
                        mLocationListener = null;
                        mLocationManager = null;
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
        }
        if (mLocationManager.isProviderEnabled(provider)) {
            if (second > 0) {//second秒刷新一次，并且位置间隔超过5米.
                mLocationManager.requestLocationUpdates(provider, second * 1000, minDistance, mLocationListener);
            } else {
                mLocationManager.requestSingleUpdate(provider, mLocationListener, null);
            }
        }
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_PICK_PHOTO://选择图片.
            case REQUEST_CODE_PICK_VIDEO://选择图片.
            case REQUEST_CODE_PICK_FILE://选择文件.
                MonkeyWebView webView = mMonkeyCallback.getWebView();
                if (null != webView) {
                    String path = getFilePathByIntentData(webView.getContext(), data);
                    mMonkeyCallback.execCallback(path);
                }
                break;
            case REQUEST_CODE_TAKE_PHOTO://拍照.
            case REQUEST_CODE_RECORD_VIDEO://录制视频.
                File file = mFile;
                if (FileUtils.isFileExists(file)) {
                    mMonkeyCallback.execCallback(file.getAbsolutePath());
                }
                break;
            default:
                return false;
        }

        mMonkeyCallback = null;
        mFile = null;

        return true;
    }

    private String getFilePathByIntentData(Context context, @Nullable Intent data) {
        if (null == data) return "";
        Uri uri = data.getData();
        if (null == uri) return "";
        File file = FileUtils.getFileByUri(context, uri);
        if (null == file) return "";
        return file.getAbsolutePath();
    }

    private String getFilePathByUri(Context context, @Nullable Uri uri) {
        if (null == uri) return "";
        File file = FileUtils.getFileByUri(context, uri);
        if (null == file) return "";
        return file.getAbsolutePath();
    }

    private static final String IMG_PATH = "DCIM/IMG";
    private static final String VIDEO_PATH = "DCIM/VIDEO";

    private File makeTakeFile(@NonNull String type) {
        File filesDir = new File(AppFolderManager.getRootFolder(), type);
        FileUtils.createOrExistsDir(filesDir);

        String prefix = TextUtils.equals(type, IMG_PATH) ? "IMG_" : "VIDEO_";
        String suffix = TextUtils.equals(type, IMG_PATH) ? ".png" : ".mp4";
        String timestamp = DateUtils.date2Str(new Date(), "yyyyMMdd_HHmmss");
        String fileName = String.format("%s%s%s", prefix, timestamp, suffix);
        return new File(filesDir, fileName);
    }

    private Intent makeTakeIntent(Context context, @NonNull String type, File outputFile) {
        Uri uri = AppFolderManager.getUriFromFile(context, outputFile);

        String action = TextUtils.equals(type, IMG_PATH) ? "android.media.action.IMAGE_CAPTURE" : "android.media.action.VIDEO_CAPTURE";
        Intent intent = new Intent(action);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }

    private void startActivity(@NonNull MonkeyWebView webView, Intent intent) {
        webView.getContext().startActivity(intent);
    }

    private void startActivityForResult(MonkeyWebView webView, Intent intent, int requestCode) {
        if (null != mFragment) {
            mFragment.startActivityForResult(intent, requestCode);
        } else {
            Activity activity = mActivity;
            if (null == activity && mContext instanceof Activity) {
                activity = (Activity) mContext;
            } else if (null == activity && webView.getContext() instanceof Activity) {
                activity = (Activity) webView.getContext();
            }
            if (null != activity) {
                activity.startActivityForResult(intent, requestCode);
            }
        }
    }
}
