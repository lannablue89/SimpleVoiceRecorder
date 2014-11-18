package com.example.lanna.simplevoicerecorder.adapter;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lanna.simplevoicerecorder.database.MyDatabase;
import com.example.lanna.simplevoicerecorder.helper.StoreAudioHelper;
import com.example.lanna.simplevoicerecorder.helper.Utilities;
import com.example.lanna.simplevoicerecorder.model.AudioModel;

import java.io.FileInputStream;
import java.io.IOException;

import static com.example.lanna.simplevoicerecorder.R.layout.inflater_recored_voice_item;

/**
 * Created by Lanna on 11/11/14.
 */
public class RecordedVoiceListAdapter extends CursorAdapter<RecordedVoiceViewHolder>
        implements RecordedVoiceViewHolder.RecordedVoiceItemClickListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private MyDatabase mDb;
    private MediaPlayer mMediaPlayer;
    private String mFilePath;
    private ImageView mIvCurrentPlayPauseIcon;

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
        mIvCurrentPlayPauseIcon = ivPlayPause;
        Utilities.makeToast(mContext, "onPlayPauseClick at " + position + ":" + item);
        if (item == null || TextUtils.isEmpty(item.getFilePath())) {
            Utilities.makeToast(mContext, "onPlayPauseClick at item not available - stop");
            mIvCurrentPlayPauseIcon.setSelected(false);
            return;
        }

        // TODO get set length of file (duration value)

        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnCompletionListener(this);
        }

        String fullFileStoragePath = StoreAudioHelper.getFileStoragePath(item.getFilePath());
        if (TextUtils.isEmpty(mFilePath) || !mFilePath.equals(fullFileStoragePath)) {
            mFilePath = fullFileStoragePath;
//            mMediaPlayer.reset();
            try {
                FileInputStream fileInputStream = new FileInputStream(mFilePath);
                mMediaPlayer.setDataSource(fileInputStream.getFD());
                fileInputStream.close();

                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            } else {
                mMediaPlayer.start();
            }
        }

        mIvCurrentPlayPauseIcon.setSelected(mMediaPlayer.isPlaying());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mIvCurrentPlayPauseIcon.setSelected(false);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Utilities.makeToast(mContext, "Media Error: " + what + ", " + extra);
        return false;
    }

    public void onDestroy() {
        mCursor.close();
    }
}
