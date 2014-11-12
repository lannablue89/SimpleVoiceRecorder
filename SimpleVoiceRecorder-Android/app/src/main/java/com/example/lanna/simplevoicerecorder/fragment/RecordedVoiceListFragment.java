package com.example.lanna.simplevoicerecorder.fragment;

/**
 * Created by Lanna on 11/7/14.
 */

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lanna.simplevoicerecorder.MyDatabase;
import com.example.lanna.simplevoicerecorder.R;
import com.example.lanna.simplevoicerecorder.adapter.RecordedVoiceListAdapter;
import com.example.lanna.simplevoicerecorder.model.AudioModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecordedVoiceListFragment extends Fragment {

    RecyclerView mRecyclerView;
    private RecordedVoiceListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Cursor audiosCursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_recorded_voice_list, container, false);

        initView(v);
        initData();

        return v;
    }

    private void initView(View v) {
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager for List
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // set adapter
        MyDatabase myDatabase = new MyDatabase(getActivity());
        audiosCursor = myDatabase.getAudios();
        mAdapter = new RecordedVoiceListAdapter(getActivity(), audiosCursor);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
//        List<AudioModel> models = new ArrayList<AudioModel>();
//        AudioModel model;
//        for (int i = 0; i < 30; i++) {
//            model = new AudioModel("model "+i, System.currentTimeMillis());
//            models.add(model);
//        }
//        mAdapter.blindData(models);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        audiosCursor.close();
    }
}