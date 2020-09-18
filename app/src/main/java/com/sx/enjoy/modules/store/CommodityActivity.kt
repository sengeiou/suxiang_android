package com.sx.enjoy.modules.store

import android.graphics.Color
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.TabLayout
import android.support.v4.widget.NestedScrollView
import android.view.View
import android.widget.TextView
import com.gyf.immersionbar.ImmersionBar
import com.sx.enjoy.R
import com.sx.enjoy.adapter.CommodityListAdapter
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import com.sx.enjoy.utils.GlideImageLoader
import com.sx.enjoy.view.NoScrollGridManager
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_commodity.*
import org.jetbrains.anko.startActivity
import q.rorbin.badgeview.Badge
import q.rorbin.badgeview.QBadgeView

class CommodityActivity : BaseActivity() {

    private var qbv1 : Badge? = null
    private var specBottomSheet: BottomSheetBehavior<View>? = null

    private lateinit var likeAdapter : CommodityListAdapter
    private lateinit var specAdapter : TagAdapter<Int>

    private val specList = arrayListOf<Int>()

    private var specSelectPosition = 0

    override fun getTitleType() = PublicTitleData (C.TITLE_CUSTOM)

    override fun getLayoutResource() = R.layout.activity_commodity

    override fun initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).titleBar(tb_commodity_title).init()

        specBottomSheet = BottomSheetBehavior.from(findViewById(R.id.bs_store_view))

        likeAdapter = CommodityListAdapter()
        rcy_like_list.layoutManager = NoScrollGridManager(this,2)
        rcy_like_list.adapter = likeAdapter

        ban_commodity_info.setImageLoader(GlideImageLoader())
        ban_commodity_info.isAutoPlay(false)

        tl_commodity_title.addTab(tl_commodity_title.newTab().setText("产品"))
        tl_commodity_title.addTab(tl_commodity_title.newTab().setText("详情"))
        tl_commodity_title.addTab(tl_commodity_title.newTab().setText("推荐"))

        qbv1 = QBadgeView(this).bindTarget(v_shop_cart).setBadgeTextSize(8f,false).setBadgeBackgroundColor(Color.RED)

        specAdapter = object : TagAdapter<Int>(specList) {
            override fun getView(parent: FlowLayout?, position: Int, t: Int): View {
                val tv = View.inflate(this@CommodityActivity, R.layout.item_spec_view, null) as TextView
                tv.text = "哈哈"
                if(t == 0){
                    tv.setTextColor(resources.getColor(R.color.title_main_color))
                    tv.setBackgroundResource(R.drawable.bg_grey_full_1)
                }else {
                    tv.setTextColor(resources.getColor(R.color.main_color))
                    tv.setBackgroundResource(R.drawable.bg_main_line_second_1)
                }
                if(t == -1){
                    tv.setTextColor(resources.getColor(R.color.color_666666))
                    tv.setBackgroundResource(R.drawable.bg_grey_full_1)
                }
                return tv
            }
        }
        tf_space_list.adapter = specAdapter

        initEvent()




        qbv1?.badgeNumber = 20

        val bannerList = arrayListOf<String>()
        bannerList.add("http://www.suxiang986.com/productImg/20200914093853308.jpg")
        bannerList.add("http://www.suxiang986.com/productImg/20200914093853308.jpg")
        bannerList.add("http://www.suxiang986.com/productImg/20200914093853308.jpg")
        ban_commodity_info.setImages(bannerList)
        ban_commodity_info.start()


        val mList = arrayListOf<String>()
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        likeAdapter.setNewData(mList)

        specList.add(0)
        specList.add(1)
        specList.add(-1)
        specList.add(0)
        specList.add(0)
        specAdapter.notifyDataChanged()

    }


    private fun initEvent(){
        rl_shop_cart.setOnClickListener {
            startActivity<ShopCartActivity>()
        }
        ll_spec_select.setOnClickListener {
            tv_spec_confirm.visibility = View.GONE
            ll_select_buy.visibility = View.VISIBLE
            specBottomSheet?.state = BottomSheetBehavior.STATE_EXPANDED
        }
        v_spec_empty.setOnClickListener {
            specBottomSheet?.state = BottomSheetBehavior.STATE_HIDDEN
        }
        iv_spec_close.setOnClickListener {
            specBottomSheet?.state = BottomSheetBehavior.STATE_HIDDEN
        }
        rl_spec_content.setOnClickListener {  }

        tv_spec_confirm.setOnClickListener {
            startActivity<OrderConfirmActivity>()
            specBottomSheet?.state = BottomSheetBehavior.STATE_HIDDEN
        }

        tv_add_cart.setOnClickListener {
            tv_spec_confirm.text = "加入购物车"
            tv_spec_confirm.visibility = View.VISIBLE
            ll_select_buy.visibility = View.GONE
            specBottomSheet?.state = BottomSheetBehavior.STATE_EXPANDED
        }
        tv_buy_now.setOnClickListener {
            tv_spec_confirm.text = "立即购买"
            tv_spec_confirm.visibility = View.VISIBLE
            ll_select_buy.visibility = View.GONE
            specBottomSheet?.state = BottomSheetBehavior.STATE_EXPANDED
        }

        tf_space_list.setOnTagClickListener { view, position, parent ->
            if(specSelectPosition != position){
                specSelectPosition = position
                for(i in specList.indices){
                    if(specList[i] != -1){
                        specList[i] = 0
                    }
                }
                if(specList[position] != -1){
                    specList[position] = 1
                }
                specAdapter.notifyDataChanged()
            }
            false
        }

        tl_commodity_title.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {}

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

}