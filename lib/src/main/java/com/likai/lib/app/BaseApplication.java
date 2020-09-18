package com.likai.lib.app;

import android.support.multidex.MultiDexApplication;

/**
 * Created by likai on 2018/3/13.
 * 功能描述：
 * 版本：
 */

public class BaseApplication extends MultiDexApplication {

    public static BaseApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

}
