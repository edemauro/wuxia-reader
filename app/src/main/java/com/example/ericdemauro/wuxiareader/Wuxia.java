package com.example.ericdemauro.wuxiareader;

public class Wuxia {
    private String mTitle;
    private String mWebpage;

    public Wuxia(String title, String webpage) {
        mTitle = title;
        mWebpage = webpage;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getWebpage() {
        return mWebpage;
    }
}
