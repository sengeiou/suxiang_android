package com.sx.enjoy.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.sx.enjoy.R
import com.sx.enjoy.utils.ImageLoaderUtil
import kotlinx.android.synthetic.main.dialog_show_single_img.*
import android.view.ViewGroup
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.likai.lib.commonutils.ScreenUtils


class SingleImageShowDialog : Dialog {

    constructor(context: Context) : super(context, R.style.CustomDialog)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_show_single_img)

        val lp = iv_show_image.layoutParams
        lp.width = ScreenUtils.getScreenWidth(context)
        lp.height = ScreenUtils.getScreenHeight(context)
        iv_show_image.layoutParams = lp

        initEvent()
    }


    fun showImage(imageUrl:String?){
        show()
        ImageLoaderUtil().displayImage(context,imageUrl,iv_show_image)
    }

    private fun initEvent(){
        ll_image_content.setOnClickListener {
            dismiss()
        }
    }

}