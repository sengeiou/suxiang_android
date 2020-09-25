package com.sx.enjoy.modules.mine

import android.content.Intent
import com.likai.lib.commonutils.LoadingDialog
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.UpLoadImageData
import com.sx.enjoy.bean.UpLoadImageList
import com.sx.enjoy.bean.UserBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.event.UserStateChangeEvent
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.utils.ImageLoaderUtil
import com.sx.enjoy.utils.UpLoadImageUtil
import com.sx.enjoy.view.dialog.NoticeDialog
import com.sx.enjoy.view.dialog.ReminderDialog
import com.sx.enjoy.view.dialog.SexSelectDialog
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.title_public_view.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import org.litepal.LitePal


class AccountActivity : BaseActivity() ,SXContract.View{

    private lateinit var reminderDialog : ReminderDialog
    private lateinit var sexDialog : SexSelectDialog
    private lateinit var noticeDialog : NoticeDialog
    private lateinit var loadingDialog : LoadingDialog

    private lateinit var present :SXPresent

    private lateinit var user : UserBean

    private var uploadTask : UpLoadImageUtil? = null

    private var photo = ""


    override fun getTitleType() = PublicTitleData(C.TITLE_RIGHT_TEXT,"个人资料","保存")

    override fun getLayoutResource() = R.layout.activity_account

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        sexDialog = SexSelectDialog(this)
        reminderDialog = ReminderDialog(this)
        noticeDialog = NoticeDialog(this)
        loadingDialog = LoadingDialog(this)

        uploadTask = UpLoadImageUtil(this,present)

        user = LitePal.findLast(UserBean::class.java)
        ImageLoaderUtil().displayHeadImage(this,user.userImg,iv_user_head)
        et_user_name.setText(if(user.userName.isEmpty()) user.userPhone else user.userName)
        tv_user_sex.text = user.sex
        et_email.setText(user.email)
        tv_user_phone.text = user.userPhone
        et_user_address.setText(user.address)
        tv_wx_number.text = user.wxNumber
        tv_user_auth.text = if(user.isReai.isEmpty()||user.isReai == "0") "未认证" else "已认证"
        et_user_recommend.setText(user.referralCode)
        if(user.referralCode.isNotEmpty()){
            et_user_recommend.isEnabled = false
        }

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
            startActivityForResult<PhoneChangeActivity>(1002,Pair("phone",user.userPhone))
        }
        tv_login_exit.setOnClickListener {
            reminderDialog.showReminder(1)
        }

        sexDialog.setOnNoticeConfirmListener(object :SexSelectDialog.OnNoticeConfirmListener{
            override fun onConfirm(sex: Int) {
                tv_user_sex.text = if(sex == 1) "男" else "女"
            }
        })
        reminderDialog.setOnNoticeConfirmListener(object :ReminderDialog.OnNoticeConfirmListener{
            override fun onConfirm() {
                C.USER_ID = ""
                C.IS_SIGN_REQUEST = false
                LitePal.deleteAll(UserBean::class.java)
                EventBus.getDefault().post(UserStateChangeEvent(0))
                finish()
            }
        })

        ll_public_right.setOnClickListener {
            if(photo.isEmpty()){
                user.userName = et_user_name.text.toString()
                user.sex = tv_user_sex.text.toString()
                user.email = et_email.text.toString()
                user.address = et_user_address.text.toString()
                present.updateUserInfo(C.USER_ID,user.userImg,et_user_name.text.toString(),tv_user_sex.text.toString(),et_email.text.toString(),et_user_address.text.toString(),et_user_recommend.text.toString())
            }else{
                val imageList = arrayListOf<UpLoadImageData>()
                val logoImage = arrayListOf<UpLoadImageList>()
                logoImage.add(UpLoadImageList(photo))
                imageList.add(UpLoadImageData(logoImage,1,"头像"))
                uploadTask?.addImagesToSources(imageList)
                uploadTask?.start()
                loadingDialog.showLoading("上传中...")
            }
        }

        uploadTask?.setOnUploadImageResultListener { result, sources ->
            loadingDialog.dismiss()
            if(result){
                user.userName = et_user_name.text.toString()
                user.userImg = sources[0].imageList[0].netPath
                user.sex = tv_user_sex.text.toString()
                user.email = et_email.text.toString()
                user.address = et_user_address.text.toString()
                present.updateUserInfo(C.USER_ID,user.userImg,et_user_name.text.toString(),tv_user_sex.text.toString(),et_email.text.toString(),et_user_address.text.toString(),et_user_recommend.text.toString())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                1001 -> {
                    val selectList = PictureSelector.obtainMultipleResult(data)
                    photo = selectList[0].compressPath
                    ImageLoaderUtil().displayImage(this,selectList[0].compressPath,iv_user_head)
                }
                1002 -> {
                    val phone = data!!.getStringExtra("phone")
                    tv_user_phone.text = phone
                    user.userPhone = phone
                    user.updateAll("userId = ?", user.userId)
                }
            }
        }
    }


    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.UPDATEUSERINFO -> {
                    noticeDialog.showNotice(4)
                    user.updateAll("userId = ?", user.userId)
                    EventBus.getDefault().post(UserStateChangeEvent(1))
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