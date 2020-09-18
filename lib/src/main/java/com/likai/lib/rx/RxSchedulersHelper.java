package com.likai.lib.rx;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by likai on 2018/3/22.
 * 功能描述：
 * 版本：
 */

public class RxSchedulersHelper {
    public static <T> ObservableTransformer<T, T> io_main() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableTransformer<T, T> io_io() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io());
    }
}
