package com.sx.enjoy.modules.home

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.View
import android.webkit.JavascriptInterface
import android.widget.LinearLayout
import com.likai.lib.commonutils.DensityUtils
import com.likai.lib.commonutils.ScreenUtils
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.NewsDetailsBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.utils.ImageLoaderUtil
import com.tencent.smtt.sdk.WebSettings
import kotlinx.android.synthetic.main.activity_news_details.*
import kotlinx.android.synthetic.main.empty_public_network.*
import org.jetbrains.anko.toast
import java.lang.reflect.Method

class NewsDetailsActivity : BaseActivity(),SXContract.View {

    private lateinit var present : SXPresent

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"新闻详情")

    override fun getLayoutResource() = R.layout.activity_news_details

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        initWebView()

        present.getNewsDetails(intent.getStringExtra("nid"))

        iv_network_error.setOnClickListener {
            present.getNewsDetails(intent.getStringExtra("nid"))
        }
    }


    private fun initWebView(){
        wb_news_details.settings.setSupportZoom(true)
        wb_news_details.settings.builtInZoomControls = true
        wb_news_details.settings.displayZoomControls = true
        wb_news_details.settings.blockNetworkImage = false
        wb_news_details.settings.loadsImagesAutomatically = true
        wb_news_details.settings.defaultTextEncodingName = "utf-8"
        wb_news_details.addJavascriptInterface(this, "App")
        wb_news_details.settings.javaScriptEnabled = true
        wb_news_details.settings.setSupportZoom(false)
        wb_news_details.settings.builtInZoomControls = false
        setZoomControlGoneX(wb_news_details.settings, arrayOf(false))

        wb_news_details.webViewClient = object : com.tencent.smtt.sdk.WebViewClient() {
            override fun onPageFinished(view: com.tencent.smtt.sdk.WebView, p1: String?) {
                super.onPageFinished(view, p1)
                try {
                    val screenWidth: Int = ScreenUtils.getScreenWidth(this@NewsDetailsActivity)
                    val width2: String = java.lang.String.valueOf(DensityUtils.px2dp(this@NewsDetailsActivity, screenWidth.toFloat()) - 20)
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
            wb_news_details.layoutParams = LinearLayout.LayoutParams(resources.displayMetrics.widthPixels, (height * resources.displayMetrics.density).toInt()+30)
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
        wb_news_details.onResume()
        wb_news_details.settings.javaScriptEnabled = true
    }

    override fun onPause() {
        super.onPause()
        wb_news_details.onPause()
        wb_news_details.settings.lightTouchEnabled = false
    }

    override fun onDestroy() {
        if (wb_news_details != null) {
            wb_news_details.destroy()
        }
        super.onDestroy()
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETNEWSDETAILS -> {
                    em_network_view.visibility = View.GONE
                    data?.let {
                        data as NewsDetailsBean
                        wb_news_details.loadData(data.content, "text/html", "UTF-8")
                        ImageLoaderUtil().displayCommodityInfoImage(this,data.img,iv_news_image)
                        tv_new_title.text = data.title
                        tv_new_time.text = data.createTime
                    }
                }
                else -> {

                }
            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {
        em_network_view.visibility = View.GONE
        toast(string!!).setGravity(Gravity.CENTER, 0, 0)
    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {
        if(boolean){
            em_network_view.visibility = View.VISIBLE
        }
    }

}
