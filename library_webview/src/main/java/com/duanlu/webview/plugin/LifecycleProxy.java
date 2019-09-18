package com.duanlu.webview.plugin;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/********************************
 * @name LifecycleProxy
 * @author 段露
 * @createDate 2019/09/11 10:43
 * @updateDate 2019/09/11 10:43
 * @version V1.0.0
 * @describe 映射Fragment生命周期.
 ********************************/
public interface LifecycleProxy {

    default void onAttach(Context context) {
    }

    default void onCreate(Bundle savedInstanceState) {
    }

    @Deprecated
    default View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    default void onViewCreated(View view, Bundle savedInstanceState) {
    }

    default void onActivityCreated(Bundle savedInstanceState) {
    }

    default void onViewStateRestored(Bundle savedInstanceState) {
    }

    default void onStart() {
    }

    default void onResume() {
    }

    default void onPause() {
    }

    default void onSaveInstanceState(Bundle outState) {
    }

    default void onStop() {
    }

    default void onDestroyView() {
    }

    default void onDestroy() {
    }

    default void onDetach() {
    }

    //////////////////////////////////////////////////////////////////////////////
    default void onHiddenChanged(boolean hidden) {

    }

    default boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }

    default void onConfigurationChanged(Configuration newConfig) {
    }

    default void onLowMemory() {
    }

    default void onTrimMemory(int level) {
    }

}
