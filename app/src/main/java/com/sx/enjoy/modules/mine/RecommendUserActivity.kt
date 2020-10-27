package com.sx.enjoy.modules.mine

import android.view.Gravity
import android.view.View
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.TeamUserBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.utils.ImageLoaderUtil
import kotlinx.android.synthetic.main.activity_recommend_user.*
import kotlinx.android.synthetic.main.empty_public_network.*
import org.jetbrains.anko.toast

class RecommendUserActivity : BaseActivity(),SXContract.View {

    private lateinit var present :SXPresent

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"个人信息")

    override fun getLayoutResource() = R.layout.activity_recommend_user

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        present.getTeamUser(intent.getStringExtra("userId"))

        iv_network_error.setOnClickListener {
            present.getTeamUser(intent.getStringExtra("userId"))
        }
    }


    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETTEAMUSER -> {
                    em_network_view.visibility = View.GONE
                    data?.let {
                        data as TeamUserBean
                        ll_user_info.visibility = View.VISIBLE
                        ll_user_time.visibility = View.VISIBLE
                        ImageLoaderUtil().displayHeadImage(this,data.userImg,iv_user_head)
                        tv_user_name.text = if(data.userName.isNullOrEmpty()) data.userPhone else data.userName
                        tv_user_level.text = "vip.${data.userLevel}"
                        tv_user_activity.text = data.userActivity
                        tv_user_phone.text = data.userPhone
                        tv_create_time.text = data.createTime
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
        em_network_view.visibility = View.VISIBLE
        toast("请检查网络连接").setGravity(Gravity.CENTER, 0, 0)
    }

}
