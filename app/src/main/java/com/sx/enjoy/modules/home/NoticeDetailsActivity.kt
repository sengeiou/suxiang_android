package com.sx.enjoy.modules.home

import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.NoticeDetailsBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import kotlinx.android.synthetic.main.activity_notice_details.*
import org.jetbrains.anko.toast

class NoticeDetailsActivity : BaseActivity() ,SXContract.View{

    private lateinit var present: SXPresent

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"通知详情")

    override fun getLayoutResource() = R.layout.activity_notice_details

    override fun initView() {
        present.getNoticeDetails(intent.getStringExtra("nid"))
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETNOTICEDETAILS -> {
                    data?.let {
                        data as NoticeDetailsBean
                        tv_title.text = data.title
                        tv_time.text = data.createTime
                        tv_content.text = data.content
                    }
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
