package com.sx.enjoy.modules.mine

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.View
import android.webkit.JavascriptInterface
import android.widget.LinearLayout
import com.tencent.smtt.sdk.WebSettings
import com.likai.lib.commonutils.DensityUtils
import com.likai.lib.commonutils.ScreenUtils
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.FutureLevelBean
import com.sx.enjoy.bean.MemberUpBean
import com.sx.enjoy.bean.UserBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.event.MemberUpSuccessEvent
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.view.dialog.LevelUpDialog
import kotlinx.android.synthetic.main.activity_talent_up.*
import kotlinx.android.synthetic.main.empty_public_network.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.litepal.LitePal
import java.lang.reflect.Method

class TalentUpActivity : BaseActivity() , SXContract.View{


    private lateinit var present: SXPresent

    private lateinit var levelDialog : LevelUpDialog

    private lateinit var user : UserBean

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL,"达人等级")

    override fun getLayoutResource() = R.layout.activity_talent_up

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        levelDialog = LevelUpDialog(this)

        user = LitePal.findLast(UserBean::class.java)

        initWebView()

        present.getFutureLevel(C.USER_ID)

        initEvent()
    }

    private fun initEvent(){
        tv_level_up.setOnClickListener {
            present.expertUpgrade(C.USER_ID)
        }
        iv_network_error.setOnClickListener {
            present.getFutureLevel(C.USER_ID)
        }
    }

    private fun initWebView(){
        wb_member_up.settings.setSupportZoom(true)
        wb_member_up.settings.builtInZoomControls = true
        wb_member_up.settings.displayZoomControls = true
        wb_member_up.settings.blockNetworkImage = false
        wb_member_up.settings.loadsImagesAutomatically = true
        wb_member_up.settings.defaultTextEncodingName = "utf-8"
        wb_member_up.settings.javaScriptEnabled = true
        wb_member_up.settings.setSupportZoom(false)
        wb_member_up.settings.builtInZoomControls = false
        wb_member_up.addJavascriptInterface(this, "App")
        setZoomControlGoneX(wb_member_up.settings, arrayOf(false))

        wb_member_up.webViewClient = object : com.tencent.smtt.sdk.WebViewClient() {
            override fun onPageFinished(view: com.tencent.smtt.sdk.WebView, p1: String?) {
                super.onPageFinished(view, p1)
                try {
                    val screenWidth: Int = ScreenUtils.getScreenWidth(this@TalentUpActivity)
                    val width2: String = java.lang.String.valueOf(DensityUtils.px2dp(this@TalentUpActivity, screenWidth.toFloat()) - 20)
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
            wb_member_up.layoutParams = LinearLayout.LayoutParams(resources.displayMetrics.widthPixels, (height * resources.displayMetrics.density).toInt()+30)
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
        wb_member_up.onResume()
        wb_member_up.settings.javaScriptEnabled = true
    }

    override fun onPause() {
        super.onPause()
        wb_member_up.onPause()
        wb_member_up.settings.lightTouchEnabled = false
    }

    override fun onDestroy() {
        if (wb_member_up != null) {
            wb_member_up.destroy()
        }
        super.onDestroy()
    }


    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETFUTURELEVEL -> {
                    em_network_view.visibility = View.GONE
                    data?.let {
                        data as FutureLevelBean
                        tv_level_up.visibility = View.VISIBLE
                        wb_member_up.loadData(data.content, "text/html", "UTF-8")
                        if(data.isUpgrade){
                            tv_level_up.isClickable = true
                            tv_level_up.setBackgroundResource(R.drawable.bg_balck_full_2)
                        }else{
                            tv_level_up.isClickable = false
                            tv_level_up.setBackgroundResource(R.drawable.bg_balck_1_full_2)
                        }
                    }
                }
                SXContract.EXPERTUPGRADE -> {
                    EventBus.getDefault().post(MemberUpSuccessEvent(2))
                    present.getFutureLevel(C.USER_ID)
                    levelDialog.show()
                }
                else -> {

                }
            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {
        if(isRefreshList){
            em_network_view.visibility = View.GONE
        }
        toast(string!!).setGravity(Gravity.CENTER, 0, 0)
    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {
        if(isRefreshList){
            em_network_view.visibility = View.VISIBLE
        }else{
            toast("请检查网络连接").setGravity(Gravity.CENTER, 0, 0)
        }
    }

}
