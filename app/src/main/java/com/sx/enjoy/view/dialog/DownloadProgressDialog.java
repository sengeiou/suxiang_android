package com.sx.enjoy.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sx.enjoy.R;
import com.sx.enjoy.utils.DownloadUtils;
import com.sx.enjoy.view.DownLoadProgressbar;


public class DownloadProgressDialog extends Dialog {

    private DownLoadProgressbar mProgressBar;
    private TextView tvProgress;
    private TextView tvTitle;

    public DownloadProgressDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_download_view);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public void initView(){
        mProgressBar = findViewById(R.id.dpb_download);
        tvProgress = findViewById(R.id.tv_download_progress);
        tvTitle = findViewById(R.id.tv_download_title);
    }

    public void setTitle(String title){
        tvTitle.setText(title);
    }

    public void setCurrentValue(int total,int curProgress) {
        int value = (int) ((curProgress / (float) total) * 100);
        mProgressBar.setProgress(value);
        tvProgress.setText(DownloadUtils.setFileSize(curProgress) +"/"+DownloadUtils.setFileSize(total));
    }


}
