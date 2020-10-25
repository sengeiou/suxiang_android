package com.sx.enjoy.modules.task

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.likai.lib.base.BaseFragment
import com.sx.enjoy.R
import com.sx.enjoy.adapter.TaskListAdapter
import com.sx.enjoy.bean.TaskListBean
import com.sx.enjoy.bean.UserBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.event.TaskBuySuccessEvent
import com.sx.enjoy.modules.login.LoginActivity
import com.sx.enjoy.modules.mine.PayPasswordActivity
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.utils.EncryptionUtil
import com.sx.enjoy.view.dialog.*
import kotlinx.android.synthetic.main.empty_public_network.view.*
import kotlinx.android.synthetic.main.fragment_task_child.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.litepal.LitePal

class TaskChildFragment : BaseFragment(),SXContract.View{

    private lateinit var present: SXPresent

    private lateinit var taskDialog: TaskGetDialog
    private lateinit var payDialog: PayPasswordDialog
    private lateinit var reminderDialog: ReminderDialog
    private lateinit var noticeDialog: NoticeDialog
    private lateinit var taskErrorDialog: TaskErrorDialog

    private lateinit var emptyView : View
    private lateinit var errorView : View

    private lateinit var mAdapter: TaskListAdapter

    private var type = 0
    private var pager = 1
    private var payTaskId = ""
    private var activityCount = ""

    override fun getLayoutResource() = R.layout.fragment_task_child

    override fun beForInitView() {
        present = SXPresent(this)
    }

    override fun initView() {
        type = arguments!!.getInt("type",0)

        taskDialog = TaskGetDialog(activity!!)
        payDialog = PayPasswordDialog(activity!!)
        reminderDialog = ReminderDialog(activity!!)
        noticeDialog = NoticeDialog(activity!!)
        taskErrorDialog = TaskErrorDialog(activity!!)

        mAdapter = TaskListAdapter(type)
        rcy_task_child.layoutManager = LinearLayoutManager(activity)
        rcy_task_child.adapter = mAdapter

        emptyView = View.inflate(activity,R.layout.empty_public_view,null)
        errorView = View.inflate(activity,R.layout.empty_public_network,null)
        mAdapter.isUseEmpty(false)

        initData()
        initEvent()

    }

    override fun initData(){
        getMyTaskList(true)
    }

    private fun getMyTaskList(isRefreshList: Boolean){
        if(isRefreshList){
            pager = 1
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            mOnRiceRefreshListener?.onRiceFresh()
        }else{
            pager++
            swipe_refresh_layout.finishRefresh()
        }
        when(type){
            0 -> present.getTaskList()
            1 -> {
                if(C.USER_ID.isEmpty()){
                    mAdapter.data.clear()
                    mAdapter.notifyDataSetChanged()
                    swipe_refresh_layout.finishRefresh()
                    return
                }
                present.getMyTaskList(C.USER_ID,"0",pager.toString(),C.PUBLIC_PAGER_NUMBER)
            }
            2 -> {
                if(C.USER_ID.isEmpty()){
                    mAdapter.data.clear()
                    mAdapter.notifyDataSetChanged()
                    swipe_refresh_layout.finishRefresh()
                    return
                }
                present.getMyTaskList(C.USER_ID,"1",pager.toString(),C.PUBLIC_PAGER_NUMBER)
            }
        }
    }

    private fun initEvent(){
        swipe_refresh_layout.setOnRefreshListener {
            mOnRiceRefreshListener?.onBuyTaskSuccess()
        }
        if(type!=0){
            mAdapter.setOnLoadMoreListener {
                getMyTaskList(false)
            }
        }
        errorView.iv_network_error.setOnClickListener {
            mOnRiceRefreshListener?.onBuyTaskSuccess()
        }
        mAdapter.setOnItemChildClickListener { _, view, position ->
            when(view.id){
                R.id.iv_task_get -> {
                    if(C.USER_ID.isEmpty()){
                        activity?.startActivity<LoginActivity>()
                        return@setOnItemChildClickListener
                    }
                    payTaskId = mAdapter.data[position].id
                    activityCount = mAdapter.data[position].activeValue
                    taskDialog.showTaskGet(mAdapter.data[position].taskRich)
                }
            }
        }
        taskDialog.setOnNoticeConfirmListener(object :TaskGetDialog.OnNoticeConfirmListener{
            override fun onConfirm() {
                val user = LitePal.findLast(UserBean::class.java)
                if(user.isPayPwd == 1){
                    payDialog.showInputPassword()
                }else{
                    reminderDialog.showReminder(2)
                }
            }
        })
        reminderDialog.setOnNoticeConfirmListener(object :ReminderDialog.OnNoticeConfirmListener{
            override fun onConfirm() {
                activity?.startActivity<PayPasswordActivity>()
            }
        })
        payDialog.setOnNoticeConfirmListener(object :PayPasswordDialog.OnNoticeConfirmListener{
            override fun onConfirm(password: String) {
                present.buyTask(C.USER_ID,payTaskId, EncryptionUtil.MD5(password))
            }
        })
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETTASKLIST,SXContract.GETMYTASKLIST -> {
                    data?.let {
                        data as List<TaskListBean>
                        if(pager<=1){
                            mAdapter.isUseEmpty(true)
                            mAdapter.emptyView = emptyView
                            swipe_refresh_layout.finishRefresh()
                            mOnRiceRefreshListener?.onRefreshSuccess()
                            if(type>0){
                                mAdapter.setEnableLoadMore(true)
                            }
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
                SXContract.BUYTASK -> {
                    noticeDialog.showNotice(1,"兑换成功!,获取${activityCount}活跃值")
                    mOnRiceRefreshListener?.onBuyTaskSuccess()
                    EventBus.getDefault().post(TaskBuySuccessEvent(1))
                }
                else -> {

                }
            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {
        if(isRefreshList){
            if(pager<=1){
                mAdapter.isUseEmpty(true)
                mAdapter.emptyView = emptyView
                swipe_refresh_layout.finishRefresh()
                if(type>0){
                    mAdapter.setEnableLoadMore(true)
                }
            }else{
                mAdapter.loadMoreFail()
            }
        }else{
            taskErrorDialog.showNotice(string!!)
        }
    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {
        if(isRefreshList){
            if(pager<=1){
                mAdapter.isUseEmpty(true)
                mAdapter.emptyView = errorView
                swipe_refresh_layout.finishRefresh()
                if(type>0){
                    mAdapter.setEnableLoadMore(true)
                }
            }else{
                mAdapter.loadMoreFail()
            }
        }else{
            activity?.toast("请检查网络连接")?.setGravity(Gravity.CENTER, 0, 0)
        }
    }

    interface OnRiceRefreshListener{
        fun onRiceFresh()
        fun onBuyTaskSuccess()
        fun onRefreshSuccess()
    }

    private var mOnRiceRefreshListener:OnRiceRefreshListener? = null

    fun setOnRiceRefreshListener(mOnRiceRefreshListener:OnRiceRefreshListener){
        this.mOnRiceRefreshListener = mOnRiceRefreshListener
    }

    companion object {
        fun newInstance(type:Int): TaskChildFragment {
            val fragment = TaskChildFragment()
            val bundle = Bundle()
            bundle.putInt("type",type)
            fragment.arguments = bundle
            return fragment
        }
    }

}