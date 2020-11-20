package com.sx.enjoy.modules.mine

import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sx.enjoy.R
import com.sx.enjoy.adapter.RecommendTeamAdapter
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.TeamCountBean
import com.sx.enjoy.bean.TeamListBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.view.dialog.QuestionDialog
import kotlinx.android.synthetic.main.activity_recommend_team.*
import kotlinx.android.synthetic.main.activity_recommend_team.swipe_refresh_layout
import kotlinx.android.synthetic.main.empty_public_network.view.*
import kotlinx.android.synthetic.main.header_recommend_team.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class RecommendTeamActivity : BaseActivity() , SXContract.View {

    private lateinit var present: SXPresent

    private lateinit var mAdapter: RecommendTeamAdapter

    private lateinit var headerView : View
    private lateinit var emptyView : View
    private lateinit var errorView : View

    private lateinit var questionDialog: QuestionDialog

    private var pager = 1

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL,"推荐团队")

    override fun getLayoutResource() = R.layout.activity_recommend_team

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        questionDialog = QuestionDialog(this)

        mAdapter = RecommendTeamAdapter()
        rcy_recommend_team.layoutManager = LinearLayoutManager(this)
        rcy_recommend_team.adapter = mAdapter

        headerView = View.inflate(this,R.layout.header_recommend_team,null)
        mAdapter.addHeaderView(headerView)

        emptyView = View.inflate(this,R.layout.empty_public_view,null)
        errorView = View.inflate(this,R.layout.empty_public_network,null)
        mAdapter.emptyView = emptyView

        present.getTeamUserCount(C.USER_ID)
        getTeamList(true)

        initEvent()
    }


    private fun initEvent(){
        swipe_refresh_layout.setOnRefreshListener {
            present.getTeamUserCount(C.USER_ID)
            getTeamList(true)
        }
        mAdapter.setOnLoadMoreListener {
            getTeamList(false)
        }
        mAdapter.setOnItemClickListener { adapter, view, position ->
            startActivity<RecommendUserActivity>(Pair("userId",mAdapter.data[position].id))
        }
        errorView.iv_network_error.setOnClickListener {
            getTeamList(true)
        }
        headerView.tv_team_value.setOnClickListener {
            questionDialog.showQuestion(1,"指实名认证成功的会员")
        }
    }


    private fun getTeamList(isRefresh: Boolean){
        if(isRefresh){
            pager = 1
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
        }else{
            pager++
            swipe_refresh_layout.finishRefresh()
        }
        present.getUserTeamList(C.USER_ID,C.PUBLIC_PAGER_NUMBER,pager.toString())
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETUSERTEAMLIST -> {
                    data?.let {
                        data as List<TeamListBean>
                        if(pager<=1){
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
                SXContract.GETTEAMUSERCOUNT -> {
                    data?.let {
                        data as TeamCountBean
                        headerView.tv_team_count.text = data.teamTotal
                        headerView.tv_team_value.text = "有效${data.teamValid}人"
                        headerView.tv_next_count.text = data.nextTotal
                        headerView.tv_next_value.text = "有效${data.nextValid}人"
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