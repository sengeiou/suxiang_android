package com.sx.enjoy.modules.mine

import android.graphics.Color
import android.os.Bundle
import com.likai.lib.base.BaseFragment
import com.sx.enjoy.R
import com.sx.enjoy.adapter.MineMoreAdapter
import com.sx.enjoy.bean.MineMoreBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.utils.GlideImageLoader
import com.sx.enjoy.view.NoScrollGridManager
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.header_home_view.view.*
import org.jetbrains.anko.startActivity
import q.rorbin.badgeview.Badge
import q.rorbin.badgeview.QBadgeView

class MineFragment : BaseFragment(){


    private lateinit var moreAdapter: MineMoreAdapter

    private var qbv1 : Badge? = null
    private var qbv2 : Badge? = null
    private var qbv3 : Badge? = null
    private var qbv4 : Badge? = null

    override fun getLayoutResource() = R.layout.fragment_mine


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

    private fun initEvent(){
        ll_mine_setting.setOnClickListener {
            activity?.startActivity<AccountActivity>()
        }
        ll_mine_feedback.setOnClickListener {
            activity?.startActivity<FeedbackActivity>()
        }
        tv_member_level.setOnClickListener {
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

        moreAdapter.setOnItemClickListener { adapter, view, position ->
            when(position){
                0 -> {

                }
                1 -> {

                }
                2 -> activity?.startActivity<FinanceActivity>()
                3 -> activity?.startActivity<SmartEarnActivity>()
                4 -> activity?.startActivity<LubricateActivity>()
                5 -> activity?.startActivity<RecommendTeamActivity>()
                6 -> activity?.startActivity<MyTaskActivity>()
            }
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