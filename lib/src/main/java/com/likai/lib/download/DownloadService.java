package com.likai.lib.download;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


/**
 * Description:
 * Created by likai on 2017/11/30.
 */
public interface DownloadService {

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

}
