package com.sx.enjoy.modules.mine

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.view.Gravity
import android.view.View
import com.likai.lib.commonutils.LoadingDialog
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.CancelCountBean
import com.sx.enjoy.bean.TransactionOrderBean
import com.sx.enjoy.bean.UpLoadImageData
import com.sx.enjoy.bean.UpLoadImageList
import com.sx.enjoy.constans.C
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.utils.GlideEngine
import com.sx.enjoy.utils.ImageLoaderUtil
import com.sx.enjoy.utils.TimeCountUtil
import com.sx.enjoy.utils.UpLoadImageUtil
import com.sx.enjoy.view.dialog.NoticeCancelDialog
import com.sx.enjoy.view.dialog.NoticeDialog
import com.sx.enjoy.view.dialog.SingleImageShowDialog
import com.yanzhenjie.permission.AndPermission
import kotlinx.android.synthetic.main.activity_transaction_details.*
import kotlinx.android.synthetic.main.empty_public_network.*
import org.jetbrains.anko.toast
import java.lang.StringBuilder
import android.content.ClipData.newPlainText
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import com.trello.rxlifecycle2.RxLifecycle.bindUntilEvent
import androidx.core.content.ContextCompat.getSystemService



class TransactionDetailsActivity : BaseActivity() ,SXContract.View ,TimeCountUtil.OnCountDownListener{

    private lateinit var noticeDialog : NoticeDialog
    private lateinit var loadingDialog : LoadingDialog
    private lateinit var singleImageDialog : SingleImageShowDialog
    private lateinit var cancelDialog: NoticeCancelDialog

    private lateinit var present: SXPresent

    private var uploadTask : UpLoadImageUtil? = null
    private var time: TimeCountUtil? = null

    private var transaction: TransactionOrderBean? = null

    private var type = 0
    private var richOrderNo = ""
    private var photo = ""

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,if(type == C.MARKET_ORDER_STATUS_BUY) "买入详情" else "卖出详情")

    override fun getLayoutResource() = R.layout.activity_transaction_details

    override fun initView() {
        type = intent.getIntExtra("type",0)
        richOrderNo = intent.getStringExtra("richOrderNo")

        noticeDialog = NoticeDialog(this)
        loadingDialog = LoadingDialog(this)
        singleImageDialog = SingleImageShowDialog(this)
        cancelDialog = NoticeCancelDialog(this)

        uploadTask = UpLoadImageUtil(this,present)

        present.getTransactionOrderDetails(C.USER_ID,richOrderNo)

        initEvent()
    }

    private fun initEvent(){
        iv_upload_documents.setOnClickListener {
            if(type == C.MARKET_ORDER_STATUS_BUY){
                when(transaction?.status){
                    1 -> {
                        PictureSelector.create(this)
                            .openGallery(PictureMimeType.ofImage())
                            .imageSpanCount(3)
                            .selectionMode(PictureConfig.MULTIPLE)
                            .maxSelectNum(1)
                            .isCamera(true)
                            .loadImageEngine(GlideEngine.createGlideEngine())
                            .compress(true)
                            .hideBottomControls(true)
                            .minimumCompressSize(1024)
                            .forResult(1001)
                    }
                    2 ,3 -> {
                        singleImageDialog.showImage(transaction?.transaction)
                    }
                }
            }else{
                if(transaction?.status == 2||transaction?.status == 3){
                    singleImageDialog.showImage(transaction?.transaction)
                }
            }
        }

        tv_submit.setOnClickListener {
            if(type == C.MARKET_ORDER_STATUS_BUY){
                if(transaction?.status == 1){
                    if(transaction == null){
                        return@setOnClickListener
                    }
                    if(photo.isEmpty()){
                        toast("请上传支付凭证").setGravity(Gravity.CENTER, 0, 0)
                    }else{
                        val imageList = arrayListOf<UpLoadImageData>()
                        val logoImage = arrayListOf<UpLoadImageList>()
                        logoImage.add(UpLoadImageList(photo))
                        imageList.add(UpLoadImageData(logoImage,1,"支付凭证"))
                        uploadTask?.addImagesToSources(imageList)
                        uploadTask?.start()
                        loadingDialog.showLoading("上传中...")
                    }
                }
            }else{
                if(transaction?.status == 2){
                    present.confirmMarketOrder(C.USER_ID,transaction!!.richUserId,transaction!!.buyNum,transaction!!.orderNo,transaction!!.type.toString())
                }
            }
        }

        uploadTask?.setOnUploadImageResultListener { result, sources ->
            loadingDialog.dismiss()
            if(result){
                present.payMarketOrder(transaction!!.orderNo,sources[0].imageList[0].netPath)
            }
        }

        ll_other_phone.setOnClickListener {
            checkPermission(tv_other_phone.text.toString())
        }

        ll_sell_phone.setOnClickListener {
            checkPermission(tv_sell_phone.text.toString())
        }
        iv_network_error.setOnClickListener {
            present.getTransactionOrderDetails(C.USER_ID,richOrderNo)
        }
        ll_ali_code.setOnClickListener {
            singleImageDialog.showImage(transaction?.payQrcode)
        }
        ll_wx_code.setOnClickListener {
            singleImageDialog.showImage(transaction?.wxQrcode)
        }
        tv_cancel.setOnClickListener {
           present.getCancelCount(C.USER_ID,false)
        }
        cancelDialog.setOnNoticeConfirmListener(object : NoticeCancelDialog.OnNoticeConfirmListener{
            override fun onConfirm() {
                present.createMarketOrder(C.USER_ID,"","","",transaction!!.richOrderNo,"","1")
            }
        })
        ll_call_service.setOnClickListener {
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val mClipData = newPlainText("Label", transaction?.kfPhone)
            cm.setPrimaryClip(mClipData)
            toast("客服微信号已复制到剪切板,请添加微信联系客服").setGravity(Gravity.CENTER, 0, 0)
        }
    }

    override fun onTickChange(millisUntil: Long) {
        val ms = millisUntil/1000
        val sb = StringBuilder()
        val h = ms/3600
        when {
            h<=0 -> sb.append("00")
            h<10 -> sb.append("0$h")
            else -> sb.append(h)
        }
        sb.append(":")
        val m = (ms - (h*3600))/60
        when {
            m<=0 -> sb.append("00")
            m<10 -> sb.append("0$m")
            else -> sb.append(m)
        }
        sb.append(":")
        val s = ms - (h*3600) - m*60
        when {
            s<=0 -> sb.append("00")
            s<10 -> sb.append("0$s")
            else -> sb.append(s)
        }
        tv_pay_time.text = sb.toString()
    }

    override fun OnFinishChanger() {
        tv_pay_time.text = "00:00:00"
        Handler().postDelayed({
            present.getCancelCount(C.USER_ID,true)
        },1000)
    }

    @SuppressLint("MissingPermission")
    private fun checkPermission(tel:String){
        AndPermission.with(this)
            .runtime()
            .permission(Manifest.permission.CALL_PHONE)
            .onGranted { permissions ->
                val intent = Intent(Intent.ACTION_CALL)
                val data = Uri.parse("tel:$tel")
                intent.data = data
                startActivity(intent)
            }
            .onDenied { permissions ->

            }
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                1001 -> {
                    val selectList = PictureSelector.obtainMultipleResult(data)
                    photo = selectList[0].compressPath
                    ImageLoaderUtil().displayImage(this,selectList[0].compressPath,iv_upload_documents)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        time?.cancel()
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETTRANSACTIONORDERDETAILS -> {
                    em_network_view.visibility = View.GONE
                    data?.let {
                        data as TransactionOrderBean
                        transaction = data
                        tv_market_price.text = "¥${data.amount}"
                        tv_market_count.text = data.buyNum
                        tv_total_money.text = "¥${data.buyAmountSum}"

                        if(type == C.MARKET_ORDER_STATUS_BUY){
                            when(data.status){
                                0 -> {
                                    iv_transaction_status_1.setImageResource(R.mipmap.ic_market_un_graphed)
                                    iv_transaction_status_2.setImageResource(R.mipmap.ic_market_un_complete)
                                    tv_transaction_status_1.setTextColor(resources.getColor(R.color.color_666666))
                                    tv_transaction_status_2.setTextColor(resources.getColor(R.color.color_666666))
                                    v_transaction_status.setBackgroundColor(resources.getColor(R.color.color_666666))
                                    ll_sell_info.visibility = View.GONE
                                    ll_sell_phone.visibility = View.GONE
                                    ll_order_time.visibility = View.GONE
                                    ll_photo_upload.visibility = View.GONE
                                    ll_buy_user.visibility = View.GONE
                                    ll_upload_documents.visibility = View.GONE
                                    ll_pay_time.visibility = View.GONE
                                    tv_submit.visibility = View.GONE
                                    tv_cancel.visibility = View.VISIBLE
                                    ll_call_service.visibility = View.GONE

                                    ll_pay_method.visibility = View.VISIBLE
                                    ll_ali_number.visibility = View.GONE
                                    ll_ali_code.visibility = View.GONE
                                    ll_wx_code.visibility = View.GONE
                                    iv_zfb_pay.visibility = if(data.isAliPay == 1) View.VISIBLE else View.GONE
                                    iv_wx_pay.visibility = if(data.isWxPay == 1) View.VISIBLE else View.GONE
                                }
                                1 -> {
                                    iv_transaction_status_1.setImageResource(R.mipmap.ic_market_graphed)
                                    iv_transaction_status_2.setImageResource(R.mipmap.ic_market_un_complete)
                                    tv_transaction_status_1.setTextColor(resources.getColor(R.color.main_color))
                                    tv_transaction_status_2.setTextColor(resources.getColor(R.color.color_666666))
                                    v_transaction_status.setBackgroundColor(resources.getColor(R.color.main_color))
                                    ImageLoaderUtil().displayHeadImage(this,data.sellUserImg,iv_user_head)
                                    tv_user_name.text = data.sellUserName
                                    tv_market_time.text = data.createTime
                                    tv_sell_phone.text = data.sellUserPhone
                                    ll_pay_time.visibility = View.VISIBLE
                                    tv_time_have.text = "剩余时间"
                                    tv_submit.text = "提交"
                                    tv_submit.visibility = View.VISIBLE
                                    tv_cancel.visibility = View.VISIBLE
                                    ll_call_service.visibility = View.GONE

                                    ll_sell_info.visibility = View.VISIBLE
                                    ll_sell_phone.visibility = View.VISIBLE
                                    ll_order_time.visibility = View.GONE
                                    ll_photo_upload.visibility = View.GONE
                                    ll_buy_user.visibility = View.GONE
                                    ll_upload_documents.visibility = View.VISIBLE

                                    ll_pay_method.visibility = View.GONE
                                    ll_ali_number.visibility = if(data.isAliPay == 1) View.VISIBLE else View.GONE
                                    ll_ali_code.visibility = if(data.isAliPay == 1) View.VISIBLE else View.GONE
                                    ll_wx_code.visibility = if(data.isWxPay == 1) View.VISIBLE else View.GONE
                                    tv_zfb_account.text = if(data.isAliPay == 1) data.alipayNumber else ""

                                    time = TimeCountUtil(data.seconds*1000,1000)
                                    time?.setOnCountDownListener(this)
                                    time?.start()
                                }
                                2 -> {
                                    iv_transaction_status_1.setImageResource(R.mipmap.ic_market_graphed)
                                    iv_transaction_status_2.setImageResource(R.mipmap.ic_market_un_complete)
                                    tv_transaction_status_1.setTextColor(resources.getColor(R.color.main_color))
                                    tv_transaction_status_2.setTextColor(resources.getColor(R.color.color_666666))
                                    v_transaction_status.setBackgroundColor(resources.getColor(R.color.main_color))
                                    ImageLoaderUtil().displayHeadImage(this,data.sellUserImg,iv_user_head)
                                    ImageLoaderUtil().displayImage(this,data.transaction,iv_upload_documents)
                                    tv_user_name.text = data.sellUserName
                                    tv_market_time.text = data.createTime
                                    tv_sell_phone.text = data.sellUserPhone
                                    tv_photo_upload.text = data.payTime
                                    tv_time_have.text = ""
                                    ll_pay_time.visibility = View.VISIBLE
                                    tv_submit.visibility = View.GONE
                                    tv_cancel.visibility = View.GONE
                                    ll_call_service.visibility = View.VISIBLE

                                    ll_sell_info.visibility = View.VISIBLE
                                    ll_sell_phone.visibility = View.VISIBLE
                                    ll_order_time.visibility = View.GONE
                                    ll_photo_upload.visibility = View.VISIBLE
                                    ll_buy_user.visibility = View.GONE
                                    ll_upload_documents.visibility = View.VISIBLE

                                    ll_pay_method.visibility = View.GONE
                                    ll_ali_number.visibility = if(data.isAliPay == 1) View.VISIBLE else View.GONE
                                    ll_ali_code.visibility = if(data.isAliPay == 1) View.VISIBLE else View.GONE
                                    ll_wx_code.visibility = if(data.isWxPay == 1) View.VISIBLE else View.GONE
                                    tv_zfb_account.text = if(data.isAliPay == 1) data.alipayNumber else ""
                                    time?.cancel()
                                    tv_pay_time.text = "等待卖家处理"
                                }
                                3 -> {
                                    iv_transaction_status_1.setImageResource(R.mipmap.ic_market_graphed)
                                    iv_transaction_status_2.setImageResource(R.mipmap.ic_market_complete)
                                    tv_transaction_status_1.setTextColor(resources.getColor(R.color.main_color))
                                    tv_transaction_status_2.setTextColor(resources.getColor(R.color.color_70DC7D))
                                    v_transaction_status.setBackgroundColor(resources.getColor(R.color.main_color))
                                    ImageLoaderUtil().displayHeadImage(this,data.sellUserImg,iv_user_head)
                                    ImageLoaderUtil().displayImage(this,data.transaction,iv_upload_documents)
                                    tv_user_name.text = data.sellUserName
                                    tv_market_time.text = data.createTime
                                    tv_sell_phone.text = data.sellUserPhone
                                    tv_photo_upload.text = data.payTime
                                    ll_pay_time.visibility = View.GONE
                                    tv_submit.visibility = View.GONE
                                    tv_cancel.visibility = View.GONE
                                    ll_call_service.visibility = View.GONE

                                    ll_sell_info.visibility = View.VISIBLE
                                    ll_sell_phone.visibility = View.VISIBLE
                                    ll_order_time.visibility = View.GONE
                                    ll_photo_upload.visibility = View.VISIBLE
                                    ll_buy_user.visibility = View.GONE
                                    ll_upload_documents.visibility = View.VISIBLE

                                    ll_pay_method.visibility = View.GONE
                                    ll_ali_number.visibility = if(data.isAliPay == 1) View.VISIBLE else View.GONE
                                    ll_ali_code.visibility = if(data.isAliPay == 1) View.VISIBLE else View.GONE
                                    ll_wx_code.visibility = if(data.isWxPay == 1) View.VISIBLE else View.GONE
                                    tv_zfb_account.text = if(data.isAliPay == 1) data.alipayNumber else ""
                                }
                                4,5 -> {
                                    tv_transaction_status_2.text = "已取消"
                                    iv_transaction_status_1.setImageResource(if(data.sellUserId == "0") R.mipmap.ic_market_un_graphed else R.mipmap.ic_market_graphed)
                                    iv_transaction_status_2.setImageResource(R.mipmap.ic_market_cancel)
                                    tv_transaction_status_1.setTextColor(if(data.sellUserId == "0") resources.getColor(R.color.color_666666) else resources.getColor(R.color.main_color))
                                    tv_transaction_status_2.setTextColor(resources.getColor(R.color.main_color))
                                    v_transaction_status.setBackgroundColor(resources.getColor(R.color.main_color))
                                    ImageLoaderUtil().displayHeadImage(this,data.sellUserImg,iv_user_head)
                                    tv_user_name.text = data.sellUserName
                                    tv_market_time.text = data.createTime
                                    tv_sell_phone.text = data.sellUserPhone
                                    ll_pay_time.visibility = View.GONE
                                    tv_submit.visibility = View.GONE
                                    tv_cancel.visibility = View.GONE
                                    ll_call_service.visibility = View.GONE

                                    ll_sell_info.visibility = if(data.sellUserId == "0") View.GONE else View.VISIBLE
                                    ll_sell_phone.visibility = if(data.sellUserId == "0") View.GONE else View.VISIBLE
                                    ll_order_time.visibility = View.GONE
                                    ll_photo_upload.visibility = View.GONE
                                    ll_buy_user.visibility = View.GONE
                                    ll_upload_documents.visibility = View.GONE

                                    ll_pay_method.visibility =  View.GONE
                                    ll_ali_number.visibility = if(data.isAliPay == 1&&data.sellUserId != "0") View.VISIBLE else View.GONE
                                    ll_ali_code.visibility = if(data.isAliPay == 1&&data.sellUserId != "0") View.VISIBLE else View.GONE
                                    ll_wx_code.visibility = if(data.isWxPay == 1&&data.sellUserId != "0") View.VISIBLE else View.GONE
                                    tv_zfb_account.text = if(data.isAliPay == 1&&data.sellUserId != "0") data.alipayNumber else ""
                                    time?.cancel()
                                }
                                else ->{}
                            }
                        }else{
                            when(data.status){
                                0 -> {
                                    iv_transaction_status_1.setImageResource(R.mipmap.ic_market_un_graphed)
                                    iv_transaction_status_2.setImageResource(R.mipmap.ic_market_un_complete)
                                    tv_transaction_status_1.setTextColor(resources.getColor(R.color.color_666666))
                                    tv_transaction_status_2.setTextColor(resources.getColor(R.color.color_666666))
                                    v_transaction_status.setBackgroundColor(resources.getColor(R.color.color_666666))
                                    tv_order_time.text = data.createTime

                                    ll_sell_info.visibility = View.GONE
                                    ll_sell_phone.visibility = View.GONE
                                    ll_order_time.visibility = View.GONE
                                    ll_photo_upload.visibility = View.GONE
                                    ll_buy_user.visibility = View.GONE
                                    ll_upload_documents.visibility = View.GONE
                                    ll_pay_time.visibility = View.GONE
                                    tv_submit.visibility = View.GONE
                                    tv_cancel.visibility = View.VISIBLE
                                    ll_call_service.visibility = View.GONE

                                    ll_pay_method.visibility = View.GONE
                                    ll_ali_number.visibility = if(data.isAliPay == 1) View.VISIBLE else View.GONE
                                    ll_ali_code.visibility = if(data.isAliPay == 1) View.VISIBLE else View.GONE
                                    ll_wx_code.visibility = if(data.isWxPay == 1) View.VISIBLE else View.GONE
                                    tv_zfb_account.text = if(data.isAliPay == 1) data.alipayNumber else ""
                                }
                                1 -> {
                                    iv_transaction_status_1.setImageResource(R.mipmap.ic_market_graphed)
                                    iv_transaction_status_2.setImageResource(R.mipmap.ic_market_un_complete)
                                    tv_transaction_status_1.setTextColor(resources.getColor(R.color.main_color))
                                    tv_transaction_status_2.setTextColor(resources.getColor(R.color.color_666666))
                                    v_transaction_status.setBackgroundColor(resources.getColor(R.color.main_color))
                                    tv_order_time.text = data.createTime
                                    ImageLoaderUtil().displayHeadImage(this,data.headImage,iv_other_head)
                                    tv_other_name.text = data.userName
                                    tv_other_phone.text = data.userPhone

                                    ll_sell_info.visibility = View.GONE
                                    ll_sell_phone.visibility = View.GONE
                                    ll_order_time.visibility = View.VISIBLE
                                    ll_photo_upload.visibility = View.GONE
                                    ll_buy_user.visibility = View.VISIBLE
                                    ll_upload_documents.visibility = View.GONE
                                    ll_pay_time.visibility = View.GONE
                                    tv_submit.visibility = View.GONE
                                    tv_cancel.visibility = View.GONE
                                    ll_call_service.visibility = View.GONE

                                    ll_pay_method.visibility = View.GONE
                                    ll_ali_number.visibility = if(data.isAliPay == 1) View.VISIBLE else View.GONE
                                    ll_ali_code.visibility = if(data.isAliPay == 1) View.VISIBLE else View.GONE
                                    ll_wx_code.visibility = if(data.isWxPay == 1) View.VISIBLE else View.GONE
                                    tv_zfb_account.text = if(data.isAliPay == 1) data.alipayNumber else ""
                                }
                                2 -> {
                                    iv_transaction_status_1.setImageResource(R.mipmap.ic_market_graphed)
                                    iv_transaction_status_2.setImageResource(R.mipmap.ic_market_un_complete)
                                    tv_transaction_status_1.setTextColor(resources.getColor(R.color.main_color))
                                    tv_transaction_status_2.setTextColor(resources.getColor(R.color.color_666666))
                                    v_transaction_status.setBackgroundColor(resources.getColor(R.color.main_color))
                                    ImageLoaderUtil().displayHeadImage(this,data.headImage,iv_other_head)
                                    ImageLoaderUtil().displayImage(this,data.transaction,iv_upload_documents)
                                    tv_order_time.text = data.createTime
                                    tv_photo_upload.text = data.payTime
                                    tv_other_name.text = data.userName
                                    tv_other_phone.text = data.userPhone
                                    tv_submit.text = "确认完成"

                                    ll_sell_info.visibility = View.GONE
                                    ll_sell_phone.visibility = View.GONE
                                    ll_order_time.visibility = View.VISIBLE
                                    ll_photo_upload.visibility = View.VISIBLE
                                    ll_buy_user.visibility = View.VISIBLE
                                    ll_upload_documents.visibility = View.VISIBLE
                                    ll_pay_time.visibility = View.GONE
                                    tv_submit.visibility = View.VISIBLE
                                    tv_cancel.visibility = View.GONE
                                    ll_call_service.visibility = View.GONE

                                    ll_pay_method.visibility = View.GONE
                                    ll_ali_number.visibility = if(data.isAliPay == 1) View.VISIBLE else View.GONE
                                    ll_ali_code.visibility = if(data.isAliPay == 1) View.VISIBLE else View.GONE
                                    ll_wx_code.visibility = if(data.isWxPay == 1) View.VISIBLE else View.GONE
                                    tv_zfb_account.text = if(data.isAliPay == 1) data.alipayNumber else ""
                                }
                                3 -> {
                                    iv_transaction_status_1.setImageResource(R.mipmap.ic_market_graphed)
                                    iv_transaction_status_2.setImageResource(R.mipmap.ic_market_complete)
                                    tv_transaction_status_1.setTextColor(resources.getColor(R.color.main_color))
                                    tv_transaction_status_2.setTextColor(resources.getColor(R.color.color_70DC7D))
                                    v_transaction_status.setBackgroundColor(resources.getColor(R.color.main_color))
                                    ImageLoaderUtil().displayHeadImage(this,data.headImage,iv_other_head)
                                    ImageLoaderUtil().displayImage(this,data.transaction,iv_upload_documents)
                                    tv_order_time.text = data.createTime
                                    tv_photo_upload.text = data.payTime
                                    tv_other_name.text = data.userName
                                    tv_other_phone.text = data.userPhone

                                    ll_sell_info.visibility = View.GONE
                                    ll_sell_phone.visibility = View.GONE
                                    ll_order_time.visibility = View.VISIBLE
                                    ll_photo_upload.visibility = View.VISIBLE
                                    ll_buy_user.visibility = View.VISIBLE
                                    ll_upload_documents.visibility = View.VISIBLE
                                    tv_cancel.visibility = View.GONE
                                    tv_submit.visibility = View.GONE
                                    tv_cancel.visibility = View.GONE
                                    ll_call_service.visibility = View.GONE

                                    ll_pay_method.visibility = View.GONE
                                    ll_ali_number.visibility = if(data.isAliPay == 1) View.VISIBLE else View.GONE
                                    ll_ali_code.visibility = if(data.isAliPay == 1) View.VISIBLE else View.GONE
                                    ll_wx_code.visibility = if(data.isWxPay == 1) View.VISIBLE else View.GONE
                                    tv_zfb_account.text = if(data.isAliPay == 1) data.alipayNumber else ""
                                }
                                4,5 -> {
                                    tv_transaction_status_2.text = "已取消"
                                    iv_transaction_status_1.setImageResource(if(data.userId == "0") R.mipmap.ic_market_un_graphed else R.mipmap.ic_market_graphed)
                                    iv_transaction_status_2.setImageResource(R.mipmap.ic_market_cancel)
                                    tv_transaction_status_1.setTextColor(if(data.userId == "0") resources.getColor(R.color.color_666666) else resources.getColor(R.color.main_color))
                                    tv_transaction_status_2.setTextColor(resources.getColor(R.color.main_color))
                                    v_transaction_status.setBackgroundColor(resources.getColor(R.color.main_color))
                                    tv_order_time.text = data.createTime
                                    ImageLoaderUtil().displayHeadImage(this,data.headImage,iv_other_head)
                                    tv_other_name.text = data.userName
                                    tv_other_phone.text = data.userPhone

                                    ll_sell_info.visibility = View.GONE
                                    ll_sell_phone.visibility = View.GONE
                                    ll_order_time.visibility = if(data.userId == "0") View.GONE else View.VISIBLE
                                    ll_photo_upload.visibility = View.GONE
                                    ll_buy_user.visibility = if(data.userId == "0") View.GONE else View.VISIBLE
                                    ll_upload_documents.visibility = View.GONE
                                    ll_pay_time.visibility = View.GONE
                                    tv_submit.visibility = View.GONE
                                    tv_cancel.visibility = View.GONE
                                    ll_call_service.visibility = View.GONE

                                    ll_pay_method.visibility = View.GONE
                                    ll_ali_number.visibility = if(data.isAliPay == 1) View.VISIBLE else View.GONE
                                    ll_ali_code.visibility = if(data.isAliPay == 1) View.VISIBLE else View.GONE
                                    ll_wx_code.visibility = if(data.isWxPay == 1) View.VISIBLE else View.GONE
                                    tv_zfb_account.text = if(data.isAliPay == 1) data.alipayNumber else ""
                                }
                                else ->{}
                            }
                        }
                    }
                }
                SXContract.PAYMARKETORDER -> {
                    noticeDialog.showNotice(7)
                    present.getTransactionOrderDetails(C.USER_ID,richOrderNo)
                }
                SXContract.CONFIRMMARKETORDER -> {
                    noticeDialog.showNotice(9)
                    present.getTransactionOrderDetails(C.USER_ID,richOrderNo)
                    setResult(RESULT_OK)
                }
                SXContract.CREATEMARKETORDER -> {
                    toast("订单已取消").setGravity(Gravity.CENTER, 0, 0)
                    present.getTransactionOrderDetails(C.USER_ID,richOrderNo)
                    setResult(RESULT_OK)
                }
                SXContract.GETCANCELCOUNT ->{
                    data?.let {
                        data as CancelCountBean
                        if(data.isAutoCancel){
                            cancelDialog.showNotice(data.cancelNum,data.systemCancelNum,false)
                            present.getTransactionOrderDetails(C.USER_ID,richOrderNo)
                            setResult(RESULT_OK)
                        }else{
                            cancelDialog.showNotice(data.cancelNum,data.systemCancelNum,true)
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