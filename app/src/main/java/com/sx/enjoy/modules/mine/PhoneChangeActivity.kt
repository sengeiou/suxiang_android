package com.sx.enjoy.modules.mine

import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import com.sx.enjoy.utils.TimeCountUtil
import kotlinx.android.synthetic.main.activity_phone_change.*

class PhoneChangeActivity : BaseActivity() {

    private lateinit var timeOld: TimeCountUtil
    private lateinit var timeNew: TimeCountUtil

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"更换手机号")

    override fun getLayoutResource() = R.layout.activity_phone_change

    override fun initView() {
        timeOld = TimeCountUtil(60000,1000)
        timeNew = TimeCountUtil(60000,1000)

        initEvent()
    }

    private fun initEvent(){
        tv_new_code.setOnClickListener {
            timeNew.start()
        }
        tv_old_code.setOnClickListener {
            timeOld.start()
        }
        timeOld.setOnCountDownListener(object : TimeCountUtil.OnCountDownListener{
            override fun onTickChange(millisUntilFinished: Long) {
                tv_old_code.text = (millisUntilFinished/1000).toString()+"s"
            }

            override fun OnFinishChanger() {
                tv_old_code.text = "重新发送"
            }
        })
        timeNew.setOnCountDownListener(object : TimeCountUtil.OnCountDownListener{
            override fun onTickChange(millisUntilFinished: Long) {
                tv_new_code.text = (millisUntilFinished/1000).toString()+"s"
            }

            override fun OnFinishChanger() {
                tv_new_code.text = "重新发送"
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        timeNew.cancel()
        timeOld.cancel()
    }

}