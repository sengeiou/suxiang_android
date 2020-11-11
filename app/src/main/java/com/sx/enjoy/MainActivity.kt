package com.sx.enjoy

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES
import android.view.Gravity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.likai.lib.base.BaseFragment
import com.likai.lib.commonutils.AppUtils
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.*
import com.sx.enjoy.constans.C
import com.sx.enjoy.event.*
import com.sx.enjoy.modules.home.HomeFragment
import com.sx.enjoy.modules.home.SignAnswerActivity
import com.sx.enjoy.modules.market.MarketFragment
import com.sx.enjoy.modules.mine.MineFragment
import com.sx.enjoy.modules.mine.TransactionDetailsActivity
import com.sx.enjoy.modules.store.StoreFragment
import com.sx.enjoy.modules.task.TaskFragment
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.service.StepCalculationService
import com.sx.enjoy.utils.APKRefreshDownload
import com.sx.enjoy.utils.DownloadUtils
import com.sx.enjoy.view.dialog.*
import com.yanzhenjie.permission.AndPermission
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.home_bottom_tab_button.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.File

class MainActivity :BaseActivity() ,SXContract.View , APKRefreshDownload.OnDownLoadCompleteListener,
    HomeFragment.OnTaskEmptyListener {

    private lateinit var present:SXPresent

    private lateinit var signDialog: SignDialog
    private lateinit var taskEmptyDialog: TaskEmptyDialog
    private lateinit var updateDialog: ApkUpdateDialog
    private lateinit var authorityDialog : OpenAuthorityDialog
    private lateinit var transactionDialog: TransactionProcessDialog

    private var fragments = arrayListOf<BaseFragment>()

    private var isFirstInitUser = false
    private var isMustUpdate = false
    private var isUpdateComplete = false
    private var isLoadUpdate = false
    private var updateUrl = ""
    private var isLoadMarket = false

    override fun getTitleType() = PublicTitleData(C.TITLE_CUSTOM)

    override fun getLayoutResource() = R.layout.activity_main

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        EventBus.getDefault().register(this)

        val intent = Intent(this,StepCalculationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }

        signDialog = SignDialog(this)
        taskEmptyDialog = TaskEmptyDialog(this)
        updateDialog = ApkUpdateDialog(this)
        authorityDialog = OpenAuthorityDialog(this)
        transactionDialog = TransactionProcessDialog(this)

        initFragment()

        updateDialog.setOnApkDownloadConfirmListener(object :ApkUpdateDialog.OnApkDownloadConfirmListener{
            override fun onConfirmDownload(info: UpdateInfoBean) {
                checkDownloadPermission()
            }
        })
        updateDialog.setOnDismissListener {
            if(isMustUpdate&&!isUpdateComplete){
                finish()
            }else{
                present.getSignResult(C.USER_ID,false)
            }
        }

        taskEmptyDialog.setOnNoticeConfirmListener(object :TaskEmptyDialog.OnNoticeConfirmListener{
            override fun onConfirm() {
                vp_home.currentItem = 1
            }
        })

        authorityDialog.setOnApkDownloadConfirmListener(object :OpenAuthorityDialog.OnOpenAuthorityConfirmListener{
            override fun onConfirmDownload() {
                val intent = Intent(ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                startActivityForResult(intent, 10003)
            }
        })

        transactionDialog.setOnNoticeConfirmListener(object :TransactionProcessDialog.OnNoticeConfirmListener{
            override fun onConfirm(orderId: String,type:Int) {
                startActivity<TransactionDetailsActivity>(Pair("marketId",orderId),Pair("type",type))
            }
        })

        if(!isLoadUpdate){
            isLoadUpdate = true
            present.getUpdateInfo(AppUtils.getVersionName(applicationContext),"Android")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isLoadUpdate",isLoadUpdate)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if(savedInstanceState!=null){
            isLoadUpdate = savedInstanceState.getBoolean("isLoadUpdate")
        }
        super.onCreate(savedInstanceState)
    }

    private fun checkDownloadPermission(){
        AndPermission.with(this)
            .runtime()
            .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            .onGranted { permissions ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val haveInstallPermission = packageManager.canRequestPackageInstalls()
                    if(haveInstallPermission){
                        APKRefreshDownload(this@MainActivity).startDownload(updateUrl,this@MainActivity)
                    }else{
                        authorityDialog.showOpenAuthority()
                    }
                }else{
                    APKRefreshDownload(this@MainActivity).startDownload(updateUrl,this@MainActivity)
                }
            }
            .onDenied { permissions ->
                toast("发现新版本，不授予相关权限无法更新应用").setGravity(Gravity.CENTER, 0, 0)
            }
            .start()
    }

    override fun onTaskEmpty() {
        vp_home.currentItem = 1
    }

    private fun initFragment(){
        fragments.clear()
        fragments.add(HomeFragment.newInstance(this))
        fragments.add(TaskFragment.newInstance())
        fragments.add(MarketFragment.newInstance())
        fragments.add(StoreFragment.newInstance())
        fragments.add(MineFragment.newInstance())

        vp_home.adapter = object : FragmentPagerAdapter(supportFragmentManager){
            override fun getItem(position: Int): Fragment = fragments[position]
            override fun getCount(): Int = fragments.size
        }

        vp_home.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if(position==0&&C.USER_ID.isNotEmpty()){
                    present.getSignResult(C.USER_ID,false)
                }
                if(position==2&&C.USER_ID.isNotEmpty()&&!isLoadMarket){
                    isLoadMarket = true
                    present.getTransactionProcess(C.USER_ID)
                }
                if(!fragments[position].isLoadComplete){
                    fragments[position].refreshData()
                }
            }
        })

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

    override fun onComplete(isComplete: Boolean) {
        isUpdateComplete = isComplete
        if(isComplete){
            openAPK()
        }else{
            toast("新版本下载失败,请稍后重试")
            finish()
        }
    }

    private fun openAPK() {
        val file = File(DownloadUtils.getSaveFilePath())
        val intent = Intent(Intent.ACTION_VIEW)
        val data: Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data = FileProvider.getUriForFile(this, "com.sx.enjoy.fileprovider", file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        } else {
            data = Uri.fromFile(file)
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive")
        startActivity(intent)
        finish()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun shakeRiceRefresh(event: RiceRefreshEvent){
        (fragments[0] as HomeFragment).initDayRice(event)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun shakeStepRefresh(event: StepRefreshEvent){
        (fragments[0] as HomeFragment).initStep(event.step)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun userStateChange(event: UserStateChangeEvent){
        isLoadMarket = false
        (fragments[0] as HomeFragment).initStepAndDayRice()
        (fragments[4] as MineFragment).backToHead()
        (fragments[1] as TaskFragment).onBuyTaskSuccess()
        (fragments[1] as TaskFragment).initTaskTitle(0)
        if(C.USER_ID.isEmpty()){
            changeUserInfo()
        }else{
            present.getUserInfo(C.USER_ID)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun taskRiceChange(event: TaskBuySuccessEvent){
        (fragments[0] as HomeFragment).getStepAndDayRice()
        if(C.USER_ID.isEmpty()){
            changeUserInfo()
        }else{
            present.getUserInfo(C.USER_ID)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun marketRiceChange(event: MarketSellSuccessEvent){
        (fragments[2] as MarketFragment).getMarketList(true,false)
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
        isFirstInitUser = true
        if(C.USER_ID.isEmpty()){
            changeUserInfo()
        }else{
            present.getUserInfo(C.USER_ID)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun authSuccess(event: UserAuthSuccessEvent){
        (fragments[1] as TaskFragment).onBuyTaskSuccess()
        if(C.USER_ID.isEmpty()){
            changeUserInfo()
        }else{
            present.getUserInfo(C.USER_ID)
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun memberUpSuccess(event: MemberUpSuccessEvent){
        (fragments[1] as TaskFragment).onBuyTaskSuccess()
        if(C.USER_ID.isEmpty()){
            changeUserInfo()
        }else{
            present.getUserInfo(C.USER_ID)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if(intent?.extras != null){
            val position = intent.getIntExtra("position",0)
            vp_home.currentItem = position
        }
    }

    override fun onResume() {
        super.onResume()
        if(isFirstInitUser){
            if(C.USER_ID.isEmpty()){
                changeUserInfo()
            }else{
                present.getUserInfo(C.USER_ID)
            }
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
                SXContract.GETSIGNRESULT -> {
                    data?.let {
                        data as SignResultBean
                        if(!data.signJudge){
                            signDialog.show()
                            signDialog.setOnNoticeConfirmListener(object :SignDialog.OnNoticeConfirmListener{
                                override fun onConfirm() {
                                    if(data.taskJudge){
                                        startActivity<SignAnswerActivity>()
                                    }else{
                                        taskEmptyDialog.show()
                                    }

                                }
                            })
                        }
                    }
                }
                SXContract.GETUSERINFO -> {
                    data?.let {
                        data as UserBean
                        data.userId = data.id.toString()
                        data.updateAll("userId = ?", data.userId)
                        changeUserInfo()
                    }
                }
                SXContract.GETUPDATEINFO -> {
                    data?.let {
                        data as UpdateInfoBean
                        if(data.isUpdate == 1){
                            updateDialog.showUpdate(data)
                            isMustUpdate = data.isMust == 1
                            updateUrl = data.downloadUrl
                        }else{
                            if(C.USER_ID.isNotEmpty()){
                                present.getSignResult(C.USER_ID,false)
                            }
                        }
                    }
                }
                SXContract.GETTRANSACTIONPROCESS -> {
                    data?.let {
                        data as TransactionProcessBean
                        transactionDialog.showNotice(data.id,data.orderType)
                    }
                }
                else -> {

                }
            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {
        toast(string!!).setGravity(Gravity.CENTER, 0, 0)
    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {}

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}