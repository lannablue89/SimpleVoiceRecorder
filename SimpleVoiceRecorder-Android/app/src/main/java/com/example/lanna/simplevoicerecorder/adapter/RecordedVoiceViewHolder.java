package com.example.lanna.simplevoicerecorder.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lanna.simplevoicerecorder.database.MyDatabase;
import com.example.lanna.simplevoicerecorder.R;
import com.example.lanna.simplevoicerecorder.model.AudioModel;

/**
 * Created by Lanna on 11/11/14.
 */
public class RecordedVoiceViewHolder extends RecyclerView.ViewHolder {

    ImageView imgPlayPause;

    TextView tvName;
    TextView tvCurrentProgress;

    public RecordedVoiceViewHolder(View itemView) {
        super(itemView);

        initView(itemView);
    }

    private void initView(View itemView) {
        imgPlayPause = (ImageView) itemView.findViewById(R.id.item_record_voice_ic_play_pause);
        tvName = (TextView) itemView.findViewById(R.id.item_record_voice_tv_name);
        tvCurrentProgress = (TextView) itemView.findViewById(R.id.item_record_voice_tv_progress);
    }

    public void updateData(AudioModel model) {
        imgPlayPause.setSelected(false); // show ic play as default
        tvName.setText(model.getName());
        tvCurrentProgress.setText(model.getCurrentProgress()+"/"+model.getTimeLength());
    }

    public void updateData(Context context, Cursor audioCursorItem) {
        AudioModel model = new AudioModel(context, audioCursorItem);
        updateData(model);
    }
}
