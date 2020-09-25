package com.sx.enjoy.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.likai.lib.net.HttpResult;
import com.sx.enjoy.bean.UpLoadImageData;
import com.sx.enjoy.bean.UpLoadImageList;
import com.sx.enjoy.bean.UploadImageBean;
import com.sx.enjoy.net.SXPresent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UpLoadImageUtil {

    private SXPresent present;
    private Context mContext;
    private List<UpLoadImageData> sources;
    private UpLoadTask task;

    public UpLoadImageUtil(Context mContext, SXPresent present){
        this.sources = new ArrayList<>();
        this.mContext = mContext;
        this.present = present;
    }

    public void addImagesToSources(List<UpLoadImageData> sources){
        this.sources.clear();
        this.sources.addAll(sources);
    }

    public void start(){
        task = new UpLoadTask();
        task.execute();
    }

    public void stop(){
        if(task != null){
            task.cancel(true);
        }
    }

    private class UpLoadTask extends AsyncTask<Void,Void,Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean isSuccess = true;
            try {
                label:
                for(int i = 0;i<sources.size();i++){
                    List<UpLoadImageList> upItems = sources.get(i).getImageList();
                    for(int j = 0;j<upItems.size();j++){
                        HttpResult<UploadImageBean> target = present.uploadFile(new File(upItems.get(j).getLocalPath()));
                        if(target!=null&&target.code == 0){
                            upItems.get(j).setNetPath(target.data.getUrl());
                            upItems.get(j).setState(1);
                        }else{
                            isSuccess = false;
                            break label;
                        }
                    }
                }
            }catch (Exception e){
                isSuccess = false;
            }
            return isSuccess;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            mOnUploadImageResultListener.onResult(aBoolean,sources);
            if(!aBoolean){
                label:
                for(UpLoadImageData ss:sources){
                    for (int i = 0; i<ss.getImageList().size();i++){
                        if(ss.getImageList().get(i).getState() == 0){
                            if(ss.getImageList().size()==1){
                                Toast.makeText(mContext,ss.getInfo()+"上传失败",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(mContext,"第"+(i+1)+"张"+ss.getInfo()+"上传失败",Toast.LENGTH_SHORT).show();
                            }
                            break label;
                        }
                    }
                }
            }
        }

    }

    public interface OnUploadImageResultListener{
        void onResult(boolean result, List<UpLoadImageData> sources);
    }

    private OnUploadImageResultListener mOnUploadImageResultListener;


    public void setOnUploadImageResultListener(OnUploadImageResultListener mOnUploadImageResultListener) {
        this.mOnUploadImageResultListener = mOnUploadImageResultListener;
    }
}
