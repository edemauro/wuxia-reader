package com.example.ericdemauro.wuxiareader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

public class EntryLoader extends AsyncTaskLoader<List<Entry>> {
    String mUrl;

    public EntryLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Entry> loadInBackground() {
        if(mUrl == null) {
            return null;
        }

        return QueryUtils.fetchEntries(mUrl);
    }
}
