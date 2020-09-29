package com.sx.enjoy.modules.mine

import android.content.Intent
import android.view.View
import com.likai.lib.commonutils.LoadingDialog
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.TransactionOrderBean
import com.sx.enjoy.bean.UpLoadImageData
import com.sx.enjoy.bean.UpLoadImageList
import com.sx.enjoy.constans.C
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.utils.ImageLoaderUtil
import com.sx.enjoy.utils.UpLoadImageUtil
import com.sx.enjoy.view.dialog.NoticeDialog
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_transaction_details.*
import kotlinx.android.synthetic.main.activity_transaction_details.iv_user_head
import org.jetbrains.anko.toast

class TransactionDetailsActivity : BaseActivity() ,SXContract.View{

    private lateinit var noticeDialog : NoticeDialog
    private lateinit var loadingDialog : LoadingDialog

    private lateinit var present: SXPresent

    private var uploadTask : UpLoadImageUtil? = null

    private var transaction: TransactionOrderBean? = null

    private var type = 0
    private var marketId = ""
    private var photo = ""

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,if(type == C.MARKET_ORDER_STATUS_BUY) "买入详情" else "卖出详情")

    override fun getLayoutResource() = R.layout.activity_transaction_details

    override fun initView() {
        type = intent.getIntExtra("type",0)
        marketId = intent.getStringExtra("marketId")

        noticeDialog = NoticeDialog(this)
        loadingDialog = LoadingDialog(this)

        uploadTask = UpLoadImageUtil(this,present)

        if(type == C.MARKET_ORDER_STATUS_BUY){
            iv_transaction_status_1.setImageResource(R.mipmap.ic_market_un_graphed)
            tv_transaction_status_1.text = "已拍下"
        }else{
            iv_transaction_status_1.setImageResource(R.mipmap.ic_market_un_pay)
            tv_transaction_status_1.text = "已付款"
        }

        present.getTransactionOrderDetails(marketId)

        initEvent()
    }

    private fun initEvent(){
        iv_upload_documents.setOnClickListener {
            if(transaction?.type == C.MARKET_ORDER_STATUS_BUY&&transaction?.status == 1){
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
        }

        tv_submit.setOnClickListener {
            if(transaction == null){
                return@setOnClickListener
            }
            if(photo.isEmpty()){
                toast("请上传支付凭证")
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

        uploadTask?.setOnUploadImageResultListener { result, sources ->
            loadingDialog.dismiss()
            if(result){
                present.payMarketOrder(transaction!!.orderNo,sources[0].imageList[0].netPath)
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
                    ImageLoaderUtil().displayImage(this,selectList[0].compressPath,iv_upload_documents)
                }
            }
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETTRANSACTIONORDERDETAILS -> {
                    data?.let {
                        data as TransactionOrderBean
                        transaction = data
                        ImageLoaderUtil().displayHeadImage(this,data.headImage,iv_user_head)
                        tv_user_name.text = data.userName
                        tv_market_time.text = data.createTime
                        tv_market_price.text = "¥${data.amount}"
                        tv_market_count.text = data.buyNum

                        if(data.orderType == C.MARKET_ORDER_STATUS_BUY){
                            ll_ali_number.visibility = View.GONE

                            when(data.status){
                                0 -> {
                                    iv_transaction_status_1.setImageResource(R.mipmap.ic_market_un_graphed)
                                    iv_transaction_status_2.setImageResource(R.mipmap.ic_market_un_complete)
                                    tv_transaction_status_1.setTextColor(resources.getColor(R.color.color_666666))
                                    tv_transaction_status_2.setTextColor(resources.getColor(R.color.color_666666))
                                    v_transaction_status.setBackgroundColor(resources.getColor(R.color.color_666666))
                                    ll_order_time.visibility = View.GONE
                                    ll_sell_user.visibility = View.GONE
                                    ll_upload_documents.visibility = View.GONE
                                    tv_submit.visibility = View.GONE
                                }
                                1 -> {
                                    iv_transaction_status_1.setImageResource(R.mipmap.ic_market_graphed)
                                    iv_transaction_status_2.setImageResource(R.mipmap.ic_market_un_complete)
                                    tv_transaction_status_1.setTextColor(resources.getColor(R.color.main_color))
                                    tv_transaction_status_2.setTextColor(resources.getColor(R.color.color_666666))
                                    v_transaction_status.setBackgroundColor(resources.getColor(R.color.main_color))
                                    ImageLoaderUtil().displayHeadImage(this,data.sellUserImg,iv_sell_head)
                                    tv_sell_name.text = data.sellUserName
                                    tv_sell_phone.text = data.sellUserPhone
                                    tv_sell_account.text = data.payNumber
                                    tv_submit.text = "提交"
                                    ll_order_time.visibility = View.GONE
                                    ll_sell_user.visibility = View.VISIBLE
                                    ll_upload_documents.visibility = View.VISIBLE
                                    tv_submit.visibility = View.VISIBLE
                                }
                                2 -> {
                                    iv_transaction_status_1.setImageResource(R.mipmap.ic_market_graphed)
                                    iv_transaction_status_2.setImageResource(R.mipmap.ic_market_un_complete)
                                    tv_transaction_status_1.setTextColor(resources.getColor(R.color.main_color))
                                    tv_transaction_status_2.setTextColor(resources.getColor(R.color.color_666666))
                                    v_transaction_status.setBackgroundColor(resources.getColor(R.color.main_color))
                                    ImageLoaderUtil().displayHeadImage(this,data.sellUserImg,iv_sell_head)
                                    ImageLoaderUtil().displayHeadImage(this,data.transaction,iv_upload_documents)
                                    tv_sell_name.text = data.sellUserName
                                    tv_sell_phone.text = data.sellUserPhone
                                    tv_sell_account.text = data.payNumber
                                    ll_order_time.visibility = View.GONE
                                    ll_sell_user.visibility = View.VISIBLE
                                    ll_upload_documents.visibility = View.VISIBLE
                                    tv_submit.visibility = View.GONE
                                }
                            }
                        }
                    }
                }
                SXContract.PAYMARKETORDER -> {
                    noticeDialog.showNotice(7)
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