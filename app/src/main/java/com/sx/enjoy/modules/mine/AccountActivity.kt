package com.sx.enjoy.modules.mine

import android.content.Intent
import android.view.Gravity
import android.view.View
import com.likai.lib.commonutils.LoadingDialog
import com.likai.lib.commonutils.SharedPreferencesUtil
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.AuthUserBean
import com.sx.enjoy.bean.UpLoadImageData
import com.sx.enjoy.bean.UpLoadImageList
import com.sx.enjoy.bean.UserBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.event.FirstInitUserEvent
import com.sx.enjoy.event.UserAuthSuccessEvent
import com.sx.enjoy.event.UserStateChangeEvent
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.utils.GlideEngine
import com.sx.enjoy.utils.ImageLoaderUtil
import com.sx.enjoy.utils.UpLoadImageUtil
import com.sx.enjoy.view.dialog.NoticeDialog
import com.sx.enjoy.view.dialog.ReminderDialog
import com.sx.enjoy.view.dialog.SexSelectDialog
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.empty_public_network.*
import kotlinx.android.synthetic.main.title_public_view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
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

    override fun getTitleType() = PublicTitleData(C.TITLE_RIGHT_TEXT_BACKGROUND,"个人资料","保存")

    override fun getLayoutResource() = R.layout.activity_account

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        EventBus.getDefault().register(this)

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
        et_user_recommend.setText(user.referralCode)
        if(user.referralCode.isNotEmpty()){
            et_user_recommend.isEnabled = false
        }

        initEvent()
        present.getAuthUser(C.USER_ID)
    }

    private fun initEvent(){
        iv_network_error.setOnClickListener {
            present.getAuthUser(C.USER_ID)
        }
        ll_user_head.setOnClickListener {
            PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .imageSpanCount(3)
                .selectionMode(PictureConfig.MULTIPLE)
                .maxSelectNum(1)
                .isCamera(true)
                .loadImageEngine(GlideEngine.createGlideEngine())
                .enableCrop(true)
                .compress(true)
                .showCropGrid(false)
                .withAspectRatio(1,1)
                .hideBottomControls(true)
                .minimumCompressSize(1024)
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
        ll_pay_method.setOnClickListener {
            startActivity<PayMethodSetActivity>()
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
                SharedPreferencesUtil.putCommonInt(this@AccountActivity,"step",0)
                C.USER_ID = ""
                C.IS_SIGN_REQUEST = false
                LitePal.deleteAll(UserBean::class.java)
                EventBus.getDefault().post(UserStateChangeEvent(0))
                MobclickAgent.onProfileSignOff()
                finish()
            }
        })

        ll_public_right.setOnClickListener {
            if(et_user_name.text.toString().isEmpty()){
                toast("用户名不能为空").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun authSuccess(event: UserAuthSuccessEvent){
        present.getAuthUser(C.USER_ID)
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


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.UPDATEUSERINFO -> {
                    noticeDialog.showNotice(4)
                    user.updateAll("userId = ?", user.userId)
                    EventBus.getDefault().post(FirstInitUserEvent(false))
                }
                SXContract.GETAUTHUSER -> {
                    data.let {
                        em_network_view.visibility = View.GONE
                        data as AuthUserBean
                        when(data.status){
                            0 -> tv_user_auth.text = "未认证"
                            1 -> tv_user_auth.text = "待审核"
                            2 -> tv_user_auth.text = "已认证"
                            3 -> tv_user_auth.text = "已拒绝"
                        }
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
        if(isRefreshList){
            em_network_view.visibility = View.VISIBLE
        }else{
            toast("请检查网络连接").setGravity(Gravity.CENTER, 0, 0)
        }
    }

}