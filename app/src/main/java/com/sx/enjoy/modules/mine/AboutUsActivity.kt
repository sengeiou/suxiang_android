package com.sx.enjoy.modules.mine

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.webkit.JavascriptInterface
import android.widget.LinearLayout
import com.likai.lib.commonutils.DensityUtils
import com.likai.lib.commonutils.ScreenUtils
import com.sx.enjoy.R
import com.sx.enjoy.WebDataBean
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.tencent.smtt.sdk.WebSettings
import com.yanzhenjie.permission.AndPermission
import kotlinx.android.synthetic.main.activity_about_us.*
import org.jetbrains.anko.toast
import java.lang.reflect.Method

class AboutUsActivity : BaseActivity(), SXContract.View{

    private lateinit var present: SXPresent

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"关于我们")

    override fun getLayoutResource() = R.layout.activity_about_us

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        initWebView()
        initEvent()

        present.getWebData("0")
    }


    private fun checkPermission(){
        AndPermission.with(this)
            .runtime()
            .permission(Manifest.permission.CALL_PHONE)
            .onGranted { permissions ->
                val intent = Intent(Intent.ACTION_CALL)
                val data = Uri.parse("tel:" + tv_phone.text.toString())
                intent.data = data
                startActivity(intent)
            }
            .onDenied { permissions ->

            }
            .start()
    }

    private fun initEvent(){
        ll_call_phone.setOnClickListener {
            checkPermission()
        }
    }

    private fun initWebView(){
        wb_about_us.settings.setSupportZoom(true)
        wb_about_us.settings.builtInZoomControls = true
        wb_about_us.settings.displayZoomControls = true
        wb_about_us.settings.blockNetworkImage = false
        wb_about_us.settings.loadsImagesAutomatically = true
        wb_about_us.settings.defaultTextEncodingName = "utf-8"
        wb_about_us.addJavascriptInterface(this, "App")
        setZoomControlGoneX(wb_about_us.settings, arrayOf(false))

        wb_about_us.webViewClient = object : com.tencent.smtt.sdk.WebViewClient() {
            override fun onPageFinished(view: com.tencent.smtt.sdk.WebView, p1: String?) {
                super.onPageFinished(view, p1)
                try {
                    val screenWidth: Int = ScreenUtils.getScreenWidth(this@AboutUsActivity)
                    val width2: String = java.lang.String.valueOf(DensityUtils.px2dp(this@AboutUsActivity, screenWidth.toFloat()) - 20)
                    val javascript = "javascript:function ResizeImages() {" +
                            "var myimg,oldwidth;" +
                            "var maxwidth = document.body.clientWidth;" +
                            "for(i=0;i <document.images.length;i++){" +
                            "myimg = document.images[i];" +
                            "if(myimg.width > " + width2 + "){" +
                            "oldwidth = myimg.width;" +
                            "myimg.width =" + width2 + ";" +
                            "}" +
                            "}" +
                            "}"
                    view.loadUrl(javascript)
                    view.loadUrl("javascript:ResizeImages();")
                    view.loadUrl("javascript:App.resize(document.body.getBoundingClientRect().height)");

                } catch (e: Exception) {
                }
            }
            override fun shouldOverrideUrlLoading(p0: com.tencent.smtt.sdk.WebView?, request: com.tencent.smtt.export.external.interfaces.WebResourceRequest?): Boolean {
                return false
            }

        }
    }

    @JavascriptInterface
    fun resize(height: Float) {
        runOnUiThread(Runnable {
            wb_about_us.layoutParams = LinearLayout.LayoutParams(resources.displayMetrics.widthPixels, (height * resources.displayMetrics.density).toInt()+30)
        })
    }

    private fun setZoomControlGoneX(view: WebSettings, args: Array<Any>) {
        val classType: Class<*> = view.javaClass
        try {
            val argsClass: Array<Class<*>?> = arrayOfNulls(args.size)
            run {
                var i = 0
                val j = args.size
                while (i < j) {
                    argsClass[i] = args[i].javaClass
                    i++
                }
            }
            val ms: Array<Method> = classType.methods
            for (i in ms.indices) {
                if (ms[i].getName().equals("setDisplayZoomControls")) {
                    try {
                        ms[i].invoke(view, false)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                    break
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onResume() {
        super.onResume()
        wb_about_us.onResume()
        wb_about_us.settings.javaScriptEnabled = true
    }

    override fun onPause() {
        super.onPause()
        wb_about_us.onPause()
        wb_about_us.settings.lightTouchEnabled = false
    }

    override fun onDestroy() {
        if (wb_about_us != null) {
            wb_about_us.destroy()
        }
        super.onDestroy()
    }


    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETWEBDATA -> {
                    data?.let {
                        data as WebDataBean
                        wb_about_us.loadData(data.content, "text/html", "UTF-8")
                        tv_user_name.text = data.contact
                        tv_phone.text = data.phone
                        tv_address.text = data.address
                    }
                }
                else -> {

                }
            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {
        toast(string!!)
    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {
        if(boolean){
            toast("请检查网络连接")
        }
    }

}
