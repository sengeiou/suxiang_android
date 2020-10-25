package com.sx.enjoy.modules.mine

import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sx.enjoy.R
import com.sx.enjoy.adapter.TaskListAdapter
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.TaskListBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import kotlinx.android.synthetic.main.activity_my_task.*
import kotlinx.android.synthetic.main.empty_public_network.view.*
import org.jetbrains.anko.toast

class MyTaskActivity : BaseActivity() , SXContract.View{

    private lateinit var present: SXPresent

    private lateinit var mAdapter: TaskListAdapter

    private lateinit var emptyView : View
    private lateinit var errorView : View

    private var pager = 1

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"我的任务")

    override fun getLayoutResource() = R.layout.activity_my_task

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {

        mAdapter = TaskListAdapter(3)
        rcy_my_task.layoutManager = LinearLayoutManager(this)
        rcy_my_task.adapter = mAdapter

        emptyView = View.inflate(this,R.layout.empty_public_view,null)
        errorView = View.inflate(this,R.layout.empty_public_network,null)
        mAdapter.isUseEmpty(false)

        getMyTaskList(true)

        initEvent()
    }

    private fun initEvent(){
        swipe_refresh_layout.setOnRefreshListener {
            getMyTaskList(true)
        }
        mAdapter.setOnLoadMoreListener {
            getMyTaskList(false)
        }
        errorView.iv_network_error.setOnClickListener {
            getMyTaskList(true)
        }
    }

    private fun getMyTaskList(isRefresh: Boolean){
        if(isRefresh){
            pager = 1
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
        }else{
            pager++
            swipe_refresh_layout.finishRefresh()
        }
        present.getMyTaskList(C.USER_ID,"",pager.toString(),C.PUBLIC_PAGER_NUMBER)
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETMYTASKLIST -> {
                    data?.let {
                        data as List<TaskListBean>
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
        toast(string!!).setGravity(Gravity.CENTER, 0, 0)
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
            toast("请检查网络连接").setGravity(Gravity.CENTER, 0, 0)
        }
    }

}