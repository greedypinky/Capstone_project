package com.project.capstone_stage2.util;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.capstone_stage2.ExerciseSwipeViewActivity;
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

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ExerciseViewHolder> {

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

        public void onShareClick(Cursor cursor);

        // pass in selected Row ID
        public boolean onAddFavClick(Cursor cursor);

        public boolean onRemoveFavClick(Cursor cursor);
    }

    // Default Constructor
    public ExerciseListAdapter() {

    }

    public ExerciseListAdapter(Context context, ExerciseItemOnClickHandler onClickHandler, boolean isALLExercise) {
        // implemented by the Activity
        exerciseItemOnClickHandler = onClickHandler;
        // assign the parent's activity
        mContext = context;
        mIsAllExercise = isALLExercise;
    }

    @Override
    public void onBindViewHolder(ExerciseListAdapter.ExerciseViewHolder holder, int position, List payloads) {
        // TODO: bind the data
//        if (mCursor != null && !mCursor.isClosed()) {
//            mCursor.moveToPosition(position);
//        }
        // get data by index
        //int weatherId = mCursor.getInt(MainActivity.INDEX_WEATHER_CONDITION_ID);
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public ExerciseListAdapter.ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Init the layout of the ViewHolder
        // TODO : why on a null reference?
        View execiseCardView;
        if (mIsAllExercise) {
            execiseCardView = LayoutInflater.from(mContext).inflate(R.layout.exercise_items, parent, false);
        } else {
            execiseCardView = LayoutInflater.from(mContext).inflate(R.layout.fav_exercise_items, parent, false);
        }
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
        Log.d(TAG, ">>>>>> DEBUG::: onBindViewHolder is called! ");
        // TODO: so before that we need to pass the cursor by setAdapterData
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.moveToPosition(position);

            boolean isFavorite = (mCursor.getInt(mCursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_FAVORITE)) == 1) ? true : false;
            Log.d(TAG, ">>>>>> onBindViewHolder: favorite flag int? " + mCursor.getInt(mCursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_FAVORITE)));
            Log.d(TAG, ">>>>>> onBindViewHolder: favorite flag boolean? " + isFavorite);
            // TODO: StaleException - after going back to MainActivity the cursor is destroyed!
            holder.mExerciseName.setText(mCursor.getString(mCursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_NAME)));
            // TODO: add back the Backend JSON with exercise description
            holder.mExerciseDesc.setText(mCursor.getString(mCursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_DESCRIPTION)));
            // set the DUMMY data for now
            //holder.mExerciseDesc.setText("<exercise description here...>");
            String imageURL = mCursor.getString(mCursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_IMAGE));
            Log.d(TAG, ">>>>>> onBindViewHolder: imageURL? " + imageURL);

            holder.toggleButtonDisable(isFavorite);
            final ExerciseViewHolder mHolder = holder;
            //mHolder.mImageProgressBar.setVisibility(View.VISIBLE);

            // holder.mExerciseImage.setImageResource(mCursor.getInt(mCursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_IMAGE)));
            if (!imageURL.isEmpty() && imageURL != null) {
//                // https://github.com/codepath/android_guides/wiki/Displaying-Images-with-the-Picasso-Library
//                // Picasso will handle loading the images on a background thread, image decompression and caching the images.
                Picasso.with(mContext).load(imageURL).fit().centerCrop().into(holder.mExerciseImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        if (mHolder.mImageProgressBar != null) {
                            mHolder.mImageProgressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError() {
                        mHolder.mImageProgressBar.setVisibility(View.GONE);
                        mHolder.mExerciseImage.setVisibility(View.GONE);
                        mHolder.mLoadImageError.setVisibility(View.VISIBLE);
                        // load the error message
                        Log.e(TAG,"Unable to load the exercise image!");
                    }
                });

               // Picasso.with(mContext).load(imageURL).into(holder.mExerciseImage);

            } else {

                // TODO: add the No image textview if no image for the exercise
            }

            // TODO: will use the default image for now - need to load a real image !
            //int defaultImage = R.drawable.exercise_default;
            // "http://atlasonlinefitness.com/wp-content/uploads/2018/03/nao-push01.png"
            //Picasso.with(mContext).load(defaultImage).into(holder.mExerciseImage);
//            Picasso.with(mContext)
//                    .load("http://atlasonlinefitness.com/wp-content/uploads/2018/03/nao-push01.png")
//                    .into(holder.mExerciseImage);
        } else {

            Log.e(TAG, "onBindViewHolder - cursor is closed");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    @Override
    public long getItemId(int position) {
        Log.e(TAG, ">>>> getItemId:" + position);
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
        Log.e(TAG, "================ swapCursor ===============");
//        if (newCursor == mCursor) {
//            return null;
//        }
        Cursor oldCursor = mCursor;
        mCursor = newCursor;
        if (newCursor != null && !newCursor.isClosed()) {
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
        public CardView mCardView = null;
        public TextView mExerciseName = null;
        public TextView mExerciseDesc = null;
        public ImageView mExerciseImage = null;
        public ProgressBar mImageProgressBar = null;
        public TextView mLoadImageError = null;
        // Share button
        public Button mShareButton = null;
        // Add Favorite button
        public Button mAddFavButton = null;
        // Remove Favorite button
        public Button mRemoveFavButton = null;

        private ExerciseViewHolder(View itemView) {
            // initialize the views inside the view holder
            super(itemView); // must be in the first line of constructor
            mCardView = (CardView) itemView.findViewById(R.id.exercise_card_view);
            mCardView.setOnClickListener(this); // adapter implements the OnClickListener
            mExerciseName = (TextView) itemView.findViewById(R.id.exercise_name);
            mExerciseDesc = (TextView) itemView.findViewById(R.id.exercise_desc);
            mExerciseImage = (ImageView) itemView.findViewById(R.id.exercise_image);
            mShareButton = (Button) itemView.findViewById(R.id.share_btn);
            mImageProgressBar = (ProgressBar) itemView.findViewById(R.id.image_progressBar);
            mLoadImageError = (TextView) itemView.findViewById(R.id.loading_error_text);

            if (mIsAllExercise) {
                Log.e(TAG, "=========== ExerciseViewHolder: init Add Favorite button! =============");
                mAddFavButton = (Button) itemView.findViewById(R.id.add_fav_btn);
                if (mAddFavButton != null) {
                    mAddFavButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (exerciseItemOnClickHandler != null) {
                                int adapterPosition = getAdapterPosition();
                                Cursor cursor = mCursor;
                                cursor.moveToPosition(adapterPosition);
                                boolean addFavorite = exerciseItemOnClickHandler.onAddFavClick(cursor);
                                Log.d(TAG, "setOnClickListener - addFavorite flag: " + addFavorite);
                                toggleButtonDisable(addFavorite);

                            }
                        }
                    });
                }
            }
            else {
                Log.e(TAG, "================== ExerciseViewHolder: init Remove Favorite button! =================");
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
            Log.d(TAG, "onClick Method to call the call back method on position");
            int adapterPosition = getAdapterPosition();
            Log.d(TAG, "onClick Method to call the call back method on position:" + adapterPosition);
            mCursor.moveToPosition(adapterPosition);
            // Use Call back to pass cursor data back to the activity
            exerciseItemOnClickHandler.onClickExercise(mCursor);
        }

        public void toggleButtonDisable(boolean disable) {
            Log.d("ExerciseListAdapter", "toogleButtonDisable method is called");
            Log.d("ExerciseListAdapter", "Disable Flag is?" + disable);
            if (mAddFavButton!=null && mIsAllExercise == true) {
                if (disable) {
                    Log.d("ExerciseListAdapter", "disable the Add Button by:" + !disable);
                    mAddFavButton.setAlpha(0.5f);
                    mAddFavButton.setEnabled(!disable);

                    Log.d("ExerciseListAdapter", "isEnabled() check:" + mAddFavButton.isEnabled());
                } else {
                    Log.d(TAG, "Enable the Add Button!");
                    Log.d(TAG, "===========================================================");
                    mAddFavButton.setAlpha(1f);
                    mAddFavButton.setEnabled(!disable);
                    Log.d("ExerciseListAdapter", "isEnabled() check:" + mAddFavButton.isEnabled());
                }
            }
        }


    }


}
