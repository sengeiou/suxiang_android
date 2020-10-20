package com.sx.enjoy.modules.store

import android.view.Gravity
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.bean.CityBean
import com.lljjcoder.bean.DistrictBean
import com.lljjcoder.bean.ProvinceBean
import com.lljjcoder.citywheel.CityConfig
import com.lljjcoder.style.citypickerview.CityPickerView
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.AddressBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.utils.RegularUtil
import com.sx.enjoy.view.dialog.NoticeDialog
import kotlinx.android.synthetic.main.activity_address_edit.*
import org.jetbrains.anko.toast


class AddressEditActivity : BaseActivity() ,SXContract.View{

    private lateinit var present : SXPresent

    private lateinit var noticeDialog: NoticeDialog
    private lateinit var mCityPicker: CityPickerView

    private var editType = 0
    private var selectProvince = ""
    private var selectCity = ""
    private var selectDistrict = ""

    private var address: AddressBean? = null


    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL,if(editType == 0) "新增收货地址" else "编辑收货地址")

    override fun getLayoutResource() = R.layout.activity_address_edit

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        editType = intent.getIntExtra("type",0)
        if(editType == 1){
            address = intent.getSerializableExtra("address") as AddressBean
            et_user_name.setText(address?.receiverName)
            et_user_phone.setText(address?.receiverPhone)
            tv_city_area.text = address?.province+" "+address?.city+" "+address?.area
            selectProvince = address?.province.toString()
            selectCity = address?.city.toString()
            selectDistrict = address?.area.toString()
            et_address_detail.setText(address?.receiverAddress)
            rg_address_default.check(if(address?.isDefault==1) R.id.rb_default_1 else R.id.rb_default_2)
        }

        noticeDialog = NoticeDialog(this)
        mCityPicker = CityPickerView()
        mCityPicker.init(this)

        val cityConfig = CityConfig.Builder()
            .title("选择城市")
            .titleTextSize(17)
            .titleTextColor("#000000")
            .titleBackgroundColor("#FFFFFF")
            .confirmText("确定")
            .confirmTextSize(15)
            .confirTextColor("#FF4713")
            .cancelText("取消")
            .cancelTextSize(15)
            .cancelTextColor("#585858")
            .showBackground(true)
            .visibleItemsCount(7)
            .province("江苏省")
            .city("徐州市")
            .district("泉山区")
            .setLineColor("#F5F5F5")
            .setLineHeigh(1)
            .build()
        mCityPicker.setConfig(cityConfig)

        initEvent()
    }

    private fun initEvent(){
        bt_save.setOnClickListener {
            if(C.USER_ID.isEmpty()){
                return@setOnClickListener
            }
            if(et_user_name.text.isEmpty()){
                toast("请输入收件人").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            if(et_user_phone.text.isEmpty()){
                toast("请输入收件人联系电话").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            if(!RegularUtil.isChinaPhoneLegal(et_user_phone.text.toString())){
                toast("联系电话不正确").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            if(tv_city_area.text.isEmpty()){
                toast("请选择省市区").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            if(et_address_detail.text.isEmpty()){
                toast("请输入详细地址").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            present.saveAddress(C.USER_ID,if(editType == 1) address?.id.toString() else "", et_address_detail.text.toString(),et_user_name.text.toString(),et_user_phone.text.toString(),selectProvince,selectCity,selectDistrict,if(rb_default_1.isChecked) "1" else "0")
        }
        ll_city.setOnClickListener {
            mCityPicker.showCityPicker()
        }
        mCityPicker.setOnCityItemClickListener(object :OnCityItemClickListener(){
            override fun onSelected(province: ProvinceBean, city: CityBean, district: DistrictBean) {
                selectProvince = province.name
                selectCity = city.name
                selectDistrict = district.name
                tv_city_area.text = province.name+" "+city.name+" "+district.name
            }
        })
        noticeDialog.setOnDismissListener {
            setResult(RESULT_OK)
            finish()
        }
    }


    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.SAVEADDRESS -> {
                    noticeDialog.showNotice(2)
                }
                else -> {

                }
            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {
        toast(string!!).setGravity(Gravity.CENTER, 0, 0)
    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {
        if(boolean){
            toast("请检查网络连接").setGravity(Gravity.CENTER, 0, 0)
        }
    }

}