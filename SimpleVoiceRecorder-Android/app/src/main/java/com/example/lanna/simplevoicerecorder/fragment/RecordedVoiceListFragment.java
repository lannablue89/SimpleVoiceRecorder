package com.example.lanna.simplevoicerecorder.fragment;

/**
 * Created by Lanna on 11/7/14.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lanna.simplevoicerecorder.R;
import com.example.lanna.simplevoicerecorder.adapter.RecordedVoiceListAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecordedVoiceListFragment extends Fragment {

    RecyclerView mRecyclerView;
    RecordedVoiceListAdapter mRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_recorded_voice_list, container, false);

        init(v);

        return v;
    }

    private void init(View v) {
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);

        mRecyclerViewAdapter = new RecordedVoiceListAdapter();
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }
}