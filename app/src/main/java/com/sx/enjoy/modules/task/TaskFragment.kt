package com.sx.enjoy.modules.task

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.gyf.immersionbar.ImmersionBar
import com.likai.lib.base.BaseFragment
import com.sx.enjoy.R
import com.sx.enjoy.bean.TaskRiceBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.modules.login.LoginActivity
import com.sx.enjoy.modules.mine.WebContentActivity
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import kotlinx.android.synthetic.main.fragment_task.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class TaskFragment : BaseFragment(),SXContract.View,TaskChildFragment.OnRiceRefreshListener{

    private lateinit var present: SXPresent

    private var fragments = arrayListOf<BaseFragment>()

    override fun getLayoutResource() = R.layout.fragment_task

    override fun beForInitView() {
        present = SXPresent(this)
    }

    override fun initView() {

        initFragment()
        initEvent()
    }

    private fun initFragment(){

        fragments.clear()
        fragments.add(TaskChildFragment.newInstance(0))
        fragments.add(TaskChildFragment.newInstance(1))
        fragments.add(TaskChildFragment.newInstance(2))

        fragments.forEach {
            (it as TaskChildFragment).setOnRiceRefreshListener(this)
        }

        vp_task.adapter = object : FragmentPagerAdapter(childFragmentManager){
            override fun getItem(position: Int): Fragment = fragments[position]
            override fun getCount(): Int = fragments.size
        }
        vp_task.offscreenPageLimit = 2

        initTaskTitle(0)
    }

    fun initUser(){
        if(C.USER_ID.isEmpty()){
            tv_rice_total.text = "总米粒数 0"
            tv_walk_total.text = "总步数 0"
            tv_car_total.text = "总车行公里数 0"
            tv_activity_total.text = "总活跃度 0"
        }else{
            present.getTaskRiceGrains(C.USER_ID)
        }
    }

    private fun initEvent(){
        rl_task_all.setOnClickListener {
            initTaskTitle(0)
        }
        tv_task_content.setOnClickListener {
            activity?.startActivity<WebContentActivity>(Pair("type",3), Pair("title","卷轴说明"))
        }
        rl_task_mine.setOnClickListener {
            if(C.USER_ID.isEmpty()){
                activity?.startActivity<LoginActivity>()
                return@setOnClickListener
            }
            initTaskTitle(1)
        }
        rl_task_history.setOnClickListener {
            if(C.USER_ID.isEmpty()){
                activity?.startActivity<LoginActivity>()
                return@setOnClickListener
            }
            initTaskTitle(2)
        }
    }

    fun initTaskTitle(position:Int){
        vp_task.setCurrentItem(position,false)
        when(position){
            0 -> {
                tv_task_all.setTextColor(resources.getColor(R.color.title_main_color))
                tv_task_mine.setTextColor(resources.getColor(R.color.color_666666))
                tv_task_history.setTextColor(resources.getColor(R.color.color_666666))
                v_task_all.visibility = View.VISIBLE
                v_task_mine.visibility = View.GONE
                v_task_history.visibility = View.GONE
            }
            1 -> {
                tv_task_all.setTextColor(resources.getColor(R.color.color_666666))
                tv_task_mine.setTextColor(resources.getColor(R.color.title_main_color))
                tv_task_history.setTextColor(resources.getColor(R.color.color_666666))
                v_task_all.visibility = View.GONE
                v_task_mine.visibility = View.VISIBLE
                v_task_history.visibility = View.GONE
            }
            2 -> {
                tv_task_all.setTextColor(resources.getColor(R.color.color_666666))
                tv_task_mine.setTextColor(resources.getColor(R.color.color_666666))
                tv_task_history.setTextColor(resources.getColor(R.color.title_main_color))
                v_task_all.visibility = View.GONE
                v_task_mine.visibility = View.GONE
                v_task_history.visibility = View.VISIBLE
            }
        }
    }

    override fun onBuyTaskSuccess() {
        fragments[1].initData()
        fragments[2].initData()
    }

    override fun onRiceFresh() {
        if(C.USER_ID.isEmpty()){
            tv_rice_total.text = "总米粒数 0"
            tv_walk_total.text = "总步数 0"
            tv_car_total.text = "总车行公里数 0"
            tv_activity_total.text = "总活跃度 0"
        }else{
            present.getTaskRiceGrains(C.USER_ID)
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETTASKRICEGRAINS -> {
                    data?.let {
                        data as TaskRiceBean
                        tv_rice_total.text = "总米粒数 ${data.riceGrains}"
                        tv_walk_total.text = "总步数 ${data.walkMinStep}"
                        tv_car_total.text = "总车行公里数 ${data.drivingMinStep}"
                        tv_activity_total.text = "总活跃度 ${data.userActivity}"
                    }
                }
                else -> {

                }
            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {
        activity?.toast(string!!)?.setGravity(Gravity.CENTER, 0, 0)
    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {
        activity?.toast("请检查网络连接")?.setGravity(Gravity.CENTER, 0, 0)
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