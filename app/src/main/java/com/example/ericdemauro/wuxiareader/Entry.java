package com.example.ericdemauro.wuxiareader;

public class Entry {
    private String mTitle;
    private String mWebpage;

    public Entry(String title, String webpage) {
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
