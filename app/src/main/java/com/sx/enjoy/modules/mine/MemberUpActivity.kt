package com.sx.enjoy.modules.mine

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.View
import android.webkit.JavascriptInterface
import android.widget.LinearLayout
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
import com.tencent.smtt.sdk.WebSettings
import kotlinx.android.synthetic.main.activity_member_up.*
import kotlinx.android.synthetic.main.empty_public_network.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.litepal.LitePal
import java.lang.reflect.Method

class MemberUpActivity : BaseActivity() , SXContract.View{

    private lateinit var present: SXPresent

    private lateinit var levelDialog : LevelUpDialog

    private lateinit var user : UserBean

    private var type = 0

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL,if(type == 0) "会员等级" else "达人等级")

    override fun getLayoutResource() = R.layout.activity_member_up

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        levelDialog = LevelUpDialog(this)

        type = intent.getIntExtra("type",0)
        user = LitePal.findLast(UserBean::class.java)

        initWebView()

        if(type == 0){
            present.getMemberInfo(C.USER_ID)
        }else{
            present.getFutureLevel(C.USER_ID)
        }

        initEvent()
    }

    private fun initEvent(){
        tv_level_up.setOnClickListener {
            if(type == 0){
                if(user.membershipLevel<=0&&user.isReai != "1"){
                    startActivity<AuthenticationActivity>()
                    return@setOnClickListener
                }
                present.memberUp(C.USER_ID)
            }else{
                present.expertUpgrade(C.USER_ID)
            }
        }
        iv_network_error.setOnClickListener {
            if(type == 0){
                present.getMemberInfo(C.USER_ID)
            }else{
                present.getFutureLevel(C.USER_ID)
            }
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
                    val screenWidth: Int = ScreenUtils.getScreenWidth(this@MemberUpActivity)
                    val width2: String = java.lang.String.valueOf(DensityUtils.px2dp(this@MemberUpActivity, screenWidth.toFloat()) - 20)
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
                SXContract.GETMEMBERINFO -> {
                    em_network_view.visibility = View.GONE
                    data?.let {
                        data as MemberUpBean
                        tv_level_up.visibility = View.VISIBLE

                        val url = "<div style=\"height: 1px;width:100%;background-color:#e6e7e8\">\n" +
                                "   </div>\n" +
                                " <div style=\"color:#323233;font-size:16px;background-color:#F7F8FA;\">\n" +
                                "  <div style=\"background-color:#FFFFFF;\">\n" +
                                "   <div style=\"padding:10px 16px;font-size:14px;\">\n" +
                                "    <div>\n" +
                                "     用户当前VIP等级\n" +
                                "    </div>\n" +
                                "    <div style=\"color:#969799;text-align:right;vertical-align:middle;margin-top: -20px\">\n" +
                                "     {{userLevel}}\n" +
                                "    </div>\n" +
                                "   </div>\n" +
                                "  </div>\n" +
                                " </div>\n" +
                                " <div style=\"height: 1px;width:100%;background-color:#e6e7e8\">\n" +
                                " </div>\n" +
                                " <div style=\"color:#323233;font-size:16px;background-color:#F7F8FA;\">\n" +
                                "  <div style=\"background-color:#FFFFFF;\">\n" +
                                "   <div style=\"padding:10px 16px;font-size:14px;\">\n" +
                                "    <div>\n" +
                                "     下一个等级\n" +
                                "    </div>\n" +
                                "    <div style=\"color:#969799;text-align:right;vertical-align:middle;margin-top: -20px\">\n" +
                                "     {{nextLevel}}\n" +
                                "    </div>\n" +
                                "   </div>\n" +
                                "  </div>\n" +
                                " </div>\n" +
                                " <div style=\"height: 1px;width:100%;background-color:#e6e7e8\">\n" +
                                " </div>\n" +
                                " <div style=\"color:#323233;font-size:16px;background-color:#F7F8FA;\">\n" +
                                "  <div style=\"background-color:#FFFFFF;\">\n" +
                                "   <div style=\"padding:10px 16px;font-size:14px;\">\n" +
                                "    <div>\n" +
                                "     当前手续费\n" +
                                "    </div>\n" +
                                "    <div style=\"color:#969799;text-align:right;vertical-align:middle;margin-top: -20px\">\n" +
                                "     {{userFee}}\n" +
                                "    </div>\n" +
                                "   </div>\n" +
                                "  </div>\n" +
                                " </div>\n" +
                                " <div style=\"height: 1px;width:100%;background-color:#e6e7e8\">\n" +
                                " </div>\n" +
                                " <div style=\"color:#323233;font-size:16px;background-color:#F7F8FA;\">\n" +
                                "  <div style=\"background-color:#FFFFFF;\">\n" +
                                "   <div style=\"padding:10px 16px;font-size:14px;\">\n" +
                                "    <div>\n" +
                                "     下一级手续费\n" +
                                "    </div>\n" +
                                "    <div style=\"color:#969799;text-align:right;vertical-align:middle;margin-top: -20px\">\n" +
                                "     {{nextFee}}\n" +
                                "    </div>\n" +
                                "   </div>\n" +
                                "  </div>\n" +
                                " </div>\n" +
                                " <div style=\"height: 1px;width:100%;background-color:#e6e7e8\">\n" +
                                " </div>\n" +
                                " <div style=\"color:#323233;font-size:16px;background-color:#F7F8FA;\">\n" +
                                "  <div style=\"background-color:#FFFFFF;\">\n" +
                                "   <div style=\"padding:10px 16px;font-size:14px;\">\n" +
                                "    <div>\n" +
                                "     卷轴要求\n" +
                                "    </div>\n" +
                                "    <div style=\"color:#969799;text-align:right;vertical-align:middle;margin-top: -20px\">\n" +
                                "     {{scrollSpread}}\n" +
                                "    </div>\n" +
                                "   </div>\n" +
                                "  </div>\n" +
                                " </div>\n" +
                                " <div style=\"height: 1px;width:100%;background-color:#e6e7e8\">\n" +
                                " </div>\n" +
                                " <div style=\"color:#323233;font-size:16px;background-color:#F7F8FA;\">\n" +
                                "  <div style=\"background-color:#FFFFFF;\">\n" +
                                "   <div style=\"padding:10px 16px;font-size:14px;\">\n" +
                                "    <div>\n" +
                                "     奖励任务卷轴\n" +
                                "    </div>\n" +
                                "    <div style=\"color:#969799;text-align:right;vertical-align:middle;margin-top: -20px\">\n" +
                                "     {{task}}\n" +
                                "    </div>\n" +
                                "   </div>\n" +
                                "  </div>\n" +
                                " </div>\n" +
                                " <div style=\"height: 1px;width:100%;background-color:#e6e7e8\">\n" +
                                " </div>\n" +
                                " <div style=\"color:#323233;font-size:16px;background-color:#F7F8FA;\">\n" +
                                "  <div style=\"background-color:#FFFFFF;\">\n" +
                                "   <div style=\"padding:10px 16px;font-size:14px;\">\n" +
                                "    <div>\n" +
                                "     奖励个数\n" +
                                "    </div>\n" +
                                "    <div style=\"color:#969799;text-align:right;vertical-align:middle;margin-top: -20px\">\n" +
                                "     {{taskCount}}\n" +
                                "    </div>\n" +
                                "   </div>\n" +
                                "  </div>\n" +
                                " </div>\n" +
                                " <div style=\"height: 1px;width:100%;background-color:#e6e7e8\">\n" +
                                " </div>\n" +
                                " <div style=\"color:#323233;font-size:16px;background-color:#F7F8FA;\">\n" +
                                "  <div style=\"background-color:#FFFFFF;\">\n" +
                                "   <div style=\"padding:10px 16px;font-size:14px;\">\n" +
                                "    <div>\n" +
                                "     奖励活跃值\n" +
                                "    </div>\n" +
                                "    <div style=\"color:#969799;text-align:right;vertical-align:middle;margin-top: -20px\">\n" +
                                "     {{active}}\n" +
                                "    </div>\n" +
                                "   </div>\n" +
                                "  </div>\n" +
                                " </div>\n" +
                                " <div style=\"height: 1px;width:100%;background-color:#e6e7e8\">\n" +
                                " </div>\n" +
                                " <div style=\"color:#323233;font-size:16px;background-color:#F7F8FA;\">\n" +
                                "  <div style=\"background-color:#FFFFFF;\">\n" +
                                "   <div style=\"padding:10px 16px;font-size:14px;\">\n" +
                                "    <div>\n" +
                                "     奖励米粒数\n" +
                                "    </div>\n" +
                                "    <div style=\"color:#969799;text-align:right;vertical-align:middle;margin-top: -20px\">\n" +
                                "     {{rice}}\n" +
                                "    </div>\n" +
                                "   </div>\n" +
                                "  </div>\n" +
                                " </div>\n" +
                                " <div style=\"height: 1px;width:100%;background-color:#e6e7e8\">\n" +
                                " </div>\n" +
                                " <div style=\"color:#323233;font-size:16px;background-color:#F7F8FA;\">\n" +
                                "  <div style=\"background-color:#FFFFFF;\">\n" +
                                "   <div style=\"padding:10px 16px;font-size:14px;\">\n" +
                                "    <div>\n" +
                                "     当前经验值\n" +
                                "    </div>\n" +
                                "    <div style=\"color:#969799;text-align:right;vertical-align:middle;margin-top: -20px\">\n" +
                                "     {{experience}}\n" +
                                "    </div>\n" +
                                "   </div>\n" +
                                "  </div>\n" +
                                " </div>\n" +
                                " <div style=\"height: 1px;width:100%;background-color:#e6e7e8\">\n" +
                                " </div>\n" +
                                " <div style=\"color:#323233;font-size:16px;background-color:#F7F8FA;\">\n" +
                                "  <div style=\"background-color:#FFFFFF;\">\n" +
                                "   <div style=\"padding:10px 16px;font-size:14px;\">\n" +
                                "    <div>\n" +
                                "     所需经验值\n" +
                                "    </div>\n" +
                                "    <div style=\"color:#969799;text-align:right;vertical-align:middle;margin-top: -20px\">\n" +
                                "     {{nextExperience}}\n" +
                                "    </div>\n" +
                                "   </div>\n" +
                                "  </div>\n" +
                                " </div>\n" +
                                " <div style=\"height: 1px;width:100%;background-color:#e6e7e8\">\n" +
                                " </div>\n" +
                                " <div style=\"color:#323233;font-size:16px;background-color:#F7F8FA;\">\n" +
                                "  <div style=\"background-color:#FFFFFF;\">\n" +
                                "   <div style=\"padding:10px 16px;font-size:14px;\">\n" +
                                "    <div>\n" +
                                "     升级要求\n" +
                                "    </div>\n" +
                                "    <div style=\"color:#969799;text-align:right;vertical-align:middle;margin-top: -20px\">\n" +
                                "     {{upgradeReq}}\n" +
                                "    </div>\n" +
                                "   </div>\n" +
                                "  </div>\n" +
                                " </div>\n" +
                                " <div style=\"height: 1px;width:100%;background-color:#e6e7e8\">\n" +
                                " </div>"

                        wb_member_up.loadData(data.content, "text/html", "UTF-8")
                        if(data.isUpgrade){
                            tv_level_up.isClickable = true
                            tv_level_up.setBackgroundResource(R.drawable.bg_main_full_2)
                        }else{
                            tv_level_up.isClickable = user.membershipLevel<=0
                            tv_level_up.setBackgroundResource(R.drawable.bg_main_1_full_2)
                        }
                    }
                }
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
                SXContract.MEMBERUP -> {
                    EventBus.getDefault().post(MemberUpSuccessEvent(1))
                    present.getMemberInfo(C.USER_ID)
                    levelDialog.show()
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
        em_network_view.visibility = View.GONE
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