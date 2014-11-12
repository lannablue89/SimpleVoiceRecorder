package com.example.lanna.simplevoicerecorder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lanna.simplevoicerecorder.R;
import com.example.lanna.simplevoicerecorder.model.AudioModel;

import java.util.List;

import static com.example.lanna.simplevoicerecorder.R.layout.inflater_recored_voice_item;

/**
 * Created by Lanna on 11/11/14.
 */
public class RecordedVoiceListAdapter extends RecyclerView.Adapter<RecoredVoiceViewHolder> {

    private List<AudioModel> mModels;

    @Override
    public RecoredVoiceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(inflater_recored_voice_item, viewGroup, false);
        RecoredVoiceViewHolder vh = new RecoredVoiceViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecoredVoiceViewHolder recoredVoiceViewHolder, int i) {
        recoredVoiceViewHolder.setData(getItem(i));
    }

    @Override
    public int getItemCount() {
        return (mModels == null) ? 0 : mModels.size();
    }

    public AudioModel getItem(int position) {
        return (mModels == null || position < 0 || position >= mModels.size())
                ? null
                : mModels.get(position);
    }

    public List<AudioModel> getModels() {
        return mModels;
    }

    public void setModels(List<AudioModel> models) {
        mModels = models;
    }

    public void blindData(List<AudioModel> models) {
        setModels(models);
        notifyDataSetChanged();
    }
}
