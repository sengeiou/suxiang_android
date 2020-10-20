package com.sx.enjoy.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ScreenShotUtil {

    /**
     * 截取指定View显示内容
     * 需要读写权限
     */
    public static boolean saveScreenshotFromView(View view,Context context) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache();
        boolean result = saveImageToGallery(bitmap,context);
        //回收资源
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return result;
    }

    /**
     * 保存图片至相册
     * 需要读写权限
     */
    private static boolean saveImageToGallery(Bitmap bmp,Context context) {
        boolean isSuccess = true;
        String path = Environment.getExternalStorageDirectory().getPath() + "/SXScreen/";
        File appDir = new File(path);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            isSuccess = false;
            e.printStackTrace();
        } catch (IOException e) {
            isSuccess = false;
            e.printStackTrace();
        }
        if(isSuccess){
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            context.sendBroadcast(intent);
        }
        return isSuccess;
    }
}
