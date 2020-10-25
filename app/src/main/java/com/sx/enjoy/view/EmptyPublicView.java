package com.sx.enjoy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sx.enjoy.R;


public class EmptyPublicView extends LinearLayout implements View.OnClickListener{


    public EmptyPublicView(Context context) {
        this(context,null);
    }

    public EmptyPublicView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EmptyPublicView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view = View.inflate(context, R.layout.empty_total_view, this);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }



    public interface OnHomeTitleSelectListener{
        void onHomeTitleSelect(int position);
    }

    private OnHomeTitleSelectListener mOnHomeTitleSelectListener;

    public void setOnSortSelectListener(OnHomeTitleSelectListener mOnHomeTitleSelectListener) {
        this.mOnHomeTitleSelectListener = mOnHomeTitleSelectListener;
    }
}
