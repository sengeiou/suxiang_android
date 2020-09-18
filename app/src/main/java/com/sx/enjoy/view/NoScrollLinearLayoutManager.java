package com.sx.enjoy.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

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
