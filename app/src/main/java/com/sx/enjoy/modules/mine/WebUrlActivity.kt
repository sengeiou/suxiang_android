package com.sx.enjoy.modules.mine

import android.annotation.SuppressLint
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import kotlinx.android.synthetic.main.activity_web_url.*

class WebUrlActivity : BaseActivity() {

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL, intent.getStringExtra("title"))

    override fun getLayoutResource(): Int = R.layout.activity_web_url

    override fun initView() {
        wb_info.settings.setSupportZoom(true)
        wb_info.settings.builtInZoomControls = true
        wb_info.settings.displayZoomControls = true
        wb_info.settings.blockNetworkImage = false
        wb_info.settings.loadsImagesAutomatically = true
        wb_info.settings.defaultTextEncodingName = "utf-8"
        wb_info.settings.javaScriptEnabled = true
        wb_info.settings.setSupportZoom(false)
        wb_info.settings.builtInZoomControls = false
        wb_info.addJavascriptInterface(this, "App")

        wb_info.webViewClient = object : com.tencent.smtt.sdk.WebViewClient() {
            override fun shouldOverrideUrlLoading(p0: com.tencent.smtt.sdk.WebView?, p1: String?): Boolean {
                p0?.loadUrl(p1)
                return super.shouldOverrideUrlLoading(p0, p1)
            }
        }

        wb_info.loadUrl(intent.getStringExtra("url"))
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onResume() {
        super.onResume()
        wb_info.onResume()
        wb_info.settings.javaScriptEnabled = true
    }

    override fun onPause() {
        super.onPause()
        wb_info.onPause()
        wb_info.settings.lightTouchEnabled = false
    }

    override fun onDestroy() {
        if (wb_info != null) {
            wb_info.destroy()
        }
        super.onDestroy()
    }
}
