package com.example.lanna.simplevoicerecorder.fragment;

/**
 * Created by Lanna on 11/7/14.
 */

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lanna.simplevoicerecorder.database.MyDatabase;
import com.example.lanna.simplevoicerecorder.R;
import com.example.lanna.simplevoicerecorder.adapter.RecordedVoiceListAdapter;

import java.net.URI;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecordedVoiceListFragment extends Fragment {

    RecyclerView mRecyclerView;
    private RecordedVoiceListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private MyDatabase db;

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
    }

    private void initData() {

        // set adapter
        db = new MyDatabase(getActivity());

        // test database actions
//        audiosCursor = db.getAudios();
//        audiosCursor.moveToFirst();
//        AudioModel firstModel = new AudioModel(audiosCursor);
//        firstModel.setName("updated model 1st 2");
//        db.insertOrUpdateAudio(firstModel); // test update
//        db.deleteAudio(firstModel); // test delete
//        db.insertOrUpdateAudio(new AudioModel("insert new ", System.currentTimeMillis())); // test insert

        // get audios and show on list
        mAdapter = new RecordedVoiceListAdapter(getActivity(), db);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.onDestroy();
        mAdapter = null;
        db.close();
    }

}