package com.example.ericdemauro.wuxiareader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

public class ListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private EntryAdapter mEntryAdapter;

    private List<Entry> mEntries;

    private static final String WUXIA_URL =
            "http://www.wuxiaworld.com/feed/";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        EntryAsyncTask task = new EntryAsyncTask();
        task.execute(WUXIA_URL);

        return view;
    }

    private void updateUI() {

        for(Entry e : mEntries) {
            Log.d("entry", e.toString());
        }

        if(mEntryAdapter == null) {
            mEntryAdapter = new EntryAdapter(mEntries);
            mRecyclerView.setAdapter(mEntryAdapter);
        }
    }

    private class EntryHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private Entry mEntry;

        private TextView mTitle;
        private TextView mPubDate;

        public EntryHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTitle = (TextView) itemView.findViewById(R.id.title);
            mPubDate = (TextView) itemView.findViewById(R.id.pub_date);
        }

        public void bindEntry(Entry entry) {
            mEntry = entry;

            Format formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

            mTitle.setText(mEntry.getTitle());
            mPubDate.setText(formatter.format(mEntry.getPubDate()));
        }

        @Override
        public void onClick(View view) {

        }
    }

    private class EntryAdapter extends RecyclerView.Adapter<EntryHolder> {
        private List<Entry> mEntries;

        public EntryAdapter(List<Entry> entries) {
            mEntries = entries;
        }

        @Override
        public EntryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_wuxia, parent, false);

            return new EntryHolder(view);
        }

        @Override
        public void onBindViewHolder(EntryHolder holder, int position) {
            Entry entry = mEntries.get(position);
            holder.bindEntry(entry);
        }

        @Override
        public int getItemCount() {
            return mEntries.size();
        }
    }

    private class EntryAsyncTask extends AsyncTask<String, Void, List<Entry>> {

        @Override
        protected List<Entry> doInBackground(String... urls) {
            return QueryUtils.fetchEntries(urls[0]);
        }

        @Override
        protected void onPostExecute(List<Entry> entries) {
            mEntries = entries;
            updateUI();
        }
    }
}
