package com.sx.enjoy.modules.mine

import android.app.Activity
import android.content.Intent
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.UserBean
import com.sx.enjoy.constans.C
import kotlinx.android.synthetic.main.activity_pay_method_set.*
import org.jetbrains.anko.startActivityForResult
import org.litepal.LitePal

class PayMethodSetActivity : BaseActivity() {

    private lateinit var user : UserBean

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"收款账号")

    override fun getLayoutResource(): Int = R.layout.activity_pay_method_set

    override fun initView() {
        user = LitePal.findLast(UserBean::class.java)

        tv_zfb_set.text = if(user.isAliPay) "已设置" else "请设置"
        tv_zfb_set.setTextColor(resources.getColor(if(user.isAliPay) R.color.title_main_color else R.color.color_666666))
        tv_wx_set.text = if(user.isWxPay) "已设置" else "请设置"
        tv_wx_set.setTextColor(resources.getColor(if(user.isWxPay) R.color.title_main_color else R.color.color_666666))

        initEvent()
    }


    private fun initEvent(){
        ll_zfb_set.setOnClickListener {
            startActivityForResult<PayMethodInfoActivity>(3001, Pair("type",1))
        }
        ll_wx_set.setOnClickListener {
            startActivityForResult<PayMethodInfoActivity>(3002,Pair("type",2))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            val status = data?.getIntExtra("status",0)
            when(requestCode){
                3001 -> {
                    tv_zfb_set.text = if(status == 1) "已设置" else "请设置"
                    tv_zfb_set.setTextColor(resources.getColor(if(status == 1) R.color.title_main_color else R.color.color_666666))
                    user.setIsAliPay(status == 1)
                    setResult(RESULT_OK)
                }
                3002 -> {
                    tv_wx_set.text = if(status == 1) "已设置" else "请设置"
                    tv_wx_set.setTextColor(resources.getColor(if(status == 1) R.color.title_main_color else R.color.color_666666))
                    user.setIsWxPay(status == 1)
                    setResult(RESULT_OK)
                }
            }
        }
    }

}
