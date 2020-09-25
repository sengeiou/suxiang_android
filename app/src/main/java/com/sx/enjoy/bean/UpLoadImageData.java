package com.sx.enjoy.bean;

import java.util.List;

public class UpLoadImageData {
    private List<UpLoadImageList> imageList;
    private int type;
    private String info;

    public UpLoadImageData(int type,String info) {
        this.type = type;
    }

    public UpLoadImageData(List<UpLoadImageList> imageList, int type,String info) {
        this.imageList = imageList;
        this.type = type;
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<UpLoadImageList> getImageList() {
        return imageList;
    }

    public void setImageList(List<UpLoadImageList> imageList) {
        this.imageList = imageList;
    }

}
