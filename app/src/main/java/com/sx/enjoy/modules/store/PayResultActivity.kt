package com.sx.enjoy.modules.store

import android.view.View
import com.sx.enjoy.MainActivity
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import com.sx.enjoy.modules.mine.OrderDetailsActivity
import kotlinx.android.synthetic.main.activity_pay_result.*
import org.jetbrains.anko.startActivity

class PayResultActivity : BaseActivity() {

    private var resultCode = 1
    private var orderNo = ""

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL, if (resultCode == 1) "支付成功" else "支付失败")

    override fun getLayoutResource() = R.layout.activity_pay_result

    override fun initView() {
        resultCode = intent.getIntExtra("resultCode",1)
        orderNo = intent.getStringExtra("orderNo")

        if(resultCode == 1){
            iv_pay_result.setImageResource(R.mipmap.ic_notice_success)
            tv_result.text = "支付成功"
            tv_result_info.text = "订单支付成功，看看其他的吧"
        }else{
            iv_pay_result.setImageResource(R.mipmap.ic_notice_error)
            tv_result.text = "支付失败"
            tv_result_info.text = "订单支付失败，看看其他的吧"
            tv_order.visibility = View.GONE
        }

        initEvent()

    }

    private fun initEvent(){
        tv_order.setOnClickListener {
            startActivity<OrderDetailsActivity>(Pair("orderNo",orderNo))
            finish()
        }
    }
}
