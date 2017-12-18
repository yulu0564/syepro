package com.syepro.app.api.network;

public class Comments {
    private String userid;
    private String newsid;

    private String contects;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNewsid() {
        return newsid;
    }

    public void setNewsid(String newsid) {
        this.newsid = newsid;
    }

    public String getContects() {
        return contects;
    }

    public void setContects(String contects) {
        this.contects = contects;
    }
}