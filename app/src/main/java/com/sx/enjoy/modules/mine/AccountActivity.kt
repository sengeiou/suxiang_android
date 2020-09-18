package com.sx.enjoy.modules.mine

import android.content.Intent
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import com.sx.enjoy.modules.login.LoginActivity
import com.sx.enjoy.utils.ImageLoaderUtil
import com.sx.enjoy.view.dialog.SexSelectDialog
import kotlinx.android.synthetic.main.activity_account.*
import org.jetbrains.anko.startActivity

class AccountActivity : BaseActivity() {

    private lateinit var sexDialog : SexSelectDialog

    override fun getTitleType() = PublicTitleData(C.TITLE_RIGHT_TEXT,"个人资料","保存")

    override fun getLayoutResource() = R.layout.activity_account

    override fun initView() {
        sexDialog = SexSelectDialog(this)

        initEvent()
    }

    private fun initEvent(){
        ll_user_head.setOnClickListener {
            PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .imageSpanCount(3)
                .selectionMode(PictureConfig.MULTIPLE)
                .maxSelectNum(1)
                .isCamera(true)
                .cropWH(100,100)
                .compress(true)
                .withAspectRatio(1,1)
                .hideBottomControls(false)
                .forResult(1001)
        }
        ll_sex_select.setOnClickListener {
            sexDialog.show()
        }
        ll_login_password.setOnClickListener {
            startActivity<LoginPasswordActivity>()
        }
        ll_pay_password.setOnClickListener {
            startActivity<PayPasswordActivity>()
        }
        ll_user_authentication.setOnClickListener {
            startActivity<AuthenticationActivity>()
        }
        ll_user_phone.setOnClickListener {
            startActivity<PhoneChangeActivity>()
        }
        tv_login_exit.setOnClickListener {
            startActivity<LoginActivity>()
        }

        sexDialog.setOnNoticeConfirmListener(object :SexSelectDialog.OnNoticeConfirmListener{
            override fun onConfirm(sex: Int) {
                tv_user_sex.text = if(sex == 1) "男" else "女"
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                1001 -> {
                    val selectList = PictureSelector.obtainMultipleResult(data)
                    ImageLoaderUtil().displayImage(this,selectList[0].compressPath,iv_user_head)
                }
            }
        }
    }

}