package com.sx.enjoy.modules.mine

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.likai.lib.base.BaseFragment
import com.sx.enjoy.R
import com.sx.enjoy.adapter.OrderListAdapter
import com.sx.enjoy.bean.NewOrderBean
import com.sx.enjoy.bean.OrderListBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.modules.store.OrderPayActivity
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.view.dialog.ReminderDialog
import kotlinx.android.synthetic.main.empty_public_network.view.*
import kotlinx.android.synthetic.main.fragment_public_list.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class OrderPagerFragment : BaseFragment(),SXContract.View{

    private lateinit var present: SXPresent

    private lateinit var reminderDialog: ReminderDialog

    private lateinit var mAdapter: OrderListAdapter

    private lateinit var emptyView : View
    private lateinit var errorView : View

    private var isFirstShow = false
    private var isInitView = false
    private var pager = 1
    private var operationType = 0
    private var operationPosition =  0

    override fun getLayoutResource() = R.layout.fragment_public_list

    override fun beForInitView() {
        present = SXPresent(this)
    }

    override fun initView() {
        reminderDialog = ReminderDialog(activity!!)

        mAdapter = OrderListAdapter()
        rcy_public_list.layoutManager = LinearLayoutManager(activity)
        rcy_public_list.adapter = mAdapter

        emptyView = View.inflate(activity,R.layout.empty_public_view,null)
        errorView = View.inflate(activity,R.layout.empty_public_network,null)
        mAdapter.isUseEmpty(false)

        isInitView = true
        if(isFirstShow){
            isFirstShow = false
            getOrderList(isRefresh = true, isShowProgress = true)
        }

        initEvent()

    }

    private fun initEvent(){
        swipe_refresh_layout.setOnRefreshListener {
            getOrderList(isRefresh = true, isShowProgress = false)
        }
        mAdapter.setOnLoadMoreListener {
            getOrderList(isRefresh = false, isShowProgress = false)
        }
        mAdapter.setOnItemClickListener { adapter, view, position ->
            activity?.startActivity<OrderDetailsActivity>(Pair("orderNo",mAdapter.data[position].orderNo))
        }
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when(view.id){
                R.id.tv_order_button_1 -> {
                    when(mAdapter.data[position].status){
                        C.ORDER_NO_PAY -> {
                            operationPosition = position
                            operationType = 1
                            reminderDialog.showReminder(4)
                        }
                        C.ORDER_ORDER_CANCEL -> {
                            operationPosition = position
                            operationType = 2
                            reminderDialog.showReminder(5)
                        }
                    }
                }
                R.id.tv_order_button_2 -> {
                    when(mAdapter.data[position].status){
                        C.ORDER_NO_PAY -> {
                            activity?.startActivity<OrderPayActivity>(Pair("order",NewOrderBean(mAdapter.data[position].orderNo,mAdapter.data[position].total)))
                        }
                    }
                }
            }
        }
        reminderDialog.setOnNoticeConfirmListener(object :ReminderDialog.OnNoticeConfirmListener{
            override fun onConfirm() {
                when(operationType){
                    1 -> present.cancelOrder(mAdapter.data[operationPosition].id)
                    2 -> present.deleteOrder(mAdapter.data[operationPosition].id)
                }
            }
        })

        errorView.iv_network_error.setOnClickListener {
            getOrderList(isRefresh = true, isShowProgress = true)
        }
    }

    fun setIsFirstShow(isFirstShow: Boolean){
        this.isFirstShow = isFirstShow
    }

    fun getOrderList(isRefresh: Boolean,isShowProgress:Boolean){
        if(!isInitView){
            return
        }
        if(isRefresh){
            pager = 1
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
        }else{
            pager++
            swipe_refresh_layout.finishRefresh()
        }
        present.getOrderList(C.USER_ID,arguments?.getInt("type",-1).toString(),pager.toString(), C.PUBLIC_PAGER_NUMBER,isShowProgress)
    }


    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETORDERLIST -> {
                    data?.let {
                        data as List<OrderListBean>
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
                SXContract.CANCELORDER -> {
                    activity?.toast("订单已取消")?.setGravity(Gravity.CENTER, 0, 0)
                    getOrderList(isRefresh = true, isShowProgress = true)
                }
                SXContract.DELETEORDER -> {
                    activity?.toast("订单已删除")?.setGravity(Gravity.CENTER, 0, 0)
                    getOrderList(isRefresh = true, isShowProgress = true)
                }
                else -> {

                }
            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {
        activity?.toast(string!!)
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
        fun newInstance(type:Int): OrderPagerFragment {
            val fragment = OrderPagerFragment()
            val bundle = Bundle()
            bundle.putInt("type",type)
            fragment.arguments = bundle
            return fragment
        }
    }

}