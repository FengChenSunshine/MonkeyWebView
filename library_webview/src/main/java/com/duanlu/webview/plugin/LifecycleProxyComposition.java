package com.duanlu.webview.plugin;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/********************************
 * @name LifecycleProxyComposition
 * @author 段露
 * @createDate 2019/09/12 18:01
 * @updateDate 2019/09/12 18:01
 * @version V1.0.0
 * @describe LifecycleProxy聚合分发.
 ********************************/
public class LifecycleProxyComposition implements LifecycleProxy {

    private List<IBridge> mBridges;

    public LifecycleProxyComposition() {
        mBridges = new ArrayList<>(1);
    }

    public void addBridge(IBridge bridge) {
        mBridges.add(bridge);
    }

    public List<IBridge> getBridges() {
        return mBridges;
    }

    public boolean removeBridge(IBridge bridge) {
        return mBridges.remove(bridge);
    }

    public void onAttach(Context context) {
        List<IBridge> list = mBridges;
        for (IBridge proxy : list) {
            proxy.onAttach(context);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        List<IBridge> list = mBridges;
        for (IBridge proxy : list) {
            proxy.onCreate(savedInstanceState);
        }
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        List<IBridge> list = mBridges;
        for (IBridge proxy : list) {
            proxy.onViewCreated(view, savedInstanceState);
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        List<IBridge> list = mBridges;
        for (IBridge proxy : list) {
            proxy.onActivityCreated(savedInstanceState);
        }
    }

    public void onViewStateRestored(Bundle savedInstanceState) {
        List<IBridge> list = mBridges;
        for (IBridge proxy : list) {
            proxy.onViewStateRestored(savedInstanceState);
        }
    }

    public void onStart() {
        List<IBridge> list = mBridges;
        for (IBridge proxy : list) {
            proxy.onStart();
        }
    }

    public void onResume() {
        List<IBridge> list = mBridges;
        for (IBridge proxy : list) {
            proxy.onResume();
        }
    }

    public void onPause() {
        List<IBridge> list = mBridges;
        for (IBridge proxy : list) {
            proxy.onPause();
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        List<IBridge> list = mBridges;
        for (IBridge proxy : list) {
            proxy.onSaveInstanceState(outState);
        }
    }

    public void onStop() {
        List<IBridge> list = mBridges;
        for (IBridge proxy : list) {
            proxy.onStop();
        }
    }

    public void onDestroyView() {
        List<IBridge> list = mBridges;
        for (IBridge proxy : list) {
            proxy.onDestroyView();
        }
    }

    public void onDestroy() {
        List<IBridge> list = mBridges;
        for (IBridge proxy : list) {
            proxy.onDestroy();
        }
    }

    public void onDetach() {
        List<IBridge> list = mBridges;
        for (IBridge proxy : list) {
            proxy.onDetach();
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    public void onHiddenChanged(boolean hidden) {
        List<IBridge> list = mBridges;
        for (IBridge proxy : list) {
            proxy.onHiddenChanged(hidden);
        }
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        List<IBridge> list = mBridges;
        for (IBridge proxy : list) {
            if (proxy.onActivityResult(requestCode, resultCode, data)) {
                return true;
            }
        }
        return false;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        List<IBridge> list = mBridges;
        for (IBridge proxy : list) {
            proxy.onConfigurationChanged(newConfig);
        }
    }

    public void onLowMemory() {
        List<IBridge> list = mBridges;
        for (IBridge proxy : list) {
            proxy.onLowMemory();
        }
    }

    public void onTrimMemory(int level) {
        List<IBridge> list = mBridges;
        for (IBridge proxy : list) {
            proxy.onTrimMemory(level);
        }
    }

}
