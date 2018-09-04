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

// https://developer.android.com/training/material/lists-cards.html
// RecyclerView provides these built-in layout managers:
//
// LinearLayoutManager shows items in a vertical or horizontal scrolling list.
//        GridLayoutManager shows items in a grid.
//        StaggeredGridLayoutManager shows items in a staggered grid.

public class FavExerciseListAdapter extends RecyclerView.Adapter<FavExerciseListAdapter.ExerciseViewHolder> {

    private static final String TAG = FavExerciseListAdapter.class.getSimpleName();
    protected boolean mDataValid;
    protected int mRowID;
    private static Cursor mCursor = null;
    // TODO: activity need to implement this listener 's call back and pass into the adapter
    private static ExerciseItemOnClickHandler exerciseItemOnClickHandler;
    private static Context mContext;

    public interface ExerciseItemOnClickHandler {
        // callback to handle when VH is clicked
        public void onClickExercise(Cursor cursor);

        public void onShareClick(Cursor cursor);

        public boolean onRemoveFavClick(Cursor cursor);
    }

    // Default Constructor
    public FavExerciseListAdapter() {

    }

    public FavExerciseListAdapter(Context context, ExerciseItemOnClickHandler onClickHandler) {
        // implemented by the Activity
        exerciseItemOnClickHandler = onClickHandler;
        // assign the parent's activity
        mContext = context;

    }

    @Override
    public void onBindViewHolder(FavExerciseListAdapter.ExerciseViewHolder holder, int position, List payloads) {

        super.onBindViewHolder(holder, position, payloads);
        // TODO: bind the data
//        if (mCursor!=null && !mCursor.isClosed()) {
//            mCursor.moveToPosition(position);
//            // get data by index
//            //int weatherId = mCursor.getInt(MainActivity.INDEX_WEATHER_CONDITION_ID);
//            super.onBindViewHolder(holder, position, payloads);
//        } else {
//
//
//                Log.e(TAG,"onBindViewHolder - cursor is closed");
//
//        }
    }

    @Override
    public FavExerciseListAdapter.ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Init the layout of the ViewHolder
        // TODO : why on a null reference?
        View execiseCardView = LayoutInflater.from(mContext).inflate(R.layout.fav_exercise_items, parent, false);
        ExerciseViewHolder viewHolder = new ExerciseViewHolder(execiseCardView);
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
        if (mCursor != null && !mCursor.isClosed() && mCursor.getCount() > 0) {
            mCursor.moveToPosition(position);
            holder.mExerciseName.setText(mCursor.getString(mCursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_NAME)));
            // TODO: add back the Backend JSON with exercise description
            holder.mExerciseDesc.setText(mCursor.getString(mCursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_DESCRIPTION)));
            String imageURL = mCursor.getString(mCursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_IMAGE));

            // holder.mExerciseImage.setImageResource(mCursor.getInt(mCursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_IMAGE)));
            if (!imageURL.isEmpty() && imageURL != null) {
                // Picasso will handle loading the images on a background thread, image decompression and caching the images.
                Picasso.with(mContext).load(imageURL).into(holder.mExerciseImage);
            } else {
                // will use the default image
                int defaultImage = R.drawable.exercise_default;
                Picasso.with(mContext).load(defaultImage).into(holder.mExerciseImage);
            }
        } else {
            Log.e(TAG, "onBindViewHolder - cursor is closed!");
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
//        if (newCursor == mCursor) {
//            return null;
//        }
        Cursor oldCursor = mCursor;
        mCursor = newCursor;
        if (mCursor != null) {
            // Why we do not have a column for _id ??
            //mRowID = newCursor.getColumnIndexOrThrow(ExerciseContract.ExerciseEntry._ID);
            mDataValid = true;
            // notify the observers about the new cursor
            notifyDataSetChanged();
            Log.d(TAG, "swapCursor: notifyDataSetChanged!");
            Log.d(TAG, "swapCursor new Cursor count:" + mCursor.getCount());
        } else {
            Log.d(TAG, "newCursor is null!");
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
    public static class ExerciseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView mCardView;
        public TextView mExerciseName;
        public TextView mExerciseDesc;
        public ImageView mExerciseImage;
        // Share button
        public Button mShareButton;
        // Add Favorite button
        public Button mRemoveFavButton;

        public ExerciseViewHolder(View itemView) {
            // initialize the views inside the view holder
            super(itemView); // must be in the first line of constructor
            mCardView = (CardView) itemView.findViewById(R.id.exercise_card_view);
            mCardView.setOnClickListener(this); // adapter implements the OnClickListener
            mExerciseName = (TextView) itemView.findViewById(R.id.exercise_name);
            mExerciseDesc = (TextView) itemView.findViewById(R.id.exercise_desc);
            mExerciseImage = (ImageView) itemView.findViewById(R.id.exercise_image);
            mShareButton = (Button) itemView.findViewById(R.id.share_btn);
            mRemoveFavButton = (Button) itemView.findViewById(R.id.remove_fav_btn);

            // TODO: add onClickListener to the Fav Button to trigger the callback?
            mRemoveFavButton = (Button) itemView.findViewById(R.id.remove_fav_btn);
            if (mRemoveFavButton != null) {
                mRemoveFavButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (exerciseItemOnClickHandler != null) {

                            int adapterPosition = getAdapterPosition();
                            // mCursor.moveToPosition(adapterPosition);
                            Cursor cursor = mCursor;
                            cursor.moveToPosition(adapterPosition);
                            boolean removeFavorite = exerciseItemOnClickHandler.onRemoveFavClick(cursor);
                        }
                    }
                });
            }

            // TODO: add onClickListener to the Fav Button to trigger the callback?

            mShareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (exerciseItemOnClickHandler != null) {

                        int adapterPosition = getAdapterPosition();
                        // mCursor.moveToPosition(adapterPosition);
                        Cursor cursor = mCursor;
                        cursor.moveToPosition(adapterPosition);
                        exerciseItemOnClickHandler.onShareClick(cursor);
                    }
                }
            });

        }

        // View.OnClickListener's method
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            // Use Call back to pass information back to the activity
            exerciseItemOnClickHandler.onClickExercise(mCursor);
        }

        // adb shell content query --uri content://settings/secure --projection name:value --where "name='new_setting'" --sort "name ASC"
        // adb shell content query --uri content://com.project.capstone_stage2.app.provider/FavoriteExercise --projection _id:category:name --where "category=squat" --sort "_id DESC"
    }
}
