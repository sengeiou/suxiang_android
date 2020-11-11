package com.sx.enjoy.modules.mine

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import com.likai.lib.base.BaseFragment
import com.sx.enjoy.R
import com.sx.enjoy.adapter.MineMoreAdapter
import com.sx.enjoy.bean.BannerListBean
import com.sx.enjoy.bean.MineMoreBean
import com.sx.enjoy.bean.OrderStatusCountBean
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

    private var present: SXPresent? = null

    private lateinit var moreAdapter: MineMoreAdapter

    private var qbv1 : Badge? = null
    private var qbv2 : Badge? = null
    private var qbv3 : Badge? = null
    private var qbv4 : Badge? = null

    private var isBannerOver = false
    private var isOrderOver = false

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
        moreList.add(MineMoreBean(R.mipmap.ic_user_auth,"实名认证"))
        moreAdapter.setNewData(moreList)

        initEvent()
        initLocalUserData()
        EventBus.getDefault().post(FirstInitUserEvent(true))

        present?.getHomeBanner()
    }

    override fun refreshData() {
        EventBus.getDefault().post(FirstInitUserEvent(false))
        present?.getHomeBanner()
    }

    fun initUser(){
        if(C.USER_ID.isEmpty()){
            ll_sub_data.visibility = View.GONE
            tv_user_name.text = "登录/注册"
            iv_user_head.setImageResource(R.mipmap.ic_user_head)
            tv_user_contribution.text = "0"
            tv_user_activity.text = "0"
            ll_member_level.visibility = View.GONE
            ll_user_level.visibility = View.GONE
            tv_rice_count.text = "0"
            tv_balance_money.text = "0.00"
            qbv1?.badgeNumber = 0
            qbv2?.badgeNumber = 0
            qbv3?.badgeNumber = 0
            qbv4?.badgeNumber = 0
        }else{
            initLocalUserData()
            present?.getMyOrderStatusCount(C.USER_ID)
        }
    }

    private fun initLocalUserData(){
        val user = LitePal.findLast(UserBean::class.java)
        if(user!=null){
            ll_sub_data.visibility = View.VISIBLE
            tv_user_name.text = if(user.userName.isEmpty()) user.userPhone else user.userName
            ImageLoaderUtil().displayHeadImage(activity,user.userImg,iv_user_head)
            tv_user_contribution.text = String.format("%.2f", user.userContrib)
            tv_user_activity.text = String.format("%.2f", user.userActivity)
            ll_member_level.visibility = View.VISIBLE
            ll_user_level.visibility = View.VISIBLE
            tv_member_level.text = user.membershipLevelName
            tv_user_level.text = user.userLevelName
            tv_rice_count.text = user.riceGrain
            tv_balance_money.text = "0.00"
        }
    }

    private fun initEvent(){
        ll_user_info.setOnClickListener {
            if(C.USER_ID.isEmpty()){
                activity?.startActivity<LoginActivity>()
            }
        }
        ll_mine_setting.setOnClickListener {
            if(C.USER_ID.isEmpty()){
                activity?.startActivity<LoginActivity>()
            }else{
                activity?.startActivity<AccountActivity>()
            }
        }
        ll_mine_feedback.setOnClickListener {
            if(C.USER_ID.isEmpty()){
                activity?.startActivity<LoginActivity>()
            }else{
                activity?.startActivity<FeedbackActivity>()
            }
        }
        ll_member_level.setOnClickListener {
            if(C.USER_ID.isEmpty()){
                activity?.startActivity<LoginActivity>()
            }else{
                activity?.startActivity<MemberUpActivity>(Pair("type",0))
            }
        }
        ll_user_level.setOnClickListener {
            if(C.USER_ID.isEmpty()){
                activity?.startActivity<LoginActivity>()
            }else{
                activity?.startActivity<MemberUpActivity>(Pair("type",1))
            }
        }
        ll_rice_record.setOnClickListener {
            if(C.USER_ID.isEmpty()){
                activity?.startActivity<LoginActivity>()
            }else{
                activity?.startActivity<RiceRecordActivity>()
            }
        }
        tv_order_all.setOnClickListener {
            if(C.USER_ID.isEmpty()){
                activity?.startActivity<LoginActivity>()
            }else{
                activity?.startActivity<OrderListActivity>(Pair("type", C.ORDER_ALL))
            }
        }
        ll_order_no_pay.setOnClickListener {
            if(C.USER_ID.isEmpty()){
                activity?.startActivity<LoginActivity>()
            }else{
                activity?.startActivity<OrderListActivity>(Pair("type",C.ORDER_NO_PAY))
            }
        }
        ll_order_no_send.setOnClickListener {
            if(C.USER_ID.isEmpty()){
                activity?.startActivity<LoginActivity>()
            }else{
                activity?.startActivity<OrderListActivity>(Pair("type",C.ORDER_NO_SEND))
            }
        }
        ll_order_no_receive.setOnClickListener {
            if(C.USER_ID.isEmpty()){
                activity?.startActivity<LoginActivity>()
            }else{
                activity?.startActivity<OrderListActivity>(Pair("type",C.ORDER_NO_RECEIVE))
            }
        }
        ll_order_receive_over.setOnClickListener {
            if(C.USER_ID.isEmpty()){
                activity?.startActivity<LoginActivity>()
            }else{
                activity?.startActivity<OrderListActivity>(Pair("type",C.ORDER_RECEIVE_OVER))
            }
        }

        moreAdapter.setOnItemClickListener { _, _, position ->
            when(position){
                0 -> {
                    if(C.USER_ID.isEmpty()){
                        activity?.startActivity<LoginActivity>()
                    }else{
                        activity?.startActivity<TransactionActivity>(Pair("type",C.MARKET_ORDER_STATUS_BUY))
                    }
                }
                1 -> {
                    if(C.USER_ID.isEmpty()){
                        activity?.startActivity<LoginActivity>()
                    }else{
                        activity?.startActivity<TransactionActivity>(Pair("type",C.MARKET_ORDER_STATUS_SELL))
                    }
                }
                2 -> {
                    activity?.toast("暂未开通,敬请关注")?.setGravity(Gravity.CENTER, 0, 0)
                    //activity?.startActivity<FinanceActivity>()
                }
                3 -> {
                    if(C.USER_ID.isEmpty()){
                        activity?.startActivity<LoginActivity>()
                    }else{
                        activity?.startActivity<SmartEarnActivity>()
                    }
                }
                4 -> {
                    activity?.toast("暂未开通,敬请关注")?.setGravity(Gravity.CENTER, 0, 0)
                    //activity?.startActivity<LubricateActivity>()
                }
                5 -> {
                    if(C.USER_ID.isEmpty()){
                        activity?.startActivity<LoginActivity>()
                    }else{
                        activity?.startActivity<RecommendTeamActivity>()
                    }
                }
                6 -> {
                    if(C.USER_ID.isEmpty()){
                        activity?.startActivity<LoginActivity>()
                    }else{
                        activity?.startActivity<MyTaskActivity>()
                    }
                }
                7 -> activity?.startActivity<AboutUsActivity>()
                8 -> activity?.startActivity<WebContentActivity>(Pair("type",1),Pair("title","交易流程"))
                9 -> {
                    if(C.USER_ID.isEmpty()){
                        activity?.startActivity<LoginActivity>()
                    }else{
                        activity?.startActivity<AuthenticationActivity>()
                    }
                }
            }
        }
        swipe_refresh_layout.setOnRefreshListener {
            present?.getHomeBanner()
            if(C.USER_ID.isEmpty()){
                swipe_refresh_layout.finishRefresh()
                return@setOnRefreshListener
            }
            EventBus.getDefault().post(FirstInitUserEvent(false))
        }
    }

    fun backToHead(){
        nsv_mine.fullScroll(View.FOCUS_UP)
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETHOMEBANNER -> {
                    data.let {
                        data as BannerListBean
                        isBannerOver = true
                        if(isBannerOver&&isOrderOver){
                            isLoadComplete = true
                        }
                        if(data.advertList.isNotEmpty()){
                            ban_mine_list.visibility = View.VISIBLE
                            val advertList = arrayListOf<String>()
                            data.advertList.forEach {
                                advertList.add(it.bannerImg)
                            }
                            ban_mine_list.setImageLoader(GlideImageLoader())
                            ban_mine_list.setImages(advertList)
                            ban_mine_list.start()
                            ban_mine_list.setOnBannerListener {
                                activity?.startActivity<WebUrlActivity>(Pair("title",data.advertList[it].title),Pair("url",data.advertList[it].url))
                            }
                        }else{
                            ban_mine_list.visibility = View.GONE
                        }
                    }
                }
                SXContract.GETMYORDERSTATUSCOUNT -> {
                    data.let {
                        data as OrderStatusCountBean
                        isOrderOver = true
                        if(isBannerOver&&isOrderOver){
                            isLoadComplete = true
                        }
                        swipe_refresh_layout.finishRefresh()
                        qbv1?.badgeNumber = data.notPayNum
                        qbv2?.badgeNumber = data.waitSendGoodsNum
                        qbv3?.badgeNumber = data.waitGoodsNum
                        qbv4?.badgeNumber = data.haveGoodsNum
                    }
                }
                else -> {

                }
            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {
        swipe_refresh_layout.finishRefresh()
        activity?.toast(string!!)?.setGravity(Gravity.CENTER, 0, 0)
    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {
        swipe_refresh_layout.finishRefresh()
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