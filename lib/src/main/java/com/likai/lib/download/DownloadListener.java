package com.likai.lib.download;

/**
 * Description: 下载进度回调
 * Created by likai on 2017/11/30.
 */
public interface DownloadListener {

    void onStartDownload();

    void onProgress(int progress);

    void onFinishDownload();

    void onFail(String errorInfo);

}
