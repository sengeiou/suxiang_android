package com.sx.enjoy.modules.mine

import android.Manifest
import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.view.Gravity
import com.gyf.immersionbar.ImmersionBar
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.UserBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.utils.CodeUtil
import com.sx.enjoy.utils.ImageLoaderUtil
import com.sx.enjoy.utils.ScreenShotUtil
import com.yanzhenjie.permission.AndPermission
import kotlinx.android.synthetic.main.activity_smart_earn.*
import org.jetbrains.anko.toast
import org.litepal.LitePal

class SmartEarnActivity : BaseActivity() {

    override fun getTitleType() = PublicTitleData(C.TITLE_CUSTOM,"速享赚赚")

    override fun getLayoutResource() = R.layout.activity_smart_earn

    override fun initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).titleBar(tb_earn_title).init()

        val user = LitePal.findLast(UserBean::class.java)

        ImageLoaderUtil().displayImage(this,user.userImg,iv_user_head)
        tv_user_name.text = user.userName
        tv_user_star.text = user.userLevelName
        if(user.userLink.isNotEmpty()){
            iv_user_code.setImageBitmap(CodeUtil.createQrcode(user.userLink))
        }


        ll_public_back.setOnClickListener {
            finish()
        }

        bt_save.setOnClickListener {
            checkDownloadPermission()
        }

    }


    private fun checkDownloadPermission(){
        AndPermission.with(this)
            .runtime()
            .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            .onGranted { permissions ->
                val saveRunnable = Runnable {
                    val isSuccess = ScreenShotUtil.saveScreenshotFromView(ll_earn_view,this)

                    val msg = Message()
                    msg.what = if(isSuccess)1 else 0
                    mHandler.sendMessage(msg)
                }

                val saveThread = Thread(saveRunnable)
                saveThread.start()
            }
            .onDenied { permissions ->

            }
            .start()
    }

    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            if(msg.what == 1){
                toast("保存成功")
            }else{
                toast("保存失败")
            }
        }
    }

}