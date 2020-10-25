package com.sx.enjoy.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ViewSwitcher;
import android.widget.ViewSwitcher.ViewFactory;

import com.sx.enjoy.R;


public class TextSwitchView extends ViewSwitcher implements ViewFactory{

    private int index= -1;
    private Context context;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
         public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    index = next();
                    break;
            }
        }
    };
    private List<String> resources;
    private Timer timer; //
    public TextSwitchView(Context context) {
        super(context);
        this.context = context;
        init();
    }
    public TextSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    private void init() {
        if(timer==null){
            timer = new Timer();
        }
        resources = new ArrayList<>();

        this.setFactory(this);
        this.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.in_animation));
        this.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.out_animation));
    }
    public void setResources(List<String> res){
        this.resources.clear();
        this.resources.addAll(res);
    }
    public void setTextStillTime(long time){
        if(timer==null){
            timer = new Timer();
        }else{
            timer.scheduleAtFixedRate(new MyTask(), 1, time);
        }
    }
    private class MyTask extends TimerTask{
        @Override
        public void run() {
            mHandler.sendEmptyMessage(1);
        }
    }
    private int next(){
        int flag = index+1;
        if(flag>resources.size()-1){
            flag=flag-resources.size();
        }
        return flag;
    }

    @Override
    public View makeView() {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_notice,null);
        return view;
    }
}
