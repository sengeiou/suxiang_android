package com.sx.enjoy.modules.store

import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.bean.CityBean
import com.lljjcoder.bean.DistrictBean
import com.lljjcoder.bean.ProvinceBean
import com.lljjcoder.citywheel.CityConfig
import com.lljjcoder.style.citypickerview.CityPickerView
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import kotlinx.android.synthetic.main.activity_address_edit.*


class AddressEditActivity : BaseActivity() {

    private lateinit var mCityPicker: CityPickerView

    private var editType = 0

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL,if(editType == 0) "新增收货地址" else "编辑收货地址")

    override fun getLayoutResource() = R.layout.activity_address_edit

    override fun initView() {
        editType = intent.getIntExtra("type",0)

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
            setResult(RESULT_OK)
            finish()
        }
        ll_city.setOnClickListener {
            mCityPicker.showCityPicker()
        }
        mCityPicker.setOnCityItemClickListener(object :OnCityItemClickListener(){
            override fun onSelected(province: ProvinceBean?, city: CityBean?, district: DistrictBean?) {
                tv_city_area.text = province?.name+" "+city?.name+" "+district?.name
            }
        })
    }

}