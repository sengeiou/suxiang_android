package com.sx.enjoy.modules.store

import android.graphics.Color
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.TabLayout
import android.support.v4.widget.NestedScrollView
import android.util.Log
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.sx.enjoy.R
import com.sx.enjoy.adapter.CommodityInfoAdapter
import com.sx.enjoy.adapter.CommodityListAdapter
import com.sx.enjoy.adapter.SpecChildListAdapter
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.*
import com.sx.enjoy.constans.C
import com.sx.enjoy.modules.login.LoginActivity
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.utils.GlideImageLoader
import com.sx.enjoy.utils.ImageLoaderUtil
import com.sx.enjoy.view.NoScrollGridManager
import com.sx.enjoy.view.NoScrollLinearLayoutManager
import kotlinx.android.synthetic.main.activity_commodity.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import q.rorbin.badgeview.Badge
import q.rorbin.badgeview.QBadgeView
import java.lang.StringBuilder
import java.util.regex.Pattern

class CommodityActivity : BaseActivity() ,SXContract.View, SpecChildListAdapter.OnSpecSelectListener {

    private lateinit var present: SXPresent

    private var qbv1 : Badge? = null
    private var specBottomSheet: BottomSheetBehavior<View>? = null

    private lateinit var likeAdapter : CommodityListAdapter
    private lateinit var infoAdapter : CommodityInfoAdapter
    private lateinit var specAdapter : SpecChildListAdapter

    private var commodity : CommodityDetailsBean? = null

    private var commodityId = ""
    private var price = ""
    private var selectNum = 1
    private var selectStock = 0
    private var buyType = 1

    override fun getTitleType() = PublicTitleData (C.TITLE_CUSTOM)

    override fun getLayoutResource() = R.layout.activity_commodity

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).titleBar(tb_commodity_title).init()

        commodityId = intent.getStringExtra("commodityId")

        specBottomSheet = BottomSheetBehavior.from(findViewById(R.id.bs_store_view))

        likeAdapter = CommodityListAdapter()
        rcy_like_list.layoutManager = NoScrollGridManager(this,2)
        rcy_like_list.adapter = likeAdapter

        infoAdapter = CommodityInfoAdapter()
        rcy_commodity_info.layoutManager = NoScrollLinearLayoutManager(this)
        rcy_commodity_info.adapter = infoAdapter

        specAdapter = SpecChildListAdapter()
        rcy_spec_list.layoutManager = NoScrollLinearLayoutManager(this)
        rcy_spec_list.adapter = specAdapter
        specAdapter.setOnSpecSelectListener(this)

        ban_commodity_info.setImageLoader(GlideImageLoader())
        ban_commodity_info.isAutoPlay(false)

        tl_commodity_title.addTab(tl_commodity_title.newTab().setText("产品"))
        tl_commodity_title.addTab(tl_commodity_title.newTab().setText("详情"))
        tl_commodity_title.addTab(tl_commodity_title.newTab().setText("推荐"))

        qbv1 = QBadgeView(this).bindTarget(v_shop_cart).setBadgeBackgroundColor(Color.RED)

        initEvent()

        present.getCommodityDetails(commodityId)
        if(C.USER_ID.isNotEmpty()){
            present.getShopCartCount(C.USER_ID)
        }else{
            qbv1?.badgeNumber = 0
        }

    }


    private fun initEvent(){
        v_back.setOnClickListener {
            finish()
        }
        rl_shop_cart.setOnClickListener {
            startActivity<ShopCartActivity>()
        }
        ll_spec_select.setOnClickListener {
            if(commodity!=null){
                tv_spec_confirm.visibility = View.GONE
                ll_select_buy.visibility = View.VISIBLE
                specBottomSheet?.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        v_spec_empty.setOnClickListener {
            specBottomSheet?.state = BottomSheetBehavior.STATE_HIDDEN
        }
        iv_spec_close.setOnClickListener {
            specBottomSheet?.state = BottomSheetBehavior.STATE_HIDDEN
        }
        rl_spec_content.setOnClickListener {  }

        tv_spec_confirm.setOnClickListener {
            buyCommodity()
        }

        tv_add_cart.setOnClickListener {
            if(commodity!=null){
                tv_spec_confirm.text = "加入购物车"
                tv_spec_confirm.visibility = View.VISIBLE
                ll_select_buy.visibility = View.GONE
                specBottomSheet?.state = BottomSheetBehavior.STATE_EXPANDED
                buyType = 1
            }
        }
        tv_buy_now.setOnClickListener {
            if(commodity!=null){
                tv_spec_confirm.text = "立即购买"
                tv_spec_confirm.visibility = View.VISIBLE
                ll_select_buy.visibility = View.GONE
                specBottomSheet?.state = BottomSheetBehavior.STATE_EXPANDED
                buyType = 2
            }
        }
        tv_add_shop.setOnClickListener {
            buyType = 1
            buyCommodity()
        }
        tv_buy_shop.setOnClickListener {
            buyType = 2
            buyCommodity()
        }

        ll_spec_sub.setOnClickListener {
            if(selectNum>1){
                selectNum --
                tv_spec_count.text = selectNum.toString()
            }
        }

        ll_spec_add.setOnClickListener {
            if(selectNum<selectStock){
                selectNum++
                tv_spec_count.text = selectNum.toString()
            }else{
                toast("库存不足")
            }
        }

        tl_commodity_title.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {
                when(p0?.position){
                    0 -> sv_commodity_details.smoothScrollTo(0,0)
                    1 -> sv_commodity_details.smoothScrollTo(0,(rl_details_title.y-tb_commodity_title.height).toInt()+5)
                    2 -> sv_commodity_details.smoothScrollTo(0,(rl_recommend_title.y-tb_commodity_title.height).toInt()+5)
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {}

            override fun onTabSelected(p0: TabLayout.Tab?) {
                when(p0?.position){
                    0 -> sv_commodity_details.smoothScrollTo(0,0)
                    1 -> sv_commodity_details.smoothScrollTo(0,(rl_details_title.y-tb_commodity_title.height).toInt()+5)
                    2 -> sv_commodity_details.smoothScrollTo(0,(rl_recommend_title.y-tb_commodity_title.height).toInt()+5)
                }
            }
        })
        sv_commodity_details.setOnScrollChangeListener { nestedScrollView: NestedScrollView?, i: Int, i2: Int, i3: Int, i4: Int ->
            if(i2<=0){
                rl_title_1.alpha = 1f
                rl_title_2.alpha = 0f
                tb_commodity_title.alpha = 1f
                tb_commodity_title.setBackgroundColor(Color.TRANSPARENT)
                tl_commodity_title.setScrollPosition(0,0f,true)
                return@setOnScrollChangeListener
            }
            val line1 = ll_sale_price.y - tb_commodity_title.height
            if(i2 in 0 until (line1/2).toInt()){
                rl_title_1.alpha = ((line1/2)-i2)/(line1/2)
                rl_title_2.alpha = 0f
                tb_commodity_title.alpha = 1f
                tb_commodity_title.setBackgroundColor(Color.TRANSPARENT)
                tl_commodity_title.setScrollPosition(0,0f,true)
                return@setOnScrollChangeListener
            }
            if(i2 in (line1/2).toInt() until line1.toInt()){
                rl_title_1.alpha = 0f
                rl_title_2.alpha = ((line1/2) - (line1-i2))/(line1/2)
                tb_commodity_title.alpha = ((line1/2) - (line1-i2))/(line1/2)
                tb_commodity_title.setBackgroundColor(Color.WHITE)
                tl_commodity_title.setScrollPosition(0,0f,true)
                return@setOnScrollChangeListener
            }
            val line2 = rl_details_title.y - tb_commodity_title.height
            if(i2 in line1.toInt() until line2.toInt()){
                rl_title_1.alpha = 0f
                rl_title_2.alpha = 1f
                tb_commodity_title.alpha = 1f
                tb_commodity_title.setBackgroundColor(Color.WHITE)
                tl_commodity_title.setScrollPosition(0,0f,true)
                return@setOnScrollChangeListener
            }
            val line3 = rl_recommend_title.y - tb_commodity_title.height
            if(i2 in line2.toInt() until line3.toInt()){
                rl_title_1.alpha = 0f
                rl_title_2.alpha = 1f
                tb_commodity_title.alpha = 1f
                tb_commodity_title.setBackgroundColor(Color.WHITE)
                tl_commodity_title.setScrollPosition(1,0f,true)
                return@setOnScrollChangeListener
            }
            if(i2>line3){
                rl_title_1.alpha = 0f
                rl_title_2.alpha = 1f
                tb_commodity_title.alpha = 1f
                tb_commodity_title.setBackgroundColor(Color.WHITE)
                tl_commodity_title.setScrollPosition(2,0f,true)
                return@setOnScrollChangeListener
            }
        }
    }

    override fun onSpecSelect() {
        selectNum = 1
        tv_spec_count.text = selectNum.toString()
        var isSelectImage = false
        var isSelectSpec = false
        val specName = StringBuilder("已选:")
        val specIds = StringBuilder()

        commodity!!.specificationVoList.forEach {
            if(it.isImage){
                var isChildSelect = false
                for(i in it.specificationVoList.indices){
                    if(!it.specificationVoList[i].isStock){
                        continue
                    }
                    if(it.specificationVoList[i].isSelected){
                        ImageLoaderUtil().displayImage(this,it.specificationVoList[i].image,iv_spec_head)
                        isSelectImage = true
                        isSelectSpec = true
                        isChildSelect = true
                        specName.append(it.specificationVoList[i].paramName+"/")
                        specIds.append(it.specificationVoList[i].id+";")
                        break
                    }
                }
                if(!isChildSelect){
                    specIds.append("[a-z0-9]{24};")
                }
            }else{
                var isChildSelect = false
                for(i in it.specificationVoList.indices){
                    if(!it.specificationVoList[i].isStock){
                        continue
                    }
                    if(it.specificationVoList[i].isSelected){
                        isSelectSpec = true
                        isChildSelect = true
                        specName.append(it.specificationVoList[i].paramName+"/")
                        specIds.append(it.specificationVoList[i].id+";")
                        break
                    }
                }
                if(!isChildSelect){
                    specIds.append("[a-z0-9]{24};")
                }
            }
        }
        if(!isSelectImage){
            ImageLoaderUtil().displayImage(this,commodity?.firstImage,iv_spec_head)
        }

        var combinationList = arrayListOf<ConstitueList>()
        if(isSelectSpec){
            tv_spec_name.text = specName.substring(0,specName.length-1)
            val ids = specIds.substring(0,specIds.length-1)
            for(i in commodity!!.sxConstituteList.indices){
                val p = Pattern.compile(ids)
                val m = p.matcher(commodity!!.sxConstituteList[i].specId)
                if(m.matches()){
                    combinationList.add(commodity!!.sxConstituteList[i])
                }
            }
        }else{
            tv_spec_name.text =  "产品规格"
        }
        if(combinationList.isNotEmpty()){
            var stock = 0
            var minPrice = combinationList[0].price.toDouble()
            var maxPrice = combinationList[0].price.toDouble()
            combinationList.forEach {
                stock += it.repertory.toInt()
                if(it.price.toDouble()<minPrice){
                    minPrice = it.price.toDouble()
                }
                if(it.price.toDouble()>maxPrice){
                    maxPrice = it.price.toDouble()
                }
            }
            if(minPrice == maxPrice){
                tv_spec_price.text = String.format("%.2f", maxPrice)
            }else{
                tv_spec_price.text = "${String.format("%.2f", minPrice)} - ${String.format("%.2f", maxPrice)}"
            }
            tv_spec_stock.text = "库存 $stock"
            selectStock = stock
            price = tv_spec_price.text.toString()
        }else{
            tv_spec_price.text = commodity?.amount
            tv_spec_stock.text = "库存 ${commodity?.repertory}"
            selectStock = commodity!!.repertory.toInt()
            price = commodity!!.amount
        }
    }

    private fun buyCommodity(){
        if(C.USER_ID.isEmpty()){
            startActivity<LoginActivity>()
            return
        }
        var isSelectAll = true
        var selectSpec = StringBuilder()
        var specName = StringBuilder()
        var image = commodity!!.firstImage
        commodity!!.specificationVoList.forEach {
            var isChildSelect = false
            for(i in it.specificationVoList.indices){
                if(it.specificationVoList[i].isSelected){
                    isChildSelect = true
                    selectSpec.append(it.specificationVoList[i].id+";")
                    specName.append(it.specificationVoList[i].paramName+"/")
                    break
                }
            }
            if(!isChildSelect){
                isSelectAll = false
                return@forEach
            }
            if(it.isImage&&isChildSelect){
                image = it.image
            }
        }
        if(!isSelectAll){
            toast("请选择产品属性")
            return
        }
        if(selectNum>selectStock){
            toast("商品库存不足")
            return
        }
        val cIds = if(selectSpec.isEmpty()) "" else selectSpec.substring(0,selectSpec.length-1)
        val sName = if(specName.isEmpty()) "" else specName.substring(0,specName.length-1)
        if(buyType == 1){
            present.addShopCart(C.USER_ID,commodityId,cIds,selectNum.toString())
        }else{
            val ol = arrayListOf<OrderSendBean>()
            val os =  OrderSendBean()
            os.conId = cIds
            os.goodsId = commodityId
            os.name = commodity!!.goodsName
            os.price = if(price.isEmpty()) commodity!!.amount else price
            os.number = selectNum.toString()
            os.image = image
            os.specName = sName
            ol.add(os)
            val order = CreateOrderBean(ol)
            startActivity<OrderConfirmActivity>(Pair("shopList",order))
        }
        specBottomSheet?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETCOMMODITYDETAILS -> {
                    data?.let {
                        data as CommodityDetailsBean
                        commodity = data
                        ban_commodity_info.visibility = View.VISIBLE
                        if(data.image.isNotEmpty()){
                            val bannerList = data.image.split(";")
                            ban_commodity_info.setImages(bannerList)
                            ban_commodity_info.start()
                        }

                        ll_sale_price.visibility = View.VISIBLE
                        tv_commodity_amount.text = data.amount

                        tv_commodity_name.visibility = View.VISIBLE
                        tv_commodity_name.text = data.goodsName

                        ll_commodity_translate.visibility = View.VISIBLE
                        tv_create_address.text = "产地 ${data.pointOrigin}"
                        tv_sale_count.text = "销量 ${data.sale}"

                        rl_details_title.visibility = View.VISIBLE
                        if(data.imageDetail.isNotEmpty()){
                            val infoList = data.imageDetail.split(";")
                            val imageList = arrayListOf<String>()
                            infoList.forEach {
                                imageList.add(it.split("&")[0])
                            }
                            infoAdapter.setNewData(imageList)
                        }

                        ImageLoaderUtil().displayImage(this,data.firstImage,iv_spec_head)
                        tv_spec_price.text = data.amount
                        tv_spec_stock.text = "库存 ${data.repertory}"

                        selectStock = data.repertory.toInt()

                        ll_spec_select.visibility = View.VISIBLE
                        specAdapter.setNewData(commodity?.specificationVoList)

                        present.getCommodityLikeList(data.goodsCateId,data.id)
                    }
                }
                SXContract.GETCOMMODITYLIKELIST -> {
                    data.let {
                        data as List<CommodityListBean>
                        rl_recommend_title.visibility = View.VISIBLE
                        likeAdapter.setNewData(data)
                    }
                }
                SXContract.GETSHOPCARTCOUNT -> {
                    data.let {
                        data as String
                        qbv1?.badgeNumber = data.toInt()
                    }
                }
                SXContract.ADDSHOPCART -> {
                    toast("添加成功，请在购物车中查看")
                    present.getShopCartCount(C.USER_ID)
                }
                else -> {

                }
            }
        }
    }

    override fun onFailed(string: String?, isRefreshList: Boolean) {
        toast(string!!)
    }

    override fun onNetError(boolean: Boolean, isRefreshList: Boolean) {
        toast("请检查网络连接")
    }
}