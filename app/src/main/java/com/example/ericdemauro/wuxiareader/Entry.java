package com.example.ericdemauro.wuxiareader;

import java.util.Date;

public class Entry {
    private String mTitle;
    private String mWebpage;
    private Date mPubDate;

    public Entry(String title, String webpage, Date pubDate) {
        mTitle = title;
        mWebpage = webpage;
        mPubDate = pubDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getWebpage() {
        return mWebpage;
    }

    public String toString() {
        return mTitle;
    }

    public Date getPubDate() {
        return mPubDate;
    }
}
