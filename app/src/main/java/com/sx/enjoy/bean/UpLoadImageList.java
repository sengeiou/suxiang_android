package com.sx.enjoy.bean;

public class UpLoadImageList{
    private String localPath;
    private String netPath;
    private int state;

    public UpLoadImageList(String localPath) {
        this.localPath = localPath;
        this.netPath = "";
        this.state = 0;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getNetPath() {
        return netPath;
    }

    public void setNetPath(String netPath) {
        this.netPath = netPath;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }



}
