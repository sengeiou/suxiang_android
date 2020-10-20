package com.sx.enjoy.view;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

public class NoScrollLinearLayoutManager extends LinearLayoutManager {

    public NoScrollLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return false;
    }

}
