package com.project.capstone_stage2.util;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.capstone_stage2.R;
import com.project.capstone_stage2.dbUtility.ExerciseContract;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.ContentValues.TAG;

public class WidgetExerciseListAdapter extends RecyclerView.Adapter<WidgetExerciseListAdapter.ExerciseViewHolder> {

    protected boolean mDataValid;
    protected int mRowID;
    private static Cursor mCursor = null;
    // TODO: activity need to implement this listener 's call back and pass into the adapter
    private static ExerciseItemOnClickHandler exerciseItemOnClickHandler;
    private static Context mContext;
    boolean mIsAllExercise = true;

    public interface ExerciseItemOnClickHandler {
        // callback to handle when VH is clicked
        public void onClickExercise(Cursor cursor);

    }

    // Default Constructor
    public WidgetExerciseListAdapter() {

    }

    public WidgetExerciseListAdapter(Context context, ExerciseItemOnClickHandler onClickHandler, boolean isALLExercise) {
        // implemented by the Activity
        exerciseItemOnClickHandler = onClickHandler;
        // assign the parent's activity
        mContext = context;
        mIsAllExercise = isALLExercise;
    }

    @Override
    public void onBindViewHolder(WidgetExerciseListAdapter.ExerciseViewHolder holder, int position, List payloads) {
        // TODO: bind the data
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.moveToPosition(position);
        }
        // get data by index
        //int weatherId = mCursor.getInt(MainActivity.INDEX_WEATHER_CONDITION_ID);
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public WidgetExerciseListAdapter.ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Init the layout of the ViewHolder
        // TODO : why on a null reference?
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.widget_exercise_item, parent, false);
        ExerciseViewHolder viewHolder = new ExerciseViewHolder(itemView);
        return viewHolder;
    }

    /**
     * public static String[] EXERCISE_PROJECTION = {
     * ExerciseContract.ExerciseEntry.CATEGORY,
     * ExerciseContract.ExerciseEntry.CATEGORY_DESC,
     * ExerciseContract.ExerciseEntry.EXERCISE_ID,
     * ExerciseContract.ExerciseEntry.EXERCISE_NAME,
     * ExerciseContract.ExerciseEntry.EXERCISE_DESCRIPTION,
     * ExerciseContract.ExerciseEntry.EXERCISE_IMAGE,
     * ExerciseContract.ExerciseEntry.EXERCISE_VIDEO
     * };
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, int position) {
        // TODO: so before that we need to pass the cursor by setAdapterData
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.moveToPosition(position);


            // TODO: StaleException - after going back to MainActivity the cursor is destroyed!
            holder.mExerciseName.setText(mCursor.getString(mCursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_NAME)));
            // TODO: add back the Backend JSON with exercise description
            // holder.mExerciseDesc.setText(mCursor.getString(mCursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_DESCRIPTION)));
            // set the DUMMY data for now
            holder.mExerciseDesc.setText("<exercise description here...>");
            String imageURL = mCursor.getString(mCursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_IMAGE));

            // holder.mExerciseImage.setImageResource(mCursor.getInt(mCursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_IMAGE)));
//        if(!imageURL.isEmpty() && imageURL != null) {
//            // Picasso will handle loading the images on a background thread, image decompression and caching the images.
//            Picasso.with(mContext).load(imageURL).into(holder.mExerciseImage);
//        }

            // TODO: will use the default image for now - need to load a real image !
            int defaultImage = R.drawable.exercise_default;
            Picasso.with(mContext).load(defaultImage).into(holder.mExerciseImage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    @Override
    public long getItemId(int position) {

        if (mCursor != null) {
            if (mCursor.moveToPosition(position)) {
                return mCursor.getLong(Integer.getInteger(ExerciseContract.ExerciseEntry._ID));
            } else {
                return RecyclerView.NO_ID;
            }
        } else {
            return RecyclerView.NO_ID;
        }

    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        } else {
            return 0;
        }
    }

    // update the Recycler list with new Cursor
    public void setAdapterData(Cursor cursor) {
        // if the data is not null
        swapCursor(cursor);
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        mCursor = newCursor;
        if (newCursor != null) {
            // Why we do not have a column for _id ??
            //mRowID = newCursor.getColumnIndexOrThrow(ExerciseContract.ExerciseEntry._ID);
            mDataValid = true;
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            mRowID = -1;
            mDataValid = false;
            // notify the observers about the lack of a data set
            notifyItemRangeRemoved(0, getItemCount());
        }
        return oldCursor;
    }

    /**
     * ExerciseViewHolder
     */
    public class ExerciseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mExerciseName;
        public TextView mExerciseDesc;
        public ImageView mExerciseImage;

        public ExerciseViewHolder(View itemView) {
            // initialize the views inside the view holder
            super(itemView); // must be in the first line of constructor
            mExerciseImage = (ImageView) itemView.findViewById(R.id.exercise_image);
            mExerciseName = (TextView) itemView.findViewById(R.id.exercise_name);
            mExerciseDesc = (TextView) itemView.findViewById(R.id.exercise_desc);

        }

        // View.OnClickListener's method
        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick Method to call the call back method on position");
            int adapterPosition = getAdapterPosition();
            Log.d(TAG, "onClick Method to call the call back method on position:" + adapterPosition);
            // mCursor.moveToPosition(adapterPosition);
            // Use Call back to pass information back to the activity
            //long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
            // mClickHandler.onClick(dateInMillis);
            // exerciseItemOnClickHandler.onClickExercise(mCursor);
        }
    }
}
