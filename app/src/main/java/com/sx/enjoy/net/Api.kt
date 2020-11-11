package com.sx.enjoy.net


import android.text.TextUtils
import android.util.Log
import android.util.SparseArray
import android.view.Gravity
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.likai.lib.app.BaseApplication
import com.likai.lib.commonutils.AppUtils
import com.likai.lib.commonutils.NetUtils
import com.likai.lib.net.TrustAllCerts
import com.sx.enjoy.App
import com.sx.enjoy.constans.C
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.security.SecureRandom
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager


class Api private constructor(hostType: Int) {
    private val retrofit: Retrofit
    private val apiService: ApiService


    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private val mRewriteCacheControlInterceptor = Interceptor{ chain ->
        var request = chain.request()
        val cacheControl = request.cacheControl().toString()
        if (!NetUtils.isNetConnected(BaseApplication.instance)) {
            request = request.newBuilder()
                    .cacheControl(if (TextUtils.isEmpty(cacheControl)) CacheControl.FORCE_NETWORK else CacheControl.FORCE_CACHE)
                    .build()
        }

        val originalResponse = chain.proceed(request)
        if(originalResponse.code() != 200){
            App.instance.runOnUiThread {
                App.instance.toast("服务器异常").setGravity(Gravity.CENTER, 0, 0)
            }
        }
         if (NetUtils.isNetConnected(BaseApplication.instance)) {
            //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
            originalResponse.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build()
        } else {
            originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=$CACHE_STALE_SEC")
                    .removeHeader("Pragma")
                    .build()
        }
    }

    init {
        //开启Log
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        //缓存
        val cacheFile = File(App.instance.cacheDir, "cache")
        val cache = Cache(cacheFile, (1024 * 1024 * 100).toLong()) //100Mb
        //增加头部信息
        val headerInterceptor = Interceptor{ chain ->
            val build = chain.request().newBuilder()
            build.addHeader("Content-Type", "application/json; charset=utf-8")
            build.addHeader("deviceType","sxApp")
            build.addHeader("versions", AppUtils.getVersionName(App.instance))
            build.addHeader("phoneSystem","Android")
            chain.proceed(build.build())
        }


        val okHttpClient = OkHttpClient.Builder()
                .readTimeout(READ_TIME_OUT.toLong(), TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIME_OUT.toLong(), TimeUnit.MILLISECONDS)
                .addInterceptor(mRewriteCacheControlInterceptor)
                .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                .sslSocketFactory(createSSLSocketFactory()!!)
                .addInterceptor(headerInterceptor)
                .addInterceptor(logInterceptor)
                .cache(cache)
                .build()
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").serializeNulls().create()
        retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(C.HX_BASE_ADDRESS)
                .build()
        apiService = retrofit.create(ApiService::class.java)
    }

    private fun createSSLSocketFactory(): SSLSocketFactory? {
        var ssfFactory: SSLSocketFactory? = null

        try {
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, arrayOf<TrustManager>(TrustAllCerts()), SecureRandom())

            ssfFactory = sc.socketFactory
        } catch (e: Exception) {
        }

        return ssfFactory
    }

    companion object {
        //读超时长，单位：毫秒
        private val READ_TIME_OUT = 10000
        //连接时长，单位：毫秒
        private val CONNECT_TIME_OUT = 1000
        private val api: Api? = null

        private val sRetrofitManager = SparseArray<Api>()

        /*************************缓存设置 */
        /*
   1. noCache 不使用缓存，全部走网络

    2. noStore 不使用缓存，也不存储缓存

    3. onlyIfCached 只使用缓存

    4. maxAge 设置最大失效时间，失效则不使用 需要服务器配合

    5. maxStale 设置最大失效时间，失效则不使用 需要服务器配合 感觉这两个类似 还没怎么弄清楚，清楚的同学欢迎留言

    6. minFresh 设置有效时间，依旧如上

    7. FORCE_NETWORK 只走网络

    8. FORCE_CACHE 只走缓存*/
        /**
         * 设缓存有效期为两天
         */
        private val CACHE_STALE_SEC = (60 * 60 * 24 * 2).toLong()
        /**
         * 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
         * max-stale 指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可接收超出超时期指定值之内的响应消息。
         */
        private val CACHE_CONTROL_CACHE = "only-if-cached, max-stale=$CACHE_STALE_SEC"
        /**
         * 查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
         * (假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
         */
        private val CACHE_CONTROL_AGE = "max-age=0"

        /**
         * @param hostType
         */
        fun getDefault(hostType: Int = C.HX_HOST_TYPE): ApiService {
            var retrofitManager: Api? = sRetrofitManager.get(hostType)
            if (retrofitManager == null) {
                retrofitManager = Api(hostType)
                sRetrofitManager.put(hostType, retrofitManager)
            }
            return retrofitManager.apiService
        }

    }
}
