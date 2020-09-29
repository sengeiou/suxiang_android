package com.sx.enjoy.modules.mine

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import com.likai.lib.base.BaseFragment
import com.sx.enjoy.R
import com.sx.enjoy.adapter.MineMoreAdapter
import com.sx.enjoy.bean.MineMoreBean
import com.sx.enjoy.bean.UserBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.event.FirstInitUserEvent
import com.sx.enjoy.modules.login.LoginActivity
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.utils.GlideImageLoader
import com.sx.enjoy.utils.ImageLoaderUtil
import com.sx.enjoy.view.NoScrollGridManager
import kotlinx.android.synthetic.main.fragment_mine.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.litepal.LitePal
import q.rorbin.badgeview.Badge
import q.rorbin.badgeview.QBadgeView

class MineFragment : BaseFragment(),SXContract.View{

    private lateinit var present: SXPresent

    private lateinit var moreAdapter: MineMoreAdapter

    private var qbv1 : Badge? = null
    private var qbv2 : Badge? = null
    private var qbv3 : Badge? = null
    private var qbv4 : Badge? = null

    override fun getLayoutResource() = R.layout.fragment_mine

    override fun beForInitView() {
        present = SXPresent(this)
    }

    override fun initView() {
        moreAdapter = MineMoreAdapter()
        rcy_mine_more.layoutManager = NoScrollGridManager(activity,4)
        rcy_mine_more.adapter = moreAdapter

        qbv1 = QBadgeView(activity).bindTarget(iv_order_state_1).setBadgeBackgroundColor(Color.RED)
        qbv2 = QBadgeView(activity).bindTarget(iv_order_state_2).setBadgeBackgroundColor(Color.RED)
        qbv3 = QBadgeView(activity).bindTarget(iv_order_state_3).setBadgeBackgroundColor(Color.RED)
        qbv4 = QBadgeView(activity).bindTarget(iv_order_state_4).setBadgeBackgroundColor(Color.RED)

        val moreList = arrayListOf<MineMoreBean>()
        moreList.add(MineMoreBean(R.mipmap.ic_mine_buy,"我购买的"))
        moreList.add(MineMoreBean(R.mipmap.ic_mine_sell,"我售卖的"))
        moreList.add(MineMoreBean(R.mipmap.ic_mine_money,"财务列表"))
        moreList.add(MineMoreBean(R.mipmap.ic_mine_take,"速享赚赚"))
        moreList.add(MineMoreBean(R.mipmap.ic_mine_lubricate,"附近加油"))
        moreList.add(MineMoreBean(R.mipmap.ic_mine_recommend,"推荐团队"))
        moreList.add(MineMoreBean(R.mipmap.ic_mine_task,"我的任务"))
        moreList.add(MineMoreBean(R.mipmap.ic_mine_about,"关于我们"))
        moreList.add(MineMoreBean(R.mipmap.ic_mine_process,"交易流程"))
        moreAdapter.setNewData(moreList)

        initEvent()
        EventBus.getDefault().post(FirstInitUserEvent(true))





        qbv1?.badgeNumber = 12
        qbv2?.badgeNumber = 5

        val bannerList = arrayListOf<String>()
        bannerList.add("http://www.suxiang986.com/ziliao/20200814104903695.jpg")
        bannerList.add("http://www.suxiang986.com/ziliao/20200814104903695.jpg")
        bannerList.add("http://www.suxiang986.com/ziliao/20200814104903695.jpg")
        ban_mine_list.setImageLoader(GlideImageLoader())
        ban_mine_list.setImages(bannerList)
        ban_mine_list.start()
    }

    fun initUser(){
        if(C.USER_ID.isEmpty()){
            ll_sub_data.visibility = View.GONE
            tv_user_name.text = "登录/注册"
            iv_user_head.setImageResource(R.mipmap.ic_user_head)
            tv_user_contribution.text = "0"
            tv_user_activity.text = "0"
            ll_member_level.visibility = View.GONE
            tv_rice_count.text = "0"
            tv_balance_money.text = "0.00"
        }else{
            val user = LitePal.findLast(UserBean::class.java)
            ll_sub_data.visibility = View.VISIBLE
            tv_user_name.text = if(user.userName.isEmpty()) user.userPhone else user.userName
            ImageLoaderUtil().displayHeadImage(activity,user.userImg,iv_user_head)
            tv_user_contribution.text = user.userContrib.toString()
            tv_user_activity.text = user.userActivity.toString()
            ll_member_level.visibility = View.VISIBLE
            if(user.membershipLevel.isEmpty()){
                tv_member_level.text = "开通会员"
            }else{
                tv_member_level.text = user.membershipLevel+"级"
            }
            tv_rice_count.text = user.riceGrains.toString()
            tv_balance_money.text = "0.00"
        }
    }

    private fun initEvent(){
        ll_user_info.setOnClickListener {
            if(C.USER_ID.isEmpty()){
                activity?.startActivity<LoginActivity>()
            }else{
                activity?.startActivity<AccountActivity>()
            }
        }
        ll_mine_feedback.setOnClickListener {
            activity?.startActivity<FeedbackActivity>()
        }
        ll_member_level.setOnClickListener {
            activity?.startActivity<MemberUpActivity>()
        }
        ll_withdraw_money.setOnClickListener {
            activity?.startActivity<BalanceActivity>()
        }
        ll_rice_record.setOnClickListener {
            activity?.startActivity<RiceRecordActivity>()
        }
        tv_order_all.setOnClickListener {
            activity?.startActivity<OrderListActivity>(Pair("type", C.ORDER_ALL))
        }
        ll_order_no_pay.setOnClickListener {
            activity?.startActivity<OrderListActivity>(Pair("type",C.ORDER_NO_PAY))
        }
        ll_order_no_send.setOnClickListener {
            activity?.startActivity<OrderListActivity>(Pair("type",C.ORDER_NO_SEND))
        }
        ll_order_no_receive.setOnClickListener {
            activity?.startActivity<OrderListActivity>(Pair("type",C.ORDER_NO_RECEIVE))
        }
        ll_order_receive_over.setOnClickListener {
            activity?.startActivity<OrderListActivity>(Pair("type",C.ORDER_RECEIVE_OVER))
        }

        moreAdapter.setOnItemClickListener { _, _, position ->
            when(position){
                0 -> activity?.startActivity<TransactionActivity>(Pair("type",C.MARKET_ORDER_STATUS_BUY))
                1 -> activity?.startActivity<TransactionActivity>(Pair("type",C.MARKET_ORDER_STATUS_SELL))
                2 -> {
                    activity?.toast("暂未开通,敬请关注")
                    //activity?.startActivity<FinanceActivity>()
                }
                3 -> activity?.startActivity<SmartEarnActivity>()
                4 -> {
                    activity?.toast("暂未开通,敬请关注")
                    //activity?.startActivity<LubricateActivity>()
                }
                5 -> activity?.startActivity<RecommendTeamActivity>()
                6 -> activity?.startActivity<MyTaskActivity>()
            }
        }

    }

    fun backToHead(){
        nsv_mine.fullScroll(View.FOCUS_UP)
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {

                else -> {

                }
            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {
        activity?.toast(string!!)
    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {
        if(boolean){
            activity?.toast("请检查网络连接")
        }
    }


    companion object {
        fun newInstance(): MineFragment {
            val fragment = MineFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

}