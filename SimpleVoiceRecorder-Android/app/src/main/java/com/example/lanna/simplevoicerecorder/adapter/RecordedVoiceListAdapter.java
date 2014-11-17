package com.example.lanna.simplevoicerecorder.adapter;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.CursorAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lanna.simplevoicerecorder.database.MyDatabase;
import com.example.lanna.simplevoicerecorder.helper.StoreAudioHelper;
import com.example.lanna.simplevoicerecorder.model.AudioModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.example.lanna.simplevoicerecorder.R.layout.inflater_recored_voice_item;

/**
 * Created by Lanna on 11/11/14.
 */
public class RecordedVoiceListAdapter extends CursorAdapter<RecordedVoiceViewHolder>
        implements RecordedVoiceViewHolder.RecordedVoiceItemClickListener {

    private MyDatabase mDb;
    private MediaPlayer sound;
    private String audioName;


    public RecordedVoiceListAdapter(Context context, MyDatabase db) {
        super(context,
                db.getAudios(context, Uri.parse(MyDatabase.URI_TO_NOTIFY_DATA_UPDATE)),
                FLAG_REGISTER_CONTENT_OBSERVER);
        mDb = db;
    }

    @Override
    public RecordedVoiceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(inflater_recored_voice_item, viewGroup, false);
        RecordedVoiceViewHolder vh = new RecordedVoiceViewHolder(view, this);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecordedVoiceViewHolder holder, Cursor cursor) {
        holder.updateData(mContext, cursor);
    }

    @Override
    protected void onContentChanged() {
//        Log.i("lanna", "onContentChanged");
        changeCursor(mDb.getAudios(mContext, Uri.parse(MyDatabase.URI_TO_NOTIFY_DATA_UPDATE)));
    }

    @Override
    public void onPlayPauseClick(ImageView ivPlayPause, int position, AudioModel item) {
        Log.i("lanna", "onPlayPauseClick at " + position + ":" + item);
        if (item == null || TextUtils.isEmpty(item.getFilename())) {
            return;
        }

        if (sound == null) {
            sound = new MediaPlayer();
        }

        if (audioName != null && !audioName.equals(item.getFilename())) {
            audioName = item.getFilename();
            sound.reset();
            try {
                File file = mContext.getFileStreamPath(audioName);
                Uri uri = Uri.fromFile(file);
                sound.setDataSource(mContext, uri);
                sound.prepare();
                sound.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            if (sound.isPlaying()) {
                sound.pause();
            } else {
                sound.start();
            }
        }
        ivPlayPause.setSelected(sound.isPlaying());

    }

    public void onDestroy() {
        mCursor.close();
    }

}
