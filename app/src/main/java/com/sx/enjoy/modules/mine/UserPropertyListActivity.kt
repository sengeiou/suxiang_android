package com.sx.enjoy.modules.mine

import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sx.enjoy.R
import com.sx.enjoy.adapter.UserPropertyAdapter
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.UserPropertyBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import kotlinx.android.synthetic.main.activity_user_property_list.*
import kotlinx.android.synthetic.main.empty_public_network.view.*
import org.jetbrains.anko.toast

class UserPropertyListActivity : BaseActivity() , SXContract.View{

    private lateinit var present: SXPresent

    private lateinit var mAdapter : UserPropertyAdapter

    private lateinit var emptyView : View
    private lateinit var errorView : View

    private var pager = 1
    private var type = 0


    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,when(type){
        0 -> "贡献值明细"
        1 -> "活跃度明细"
        else -> "经验值明细"
    } )

    override fun getLayoutResource() = R.layout.activity_user_property_list

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        type = intent.getIntExtra("type",0)

        mAdapter = UserPropertyAdapter(type)
        rcy_user_property.layoutManager = LinearLayoutManager(this)
        rcy_user_property.adapter = mAdapter

        emptyView = View.inflate(this,R.layout.empty_public_view,null)
        errorView = View.inflate(this,R.layout.empty_public_network,null)
        mAdapter.isUseEmpty(false)

        getUserPropertyList(true)

        initEvent()
    }

    private fun initEvent(){
        swipe_refresh_layout.setOnRefreshListener {
            getUserPropertyList(true)
        }
        mAdapter.setOnLoadMoreListener {
            getUserPropertyList(false)
        }
        errorView.iv_network_error.setOnClickListener {
            getUserPropertyList(true)
        }
    }

    private fun getUserPropertyList(isRefresh: Boolean){
        if(isRefresh){
            pager = 1
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
        }else{
            pager++
            swipe_refresh_layout.finishRefresh()
        }
        when(type){
            0 -> present.getMyContribList(C.USER_ID, C.PUBLIC_PAGER_NUMBER,pager.toString())
            1 -> present.getMyActivityList(C.USER_ID, C.PUBLIC_PAGER_NUMBER,pager.toString())
            2 -> present.getMySufferList(C.USER_ID, C.PUBLIC_PAGER_NUMBER,pager.toString())
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETMYCONTRIBLIST ,SXContract.GETMYACTIVITYLIST,SXContract.GETMYSUFFERLIST -> {
                    data?.let {
                        data as List<UserPropertyBean>
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
