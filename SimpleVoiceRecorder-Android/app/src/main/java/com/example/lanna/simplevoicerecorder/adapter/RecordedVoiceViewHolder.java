package com.example.lanna.simplevoicerecorder.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lanna.simplevoicerecorder.R;
import com.example.lanna.simplevoicerecorder.model.AudioModel;

/**
 * Created by Lanna on 11/11/14.
 */
public class RecordedVoiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public interface RecordedVoiceItemClickListener {
        public void onPlayPauseClick(RecordedVoiceViewHolder vh, int position, AudioModel item);

    }

    private ImageView imgPlayPause;
    private TextView tvName;
    private TextView tvCurrentProgress;
    private ProgressBar progressBar;

    private RecordedVoiceItemClickListener mItemClickListener;
    private AudioModel mModel;


    public RecordedVoiceViewHolder(View itemView, RecordedVoiceItemClickListener itemClickListener) {
        super(itemView);
        mItemClickListener = itemClickListener;
        initView(itemView);
    }

    private void initView(View itemView) {
        imgPlayPause = (ImageView) itemView.findViewById(R.id.item_record_voice_ic_play_pause);
        tvName = (TextView) itemView.findViewById(R.id.item_record_voice_tv_name);
        tvCurrentProgress = (TextView) itemView.findViewById(R.id.item_record_voice_tv_progress);
        progressBar = (ProgressBar) itemView.findViewById(R.id.item_record_voice_progressBar);

        imgPlayPause.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.item_record_voice_ic_play_pause:
                if (mItemClickListener != null) {
                    mItemClickListener.onPlayPauseClick(this, getPosition(), mModel);
                }
                break;
        }
    }

    public void updateData(AudioModel model) {
//        Log.i("lanna", "updateData model=" + model);
        mModel = model;

        tvName.setText(mModel.getName());
//        setPlayState(false); // default false: show ic play
        setProgress(0);
    }

    public void updateData(Cursor audioCursorItem) {
        updateData(new AudioModel(audioCursorItem));
    }

    public void setPlayState(boolean isPlay) {
        imgPlayPause.setSelected(isPlay);
    }

    public void setProgress(long progress) {
        long duration = mModel.getDuration();
        if (progress > duration) {
            progress = duration; // progress can not larger than duration
        }
        Log.i("lanna", "updateData item:"+mModel+", progress:"+progress+", percent:"+progress*100/duration+"%");
        tvCurrentProgress.setText(String.format("%s:%s/%s:%s",
                getMinus(progress), getSecond(progress), getMinus(duration), getSecond(duration)));
        progressBar.setProgress((int) (progress*100/duration));
    }

    private String getMinus(long ms) {
        return timeString(ms/1000/60);
    }

    private String getSecond(long ms) {
        return timeString(ms/1000%60);
    }

    private String timeString(long time) {
        return (time < 10 ? "0" : "") + time;
    }

}
