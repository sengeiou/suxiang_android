package com.likai.lib.commonutils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.likai.lib.R;

public class LoadingDialog extends Dialog {

    private TextView tv_loading;
    private ImageView iv_loading;


    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading_view);
        setCanceledOnTouchOutside(false);

        tv_loading = findViewById(R.id.tv_loading_text);
        iv_loading = findViewById(R.id.iv_loading);

        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                getContext(), R.anim.loading_animation);
        iv_loading.startAnimation(hyperspaceJumpAnimation);
    }


    public void showLoading(String text){
        show();
        tv_loading.setText(text);
    }

}
