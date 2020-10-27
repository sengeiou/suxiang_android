package com.sx.enjoy.modules.mine

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.likai.lib.base.BaseFragment
import com.sx.enjoy.R
import com.sx.enjoy.adapter.TransactionListAdapter
import com.sx.enjoy.bean.MarketTransactionListBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import kotlinx.android.synthetic.main.empty_public_network.view.*
import kotlinx.android.synthetic.main.fragment_public_list.*
import kotlinx.android.synthetic.main.fragment_public_list.swipe_refresh_layout
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast

class TransactionFragment : BaseFragment(), SXContract.View{

    private lateinit var present: SXPresent

    private lateinit var mAdapter : TransactionListAdapter

    private lateinit var emptyView : View
    private lateinit var errorView : View

    private var type = 0
    private var status = 0
    private var pager = 1

    override fun getLayoutResource() = R.layout.fragment_public_list

    override fun beForInitView() {
        present = SXPresent(this)
    }

    override fun initView() {
        type = arguments!!.getInt("type",0)
        status = arguments!!.getInt("status",0)

        mAdapter = TransactionListAdapter(type)
        rcy_public_list.layoutManager = LinearLayoutManager(activity)
        rcy_public_list.adapter = mAdapter

        emptyView = View.inflate(activity,R.layout.empty_public_view,null)
        errorView = View.inflate(activity,R.layout.empty_public_network,null)
        mAdapter.isUseEmpty(false)

        getMyMarketOrderList(true)

        initEvent()
    }

    private fun initEvent(){
        swipe_refresh_layout.setOnRefreshListener {
            getMyMarketOrderList(true)
        }
        mAdapter.setOnLoadMoreListener {
            getMyMarketOrderList(false)
        }
        mAdapter.setOnItemClickListener { adapter, view, position ->
            activity?.startActivityForResult<TransactionDetailsActivity>(3003,
                Pair("marketId",mAdapter.data[position].id),
                Pair("type",type)
            )
        }
        errorView.iv_network_error.setOnClickListener {
            getMyMarketOrderList(true)
        }
    }

    fun getMyMarketOrderList(isRefresh: Boolean){
        if(isRefresh){
            pager = 1
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
        }else{
            pager++
            swipe_refresh_layout.finishRefresh()
        }
        present.getMyMarketOrderList(C.USER_ID,type.toString(),status.toString(),pager.toString(), C.PUBLIC_PAGER_NUMBER)
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETMYMARKETORDERLIST -> {
                    data?.let {
                        data as List<MarketTransactionListBean>
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
        fun newInstance(type:Int,status:Int): TransactionFragment {
            val fragment = TransactionFragment()
            val bundle = Bundle()
            bundle.putInt("type",type)
            bundle.putInt("status",status)
            fragment.arguments = bundle
            return fragment
        }
    }

}