package com.sx.enjoy.modules.task

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.gyf.immersionbar.ImmersionBar
import com.likai.lib.base.BaseFragment
import com.sx.enjoy.R
import kotlinx.android.synthetic.main.fragment_task.*

class TaskFragment : BaseFragment(){

    private var fragments = arrayListOf<Fragment>()
    private var pagerTitle = arrayListOf("任务卷轴","我的任务","历史任务")

    override fun getLayoutResource() = R.layout.fragment_task


    override fun initView() {
        ImmersionBar.with(activity!!).statusBarDarkFont(true).titleBar(tb_task_title).init()

        initFragment()
    }

    private fun initFragment(){

        fragments.clear()
        fragments.add(TaskChildFragment.newInstance(0))
        fragments.add(TaskChildFragment.newInstance(1))
        fragments.add(TaskChildFragment.newInstance(2))

        vp_task.adapter = object : FragmentPagerAdapter(childFragmentManager){
            override fun getItem(position: Int): Fragment = fragments[position]
            override fun getCount(): Int = fragments.size
            override fun getPageTitle(position: Int): CharSequence? = pagerTitle[position]
        }
        tl_task.setupWithViewPager(vp_task)
    }

    private fun initEvent(){

    }


    companion object {
        fun newInstance(): TaskFragment {
            val fragment = TaskFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

}