package com.sx.enjoy.modules.home

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.likai.lib.base.BaseFragment
import com.likai.lib.commonutils.DensityUtils
import com.sx.enjoy.R
import com.sx.enjoy.adapter.HomeListAdapter
import com.sx.enjoy.bean.*
import com.sx.enjoy.constans.C
import com.sx.enjoy.modules.login.LoginActivity
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.utils.GlideImageLoader
import com.sx.enjoy.utils.ShakeDetector
import com.sx.enjoy.view.dialog.SignDialog
import com.sx.enjoy.view.dialog.SignOverDialog
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.header_home_view.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.litepal.LitePal


class HomeFragment : BaseFragment(),SXContract.View{

    private lateinit var signDialog: SignDialog
    private lateinit var signOverDialog: SignOverDialog

    private lateinit var present: SXPresent

    private lateinit var headView:View
    private lateinit var mAdapter:HomeListAdapter


    private var pager = 1
    private var titleType = 0

    override fun getLayoutResource() = R.layout.fragment_home

    override fun beForInitView() {
        present = SXPresent(this)
    }

    override fun initView() {
        ImmersionBar.with(activity!!).statusBarDarkFont(true).titleBar(tb_home_title).init()

        signDialog = SignDialog(activity!!)
        signOverDialog = SignOverDialog(activity!!)

        mAdapter = HomeListAdapter()
        rcy_home_list.layoutManager = LinearLayoutManager(activity)
        rcy_home_list.adapter = mAdapter

        headView = View.inflate(activity,R.layout.header_home_view,null)
        mAdapter.addHeaderView(headView)


        present.getHomeBanner()
        present.getHomeNotice()
        getNewsList(true)


        initData()
        initEvent()
    }


    override fun initData() {
        if(!C.IS_SIGN_REQUEST&&C.USER_ID.isNotEmpty()){
            C.IS_SIGN_REQUEST = true
            present.getSignResult(C.USER_ID,false)
        }
    }

    fun initUser(){
        val user = LitePal.findLast(UserBean::class.java)
        if(user!=null){
            headView.tv_user_activity.text = user.userActivity.toString()
            headView.tv_user_contribution.text = user.userContrib.toString()
            headView.tv_user_rice.text = user.riceGrains.toString()
        }else{
            headView.tv_user_activity.text = "0"
            headView.tv_user_contribution.text = "0"
            headView.tv_user_rice.text = "0"
        }
    }

    fun initStep(){
        headView.tv_distances.text = C.USER_STEP.toString()
    }
    fun initDayRice(data:StepRiceBean?){
        if(data==null){
            headView.tv_rice.text = "0"
        }else{
            headView.tv_rice.text = if(titleType == 0) data.walkRiceGrains else data.drivingRiceGrains
        }
    }

    private fun initEvent(){
        iv_home_sign.setOnClickListener {
            if(C.USER_ID.isEmpty()){
                activity?.startActivity<LoginActivity>()
            }else{
                present.getSignResult(C.USER_ID,true)
            }
        }
        tv_note_list.setOnClickListener {
            if(C.USER_ID.isEmpty()){
                activity?.startActivity<LoginActivity>()
            }else{
                activity?.startActivity<WalkHistoryActivity>(Pair("titleType",titleType))
            }
        }
        hst_title.setOnSortSelectListener {
            titleType = it
            when(it){
                0 -> {
                    tv_note_list.text = "历史步数"
                    iv_home_head.setImageResource(R.mipmap.ic_home_bg_walk)
                    iv_note_type.setImageResource(R.mipmap.ic_people)
                    headView.tv_distances.text = "0"
                    headView.tv_distances_unit.text = "步"
                    headView.tv_distances_today.text = "当日步数"
                }
                1 -> {
                    tv_note_list.text = "车行记录"
                    iv_home_head.setImageResource(R.mipmap.ic_home_bg_car)
                    iv_note_type.setImageResource(R.mipmap.ic_car)
                    headView.tv_distances.text = "0"
                    headView.tv_distances_unit.text = "km"
                    headView.tv_distances_today.text = "当日车行公里数"
                }
            }
        }
        signDialog.setOnNoticeConfirmListener(object :SignDialog.OnNoticeConfirmListener{
            override fun onConfirm() {
                activity?.startActivity<SignAnswerActivity>()
            }
        })

        swipe_refresh_layout.setOnRefreshListener {
            present.getHomeBanner()
            present.getHomeNotice()
            getNewsList(true)
        }

        mAdapter.setOnLoadMoreListener {
            getNewsList(false)
        }

        rcy_home_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val position: Int = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if(position<=0){
                    val bottom = DensityUtils.dp2px(activity,225f)
                    val curIndex = rcy_home_list.getChildAt(0).top
                    when {
                        curIndex>=0 -> {
                            v_home_title.alpha = 0f
                            v_status_bar.alpha = 0f
                        }
                        bottom+curIndex<0 -> {
                            v_home_title.alpha = 1f
                            v_status_bar.alpha = 1f
                        }
                        else -> {
                            v_home_title.alpha = (bottom-(bottom+curIndex))/bottom
                            v_status_bar.alpha = (bottom-(bottom+curIndex))/bottom
                        }
                    }
                }else{
                    v_home_title.alpha = 1f
                    v_status_bar.alpha = 1f
                }
            }
        })
    }

    private fun getNewsList(isRefreshList: Boolean){
        if(isRefreshList){
            pager = 1
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
        }else{
            pager++
            swipe_refresh_layout.finishRefresh()
        }
        present.getHomeNews(C.PUBLIC_PAGER_NUMBER,pager.toString())
    }

    override fun onResume() {
        super.onResume()
        headView.tb_home_notice.startViewAnimator()
        headView.ban_top_list.startAutoPlay()
        headView. ban_bottom_list.startAutoPlay()
    }



    override fun onStop() {
        super.onStop()
        headView.tb_home_notice.stopViewAnimator()
        headView.ban_top_list.stopAutoPlay()
        headView.ban_bottom_list.stopAutoPlay()
    }



    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETSIGNRESULT+"0" -> {
                    data?.let {
                        data as Boolean
                        if(!data){
                            signDialog.show()
                        }
                    }
                }
                SXContract.GETSIGNRESULT+"1" -> {
                    data?.let {
                        data as Boolean
                        if(data){
                            signOverDialog.show()
                        }else{
                            activity?.startActivity<SignAnswerActivity>()
                        }
                    }
                }
                SXContract.GETHOMEBANNER -> {
                    data?.let {
                        data as BannerListBean
                        if(data.shuffList.isNotEmpty()){
                            headView.ban_top_list.visibility = View.VISIBLE
                            val bannerList = arrayListOf<String>()
                            data.shuffList.forEach {
                                bannerList.add(it.bannerImg)
                            }
                            headView.ban_top_list.setImageLoader(GlideImageLoader())
                            headView.ban_top_list.setImages(bannerList)
                            headView.ban_top_list.start()
                        }else{
                            headView.ban_top_list.visibility = View.GONE
                        }
                        if(data.advertList.isNotEmpty()){
                            headView.ban_bottom_list.visibility = View.VISIBLE
                            val advertList = arrayListOf<String>()
                            data.advertList.forEach {
                                advertList.add(it.bannerImg)
                            }
                            headView.ban_bottom_list.setImageLoader(GlideImageLoader())
                            headView.ban_bottom_list.setImages(advertList)
                            headView.ban_bottom_list.start()
                        }else{
                            headView.ban_bottom_list.visibility = View.GONE
                        }
                    }
                }
                SXContract.GETHOMENOTICE -> {
                    data?.let {
                        swipe_refresh_layout.finishRefresh()
                        data as List<NoticeListBean>
                        if(data.isNotEmpty()){
                            headView.ll_home_notice.visibility = View.VISIBLE
                            val noticeList = arrayListOf<String>()
                            data.forEach {
                                noticeList.add(it.title)
                            }
                            headView.tb_home_notice.setDatas(noticeList)
                        }else{
                            headView.ll_home_notice.visibility = View.GONE
                        }

                    }
                }
                SXContract.GETHOMENEWS -> {
                    data?.let {
                        data as List<NewsListBean>
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
        activity?.toast(string!!)
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
        }
    }


    companion object {
        fun newInstance(): HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

}