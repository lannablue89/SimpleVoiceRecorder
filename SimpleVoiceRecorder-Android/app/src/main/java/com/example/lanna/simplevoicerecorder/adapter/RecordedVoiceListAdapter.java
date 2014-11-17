package com.example.lanna.simplevoicerecorder.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lanna.simplevoicerecorder.database.MyDatabase;
import com.example.lanna.simplevoicerecorder.model.AudioModel;

import java.util.List;

import static com.example.lanna.simplevoicerecorder.R.layout.inflater_recored_voice_item;

/**
 * Created by Lanna on 11/11/14.
 */
public class RecordedVoiceListAdapter extends CursorAdapter<RecordedVoiceViewHolder> {

//    private List<AudioModel> mModels;

    private MyDatabase mDb;

    /**
     * Recommended constructor.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     * @param flags   Flags used to determine the behavior of the adapter;
     *                Currently it accept {@link #FLAG_REGISTER_CONTENT_OBSERVER}.
     */
    public RecordedVoiceListAdapter(Context context, MyDatabase db) {
        super(context, db.getAudios(context, Uri.parse(MyDatabase.URI_TO_NOTIFY_DATA_UPDATE)),
                FLAG_REGISTER_CONTENT_OBSERVER);
        mDb = db;
    }

    @Override
    public RecordedVoiceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(inflater_recored_voice_item, viewGroup, false);
        RecordedVoiceViewHolder vh = new RecordedVoiceViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecordedVoiceViewHolder holder, Cursor cursor) {
        holder.updateData(mContext, cursor);
    }

    @Override
    protected void onContentChanged() {
        Log.i("lanna", "onContentChanged");
        changeCursor(mDb.getAudios(mContext, Uri.parse(MyDatabase.URI_TO_NOTIFY_DATA_UPDATE)));
    }

    public void onDestroy() {
        mCursor.close();
    }

//    @Override
//    public void onBindViewHolder(RecordedVoiceViewHolder recordedVoiceViewHolder, int i) {
//        recordedVoiceViewHolder.updateData(getItem(i));
//    }
//
//    @Override
//    public int getItemCount() {
//        return (mModels == null) ? 0 : mModels.size();
//    }
//
//    public AudioModel getItem(int position) {
//        return (mModels == null || position < 0 || position >= mModels.size())
//                ? null
//                : mModels.get(position);
//    }
//
//    public List<AudioModel> getModels() {
//        return mModels;
//    }
//
//    public void setModels(List<AudioModel> models) {
//        mModels = models;
//    }
//
//    public void blindData(List<AudioModel> models) {
//        setModels(models);
//        notifyDataSetChanged();
//    }

}
