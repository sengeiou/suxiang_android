package com.sx.enjoy.modules.store

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import com.sx.enjoy.R
import com.sx.enjoy.adapter.MyAddressAdapter
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.AddressBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import kotlinx.android.synthetic.main.activity_my_address.*
import kotlinx.android.synthetic.main.title_public_view.*
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast

class MyAddressActivity : BaseActivity() , SXContract.View {

    private lateinit var present : SXPresent

    private lateinit var mAdapter: MyAddressAdapter

    override fun getTitleType() = PublicTitleData (C.TITLE_RIGHT_TEXT,"收货地址","新增地址",0,R.color.color_1A1A1A)

    override fun getLayoutResource() = R.layout.activity_my_address

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {

        mAdapter = MyAddressAdapter()
        rcy_address_list.layoutManager = LinearLayoutManager(this)
        rcy_address_list.adapter = mAdapter

        getMyAddress()

        initEvent()
    }

    private fun initEvent(){
        ll_public_right.setOnClickListener {
            startActivityForResult<AddressEditActivity>(3001,Pair("type",0))
        }

        swipe_refresh_layout.setOnRefreshListener {
            getMyAddress()
        }

        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when(view.id){
//                R.id.ll_address_edit -> startActivityForResult<AddressUpdateActivity>(3001,
//                    Pair("address",mAdapter.data[position])
//                )
//                R.id.ll_address_delete -> present?.deleteAddress(mAdapter.data[position].id,cityId)
//                R.id.ll_set_default -> {
//                    if(mAdapter.data[position].isdefaul == "0"){
//                        present?.setAddressToDefault(cityId,mAdapter.data[position].id)
//                    }
//                }
            }
        }
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val intent = Intent()
            intent.putExtra("address",mAdapter.data[position])
            setResult(RESULT_OK,intent)
            finish()
        }
    }

    private fun getMyAddress(){
        present.getMyAddressList(C.USER_ID)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RESULT_OK&&requestCode == 3001){
            getMyAddress()
        }
    }


    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETMYADDRESSLIST -> {
                    data?.let {
                        data as List<AddressBean>
                        swipe_refresh_layout.finishRefresh()
                        mAdapter.setNewData(data)
                    }
                }
                else -> {

                }
            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {
        swipe_refresh_layout.finishRefresh()
        toast(string!!)
    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {
        swipe_refresh_layout.finishRefresh()
        if(boolean){
            toast("请检查网络连接")
        }
    }

}
