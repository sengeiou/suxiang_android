package com.sx.enjoy.modules.store

import android.view.Gravity
import androidx.recyclerview.widget.GridLayoutManager
import com.sx.enjoy.R
import com.sx.enjoy.adapter.CommodityListAdapter
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.CommodityListBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import kotlinx.android.synthetic.main.activity_commodity_list.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class CommodityListActivity : BaseActivity() , SXContract.View{

    private lateinit var present: SXPresent

    private lateinit var mAdapter : CommodityListAdapter

    private var pager = 1
    private var priceState = -1
    private var sale = -1
    private var cartId = ""
    private var key = ""

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"产品列表")

    override fun getLayoutResource() = R.layout.activity_commodity_list

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        if(intent.hasExtra("cartId")){
            cartId = intent.getStringExtra("cartId")
        }
        if(intent.hasExtra("key")){
            key = intent.getStringExtra("key")
        }

        mAdapter = CommodityListAdapter()
        rcy_commodity_list.layoutManager = GridLayoutManager(this,2)
        rcy_commodity_list.adapter = mAdapter

        getCommodityList(true)

        initEvent()
    }

    private fun getCommodityList(isRefresh: Boolean){
        if(isRefresh){
            pager = 1
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
        }else{
            pager++
            swipe_refresh_layout.finishRefresh()
        }
        present.getCommodityList(cartId,key,sale.toString(),priceState.toString(),C.PUBLIC_PAGER_NUMBER,pager.toString())
    }

    private fun initEvent(){
        ll_sale_price.setOnClickListener {
            tv_sale_price.setTextColor(resources.getColor(R.color.main_color))
            tv_sale_count.setTextColor(resources.getColor(R.color.color_3A3A3A))
            sale = -1
            when(priceState){
                -1 -> {
                    priceState = 0
                    iv_up_img.setImageResource(R.mipmap.ic_up_checked)
                    iv_down_img.setImageResource(R.mipmap.ic_down_normal)
                }
                0 -> {
                    priceState = 1
                    iv_up_img.setImageResource(R.mipmap.ic_up_normal)
                    iv_down_img.setImageResource(R.mipmap.ic_down_checked)
                }
                1 -> {
                    priceState = 0
                    iv_up_img.setImageResource(R.mipmap.ic_up_checked)
                    iv_down_img.setImageResource(R.mipmap.ic_down_normal)
                }
            }
            getCommodityList(true)
        }
        tv_sale_count.setOnClickListener {
            if(sale != 1){
                tv_sale_price.setTextColor(resources.getColor(R.color.color_3A3A3A))
                tv_sale_count.setTextColor(resources.getColor(R.color.main_color))
                priceState = -1
                iv_up_img.setImageResource(R.mipmap.ic_up_normal)
                iv_down_img.setImageResource(R.mipmap.ic_down_normal)
                sale = 1
                getCommodityList(true)
            }
        }

        tv_commodity_search.setOnClickListener {
            key = et_commodity_search.text.toString()
            getCommodityList(true)
        }

        mAdapter.setOnItemClickListener { adapter, view, position ->1
            startActivity<CommodityActivity>(Pair("commodityId",mAdapter.data[position].id))
        }

        swipe_refresh_layout.setOnRefreshListener {
            getCommodityList(true)
        }
        mAdapter.setOnLoadMoreListener {
            getCommodityList(false)
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETCOMMODITYLIST -> {
                    data?.let {
                        data as List<CommodityListBean>
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
                else -> {

                }
            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {
        toast(string!!).setGravity(Gravity.CENTER, 0, 0)
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
            toast("请检查网络连接").setGravity(Gravity.CENTER, 0, 0)
        }
    }

}