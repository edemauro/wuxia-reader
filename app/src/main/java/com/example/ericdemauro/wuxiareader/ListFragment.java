package com.example.ericdemauro.wuxiareader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<Entry>> {
    private RecyclerView mRecyclerView;
    private EntryAdapter mEntryAdapter;

    private List<Entry> mEntries;

    private static final String WUXIA_URL =
            "http://www.wuxiaworld.com/feed/";

    private static final int ENTRY_LOADER_ID = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        getLoaderManager().initLoader(ENTRY_LOADER_ID, null, this);

        return view;
    }

    private void updateUI() {
        if(mEntryAdapter == null) {
            mEntryAdapter = new EntryAdapter(new ArrayList<Entry>());
            mRecyclerView.setAdapter(mEntryAdapter);
        }
    }

    @Override
    public Loader<List<Entry>> onCreateLoader(int id, Bundle args) {
        return new EntryLoader(getActivity(), WUXIA_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Entry>> loader, List<Entry> data) {
        mEntryAdapter.clear();

        if(data != null && !data.isEmpty()) {
            mEntryAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Entry>> loader) {
        mEntryAdapter.clear();
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
            Uri entryUri = Uri.parse(mEntry.getWebpage());

            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, entryUri);

            startActivity(websiteIntent);
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

        public void clear() {
            int size = this.mEntries.size();
            this.mEntries.clear();
            notifyItemRangeRemoved(0, size);
        }

        public void addAll(List<Entry> data) {
            mEntries.addAll(data);
            notifyDataSetChanged();
        }
    }
}
