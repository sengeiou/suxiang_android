package com.sx.enjoy.view.popupwindow

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.sx.enjoy.R


class LeftMenuPopupWindow : PopupWindow {

    private val mContext: Context
    private val tvManage1 : TextView
    private val tvManage2 : TextView
    private val tvManage3 : TextView
    private var ivChecked1 : ImageView
    private var ivChecked2 : ImageView
    private var ivChecked3 : ImageView
    private val vShade : View
    private val llContent : LinearLayout

    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        mContext = context
        val contentView = View.inflate(context, R.layout.pop_commodity_right_menu,null)
        setContentView(contentView)
        tvManage1 = contentView.findViewById(R.id.tv_menu_1)
        tvManage2 = contentView.findViewById(R.id.tv_menu_2)
        tvManage3 = contentView.findViewById(R.id.tv_menu_3)
        ivChecked1 = contentView.findViewById(R.id.iv_checked_1)
        ivChecked2 = contentView.findViewById(R.id.iv_checked_2)
        ivChecked3 = contentView.findViewById(R.id.iv_checked_3)
        vShade = contentView.findViewById(R.id.v_shade)
        llContent = contentView.findViewById(R.id.ll_content)
        initWindow()
        initEvent()
    }

    private fun initWindow() {
        val d: DisplayMetrics = mContext.resources.displayMetrics
        this.width = d.widthPixels
        this.height = ViewGroup.LayoutParams.WRAP_CONTENT
        this.isFocusable = true
        this.isOutsideTouchable = true
        this.update()
        setOnDismissListener {
            mOnRightMenuSelectListener?.onPopupStatus(false)
        }
    }

    private fun initEvent(){
        tvManage1.setOnClickListener {
            mOnRightMenuSelectListener?.onTypeSelectPosition(0)
            ivChecked1.visibility = View.VISIBLE
            ivChecked2.visibility = View.GONE
            ivChecked3.visibility = View.GONE
            dismiss()
        }
        tvManage2.setOnClickListener {
            mOnRightMenuSelectListener?.onTypeSelectPosition(1)
            ivChecked1.visibility = View.GONE
            ivChecked2.visibility = View.VISIBLE
            ivChecked3.visibility = View.GONE
            dismiss()
        }
        tvManage3.setOnClickListener {
            mOnRightMenuSelectListener?.onTypeSelectPosition(2)
            ivChecked1.visibility = View.GONE
            ivChecked2.visibility = View.GONE
            ivChecked3.visibility = View.VISIBLE
            dismiss()
        }
        vShade.setOnClickListener {
            dismiss()
        }
    }

    fun showAtBottom(view: View) {
        val d: DisplayMetrics = mContext.resources.displayMetrics
        val params = vShade.layoutParams as LinearLayout.LayoutParams
        params.height = d.heightPixels - view.bottom - llContent.height
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        vShade.layoutParams = params
        showAsDropDown(view, 0, 0)
        mOnRightMenuSelectListener?.onPopupStatus(true)
    }

    interface OnRightMenuSelectListener{
        fun onTypeSelectPosition(position:Int)
        fun onPopupStatus(isShow:Boolean)
    }

    private var mOnRightMenuSelectListener:OnRightMenuSelectListener? = null

    fun setOnRightMenuSelectListener(mOnRightMenuSelectListener:OnRightMenuSelectListener){
        this.mOnRightMenuSelectListener = mOnRightMenuSelectListener
    }

}