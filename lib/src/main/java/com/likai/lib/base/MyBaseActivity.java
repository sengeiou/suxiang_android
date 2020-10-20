package com.likai.lib.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.likai.lib.R;
import com.likai.lib.listener.LifeCycleListener;
import com.likai.lib.manager.ActivityStackManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created by likai on 2018/3/21.
 * 功能描述：
 * 版本：
 */

public abstract class MyBaseActivity extends RxAppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beForSetContentView();
        if (mListener != null) {
            mListener.onCreate(savedInstanceState);
            mListener.setContext(this);
        }
        ActivityStackManager.getManager().push(this);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        SetTransparencyActionBar();
        setContentView(getLayoutResource());
    }

    public void SetTransparencyActionBar(){
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }


    protected abstract int getLayoutResource();
    protected abstract void initView();
    protected void beForSetContentView(){}

    public void initToolbar(Toolbar toolbar){
        initToolbar(toolbar,false,"","");
    }

    public void initToolbar(Toolbar toolbar, Boolean home){
        initToolbar(toolbar, home,"","");
    }

    public void initToolbar(Toolbar toolbar, Boolean home, String title){
        initToolbar(toolbar, home, title,"");
    }

    public void initToolbar(Toolbar toolbar, Boolean home, String title, String rightText){
        toolbar.setTitle("");
        TextView tv_title = toolbar.findViewById(R.id.tv_title);
        tv_title.setText(title);
        setSupportActionBar(toolbar);
        if(home){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        TextView tv_right = toolbar.findViewById(R.id.tv_right);
        if(TextUtils.isEmpty(rightText)){
            tv_right.setVisibility(View.GONE);
        }else {
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setText(rightText);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 回调函数
     */
    public LifeCycleListener mListener;

    public void setOnLifeCycleListener(LifeCycleListener listener) {
        mListener = listener;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mListener != null) {
            mListener.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListener != null) {
            mListener.onResume();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mListener != null) {
            mListener.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mListener != null) {
            mListener.onDestroy();
        }
        ActivityStackManager.getManager().remove(this);
    }

}
