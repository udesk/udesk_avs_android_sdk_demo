package cn.udesk.udeskavsdemo;

import android.app.Application;

import androidx.multidex.MultiDex;

import cn.udesk.udeskavssdk.utils.UdeskLogUtils;


public class UdeskApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        UdeskLogUtils.setLogLevel(UdeskLogUtils.DEBUG);
        MultiDex.install(this);
    }
}
