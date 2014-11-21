package com.example.lanna.simplevoicerecorder.adapter;

import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CursorAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private static final int STATE_PLAY     = 1;
    private static final int STATE_PAUSE    = 2;
    private static final int STATE_CONTINUE = 3;
    private static final int STATE_STOP     = 4;

    private static final int UPDATE_STATE_PLAYING = 1;

    private MyDatabase mDb;
    private static final Uri mUri = Uri.parse(MyDatabase.URI_TO_NOTIFY_DATA_UPDATE);

    private MediaPlayer mMediaPlayer;
    private int mFileIndex = -1;
    private RecordedVoiceViewHolder mCurrentViewHolder;

    private Handler mHandler;
    private long mTimeStart;
    private long mProgress;

    private AudioModel mCurrentModel;
    private CountDownTimer mCountDownTimer;

    public RecordedVoiceListAdapter(Context context, MyDatabase db) {
        super(context, db.getAudios(context, mUri), FLAG_REGISTER_CONTENT_OBSERVER);
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
        holder.updateData(cursor);
    }

    @Override
    protected void onContentChanged() {
//        Log.i("lanna", "onContentChanged");
        changeCursor(mDb.getAudios(mContext, Uri.parse(MyDatabase.URI_TO_NOTIFY_DATA_UPDATE)));
    }

    @Override
    public void onPlayPauseClick(RecordedVoiceViewHolder vh, int position, AudioModel item) {
//        Utilities.makeToast(mContext, "onPlayPauseClick at " + position + ":" + item);

        if (null == item || TextUtils.isEmpty(item.getFilePath())) {
            Utilities.makeToast(mContext, "play item not available - stop");
            setPlayOrPause(STATE_STOP);
            return;
        }

        if (null == mMediaPlayer) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setVolume(1, 1);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnCompletionListener(this);
        }

        // check play new audio
        if (mFileIndex < 0 || mFileIndex != position) {
            setPlayOrPause(STATE_STOP); // set old item to pause to start new one
            mCurrentViewHolder = vh;
            mCurrentModel = item;
            mFileIndex = position;
            startPlayMusic(StoreAudioHelper.getFileStoragePath(item.getFilePath())); // "Download/abcd.mp3"
        }
        // continue play/pause current audio
        else {
            setPlayOrPause(mMediaPlayer.isPlaying() ? STATE_PAUSE : STATE_CONTINUE);
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
                mHandler.sendEmptyMessage(UPDATE_STATE_PLAYING);
            }
        }).start();
    }

    private void stopPlayMusic() {
        if (null != mMediaPlayer) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        setPlayOrPause(STATE_STOP);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Utilities.makeToast(mContext, "Media Error: " + what + ", " + extra);
        return false;
    }

    public void onDestroy() {
        mCursor.close();
        stopPlayMusic();
        setPlayOrPause(STATE_STOP);
    }

    @Override
    public boolean handleMessage(Message msg) {
        int what = msg.what;
        switch (what) {
            case UPDATE_STATE_PLAYING:
                setPlayOrPause(STATE_PLAY);
                return true;
        }
        return false;
    }

    private void setPlayOrPause(int state) {
        if (null == mCurrentViewHolder)
            return;

        Log.i("lanna", "setPlayOrPause state=" + state + " (PLAY = 1, PAUSE = 2, CONTINUE = 3, STOP = 4)");
        if (state == STATE_STOP || state == STATE_PAUSE) {
            mProgress = (state == STATE_PAUSE) ? System.currentTimeMillis() - mTimeStart : 0;
            mTimeStart = 0;
            if (state == STATE_PAUSE) {
                mMediaPlayer.pause();
            } else {
                mMediaPlayer.stop();
            }
            mCountDownTimer.cancel();
            mCurrentViewHolder.setProgress(mProgress);
            mCurrentViewHolder.setPlayState(false);
        }
        else {
            mCurrentViewHolder.setPlayState(true);
            if (state == STATE_PLAY) {
                mProgress = 0;
                mTimeStart = System.currentTimeMillis();
            }
            else { // STATE_CONTINUE
                // this time maybe restart time, so we use mProgress to refill missed time when pause
                mTimeStart = System.currentTimeMillis() - mProgress;
            }
            mCurrentViewHolder.setProgress(mProgress);
            mCountDownTimer = new CountDownTimer(mCurrentModel.getDuration() - mProgress + 1000, 100) {

                @Override
                public void onTick(long millisUntilFinished) {
                    mProgress = System.currentTimeMillis() - mTimeStart;
                    mCurrentViewHolder.setProgress(mProgress);
                    if (mProgress >= mCurrentModel.getDuration()) {
                        mProgress = 0;
                        mCurrentViewHolder.setProgress(0);
                        mCountDownTimer.cancel();
                    }
                }

                @Override
                public void onFinish() { }
            };
            mCountDownTimer.start();
            mMediaPlayer.start();
        }
    }

}
