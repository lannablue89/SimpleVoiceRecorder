package com.example.lanna.simplevoicerecorder.adapter;

import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CursorAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lanna.simplevoicerecorder.database.MyDatabase;
import com.example.lanna.simplevoicerecorder.helper.StoreAudioHelper;
import com.example.lanna.simplevoicerecorder.helper.Utilities;
import com.example.lanna.simplevoicerecorder.model.AudioModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.example.lanna.simplevoicerecorder.R.layout.inflater_recored_voice_item;

/**
 * Created by Lanna on 11/11/14.
 */
public class RecordedVoiceListAdapter extends CursorAdapter<RecordedVoiceViewHolder>
        implements RecordedVoiceViewHolder.RecordedVoiceItemClickListener, Handler.Callback,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private static final int UPDATE_STATE_PLAYING = 1;

    private MyDatabase mDb;
    private MediaPlayer mMediaPlayer;
    private int mFileIndex = -1;
    private ImageView mIvCurrentPlayPauseIcon;

    private Handler mHandler;

    public RecordedVoiceListAdapter(Context context, MyDatabase db) {
        super(context,
                db.getAudios(context, Uri.parse(MyDatabase.URI_TO_NOTIFY_DATA_UPDATE)),
                FLAG_REGISTER_CONTENT_OBSERVER);
        mDb = db;
        mHandler = new Handler(this);
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
//        Utilities.makeToast(mContext, "onPlayPauseClick at " + position + ":" + item);
        if (null == item || TextUtils.isEmpty(item.getFilePath())) {
            Utilities.makeToast(mContext, "play item not available - stop");
            setIconPlayOrPause(false);
            return;
        }

        // TODO get set length of file (duration value)

        if (null == mMediaPlayer) {
//            mMediaPlayer = MediaPlayer.create(mContext, R.raw.abc);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setVolume(1, 1);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnCompletionListener(this);
        }

        // check play new audio
        if (mFileIndex < 0 || mFileIndex != position) {
            setIconPlayOrPause(false);
            mIvCurrentPlayPauseIcon = ivPlayPause;
            mFileIndex = position;
            startPlayMusic(StoreAudioHelper.getFileStoragePath(item.getFilePath())); // "Download/abcd.mp3"
        }
        // continue play/pause current audio
        else if (null != mMediaPlayer) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                setIconPlayOrPause(false);
            } else {
                mMediaPlayer.start();
                setIconPlayOrPause(true);
            }
        }
    }

    private void startPlayMusic(final String fullFileStoragePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mMediaPlayer.reset(); // to set other data source FD
                try {
                    FileInputStream fileInputStream = new FileInputStream(fullFileStoragePath); // FileNotFoundException
                    mMediaPlayer.setDataSource(fileInputStream.getFD()); // IOException, IllegalArgumentException, IllegalStateException
                    fileInputStream.close();

                    mMediaPlayer.prepare();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return;
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                mMediaPlayer.start();
                mHandler.sendEmptyMessage(UPDATE_STATE_PLAYING);
            }
        }).start();
    }

    private void stopPlayMusic() {
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        mMediaPlayer = null;
        setIconPlayOrPause(false);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        setIconPlayOrPause(false);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Utilities.makeToast(mContext, "Media Error: " + what + ", " + extra);
        return false;
    }

    public void onDestroy() {
        mCursor.close();
        stopPlayMusic();
    }

    @Override
    public boolean handleMessage(Message msg) {
        int what = msg.what;
        switch (what) {
            case UPDATE_STATE_PLAYING:
                setIconPlayOrPause(true);
                return true;

        }
        return false;
    }

    private void setIconPlayOrPause(boolean isPlay) {
//        Log.i("lanna", "updateIconPlayPause isPlay="+isPlay);
        if (null != mIvCurrentPlayPauseIcon) {
            mIvCurrentPlayPauseIcon.setSelected(isPlay);
        }
    }
}
