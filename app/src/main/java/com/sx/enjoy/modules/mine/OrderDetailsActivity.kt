package com.sx.enjoy.modules.mine

import android.view.View
import com.sx.enjoy.R
import com.sx.enjoy.adapter.OrderCommodityAdapter
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.NewOrderBean
import com.sx.enjoy.bean.OrderDetailsBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.modules.store.OrderPayActivity
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.view.NoScrollLinearLayoutManager
import com.sx.enjoy.view.dialog.NoticeDialog
import com.sx.enjoy.view.dialog.ReminderDialog
import kotlinx.android.synthetic.main.activity_order_details.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class OrderDetailsActivity : BaseActivity() ,SXContract.View{

    private lateinit var present : SXPresent

    private lateinit var reminderDialog: ReminderDialog
    private lateinit var noticeDialog : NoticeDialog

    private lateinit var mAdapter: OrderCommodityAdapter

    private var order : OrderDetailsBean? = null
    private var operationType = 0

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"订单详情")

    override fun getLayoutResource() = R.layout.activity_order_details

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        reminderDialog = ReminderDialog(this)
        noticeDialog = NoticeDialog(this)

        mAdapter = OrderCommodityAdapter()
        rcy_commodity_list.layoutManager = NoScrollLinearLayoutManager(this)
        rcy_commodity_list.adapter = mAdapter

        initEvent()

        getOrderDetails()
    }

    private fun getOrderDetails(){
        present.getOrderDetails(intent.getStringExtra("orderNo"))
    }

    private fun initEvent(){
        tv_order_button_1.setOnClickListener {
            when(order?.status){
                C.ORDER_NO_PAY -> {
                    operationType = 1
                    reminderDialog.showReminder(4)
                }
                C.ORDER_NO_SEND -> {}
                C.ORDER_ORDER_CANCEL -> {
                    operationType = 2
                    reminderDialog.showReminder(5)
                }
            }
        }
        tv_order_button_2.setOnClickListener {
            when(order?.status){
                C.ORDER_NO_PAY -> startActivity<OrderPayActivity>(Pair("order",NewOrderBean(order!!.orderNo,order!!.total)))
            }
        }

        reminderDialog.setOnNoticeConfirmListener(object :ReminderDialog.OnNoticeConfirmListener{
            override fun onConfirm() {
                when(operationType){
                    1 -> present.cancelOrder(order!!.id)
                    2 -> present.deleteOrder(order!!.id)
                }
            }
        })
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETORDERDETAILS -> {
                    data?.let {
                        data as OrderDetailsBean
                        order = data
                        tv_order_status.text = data.statusStr
                        tv_order_price.text = data.total
                        tv_user_name.text = data.receiverName
                        tv_user_phone.text = data.receiverPhone
                        tv_user_address.text = "收货地址: ${data.province+data.city+data.area+data.receiverAddress}"
                        tv_order_no.text = "订单号: ${data.orderNo}"
                        tv_order_time.text = "下单时间: ${data.createTime}"
                        tv_order_total.text = "¥${data.total}"
                        tv_order_remark.text = data.remark

                        mAdapter.setNewData(data.storeInfoList)

                        ll_order_address.visibility = View.VISIBLE
                        ll_order_bottom.visibility = View.VISIBLE
                        ll_order_remark.visibility = if(data.remark.isEmpty()) View.GONE else View.VISIBLE

                        when(data.status){
                            C.ORDER_NO_PAY -> {
                                tv_order_button_1.text = "取消订单"
                                tv_order_button_2.text = "去支付"
                                ll_order_price.visibility = View.VISIBLE
                                tv_order_button_1.visibility = View.VISIBLE
                                tv_order_button_2.visibility = View.VISIBLE
                                tv_order_pay.visibility = View.VISIBLE
                                tv_order_total.visibility = View.VISIBLE
                            }
                            C.ORDER_NO_SEND -> {
                                tv_order_button_1.text = "提醒发货"
                                ll_order_price.visibility = View.VISIBLE
                                tv_order_button_1.visibility = View.VISIBLE
                                tv_order_button_2.visibility = View.GONE
                                tv_order_pay.visibility = View.INVISIBLE
                                tv_order_total.visibility = View.INVISIBLE
                            }
                            C.ORDER_ORDER_CANCEL -> {
                                tv_order_button_1.text = "删除订单"
                                ll_order_price.visibility = View.GONE
                                tv_order_button_1.visibility = View.VISIBLE
                                tv_order_button_2.visibility = View.GONE
                                tv_order_pay.visibility = View.INVISIBLE
                                tv_order_total.visibility = View.INVISIBLE
                            }
                            C.ORDER_NO_RECEIVE -> {
                                tv_order_button_2.text = "确认收货"
                                ll_order_price.visibility = View.VISIBLE
                                tv_order_button_1.visibility = View.GONE
                                tv_order_button_2.visibility = View.VISIBLE
                                tv_order_pay.visibility = View.INVISIBLE
                                tv_order_total.visibility = View.INVISIBLE
                            }
                            C.ORDER_RECEIVE_OVER -> {
                                ll_order_price.visibility = View.VISIBLE
                                ll_order_bottom.visibility = View.GONE
                            }
                        }

                    }
                }
                SXContract.CANCELORDER -> {
                    toast("订单已取消")
                    getOrderDetails()
                }
                SXContract.DELETEORDER -> {
                    toast("订单已删除")
                    finish()
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