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
import com.sx.enjoy.utils.GlideImageLoader
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.header_home_view.view.*
import org.jetbrains.anko.startActivity


class HomeFragment : BaseFragment(){

    private lateinit var headView:View
    private lateinit var mAdapter:HomeListAdapter


    private var titleType = 0

    override fun getLayoutResource() = R.layout.fragment_home


    override fun initView() {
        ImmersionBar.with(activity!!).statusBarDarkFont(true).titleBar(tb_home_title).init()

        mAdapter = HomeListAdapter()
        rcy_home_list.layoutManager = LinearLayoutManager(activity)
        rcy_home_list.adapter = mAdapter

        headView = View.inflate(activity,R.layout.header_home_view,null)
        mAdapter.addHeaderView(headView)


        val noticeList = arrayListOf<String>()
        noticeList.add("学好Java、Android、C#、C、ios、html+css+js")
        noticeList.add("走遍天下都不怕！！！！！")
        noticeList.add("不是我吹，就怕你做不到，哈哈")
        noticeList.add("你是最棒的，奔跑吧孩子！")
        headView.tb_home_notice.setDatas(noticeList)

        val bannerList = arrayListOf<String>()
        bannerList.add("http://www.suxiang986.com/ziliao/20200814104903695.jpg")
        bannerList.add("http://www.suxiang986.com/ziliao/20200814104903695.jpg")
        bannerList.add("http://www.suxiang986.com/ziliao/20200814104903695.jpg")
        headView.ban_top_list.setImageLoader(GlideImageLoader())
        headView.ban_top_list.setImages(bannerList)
        headView.ban_top_list.start()

        headView.ban_bottom_list.setImageLoader(GlideImageLoader())
        headView.ban_bottom_list.setImages(bannerList)
        headView.ban_bottom_list.start()

        val mList = arrayListOf<String>()
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mAdapter.setNewData(mList)

        initEvent()
    }

    private fun initEvent(){
        iv_home_sign.setOnClickListener {
            activity?.startActivity<SignAnswerActivity>()
        }
        tv_note_list.setOnClickListener {
            activity?.startActivity<WalkHistoryActivity>(Pair("titleType",titleType))
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
        swipe_refresh_layout.setOnRefreshListener {
            Log.e("Test","刷新------------>")
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

    companion object {
        fun newInstance(): HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

}