package com.sx.enjoy.modules.market

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
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
import kotlinx.android.synthetic.main.fragment_market.*
import kotlinx.android.synthetic.main.header_market_view.view.*
import org.jetbrains.anko.startActivity


class MarketFragment : BaseFragment(){

    private lateinit var headView:View
    private lateinit var mAdapter: MarketListAdapter

    override fun getLayoutResource() = R.layout.fragment_market


    override fun initView() {
        ImmersionBar.with(activity!!).statusBarDarkFont(true).titleBar(tb_market_title).init()

        mAdapter = MarketListAdapter()
        rcy_market_list.layoutManager = LinearLayoutManager(activity)
        rcy_market_list.adapter = mAdapter

        headView = View.inflate(activity,R.layout.header_market_view,null)
        mAdapter.addHeaderView(headView)


        initMarkChartView()
        initEvent()

        val mList = arrayListOf<String>()
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mAdapter.setNewData(mList)

        setMarkChartData()
    }

    override fun initData() {

    }

    private fun initEvent(){
        tv_market_income.setOnClickListener {
            activity?.startActivity<IncomeActivity>()
        }
        headView.tv_buy_in.setOnClickListener {
            activity?.startActivity<BuyInActivity>()
        }
        headView.tv_sell_out.setOnClickListener {
            activity?.startActivity<SellOutActivity>()
        }
        mAdapter.setOnItemClickListener { adapter, view, position ->
            activity?.startActivity<MarkDetailActivity>()
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
        val xAxisName = arrayOf("3/23", "3/24", "3/25", "3/26", "3/27", "3/28", "3/29")
        xAxis.valueFormatter = IAxisValueFormatter { value, axis ->
            xAxisName[value.toInt()]
        }

        //透明化图例
        val legend: Legend = headView.lc_market_quotations.legend
        legend.form = Legend.LegendForm.NONE
        legend.textColor = Color.WHITE

        //隐藏x轴描述
        val description = Description()
        description.isEnabled = false
        headView.lc_market_quotations.description = description
    }

    private fun setMarkChartData() {
        val entries = arrayListOf<Entry>()
        entries.add(Entry(0f, 4f))
        entries.add(Entry(1f, 9f))
        entries.add(Entry(2f, 11f))
        entries.add(Entry(3f, 5f))
        entries.add(Entry(4f, 20f))
        entries.add(Entry(5f, 15f))
        entries.add(Entry(6f, 20f))
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

    companion object {
        fun newInstance(): MarketFragment {
            val fragment = MarketFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

}