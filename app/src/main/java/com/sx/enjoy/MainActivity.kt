package com.sx.enjoy

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.likai.lib.base.BaseFragment
import com.likai.lib.commonutils.SharedPreferencesUtil
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.StepRiceBean
import com.sx.enjoy.bean.UserBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.event.FirstInitUserEvent
import com.sx.enjoy.event.MarketSellSuccessEvent
import com.sx.enjoy.event.TaskBuySuccessEvent
import com.sx.enjoy.event.UserStateChangeEvent
import com.sx.enjoy.modules.home.HomeFragment
import com.sx.enjoy.modules.market.MarketFragment
import com.sx.enjoy.modules.mine.MineFragment
import com.sx.enjoy.modules.store.StoreFragment
import com.sx.enjoy.modules.task.TaskFragment
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.service.StepCalculationService
import com.sx.enjoy.utils.ShakeDetector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.home_bottom_tab_button.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast


class MainActivity :BaseActivity() ,SXContract.View , ShakeDetector.OnShakeListener ,
    StepCalculationService.OnUploadStepSuccessListener {

    private var mBinder : StepCalculationService.MyBinder ? = null

    private lateinit var present:SXPresent

    private lateinit var mShakeDetector :ShakeDetector

    private var fragments = arrayListOf<BaseFragment>()

    private var saveSecond = 0L

    override fun getTitleType() = PublicTitleData(C.TITLE_CUSTOM)

    override fun getLayoutResource() = R.layout.activity_main

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        EventBus.getDefault().register(this)

        val bindIntent = Intent(this, StepCalculationService::class.java)
        bindService(bindIntent, mConnection, Context.BIND_AUTO_CREATE)

        mShakeDetector = ShakeDetector(this)
        mShakeDetector.registerOnShakeListener(this)
        mShakeDetector.start()

        initFragment()
    }

    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mBinder = service as StepCalculationService.MyBinder
            mBinder?.startStepTask()
            mBinder?.setOnUploadStepSuccessListener(this@MainActivity)
        }
        override fun onServiceDisconnected(name: ComponentName) {}
    }


    private fun initFragment(){
        fragments.clear()
        fragments.add(HomeFragment.newInstance())
        fragments.add(TaskFragment.newInstance())
        fragments.add(MarketFragment.newInstance())
        fragments.add(StoreFragment.newInstance())
        fragments.add(MineFragment.newInstance())

        vp_home.adapter = object : FragmentPagerAdapter(supportFragmentManager){
            override fun getItem(position: Int): Fragment = fragments[position]
            override fun getCount(): Int = fragments.size
        }

        vp_home.offscreenPageLimit = 4
        ll_home.setOnClickListener {
            select(0)
        }
        ll_task.setOnClickListener {
            select(1)
        }
        ll_market.setOnClickListener {
            select(2)
        }
        ll_shop.setOnClickListener {
            select(3)
        }
        ll_mine.setOnClickListener {
            select(4)
        }
        select(0)
    }

    private fun select(position:Int) {
        when (position) {
            0 -> {
                img_home.isSelected = true
                img_task.isSelected = false
                img_market.isSelected = false
                img_shop.isSelected = false
                img_mine.isSelected = false
                tv_home.isSelected = true
                tv_task.isSelected = false
                tv_market.isSelected = false
                tv_shop.isSelected = false
                tv_mine.isSelected = false
                vp_home.setCurrentItem(0, false)
            }
            1 -> {
                img_home.isSelected = false
                img_task.isSelected = true
                img_market.isSelected = false
                img_shop.isSelected = false
                img_mine.isSelected = false
                tv_home.isSelected = false
                tv_task.isSelected = true
                tv_market.isSelected = false
                tv_shop.isSelected = false
                tv_mine.isSelected = false
                vp_home.setCurrentItem(1, false)
            }
            2 -> {
                img_home.isSelected = false
                img_task.isSelected = false
                img_market.isSelected = true
                img_shop.isSelected = false
                img_mine.isSelected = false
                tv_home.isSelected = false
                tv_task.isSelected = false
                tv_market.isSelected = true
                tv_shop.isSelected = false
                tv_mine.isSelected = false
                vp_home.setCurrentItem(2, false)
            }
            3 -> {
                img_home.isSelected = false
                img_task.isSelected = false
                img_market.isSelected = false
                img_shop.isSelected = true
                img_mine.isSelected = false
                tv_home.isSelected = false
                tv_task.isSelected = false
                tv_market.isSelected = false
                tv_shop.isSelected = true
                tv_mine.isSelected = false
                vp_home.setCurrentItem(3, false)
            }
            4 -> {
                img_home.isSelected = false
                img_task.isSelected = false
                img_market.isSelected = false
                img_shop.isSelected = false
                img_mine.isSelected = true
                tv_home.isSelected = false
                tv_task.isSelected = false
                tv_market.isSelected = false
                tv_shop.isSelected = false
                tv_mine.isSelected = true
                vp_home.setCurrentItem(4, false)
            }
        }
    }

    override fun onShake() {
        if(C.USER_ID.isNotEmpty()){
            C.USER_STEP++
            (fragments[0] as HomeFragment).initStep()
            if(System.currentTimeMillis() - saveSecond>1000){
                saveSecond = System.currentTimeMillis()
                SharedPreferencesUtil.putCommonInt(this,"step",C.USER_STEP)
            }
        }
    }

    override fun onUploadSuccess(data: StepRiceBean) {
        if(C.USER_STEP == 0){
            C.USER_STEP = data.rotateMinStep
            SharedPreferencesUtil.putCommonInt(this,"step",C.USER_STEP)
            (fragments[0] as HomeFragment).initStep()
        }
        (fragments[0] as HomeFragment).initDayRice(data)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun userStateChange(event: UserStateChangeEvent){
        (fragments[0] as HomeFragment).initStep()
        (fragments[0] as HomeFragment).initDayRice(null)
        (fragments[4] as MineFragment).backToHead()
        (fragments[1] as TaskFragment).onBuyTaskSuccess()
        (fragments[1] as TaskFragment).initTaskTitle(0)
        if(C.USER_ID.isEmpty()){
            changeUserInfo()
        }else{
            present.getUserInfo(C.USER_ID)
            mBinder?.upload()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun taskRiceChange(event: TaskBuySuccessEvent){
        if(C.USER_ID.isEmpty()){
            changeUserInfo()
        }else{
            present.getUserInfo(C.USER_ID)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun marketRiceChange(event: MarketSellSuccessEvent){
        (fragments[2] as MarketFragment).getMarketList(true)
        if(event.state == 1){
            if(C.USER_ID.isEmpty()){
                changeUserInfo()
            }else{
                present.getUserInfo(C.USER_ID)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun initUser(event: FirstInitUserEvent){
        if(C.USER_ID.isEmpty()){
            changeUserInfo()
        }else{
            present.getUserInfo(C.USER_ID)
        }
    }

    private fun changeUserInfo(){
        (fragments[0] as HomeFragment).initUser()
        (fragments[1] as TaskFragment).initUser()
        (fragments[4] as MineFragment).initUser()
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETUSERINFO -> {
                    data?.let {
                        data as UserBean
                        data.userId = data.id.toString()
                        data.updateAll("userId = ?", data.userId)
                        changeUserInfo()
                    }
                }
                else -> {

                }
            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {
        toast(string!!)
    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {}

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        unbindService(mConnection)
        mShakeDetector.stop()
    }

}