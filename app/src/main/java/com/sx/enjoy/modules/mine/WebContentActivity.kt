package com.sx.enjoy.modules.mine

import android.annotation.SuppressLint
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
import kotlinx.android.synthetic.main.activity_web_content.*
import org.jetbrains.anko.toast
import java.lang.reflect.Method

class WebContentActivity : BaseActivity() ,SXContract.View{

    private lateinit var present: SXPresent

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL,intent.getStringExtra("title"))

    override fun getLayoutResource() = R.layout.activity_web_content

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        initWebView()
        present.getWebData(intent.getIntExtra("type",1).toString())
    }


    private fun initWebView(){
        wb_view.settings.setSupportZoom(true)
        wb_view.settings.builtInZoomControls = true
        wb_view.settings.displayZoomControls = true
        wb_view.settings.blockNetworkImage = false
        wb_view.settings.loadsImagesAutomatically = true
        wb_view.settings.defaultTextEncodingName = "utf-8"
        wb_view.addJavascriptInterface(this, "App")
        wb_view.settings.javaScriptEnabled = true
        wb_view.settings.setSupportZoom(false)
        wb_view.settings.builtInZoomControls = false
        setZoomControlGoneX(wb_view.settings, arrayOf(false))

        wb_view.webViewClient = object : com.tencent.smtt.sdk.WebViewClient() {
            override fun onPageFinished(view: com.tencent.smtt.sdk.WebView, p1: String?) {
                super.onPageFinished(view, p1)
                try {
                    val screenWidth: Int = ScreenUtils.getScreenWidth(this@WebContentActivity)
                    val width2: String = java.lang.String.valueOf(DensityUtils.px2dp(this@WebContentActivity, screenWidth.toFloat()) - 20)
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
            wb_view.layoutParams = LinearLayout.LayoutParams(resources.displayMetrics.widthPixels, (height * resources.displayMetrics.density).toInt()+30)
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
        wb_view.onResume()
        wb_view.settings.javaScriptEnabled = true
    }

    override fun onPause() {
        super.onPause()
        wb_view.onPause()
        wb_view.settings.lightTouchEnabled = false
    }

    override fun onDestroy() {
        if (wb_view != null) {
            wb_view.destroy()
        }
        super.onDestroy()
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETWEBDATA -> {
                    data?.let {
                        data as WebDataBean
                        wb_view.loadData(data.content, "text/html", "UTF-8")
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
