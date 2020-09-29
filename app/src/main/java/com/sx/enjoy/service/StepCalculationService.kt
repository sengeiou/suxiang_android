package com.sx.enjoy.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.sx.enjoy.bean.StepRiceBean
import com.sx.enjoy.bean.UserBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.event.UserStateChangeEvent
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast
import kotlin.concurrent.thread

class StepCalculationService : Service() ,SXContract.View {

    private lateinit var present: SXPresent

    private var isStart = true

    private var mBinder: MyBinder? = null

    override fun onCreate() {
        super.onCreate()
        mBinder = MyBinder()
        present = SXPresent(this)
        isStart = true
    }

    override fun onDestroy() {
        super.onDestroy()
        isStart = false
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    inner class MyBinder : Binder() {
        fun startStepTask(){
            Thread {
                while (isStart){
                    if(C.USER_ID.isNotEmpty()){
                        uploadStepAndLocation()
                    }
                    Thread.sleep(30000)
                }
            }.start()
        }
        fun setOnUploadStepSuccessListener(listener: OnUploadStepSuccessListener){
            mOnUploadStepSuccessListener = listener
        }
        fun upload(){
            uploadStepAndLocation()
        }
    }

    private fun uploadStepAndLocation(){
        present.getRiceFromStep(C.USER_ID,"","0","0",C.USER_STEP.toString(),"0")
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETRICEFROMSTEP -> {
                    data?.let {
                        data as StepRiceBean
                        mOnUploadStepSuccessListener?.onUploadSuccess(data)
                    }
                }
                else -> {

                }
            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {}

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {}


    interface OnUploadStepSuccessListener{
        fun onUploadSuccess(data : StepRiceBean)
    }

    private var mOnUploadStepSuccessListener: OnUploadStepSuccessListener? = null

}
