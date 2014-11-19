package com.example.lanna.simplevoicerecorder.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lanna.simplevoicerecorder.database.MyDatabase;
import com.example.lanna.simplevoicerecorder.R;
import com.example.lanna.simplevoicerecorder.helper.StoreAudioHelper;
import com.example.lanna.simplevoicerecorder.model.AudioModel;

/**
 * Created by Lanna on 11/11/14.
 */
public class RecordedVoiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public interface RecordedVoiceItemClickListener {
        public void onPlayPauseClick(ImageView ivPlayPause, int position, AudioModel item);

    }

    private ImageView imgPlayPause;
    private TextView tvName;
    private TextView tvCurrentProgress;

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

        imgPlayPause.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.item_record_voice_ic_play_pause:
                if (mItemClickListener != null) {
                    mItemClickListener.onPlayPauseClick((ImageView)v, getPosition(), mModel);
                }
                break;
        }
    }

    public void updateData(AudioModel model) {
        mModel = model;

//        imgPlayPause.setSelected(false); // default false: show ic play
        tvName.setText(mModel.getName());
        tvCurrentProgress.setText(mModel.getCurrentProgress()+"/"+mModel.getTimeLength());
    }

    public void updateData(Context context, Cursor audioCursorItem) {
        updateData(new AudioModel(context, audioCursorItem));
    }
}
