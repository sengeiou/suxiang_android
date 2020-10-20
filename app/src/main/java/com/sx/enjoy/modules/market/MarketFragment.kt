package com.sx.enjoy.modules.market

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.gyf.immersionbar.ImmersionBar
import com.likai.lib.base.BaseFragment
import com.sx.enjoy.R
import com.sx.enjoy.adapter.MarketListAdapter
import com.sx.enjoy.bean.MarketListBean
import com.sx.enjoy.bean.MarketQuotesBean
import com.sx.enjoy.bean.TaskListBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.event.TaskBuySuccessEvent
import com.sx.enjoy.modules.login.LoginActivity
import com.sx.enjoy.modules.mine.WebContentActivity
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import kotlinx.android.synthetic.main.fragment_market.*
import kotlinx.android.synthetic.main.fragment_market.swipe_refresh_layout
import kotlinx.android.synthetic.main.fragment_task_child.*
import kotlinx.android.synthetic.main.header_market_view.view.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat


class MarketFragment : BaseFragment(), SXContract.View{

    private lateinit var present: SXPresent

    private lateinit var headView:View
    private lateinit var mAdapter: MarketListAdapter

    private var pager = 1

    override fun getLayoutResource() = R.layout.fragment_market

    override fun beForInitView() {
        present = SXPresent(this)
    }

    override fun initView() {
        ImmersionBar.with(activity!!).statusBarDarkFont(true).titleBar(tb_market_title).init()

        mAdapter = MarketListAdapter()
        rcy_market_list.layoutManager = LinearLayoutManager(activity)
        rcy_market_list.adapter = mAdapter

        headView = View.inflate(activity,R.layout.header_market_view,null)
        mAdapter.addHeaderView(headView)


        initMarkChartView()
        initData()
        initEvent()

    }

    override fun initData() {
        present.getMarketQuotes("1","7")
        present.getMarketList(pager.toString(), C.PUBLIC_PAGER_NUMBER)
    }

    fun getMarketList(isRefresh: Boolean){
        if(isRefresh){
            pager = 1
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            present.getMarketQuotes("1","7")
        }else{
            pager++
            swipe_refresh_layout.finishRefresh()
        }
        present.getMarketList(pager.toString(), C.PUBLIC_PAGER_NUMBER)
    }

    private fun initEvent(){
        tv_market_income.setOnClickListener {
            if(C.USER_ID.isEmpty()){
                activity?.startActivity<LoginActivity>()
            }else{
                activity?.startActivity<IncomeActivity>()
            }
        }
        tv_transaction_content.setOnClickListener {
            activity?.startActivity<WebContentActivity>(Pair("type",1),Pair("title","交易流程"))
        }
        swipe_refresh_layout.setOnRefreshListener {
            getMarketList(true)
        }
        mAdapter.setOnLoadMoreListener {
            getMarketList(false)
        }
        headView.tv_buy_in.setOnClickListener {
            if(C.USER_ID.isEmpty()){
                activity?.startActivity<LoginActivity>()
            }else{
                val intent = Intent(activity,BuyInActivity::class.java)
                startActivity(intent)
            }
        }
        headView.tv_sell_out.setOnClickListener {
            if(C.USER_ID.isEmpty()){
                activity?.startActivity<LoginActivity>()
            }else{
                val intent = Intent(activity,SellOutActivity::class.java)
                startActivity(intent)
            }
        }
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val intent = Intent(activity,MarkDetailActivity::class.java)
            intent.putExtra("marketId",mAdapter.data[position].id)
            intent.putExtra("type",mAdapter.data[position].type)
            startActivity(intent)
        }
    }

    private fun initMarkChartView(){
        //设置样式
        val rightAxis = headView.lc_market_quotations.axisRight
        rightAxis.isEnabled = false
        val leftAxis = headView.lc_market_quotations.axisLeft
        leftAxis.isEnabled = true
        leftAxis.textColor = resources.getColor(R.color.title_second_color)
        leftAxis.setDrawAxisLine(false)
        leftAxis.axisMinimum = 0f
        leftAxis.textSize = 12f
        leftAxis.textColor = resources.getColor(R.color.title_second_color)
        leftAxis.gridColor = resources.getColor(R.color.color_CCFFCB)
        leftAxis.gridLineWidth = 1f

        //设置x轴
        val xAxis = headView.lc_market_quotations.xAxis
        xAxis.textColor = resources.getColor(R.color.title_second_color)
        xAxis.textSize = 12f
        xAxis.axisMinimum = 0f
        xAxis.setDrawAxisLine(false) //是否绘制轴线
        xAxis.setDrawGridLines(false) //设置x轴上每个点对应的线
        xAxis.setDrawLabels(true) //绘制标签  指x轴上的对应数值
        xAxis.position = XAxis.XAxisPosition.BOTTOM //设置x轴的显示位置
        xAxis.granularity = 1f //禁止放大后x轴标签重绘


        //透明化图例
        val legend: Legend = headView.lc_market_quotations.legend
        legend.form = Legend.LegendForm.NONE
        legend.textColor = Color.WHITE

        //隐藏x轴描述
        val description = Description()
        description.isEnabled = false
        headView.lc_market_quotations.description = description
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETMARKETLIST -> {
                    data?.let {
                        data as List<MarketListBean>
                        if(pager<=1){
                            swipe_refresh_layout.finishRefresh()
                            mAdapter.setEnableLoadMore(true)
                            mAdapter.setNewData(data)
                        }else{
                            if(data.isEmpty()){
                                mAdapter.loadMoreEnd()
                            }else{
                                mAdapter.addData(data)
                                mAdapter.loadMoreComplete()
                            }
                        }
                    }
                }
                SXContract.GETMARKETQUOTES -> {
                    data?.let {
                        data as List<MarketQuotesBean>
                        if(data.isNotEmpty()){
                            val xAxis = headView.lc_market_quotations.xAxis
                            xAxis.valueFormatter = IAxisValueFormatter { value, _ ->
                                data[value.toInt()].createTimeStr
                            }

                            val entries = arrayListOf<Entry>()
                            for (i in data.indices){
                                entries.add(Entry(i.toFloat(), data[i].amount.toFloat()))
                            }
                            val dataSet = LineDataSet(entries, "")
                            dataSet.setDrawValues(true) // 设置是否显示数据点的值
                            dataSet.setDrawCircleHole(true) // 设置数据点是空心还是实心，默认空心
                            dataSet.circleSize = 5f // 设置数据点的大小
                            dataSet.lineWidth = 2f //线条宽度
                            dataSet.valueTextSize = 11f
                            dataSet.color = resources.getColor(R.color.color_086E07)
                            dataSet.setCircleColor(resources.getColor(R.color.color_086E07))

                            val lineData = LineData(dataSet)
                            headView.lc_market_quotations.data = lineData
                            headView.lc_market_quotations.invalidate()
                        }
                    }
                }
                else -> {

                }
            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {
        activity?.toast(string!!)?.setGravity(Gravity.CENTER, 0, 0)
        if(isRefreshList){
            if(pager<=1){
                swipe_refresh_layout.finishRefresh()
                mAdapter.setEnableLoadMore(true)
            }else{
                mAdapter.loadMoreFail()
            }
        }
    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {
        if(isRefreshList){
            if(pager<=1){
                swipe_refresh_layout.finishRefresh()
                mAdapter.setEnableLoadMore(true)
            }else{
                mAdapter.loadMoreFail()
            }
        }else{
            activity?.toast("请检查网络连接")?.setGravity(Gravity.CENTER, 0, 0)
        }
    }

    companion object {
        fun newInstance(): MarketFragment {
            val fragment = MarketFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

}