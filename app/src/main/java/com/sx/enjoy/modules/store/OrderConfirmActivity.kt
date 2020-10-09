package com.sx.enjoy.modules.store

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.sx.enjoy.R
import com.sx.enjoy.adapter.OrderConfirmAdapter
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.AddressBean
import com.sx.enjoy.bean.CreateOrderBean
import com.sx.enjoy.bean.NewOrderBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.modules.login.LoginActivity
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import kotlinx.android.synthetic.main.activity_order_confirm.*
import kotlinx.android.synthetic.main.footer_order_confirm.view.*
import kotlinx.android.synthetic.main.header_order_confirm.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast

class OrderConfirmActivity : BaseActivity() ,SXContract.View{

    private lateinit var present: SXPresent

    private lateinit var mAdapter: OrderConfirmAdapter
    private lateinit var headView: View
    private lateinit var footView: View

    private var addressId = ""

    private var shopList : CreateOrderBean? = null

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"确认订单")

    override fun getLayoutResource() = R.layout.activity_order_confirm

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        shopList = intent.getSerializableExtra("shopList") as CreateOrderBean

        mAdapter = OrderConfirmAdapter()
        rcy_order_confirm.layoutManager = LinearLayoutManager(this)
        rcy_order_confirm.adapter = mAdapter

        headView = View.inflate(this,R.layout.header_order_confirm,null)
        footView = View.inflate(this,R.layout.footer_order_confirm,null)

        mAdapter.setNewData(shopList?.orderList)
        mAdapter.addHeaderView(headView)
        mAdapter.addFooterView(footView)

        var total = 0.0
        if(shopList != null){
            shopList!!.orderList.forEach {
                total+=(it.price.toDouble()*it.number.toInt())
            }
        }
        tv_total_money.text = "¥${String.format("%.2f", total)}"

        present.getFirstAddress(C.USER_ID)

        initEvent()

    }

    private fun initEvent(){
        headView.ll_order_address.setOnClickListener {
            if(addressId.isEmpty()){
                startActivityForResult<AddressEditActivity>(1002,Pair("type",0))
            }else{
                startActivityForResult<MyAddressActivity>(1003)
            }
        }

        tv_order_confirm.setOnClickListener {
            if(C.USER_ID.isEmpty()){
                startActivity<LoginActivity>()
            }
            if(shopList==null){
                return@setOnClickListener
            }
            if(addressId.isEmpty()){
                toast("请添加收货地址")
                return@setOnClickListener
            }
            present.createOrder(C.USER_ID,addressId,footView.et_user_remark.text.toString(),shopList!!.orderList)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            when(requestCode){
                1002 -> present.getFirstAddress(C.USER_ID)
                1003 -> {
                    val address = data?.getSerializableExtra("address") as AddressBean
                    addressId = address.id
                    headView.tv_user_name.text = address.receiverName
                    headView.tv_user_phone.text = address.receiverPhone
                    headView.tv_user_address.text = address.province+address.city+address.area+address.receiverAddress
                    headView.tv_add_address.visibility = View.GONE
                    headView.ll_address_view.visibility = View.VISIBLE
                }
            }
        }
    }


    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETFIRSTADDRESS -> {
                    if(data != null){
                        data as AddressBean
                        addressId = data.id
                        headView.tv_user_name.text = data.receiverName
                        headView.tv_user_phone.text = data.receiverPhone
                        headView.tv_user_address.text = data.province+data.city+data.area+data.receiverAddress
                        headView.tv_add_address.visibility = View.GONE
                        headView.ll_address_view.visibility = View.VISIBLE
                    }else{
                        headView.tv_add_address.text = "添加收货地址"
                        headView.tv_add_address.visibility = View.VISIBLE
                        headView.ll_address_view.visibility = View.GONE
                    }
                }
                SXContract.CREATEORDER -> {
                    data.let {
                        data as NewOrderBean
                        setResult(RESULT_OK)
                        startActivity<OrderPayActivity>(Pair("order",data))
                        finish()
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

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {
        if(boolean){
            toast("请检查网络连接")
        }
    }

}