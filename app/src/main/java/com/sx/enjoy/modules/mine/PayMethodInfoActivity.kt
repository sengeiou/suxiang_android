package com.sx.enjoy.modules.mine

import android.app.Activity
import android.content.Intent
import android.view.Gravity
import android.view.View
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
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.utils.GlideEngine
import com.sx.enjoy.utils.ImageLoaderUtil
import com.sx.enjoy.utils.UpLoadImageUtil
import com.sx.enjoy.view.dialog.NoticeDialog
import com.sx.enjoy.view.dialog.ReminderDialog
import kotlinx.android.synthetic.main.activity_pay_method_info.*
import kotlinx.android.synthetic.main.title_public_view.*
import org.jetbrains.anko.toast
import org.litepal.LitePal

class PayMethodInfoActivity : BaseActivity() ,SXContract.View{

    private lateinit var present: SXPresent

    private var uploadTask : UpLoadImageUtil? = null

    private lateinit var loadingDialog : LoadingDialog
    private lateinit var noticeDialog : NoticeDialog
    private lateinit var reminderDialog: ReminderDialog

    private lateinit var user : UserBean

    private var type = 1
    private var photo = ""

    override fun getTitleType() = PublicTitleData (C.TITLE_RIGHT_TEXT,"收款账号","删除",0,R.color.title_main_color)

    override fun getLayoutResource() = R.layout.activity_pay_method_info

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        user = LitePal.findLast(UserBean::class.java)

        type = intent.getIntExtra("type",1)

        uploadTask = UpLoadImageUtil(this,present)
        loadingDialog = LoadingDialog(this)
        noticeDialog = NoticeDialog(this)
        reminderDialog = ReminderDialog(this)

        if(type == 1){
            tv_pay_method.text = "支付宝"
            if(user.isAliPay){
                et_account.setText(user.aliNumber)
                et_user_name.setText(user.aliPayName)
                ImageLoaderUtil().displayImage(this,user.payQrcode,iv_upload_documents)
            }
        }else{
            tv_pay_method.text = "微信"
            ll_pay_account.visibility = View.GONE
            if(user.isWxPay){
                et_user_name.setText(user.wxPayName)
                ImageLoaderUtil().displayImage(this,user.wxQrcode,iv_upload_documents)
            }
        }


        initEvent()
    }

    private fun initEvent(){
        iv_upload_documents.setOnClickListener {
            PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .imageSpanCount(3)
                .selectionMode(PictureConfig.MULTIPLE)
                .maxSelectNum(1)
                .isCamera(true)
                .loadImageEngine(GlideEngine.createGlideEngine())
                .enableCrop(true)
                .compress(true)
                .isDragFrame(true)
                .freeStyleCropEnabled(true)
                .showCropGrid(false)
                .withAspectRatio(1,1)
                .hideBottomControls(true)
                .minimumCompressSize(1024)
                .forResult(4001)
        }

        ll_public_right.setOnClickListener {
            reminderDialog.showReminder(8)
        }

        tv_save.setOnClickListener {
            if(type == 1&&et_account.text.isEmpty()){
                toast("请输入账号").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            if(et_user_name.text.isEmpty()){
                toast("请输入真实姓名").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            if(type == 1){
                if(photo.isEmpty()&&!user.isAliPay){
                    toast("请选择上传收款码").setGravity(Gravity.CENTER, 0, 0)
                    return@setOnClickListener
                }
            }else{
                if(photo.isEmpty()&&!user.isWxPay){
                    toast("请选择上传收款码").setGravity(Gravity.CENTER, 0, 0)
                    return@setOnClickListener
                }
            }
            if(photo.isEmpty()){
                if(type == 1){
                    user.isAliPay = true
                    user.aliNumber = et_account.text.toString()
                    user.aliPayName = et_user_name.text.toString()
                    present.postPayMethodInfo(C.USER_ID,type.toString(),"","",et_user_name.text.toString(),user.payQrcode,et_account.text.toString())
                }else{
                    user.isWxPay = true
                    user.wxPayName = et_user_name.text.toString()
                    present.postPayMethodInfo(C.USER_ID,type.toString(),et_user_name.text.toString(),user.wxQrcode,"","","")
                }
            }else{
                val imageList = arrayListOf<UpLoadImageData>()
                val logoImage = arrayListOf<UpLoadImageList>()
                logoImage.add(UpLoadImageList(photo))
                imageList.add(UpLoadImageData(logoImage,1,"收款码"))
                uploadTask?.addImagesToSources(imageList)
                uploadTask?.start()
                loadingDialog.showLoading("上传中...")
            }
        }

        uploadTask?.setOnUploadImageResultListener { result, sources ->
            loadingDialog.dismiss()
            if(result){
                if(type == 1){
                    user.setIsAliPay(true)
                    user.aliNumber = et_account.text.toString()
                    user.aliPayName = et_user_name.text.toString()
                    user.payQrcode = sources[0].imageList[0].netPath
                    present.postPayMethodInfo(C.USER_ID,type.toString(),"","",et_user_name.text.toString(),sources[0].imageList[0].netPath,et_account.text.toString())
                }else{
                    user.setIsWxPay(true)
                    user.wxPayName = et_user_name.text.toString()
                    user.wxQrcode = sources[0].imageList[0].netPath
                    present.postPayMethodInfo(C.USER_ID,type.toString(),et_user_name.text.toString(),sources[0].imageList[0].netPath,"","","")
                }
            }
        }

        reminderDialog.setOnNoticeConfirmListener(object :ReminderDialog.OnNoticeConfirmListener{
            override fun onConfirm() {
                if(type == 1){
                    user.setIsAliPay(false)
                    user.aliPayName = ""
                    user.payQrcode = ""
                    user.aliNumber = ""
                }else{
                    user.setIsWxPay(false)
                    user.wxPayName = ""
                    user.wxQrcode = ""
                }
                present.deletePayMethodInfo(C.USER_ID,type.toString())
            }
        })
        noticeDialog.setOnDismissListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                4001 -> {
                    val selectList = PictureSelector.obtainMultipleResult(data)
                    photo = selectList[0].compressPath
                    ImageLoaderUtil().displayImage(this,selectList[0].compressPath,iv_upload_documents)
                }
            }
        }
    }


    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.POSTPAYMETHODINFO -> {
                    noticeDialog.showNotice(7)
                    user.updateAll("userId = ?", user.userId)
                    val intent = Intent()
                    intent.putExtra("status",1)
                    setResult(RESULT_OK,intent)
                }
                SXContract.DELETEPAYMETHODINFO -> {
                    toast("删除成功").setGravity(Gravity.CENTER, 0, 0)
                    user.updateAll("userId = ?", user.userId)
                    val intent = Intent()
                    intent.putExtra("status",0)
                    setResult(RESULT_OK,intent)
                    finish()
                }
                else -> {

                }
            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {
        toast(string!!).setGravity(Gravity.CENTER, 0, 0)
    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {
        toast("请检查网络连接").setGravity(Gravity.CENTER, 0, 0)
    }

}
