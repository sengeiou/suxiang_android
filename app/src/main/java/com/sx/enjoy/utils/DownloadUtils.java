package com.sx.enjoy.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;
import java.text.DecimalFormat;

public class DownloadUtils {

    public static String getVersionCode(Context ctx) {
        // 获取packagemanager的实例
        String version = "";
        try {
            PackageManager packageManager = ctx.getPackageManager();
            //getPackageName()是你当前程序的包名
            PackageInfo packInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    public static String getSaveFilePath(){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"Download";
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        String newFilePath = path +File.separator+ "huixiang.apk";
        return newFilePath;
    }

    public static String setFileSize(int size) {
        DecimalFormat df = new DecimalFormat("###.##");
        float f = ((float) size / (float) (1024 * 1024));

        if (f < 1.0) {
            float f2 = ((float) size / (float) (1024));

            return df.format(new Float(f2).doubleValue()) + "KB";

        } else {
            return df.format(new Float(f).doubleValue()) + "M";
        }

    }


}
