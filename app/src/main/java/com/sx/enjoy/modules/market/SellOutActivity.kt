package com.sx.enjoy.modules.market

import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.view.dialog.NoticeDialog
import kotlinx.android.synthetic.main.activity_sell_out.*
import org.jetbrains.anko.toast

class SellOutActivity : BaseActivity() , SXContract.View{

    private lateinit var present: SXPresent

    private lateinit var noticeDialog : NoticeDialog

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL,"我要卖出")

    override fun getLayoutResource() = R.layout.activity_sell_out

    override fun initView() {
        noticeDialog = NoticeDialog(this)

        present = SXPresent(this)

        initEvent()
    }


    private fun initEvent(){
        tv_submit.setOnClickListener {
            if(et_sell_count.text.isEmpty()){
                toast("请输入卖出数量")
                return@setOnClickListener
            }
            if(et_sell_price.text.isEmpty()){
                toast("请输入买入单价")
                return@setOnClickListener
            }
            if(et_zfb_number.text.isEmpty()){
                toast("请输入支付宝账号")
                return@setOnClickListener
            }
            present.publishMarketInfo(C.USER_ID,"1",et_sell_count.text.toString(),et_sell_price.text.toString(),et_zfb_number.text.toString())
        }
        noticeDialog.setOnDismissListener {
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.PUBLISHMARKETINFO -> {
                    noticeDialog.showNotice(3)
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