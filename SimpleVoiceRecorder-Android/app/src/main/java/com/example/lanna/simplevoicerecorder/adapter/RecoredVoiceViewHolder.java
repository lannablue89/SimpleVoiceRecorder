package com.example.lanna.simplevoicerecorder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.lanna.simplevoicerecorder.R;

/**
 * Created by Lanna on 11/11/14.
 */
public class RecoredVoiceViewHolder extends RecyclerView.ViewHolder {

    TextView tvName;

    public RecoredVoiceViewHolder(View itemView) {
        super(itemView);

        initView(itemView);
    }

    private void initView(View itemView) {
        tvName = (TextView) itemView.findViewById(R.id.item_record_voice_tv_name);
    }

    public void setName(String name) {
        tvName.setText(name);
    }
}
