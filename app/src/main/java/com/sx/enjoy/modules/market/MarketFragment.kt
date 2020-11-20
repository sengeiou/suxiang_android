package com.sx.enjoy.modules.market

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
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
import com.likai.lib.commonutils.DensityUtils
import com.likai.lib.commonutils.ScreenUtils
import com.sx.enjoy.R
import com.sx.enjoy.adapter.MarketListAdapter
import com.sx.enjoy.bean.*
import com.sx.enjoy.constans.C
import com.sx.enjoy.modules.login.LoginActivity
import com.sx.enjoy.modules.mine.WebContentActivity
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.view.NoScrollLinearLayoutManager
import com.sx.enjoy.view.popupwindow.LeftMenuPopupWindow
import kotlinx.android.synthetic.main.empty_public_network.view.*
import kotlinx.android.synthetic.main.empty_public_view.view.*
import kotlinx.android.synthetic.main.fragment_market.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class MarketFragment : BaseFragment(), SXContract.View{

    private lateinit var present: SXPresent

    private lateinit var mAdapter: MarketListAdapter

    private lateinit var emptyView : View
    private lateinit var errorView : View

    private lateinit var popupWindow: LeftMenuPopupWindow

    private var pager = 1
    private var selectType = 0

    private var isTitleOver = false
    private var isListOver = false
    private var isDemandOver = false

    private var sellType = "-1"
    private var payType = "-1"
    private var sectionPosition = -1

    private var type = 0

    private var sectionList = arrayListOf<SectionList>()

    override fun getLayoutResource() = R.layout.fragment_market

    override fun beForInitView() {
        present = SXPresent(this)
    }

    override fun refreshData() {
        getMarketList(true,false)
    }

    override fun initView() {
        ImmersionBar.with(activity!!).statusBarDarkFont(true).titleBar(tb_market_title).init()

        popupWindow = LeftMenuPopupWindow(activity!!)

        mAdapter = MarketListAdapter()
        rcy_market_list.layoutManager = NoScrollLinearLayoutManager(activity)
        rcy_market_list.adapter = mAdapter

        emptyView = View.inflate(activity,R.layout.empty_public_view,null)
        errorView = View.inflate(activity,R.layout.empty_public_network,null)
        mAdapter.isUseEmpty(false)


        rcy_market_list.post {
            rcy_market_list.minimumHeight = nsv_market_list.height - DensityUtils.dp2px(activity,80)
        }

        initMarkChartView()
        initData()
        initEvent()

    }

    override fun initData() {
        present.getMarketQuotes(type.toString(),"1","7")
        present.getMarketDemand()
        present.getNewMarketList(sellType,et_commodity_search_2.text.toString(),et_count_min.text.toString(),et_count_max.text.toString(),et_price_min.text.toString(),et_price_max.text.toString(), payType,"",pager.toString(),C.PUBLIC_PAGER_NUMBER,false)
    }

    fun getMarketList(isRefresh: Boolean,isShowProgress:Boolean){
        if(isRefresh){
            pager = 1
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            present.getMarketQuotes(type.toString(),"1","7")
            present.getMarketDemand()
        }else{
            pager++
            swipe_refresh_layout.finishRefresh()
        }
        val sectionId = if(sectionPosition != -1 && sectionPosition<sectionList.size) sectionList[sectionPosition].sectionId else ""
        present.getNewMarketList(sellType,et_commodity_search_2.text.toString(),et_count_min.text.toString(),et_count_max.text.toString(),et_price_min.text.toString(),et_price_max.text.toString(), payType,sectionId,pager.toString(),C.PUBLIC_PAGER_NUMBER,isShowProgress)
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
            getMarketList(true,false)
            present.getMarketQuotes(type.toString(),"1","7")
        }
        mAdapter.setOnLoadMoreListener {
            getMarketList(false,false)
        }
        tv_buy_in.setOnClickListener {
            if(C.USER_ID.isEmpty()){
                activity?.startActivity<LoginActivity>()
            }else{
                selectType = 0
                present.getTransactionLimit("0")
            }
        }
        tv_sell_out.setOnClickListener {
            if(C.USER_ID.isEmpty()){
                activity?.startActivity<LoginActivity>()
            }else{
                selectType = 1
                present.getTransactionLimit("1")
            }
        }
        errorView.iv_network_error.setOnClickListener {
            getMarketList(true,true)
            present.getMarketQuotes(type.toString(),"1","7")
        }
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val intent = Intent(activity,MarkDetailActivity::class.java)
            intent.putExtra("marketId",mAdapter.data[position].id)
            intent.putExtra("type",mAdapter.data[position].type)
            startActivity(intent)
        }
        tv_cart_day.setOnClickListener {
            tv_cart_day.setTextColor(resources.getColor(R.color.color_3A7CA3))
            tv_cart_day.setBackgroundResource(R.drawable.bg_blue_full_15)
            tv_cart_week.setTextColor(resources.getColor(R.color.color_666666))
            tv_cart_week.setBackgroundColor(resources.getColor(R.color.white))
            type = 0
            present.getMarketQuotes(type.toString(),"1","7")
        }
        tv_cart_week.setOnClickListener {
            tv_cart_day.setTextColor(resources.getColor(R.color.color_666666))
            tv_cart_day.setBackgroundColor(resources.getColor(R.color.white))
            tv_cart_week.setTextColor(resources.getColor(R.color.color_3A7CA3))
            tv_cart_week.setBackgroundResource(R.drawable.bg_blue_full_15)
            type = 1
            present.getMarketQuotes(type.toString(),"1","7")
        }
        nsv_market_list.setOnScrollListener {
            if(it>=ll_market_header.height){
                ll_select_limit.visibility = View.VISIBLE
            }else{
                ll_select_limit.visibility = View.GONE
            }
        }
        ll_menu_select_1.setOnClickListener {
            popupWindow.showAtBottom(ll_menu_top_1)
        }
        ll_menu_select_2.setOnClickListener {
            popupWindow.showAtBottom(ll_menu_top_2)
        }
        ll_filtrate_1.setOnClickListener {
            dl_market_filtrate.openDrawer(GravityCompat.END)
        }
        ll_filtrate_2.setOnClickListener {
            dl_market_filtrate.openDrawer(GravityCompat.END)
        }
        et_commodity_search_1.setOnClickListener {
            nsv_market_list.smoothScrollTo(0,ll_market_head.height+DensityUtils.dp2px(activity,21))
            Handler().postDelayed({
                et_commodity_search_2.requestFocus()
                val manager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.showSoftInput(et_commodity_search_2, InputMethodManager.SHOW_IMPLICIT)
            },100)
        }
        et_commodity_search_2.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                et_commodity_search_1.text = s
            }
        })
        tv_count_1_1.setOnClickListener {
            if(sectionList.isNotEmpty()){
                if(sectionPosition == 0){
                    sectionPosition = -1
                    tv_count_1_1.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_1_2.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_1_3.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_1.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_2.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_3.setTextColor(resources.getColor(R.color.title_main_color))
                }else{
                    sectionPosition = 0
                    tv_count_1_1.setTextColor(resources.getColor(R.color.main_color))
                    tv_count_1_2.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_1_3.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_1.setTextColor(resources.getColor(R.color.main_color))
                    tv_count_2_2.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_3.setTextColor(resources.getColor(R.color.title_main_color))
                    et_count_min.setText("")
                    et_count_max.setText("")
                }
                getMarketList(true,true)
            }
        }
        tv_count_1_2.setOnClickListener {
            if(sectionList.size>1){
                if(sectionPosition == 1){
                    sectionPosition = -1
                    tv_count_1_1.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_1_2.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_1_3.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_1.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_2.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_3.setTextColor(resources.getColor(R.color.title_main_color))
                }else{
                    sectionPosition = 1
                    tv_count_1_1.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_1_2.setTextColor(resources.getColor(R.color.main_color))
                    tv_count_1_3.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_1.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_2.setTextColor(resources.getColor(R.color.main_color))
                    tv_count_2_3.setTextColor(resources.getColor(R.color.title_main_color))
                    et_count_min.setText("")
                    et_count_max.setText("")
                }
                getMarketList(true,true)
            }
        }
        tv_count_1_3.setOnClickListener {
            if(sectionList.size>2){
                if(sectionPosition == 2){
                    sectionPosition = -1
                    tv_count_1_1.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_1_2.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_1_3.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_1.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_2.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_3.setTextColor(resources.getColor(R.color.title_main_color))
                }else{
                    sectionPosition = 2
                    tv_count_1_1.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_1_2.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_1_3.setTextColor(resources.getColor(R.color.main_color))
                    tv_count_2_1.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_2.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_3.setTextColor(resources.getColor(R.color.main_color))
                    et_count_min.setText("")
                    et_count_max.setText("")
                }
                getMarketList(true,true)
            }
        }
        tv_count_2_1.setOnClickListener {
            if(sectionList.isNotEmpty()){
                if(sectionPosition == 0){
                    sectionPosition = -1
                    tv_count_1_1.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_1_2.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_1_3.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_1.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_2.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_3.setTextColor(resources.getColor(R.color.title_main_color))
                }else{
                    sectionPosition = 0
                    tv_count_1_1.setTextColor(resources.getColor(R.color.main_color))
                    tv_count_1_2.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_1_3.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_1.setTextColor(resources.getColor(R.color.main_color))
                    tv_count_2_2.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_3.setTextColor(resources.getColor(R.color.title_main_color))
                    et_count_min.setText("")
                    et_count_max.setText("")
                }
                getMarketList(true,true)
            }
        }
        tv_count_2_2.setOnClickListener {
            if(sectionList.size>1){
                if(sectionPosition == 1){
                    sectionPosition = -1
                    tv_count_1_1.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_1_2.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_1_3.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_1.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_2.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_3.setTextColor(resources.getColor(R.color.title_main_color))
                }else{
                    sectionPosition = 1
                    tv_count_1_1.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_1_2.setTextColor(resources.getColor(R.color.main_color))
                    tv_count_1_3.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_1.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_2.setTextColor(resources.getColor(R.color.main_color))
                    tv_count_2_3.setTextColor(resources.getColor(R.color.title_main_color))
                    et_count_min.setText("")
                    et_count_max.setText("")
                }
                getMarketList(true,true)
            }
        }
        tv_count_2_3.setOnClickListener {
            if(sectionList.size>2){
                if(sectionPosition == 2){
                    sectionPosition = -1
                    tv_count_1_1.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_1_2.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_1_3.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_1.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_2.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_3.setTextColor(resources.getColor(R.color.title_main_color))
                }else{
                    sectionPosition = 2
                    tv_count_1_1.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_1_2.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_1_3.setTextColor(resources.getColor(R.color.main_color))
                    tv_count_2_1.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_2.setTextColor(resources.getColor(R.color.title_main_color))
                    tv_count_2_3.setTextColor(resources.getColor(R.color.main_color))
                    et_count_min.setText("")
                    et_count_max.setText("")
                }
                getMarketList(true,true)
            }
        }
        tv_pay_1.setOnClickListener {
            payType = "-1"
            tv_pay_1.setTextColor(resources.getColor(R.color.white))
            tv_pay_2.setTextColor(resources.getColor(R.color.color_666666))
            tv_pay_3.setTextColor(resources.getColor(R.color.color_666666))
            tv_pay_1.setBackgroundResource(R.drawable.bg_main_full_15)
            tv_pay_2.setBackgroundResource(R.drawable.bg_grey_full_15)
            tv_pay_3.setBackgroundResource(R.drawable.bg_grey_full_15)
        }
        tv_pay_2.setOnClickListener {
            payType = "0"
            tv_pay_1.setTextColor(resources.getColor(R.color.color_666666))
            tv_pay_2.setTextColor(resources.getColor(R.color.white))
            tv_pay_3.setTextColor(resources.getColor(R.color.color_666666))
            tv_pay_1.setBackgroundResource(R.drawable.bg_grey_full_15)
            tv_pay_2.setBackgroundResource(R.drawable.bg_main_full_15)
            tv_pay_3.setBackgroundResource(R.drawable.bg_grey_full_15)
        }
        tv_pay_3.setOnClickListener {
            payType = "1"
            tv_pay_1.setTextColor(resources.getColor(R.color.color_666666))
            tv_pay_2.setTextColor(resources.getColor(R.color.color_666666))
            tv_pay_3.setTextColor(resources.getColor(R.color.white))
            tv_pay_1.setBackgroundResource(R.drawable.bg_grey_full_15)
            tv_pay_2.setBackgroundResource(R.drawable.bg_grey_full_15)
            tv_pay_3.setBackgroundResource(R.drawable.bg_main_full_15)
        }
        tv_market_search_1.setOnClickListener {
            getMarketList(true,true)
        }
        tv_market_search_2.setOnClickListener {
            getMarketList(true,true)
        }
        et_commodity_search_2.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getMarketList(true,true)
                true
            }
            false

        }
        tv_save_select.setOnClickListener {
            if(et_count_min.text.isNotEmpty()&&et_count_max.text.isNotEmpty()&&et_count_min.text.toString().toInt()>et_count_max.text.toString().toInt()){
                activity?.toast("最高数应该大于最低数")?.setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            if(et_price_min.text.isNotEmpty()&&et_price_max.text.isNotEmpty()&&et_price_min.text.toString().toInt()>et_price_max.text.toString().toInt()){
                activity?.toast("最高价应该大于最低价")?.setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            dl_market_filtrate.closeDrawer(GravityCompat.END)
            if(et_count_max.text.isNotEmpty()||et_count_min.text.isNotEmpty()){
                sectionPosition = -1
                tv_count_1_1.setTextColor(resources.getColor(R.color.title_main_color))
                tv_count_1_2.setTextColor(resources.getColor(R.color.title_main_color))
                tv_count_1_3.setTextColor(resources.getColor(R.color.title_main_color))
                tv_count_2_1.setTextColor(resources.getColor(R.color.title_main_color))
                tv_count_2_2.setTextColor(resources.getColor(R.color.title_main_color))
                tv_count_2_3.setTextColor(resources.getColor(R.color.title_main_color))
            }
            getMarketList(true,true)
        }
        tv_reset_select.setOnClickListener {
            et_count_min.setText("")
            et_count_max.setText("")
            et_price_min.setText("")
            et_price_max.setText("")

            payType = "-1"
            tv_pay_1.setTextColor(resources.getColor(R.color.white))
            tv_pay_2.setTextColor(resources.getColor(R.color.color_666666))
            tv_pay_3.setTextColor(resources.getColor(R.color.color_666666))
            tv_pay_1.setBackgroundResource(R.drawable.bg_main_full_15)
            tv_pay_2.setBackgroundResource(R.drawable.bg_grey_full_15)
            tv_pay_3.setBackgroundResource(R.drawable.bg_grey_full_15)
        }
        ll_select_right.setOnClickListener {  }
        popupWindow.setOnRightMenuSelectListener(object : LeftMenuPopupWindow.OnRightMenuSelectListener{
            override fun onTypeSelectPosition(position: Int) {
                when(position){
                    0 -> {
                        sellType = "-1"
                        tv_select_name_1.text = "全部交易"
                        tv_select_name_2.text = "全部交易"
                    }
                    1 -> {
                        sellType = "1"
                        tv_select_name_1.text = "卖出"
                        tv_select_name_2.text = "卖出"
                    }
                    2 -> {
                        sellType = "0"
                        tv_select_name_1.text = "买入"
                        tv_select_name_2.text = "买入"
                    }
                }
                getMarketList(true,true)
            }

            override fun onPopupStatus(isShow: Boolean) {
                iv_market_arrow_1.setImageResource(if(isShow) R.mipmap.ic_market_up else R.mipmap.ic_market_down)
                iv_market_arrow_2.setImageResource(if(isShow) R.mipmap.ic_market_up else R.mipmap.ic_market_down)
            }
        })
    }

    private fun initMarkChartView(){
        //设置样式
        val rightAxis = lc_market_quotations.axisRight
        rightAxis.isEnabled = false
        val leftAxis = lc_market_quotations.axisLeft
        leftAxis.isEnabled = true
        leftAxis.textColor = resources.getColor(R.color.title_second_color)
        leftAxis.setDrawAxisLine(false)
        leftAxis.axisMinimum = 0f
        leftAxis.textSize = 12f
        leftAxis.textColor = resources.getColor(R.color.color_666666)
        leftAxis.gridColor = resources.getColor(R.color.color_666666)

        //设置x轴
        val xAxis = lc_market_quotations.xAxis
        xAxis.textColor = resources.getColor(R.color.color_666666)
        xAxis.textSize = 12f
        xAxis.axisMinimum = 0f
        xAxis.setDrawAxisLine(true) //是否绘制轴线
        xAxis.setDrawGridLines(true) //设置x轴上每个点对应的线
        xAxis.setDrawLabels(true) //绘制标签  指x轴上的对应数值
        leftAxis.textColor = resources.getColor(R.color.color_666666)
        xAxis.gridColor = resources.getColor(R.color.color_666666)
        xAxis.position = XAxis.XAxisPosition.BOTTOM //设置x轴的显示位置
        xAxis.granularity = 1f //禁止放大后x轴标签重绘

        val yAxis = lc_market_quotations.axisLeft
        yAxis.setStartAtZero(false)

        //透明化图例
        val legend: Legend = lc_market_quotations.legend
        legend.form = Legend.LegendForm.NONE
        legend.textColor = Color.WHITE

        //隐藏x轴描述
        val description = Description()
        description.isEnabled = false
        lc_market_quotations.description = description
        lc_market_quotations.setTouchEnabled(false)
        lc_market_quotations.isDragEnabled = false
        lc_market_quotations.isScaleYEnabled = false
        lc_market_quotations.isScaleXEnabled = false
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETNEWMARKETLIST -> {
                    data?.let {
                        data as List<NewMarketListBean>
                        isListOver = true
                        if(isListOver&&isTitleOver&&isDemandOver){
                            isLoadComplete = true
                        }
                        if(pager<=1){
                            mAdapter.isUseEmpty(true)
                            mAdapter.emptyView = emptyView
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
                        isTitleOver = true
                        if(isListOver&&isTitleOver&&isDemandOver){
                            isLoadComplete = true
                        }
                        if(data.isNotEmpty()){
                            val xAxis = lc_market_quotations.xAxis
                            xAxis.valueFormatter = IAxisValueFormatter { value, _ ->
                                if(value.toInt()<0||value.toInt()>=data.size){
                                    ""
                                }else{
                                    data[value.toInt()].createTimeStr
                                }
                            }

                            val entries = arrayListOf<Entry>()
                            for (i in data.indices){
                                entries.add(Entry(i.toFloat(), data[i].amount.toFloat()))
                            }
                            val dataSet = LineDataSet(entries, "")
                            dataSet.setDrawValues(true) // 设置是否显示数据点的值
                            dataSet.setDrawCircleHole(true) // 设置数据点是空心还是实心，默认空心
                            dataSet.circleSize = 3f // 设置数据点的大小
                            dataSet.lineWidth = 1f //线条宽度
                            dataSet.valueTextSize = 11f
                            dataSet.color = resources.getColor(R.color.color_3A7CA3)
                            dataSet.setCircleColor(resources.getColor(R.color.color_3A7CA3))
                            dataSet.setDrawFilled(true)
                            dataSet.fillDrawable = ContextCompat.getDrawable(activity!!, R.drawable.bg_market_line)

                            val lineData = LineData(dataSet)
                            lc_market_quotations.data = lineData
                            lc_market_quotations.invalidate()
                        }
                    }
                }
                SXContract.GETTRANSACTIONLIMIT -> {
                    if(selectType == 0){
                        val intent = Intent(activity,BuyInActivity::class.java)
                        startActivity(intent)
                    }else{
                        val intent = Intent(activity,SellOutActivity::class.java)
                        startActivity(intent)
                    }
                }
                SXContract.GETMARKETDEMAND -> {
                    data.let {
                        data as MarketDemandBean
                        isDemandOver = true
                        if(isListOver&&isTitleOver&&isDemandOver){
                            isLoadComplete = true
                        }
                        tv_demand_sum.text = data.demandSum
                        tv_transaction_sum.text = data.transactionSum
                        tv_us_number.text = data.usdt+"USDT"
                        tv_zg_number.text = "≈${data.todayRichQuotes}ZGCT"

                        sectionList.clear()
                        sectionList.addAll(data.sections)

                        if(data.sections.isNotEmpty()){
                            tv_count_1_1.text = data.sections[0].sectionStr
                            tv_count_2_1.text = data.sections[0].sectionStr
                        }
                        if(data.sections.size>1){
                            tv_count_1_2.text = data.sections[1].sectionStr
                            tv_count_2_2.text = data.sections[1].sectionStr
                        }
                        if(data.sections.size>2){
                            tv_count_1_3.text = data.sections[2].sectionStr
                            tv_count_2_3.text = data.sections[2].sectionStr
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
                mAdapter.isUseEmpty(true)
                mAdapter.emptyView = emptyView
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
                mAdapter.isUseEmpty(true)
                mAdapter.emptyView = errorView
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