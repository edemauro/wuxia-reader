package com.example.ericdemauro.wuxiareader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private WuxiaAdapter mWuxiaAdapter;

    private List<Wuxia> mWuxias;

    private static final String WUXIA_URL =
            "http://www.wuxiaworld.com/feed/";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        WuxiaAsyncTask task = new WuxiaAsyncTask();
        task.execute(WUXIA_URL);

        return view;
    }

    private void updateUI() {
        if(mWuxiaAdapter == null) {
            mWuxiaAdapter = new WuxiaAdapter(mWuxias);
            mRecyclerView.setAdapter(mWuxiaAdapter);
        }
    }

    private class WuxiaHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private Wuxia mWuxia;

        private TextView mTextView;

        public WuxiaHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTextView = (TextView) itemView.findViewById(R.id.text_view);
        }

        @Override
        public void onClick(View view) {

        }

        public void bindWuxia(Wuxia wuxia) {
            mWuxia = wuxia;
            mTextView.setText("Placeholder text");
        }
    }

    private class WuxiaAdapter extends RecyclerView.Adapter<WuxiaHolder> {
        private List<Wuxia> mWuxias;

        public WuxiaAdapter(List<Wuxia> wuxias) {
            mWuxias = wuxias;
        }

        @Override
        public WuxiaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_wuxia, parent, false);

            return new WuxiaHolder(view);
        }

        @Override
        public void onBindViewHolder(WuxiaHolder holder, int position) {
            Wuxia wuxia = mWuxias.get(position);
            holder.bindWuxia(wuxia);
        }

        @Override
        public int getItemCount() {
            return mWuxias.size();
        }
    }

    private class WuxiaAsyncTask extends AsyncTask<String, Void, List<Wuxia>> {

        @Override
        protected List<Wuxia> doInBackground(String... urls) {
            return mWuxias = QueryUtils.fetchWuxias(urls[0]);
        }

        @Override
        protected void onPostExecute(List<Wuxia> wuxias) {
            updateUI();
        }
    }
}
