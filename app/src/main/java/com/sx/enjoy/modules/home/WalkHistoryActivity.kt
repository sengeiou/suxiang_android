package com.sx.enjoy.modules.home

import android.view.Gravity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sx.enjoy.R
import com.sx.enjoy.adapter.WalkHistoryAdapter
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.WalkHistoryBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import kotlinx.android.synthetic.main.activity_walk_history.*
import org.jetbrains.anko.toast

class WalkHistoryActivity : BaseActivity() , SXContract.View{

    private lateinit var present: SXPresent

    private lateinit var mAdapter: WalkHistoryAdapter

    private var pager = 1
    private var type = 0

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL,if(type == 0) "历史步数" else "车行记录")

    override fun getLayoutResource() = R.layout.activity_walk_history

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        type = intent.getIntExtra("titleType",0)

        mAdapter = WalkHistoryAdapter()
        rcy_walk_history.layoutManager = LinearLayoutManager(this)
        rcy_walk_history.adapter = mAdapter

        if(type == 0){
            tv_sub_title.text = "步数"
        }else{
            tv_sub_title.text = "公里数"
        }

        getWalkHistory(true)

        initEvent()
    }

    private fun initEvent(){
        swipe_refresh_layout.setOnRefreshListener {
            getWalkHistory(true)
        }
        mAdapter.setOnLoadMoreListener {
            getWalkHistory(false)
        }
    }

    private fun getWalkHistory(isRefresh: Boolean){
        if(isRefresh){
            pager = 1
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
        }else{
            pager++
            swipe_refresh_layout.finishRefresh()
        }
        present.getWalkHistory(C.USER_ID,type.toString(),C.PUBLIC_PAGER_NUMBER,pager.toString())
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETWALKHISTORY -> {
                    data?.let {
                        data as List<WalkHistoryBean>
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