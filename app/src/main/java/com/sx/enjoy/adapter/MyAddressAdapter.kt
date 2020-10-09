package com.sx.enjoy.adapter

import android.widget.ToggleButton
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sx.enjoy.R
import com.sx.enjoy.bean.AddressBean

class MyAddressAdapter: BaseQuickAdapter<AddressBean, BaseViewHolder>(R.layout.item_my_address){
    override fun convert(helper: BaseViewHolder?, item: AddressBean?) {
        helper?.setText(R.id.tv_name,item?.receiverName)
        helper?.setText(R.id.tv_phone,item?.receiverPhone)
        helper?.setText(R.id.tv_address,item?.province+item?.city+item?.area+item?.receiverAddress)
        helper?.getView<ToggleButton>(R.id.tb_checked)?.isChecked = item?.isDefault == 1

        helper?.addOnClickListener(R.id.ll_address_edit)
        helper?.addOnClickListener(R.id.ll_address_delete)
        helper?.addOnClickListener(R.id.ll_set_default)
    }
}