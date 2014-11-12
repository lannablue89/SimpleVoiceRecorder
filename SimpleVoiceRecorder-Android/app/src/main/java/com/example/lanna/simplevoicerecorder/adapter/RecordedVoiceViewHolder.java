package com.example.lanna.simplevoicerecorder.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.lanna.simplevoicerecorder.MyDatabase;
import com.example.lanna.simplevoicerecorder.R;
import com.example.lanna.simplevoicerecorder.model.AudioModel;

/**
 * Created by Lanna on 11/11/14.
 */
public class RecordedVoiceViewHolder extends RecyclerView.ViewHolder {

    TextView tvName;

    public RecordedVoiceViewHolder(View itemView) {
        super(itemView);

        initView(itemView);
    }

    private void initView(View itemView) {
        tvName = (TextView) itemView.findViewById(R.id.item_record_voice_tv_name);
    }

    public void updateData(AudioModel item) {
        tvName.setText(item.getName());
    }

    public void updateData(Cursor audioCursorItem) {
//        tvName.setText(audioCursorItem.getString(MyDatabase.FLD_AUDIO_INDEX_NAME));
    }
}
