package com.project.capstone_stage2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.project.capstone_stage2.dbUtility.ExerciseContract;
import com.project.capstone_stage2.util.Exercise;
import com.project.capstone_stage2.util.ExerciseListAdapter;
import com.project.capstone_stage2.util.ExerciseUtil;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllExerciseFragment extends Fragment implements ExerciseListAdapter.ExerciseItemOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    private static final String TAG = AllExerciseFragment.class.getSimpleName();
    private static final String ARG_PAGE = "page";
    private static final String ARG_TITLE = "page_title";
    private static final String HAS_DATA_KEY = "has_data";

    private String title;
    private int page;
    private static String TWO_PANE_KEY = "twoPaneMode";
    private static String FAV_MAP_KEY = "favoriteHashMap";
    private RecyclerView mRecyclerView;
    private TextView mNoDataText;
    private ProgressBar mProgressIndicator;
    private boolean mHasData = false;
    private ExerciseListAdapter mAdapter;
    private Cursor mCursor = null;
    private boolean mTwoPane = false;
    private boolean mIsAllExercise = true; // by default it is for all exercise
    private Tracker mTracker;


    private AllExerciseFragment.OnFragmentInteractionListener mListener = null;

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void twoPaneModeOnClick(String exerciseID, String steps, String videoURL);
        void updateData();
    }

    public AllExerciseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AllExerciseFragment.
     */
    // TODO: Rename and change types and number of parameter
    public static AllExerciseFragment newInstance(int page, String title, boolean twoPane, boolean isAllExercise) {
        Log.d(TAG, "AllExerciseFragement newInstance is created, what is the paneMode?" + twoPane);
        AllExerciseFragment fragment = new AllExerciseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        fragment.setPaneMode(twoPane);
        fragment.setAllListType(isAllExercise);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "DEBUG:: onCreate is called");

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page = getArguments().getInt(ARG_PAGE);
            title = getArguments().getString(ARG_TITLE);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "DEBUG:: 1. =========== onCreateView is called ===========");
        mTwoPane = getResources().getBoolean(R.bool.has_two_panes);
        Log.d(TAG, "###### onCreateView IS IT TWO PANE ?? #### " + mTwoPane);

        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_all_exercise, container, false);
        initViewsLayout(rootView);
        mProgressIndicator.setVisibility(View.VISIBLE); // show loading indicator until lo

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(HAS_DATA_KEY)) {
                mHasData = savedInstanceState.getBoolean(HAS_DATA_KEY);
                if (mHasData) {
                    showData(true);
                }

            }
        } else {
            Toast.makeText(getContext(),"no data yet? why?",Toast.LENGTH_SHORT);
            showData(false); // this is false until the Activity Set the Cursor
        }


        return rootView;
    }

    private void initViewsLayout(View rootView) {

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.all_execise_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        Log.d(TAG, "onCreateView:-create new instance for Recycler Adapter");
        Log.d(TAG, "DEBUG::: onCreateView: mAdapter is created!");
        mAdapter = new ExerciseListAdapter(getContext(), this, mIsAllExercise);
        mRecyclerView.setAdapter(mAdapter);
        mNoDataText = (TextView) rootView.findViewById(R.id.all_exercise_no_data_error_text);
        mProgressIndicator = (ProgressBar) rootView.findViewById(R.id.all_exercise_loading_indicator);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    public void showData(boolean hasData) {
        if (hasData) {
            Log.d(TAG, "showData method: when has data!");
            Log.d(TAG, "show the recycler view!");
            mProgressIndicator.setVisibility(View.GONE);
            mNoDataText.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mHasData = true;
        } else {
            Log.d(TAG, "showData method: when No Data!! ");
            Log.d(TAG, "hide the recycler view!");
            Log.d(TAG, "progress indicator is null ?? " + mProgressIndicator);
            if(mProgressIndicator != null) {
                mProgressIndicator.setVisibility(View.GONE);
            }
            if ( mNoDataText != null) {
                mNoDataText.setVisibility(View.VISIBLE); // show No Exercise Text
            }
            if (mRecyclerView !=null) {
                mRecyclerView.setVisibility(View.INVISIBLE);
            }
            mHasData = false;
        }
    }

    /**
     * Call by SwipeView activity to update the cursor
     *
     * @param cursor
     */
    public void updateAdapterData(Cursor cursor) {
        if (cursor != null) {
            Log.d(TAG, "DEBUG:2. ==========updateAdapterData===============");
            // TODO: how come quickly click on tab page the mAdapter is not ready ??
            // we set the adapter in the onCreateView already!
            if (cursor != null && !cursor.isClosed() && cursor.getCount() > 0) {
                Log.d(TAG, "Cursor has data!");
                if (mAdapter != null) {
                    mCursor = cursor;
                    mAdapter.setAdapterData(mCursor);
                    // Show the Recycler view
                    showData(true);
                } else {
                    Log.e(TAG, "Error:Adapter is null ? why?");
                    // showData(false);
                }
            }
        } else {
            Log.d(TAG, "Cursor has No data!");
            mHasData = false;
            showData(false);
        }
    }

    /**
     * Set Cursor to Null
     */
    public void resetAdapterData() {
        mCursor = null;
    }

    public void setPaneMode(boolean mode) {
        Log.d(TAG, ">>>> DEBUG::: setPaneMode:" + mode);
        mTwoPane = mode;
    }

    public void setAllListType(boolean isAllExercise) {
        mIsAllExercise = isAllExercise;
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "DEBUG::: onAttach");
        super.onAttach(context);
        // TODO: uncomment the callback listener if it is needed !
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "DEBUG::: onDetach()!");
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "DEBUG::: onDestroy()!");
        super.onDestroy();
    }

    // This is a callback method of ExerciseListAdapter
    // when click it will launch the Exercise Detail Activity
    @Override
    public void onClickExercise(Cursor cursor) {
        Log.d(TAG, "onClickExercise: what is the pane mode?" + mTwoPane);
        Log.d(TAG, "onClickExercise:check 2 Pane mode is:" + mTwoPane);
        String exeCategory = null;
        String exeCategoryDesc = null;
        String exeName = null;
        String exeID = null;
        String exeImageURL = null;
        String exeVideoURL = null;
        String exeSteps = null;

        if (cursor != null && !cursor.isClosed()) {
            exeCategory = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.CATEGORY));
            exeCategoryDesc = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.CATEGORY_DESC));
            exeName = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_NAME));
            exeID = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_ID));
            exeImageURL = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_IMAGE));
            exeVideoURL = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_VIDEO));
            exeSteps = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_STEPS));
        }

        // One-Pane
        if (!mTwoPane) {
            // FOR DEBUG
            // Toast.makeText(getContext(), "One Pane - navigate to Detail view after click", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getContext(), ExerciseDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(ExerciseDetailActivity.EXERCISE_CATEGORY, exeCategory);
            bundle.putString(ExerciseDetailActivity.EXERCISE_KEY, exeID);
            bundle.putString(ExerciseDetailActivity.EXERCISE_NAME, exeName);
            bundle.putString(ExerciseDetailActivity.EXERCISE_STEPS, exeSteps);
            bundle.putString(ExerciseDetailActivity.EXERCISE_VIDEO_URL, exeVideoURL);
            // TODO: try to send an exercise parcelable object and get it from Detail View!
            // because I want to know if my exercise class is working or not
            Exercise exercise = new Exercise(exeID,exeCategory,exeCategory,exeName,exeImageURL,exeVideoURL,exeSteps);
            bundle.putParcelable("exercise", exercise);
            intent.putExtras(bundle);

            startActivity(intent);
            // Two-Pane
        } else {
            // when in Two pane!
            // Activity need to implement the Fragment listener to pass back the information to show the video
            // show the video on the right pane - Pass the information back to the Parent Activity to play the video on the Right pane.
            mListener.twoPaneModeOnClick(exeID, exeSteps, exeVideoURL);
        }
    }

    @Override
    public void onShareClick(Cursor cursor) {
        //Toast.makeText(getContext(), "share content!", Toast.LENGTH_LONG).show();
        Bundle bundle = new Bundle();
        String exeName = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_NAME));
        String exeVideoURL = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_VIDEO));
        String exeSteps = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_STEPS));

        // Add analytics to track which exercise is shared
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Exercise Name:" + exeName)
                .setAction("Share Exericse")
                .build());

        StringBuilder builder = new StringBuilder();
        builder.append("Exercise Name:" + exeName + "\n")
                .append("Exercise Steps:" + exeSteps + "\n");
        if (exeVideoURL != null && !exeVideoURL.isEmpty()) {
            String youtubeURL = ExerciseUtil.YOUTUBE_URL_PREFIX + exeVideoURL;
            builder.append("Please check out our exercise by this link: " + youtubeURL + "\n");
        }

        startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                .setType("text/plain")
                .setChooserTitle(getString(R.string.share_exercise_title))
                .setText(builder.toString())
                .getIntent(), getString(R.string.share_exercise_sendTo)));
    }

    // Call back method for ExerciseListAdapter to pass in the cursor
    // When Add Favorite button is click - Set the Favorite flag as favorite
//    @Override
//    public boolean onAddFavClick(Cursor cursor) {
//        // https://developer.android.com/guide/topics/providers/content-provider-basics.html
//        // select the row from All Exercise and add the parameter to Favorite Exercise
//        int id = cursor.getColumnIndex(ExerciseContract.ExerciseEntry._ID);
//        int name_index = cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_NAME);
//        String exeID = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_ID));
//        String exeName = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_NAME));
//        String category = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.CATEGORY));
//
//        // Add analytics to track which exercise is add as favorite
//        mTracker.send(new HitBuilders.EventBuilder()
//                .setCategory("Exercise Name:" + exeName)
//                .setAction("Add Exercise as favorite")
//                .build());
//
//
//        Log.d(TAG, "insert favorite index0: " + cursor.getString(0));
//        Log.d(TAG, "insert favorite index1: " + cursor.getString(1));
//        Log.d(TAG, "insert favorite index2: " + cursor.getString(2));
//        Log.d(TAG, "insert favorite index3: " + cursor.getString(3));
//        Log.d(TAG, "insert favorite index4: " + cursor.getString(4));
//        Log.d(TAG, "insert favorite index5: " + cursor.getString(5));
//        Log.d(TAG, "insert favorite index6: " + cursor.getString(6));
//        Log.d(TAG, "insert favorite index0: " + cursor.getString(7));
//        Log.d(TAG, "insert favorite index0: " + cursor.getString(8));
//
//        Toast.makeText(getContext(), getString(R.string.toast_add_exercise), Toast.LENGTH_LONG).show();
//
//        if (cursor != null || !cursor.isClosed()) {
//            // Defines an object to contain the new values to insert
//            ContentValues mNewValues = new ContentValues();
//            mNewValues.put(ExerciseContract.ExerciseEntry.CATEGORY, cursor.getString(1));
//            mNewValues.put(ExerciseContract.ExerciseEntry.CATEGORY_DESC, cursor.getString(2));
//            mNewValues.put(ExerciseContract.ExerciseEntry.EXERCISE_ID, cursor.getString(3));
//            mNewValues.put(ExerciseContract.ExerciseEntry.EXERCISE_NAME, cursor.getString(4));
//            mNewValues.put(ExerciseContract.ExerciseEntry.EXERCISE_DESCRIPTION, cursor.getString(5));
//            mNewValues.put(ExerciseContract.ExerciseEntry.EXERCISE_STEPS, cursor.getString(6));
//            //mNewValues.put(ExerciseContract.ExerciseEntry.EXERCISE_STEPS, "no steps");
//            mNewValues.put(ExerciseContract.ExerciseEntry.EXERCISE_IMAGE, cursor.getString(7));
//            mNewValues.put(ExerciseContract.ExerciseEntry.EXERCISE_VIDEO, cursor.getString(8));
//
//            Uri insertUri = getActivity().getContentResolver()
//                    .insert(ExerciseContract.ExerciseEntry.CONTENT_URI_FAV, mNewValues);
//            Log.d(TAG, "insert favorite exercise result uri: " + insertUri);
//        } else {
//            Log.e(TAG, "Error:Cursor is not valid! ");
//        }
//
//        if (checkAlreadyInsertAsFavorite(exeID)) {
//            // TODO: disable the ADD Favorite button!
//            ContentValues contentValues = new ContentValues();
//            // set the favorite flag = 1
//            contentValues.put(ExerciseContract.ExerciseEntry.EXERCISE_FAVORITE, 1);
//            // TODO: update the AllExercise table's favorite flag
//            Uri uri = ExerciseContract.ExerciseEntry.CONTENT_URI_ALL;
//           // updateAllExerciseFavoriteCol(uri, contentValues, exeID, category);
//            return true;
//        }
//
//        return false;
//    }

    // call back method when "Add Favorite" button is added
    @Override
    public boolean onAddFavClick(Cursor cursor) {
        Log.d(TAG, "callback onAddFavClick");
        if (cursor == null || cursor.isClosed()) {
            Log.d(TAG, "Cursor is null ? why");
            return false;
        }
        // https://developer.android.com/guide/topics/providers/content-provider-basics.html
        // select the row from All Exercise and add the parameter to Favorite Exercise
        int id = cursor.getColumnIndex(ExerciseContract.ExerciseEntry._ID);
        int name_index = cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_NAME);
        String exeID = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_ID));
        String exeName = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_NAME));
        String category = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.CATEGORY));

        // Add analytics to track which exercise is add as favorite
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Exercise Name:" + exeName)
                .setAction("Add Exercise as favorite")
                .build());

        Toast.makeText(getContext(), getString(R.string.toast_add_exercise), Toast.LENGTH_LONG).show();

        if (cursor != null || !cursor.isClosed()) {
            // Defines an object to contain the new values to insert
            ContentValues mNewValues = new ContentValues();
            mNewValues.put(ExerciseContract.ExerciseEntry.EXERCISE_FAVORITE, 1);
            String whereClause = ExerciseContract.ExerciseEntry.EXERCISE_ID + " = ?" ;
            Uri queryUri = ExerciseContract.ExerciseEntry.CONTENT_URI_ALL;
            // return the number of rows updated
            int result = getActivity().getContentResolver().update(queryUri, mNewValues, whereClause, new String[]{exeID});
            Log.d(TAG, "onAddFavorite button is clicked - Update favorited exercise flag result: " + result);
            if(result > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            Log.e(TAG, "Error:Cursor is not valid! ");
            return false;
        }

    }

    @Override
    public boolean onRemoveFavClick(Cursor cursor) {
        Log.d(TAG, "callback onRemoveFavClick");
        // cursor is stale and could cause StaleDataException
        String id = cursor.getString(cursor.getColumnIndex("_id"));
        String exeID = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_ID));
        String exeName = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_NAME));
        String catName = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.CATEGORY));
        Log.e(TAG, "update the favorite flag in the All table!");
        // update All Exercise table's favorite flag to 0
        ContentValues contentValues = new ContentValues();
        contentValues.put(ExerciseContract.ExerciseEntry.EXERCISE_FAVORITE, 0);
        Uri updateALlExerciseURI = ExerciseContract.ExerciseEntry.CONTENT_URI_ALL;
        ExerciseUtil.updateAllExerciseFavoriteCol(this, updateALlExerciseURI, contentValues, exeID, catName);

        // reload the favorite list
        reloadDataFavoriteList(catName);
        return true;
    }



    // LoaderManager's Callback methods
    // TODO: 1) add back the login to get the data from backend API!
    // TODO: 2) parse the JSON and save into the DB (not to the Exercise Object Array)
    // TODO: 3) set the Content Provider Cursor to the Recycler View's Adapter to show the data

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onActivityCreated");
        if (mListener != null) {
            mListener.updateData();
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public boolean checkAlreadyInsertAsFavorite(String exerciseID) {
        Log.d(TAG, "callback onRemoveFavClick");
        Uri uri = ExerciseContract.ExerciseEntry.CONTENT_URI_ALL;
        String[] projections = {ExerciseContract.ExerciseEntry.EXERCISE_ID, ExerciseContract.ExerciseEntry.EXERCISE_NAME,ExerciseContract.ExerciseEntry.EXERCISE_FAVORITE};
        String selection = ExerciseContract.ExerciseEntry.EXERCISE_ID + "=?";
        String[] selectionArgs = {exerciseID};

        Cursor cursor = getActivity().getContentResolver().query(uri, projections, selection, selectionArgs, null);
        if (cursor.getCount() > 0) {
            Log.d(TAG, "Assert if favorite is added ::: checkAlreadyInsertAsFavorite:" +
                    cursor.getColumnName(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_FAVORITE)));
            return true;
        } else {
            Log.e(TAG, "Error:::unable to query the added favorite exercise!");
            return false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(HAS_DATA_KEY, mHasData);

    }

    public ExerciseListAdapter getAdapter() {
        return mAdapter;
    }

    public void checkIsFavorite(Cursor cursor) {

        if (cursor != null && !cursor.isClosed()) {
            Log.d(TAG, "checkIsFavorite - what is the cursor size?" + cursor.getCount());
            Log.d(TAG, "Loop through cursor and toggle the button!");
            cursor.moveToPosition(-1);
            Log.d(TAG, "while loop=====================================================");
            while (cursor.moveToNext()) {

                String exerciseName = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_NAME));
                int favorite = cursor.getInt(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_FAVORITE));
                Log.d(TAG, ">>>>> Curosr 's ExerciseName:" + exerciseName);
                Log.d(TAG, ">>>>> Cursor 's favorite flag:" + favorite);
                boolean toggle = (favorite == 1) ? true : false;
                Log.d(TAG, ">>>>> if favorite ==1 set to true,else false:" + toggle);
                updateButtonState(toggle, exerciseName);

            }
            Log.d(TAG, "End of while loop=====================================================");
        }
    }

    // Use another method which is to check the Favorite Table instead!!
    public void checkIsFavorite2(String catName) {
        Log.d(TAG, "========checkFavorite2========");
        Uri queryURI = ExerciseContract.ExerciseEntry.CONTENT_URI_FAV;
                /* Sort order: Ascending by exercise id */
        String favSortOrder = ExerciseContract.ExerciseEntry.EXERCISE_ID + " ASC";
        String selectionByCategoryName = ExerciseContract.ExerciseEntry.CATEGORY + " = ?";
        if (getActivity() != null && getActivity().getContentResolver() != null) {
            Cursor favCursor = getActivity().getContentResolver().query(queryURI, ExerciseSwipeViewActivity.FAV_EXERCISE_PROJECTION, selectionByCategoryName, new String[]{catName}, favSortOrder);
            if (favCursor != null && favCursor.getCount() > 0) {
                while (favCursor.moveToNext()) {

                    String exeName = favCursor.getString(favCursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_NAME));
                    // int favorite = favCursor.getInt(favCursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_FAVORITE));
                    Log.e(TAG, "checkFavorite2's -> Favorite table Cursor 's ExerciseName:" + exeName);
                    if (exeName != null) {
                        updateButtonState(true, exeName);
                    }
                }
            } else {
                // Reset all the button to enable state
                resetButtonStateToEnabled();
            }
        }

    }


    public void checkIsFavoriteFromExerciseTable(String catName) {
        Log.d(TAG, "========checkIsFavoriteFromExerciseTable========");
        Uri queryURI = ExerciseContract.ExerciseEntry.CONTENT_URI_ALL;
                /* Sort order: Ascending by exercise id */
        String sortOrder = ExerciseContract.ExerciseEntry.EXERCISE_ID + " ASC";
        String selectionByCategoryName = ExerciseContract.ExerciseEntry.CATEGORY + " = ?";
        String selectionByFavorite = ExerciseContract.ExerciseEntry.EXERCISE_FAVORITE + " = ?";
        String multiSelection = selectionByCategoryName + " and " + selectionByFavorite;
        if (getActivity() != null && getActivity().getContentResolver() != null) {
            Cursor favCursor = getActivity().getContentResolver().query(queryURI, ExerciseSwipeViewActivity.EXERCISE_PROJECTION, multiSelection, new String[]{catName,"1"}, sortOrder);
            if (favCursor != null && favCursor.getCount() > 0) {
                while (favCursor.moveToNext()) {

                    String exeName = favCursor.getString(favCursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_NAME));
                    // int favorite = favCursor.getInt(favCursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_FAVORITE));
                    Log.e(TAG, "Favorited exercise name: " + exeName);
                    if (exeName != null) {
                        Log.e(TAG, "Toggle the Add Exercise button to disable!!");
                        updateButtonState(true, exeName);
                    }
                }
            } else {
                // Reset all the button to enable state
                resetButtonStateToEnabled();
            }
        }

    }

    private void resetButtonStateToEnabled() {
        if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
            Log.e(TAG, "updateButtonState() is called!");
            int itemCount = mRecyclerView.getAdapter().getItemCount();
            for (int i = 0; i < itemCount; i++) {
                ExerciseListAdapter.ExerciseViewHolder vh = (ExerciseListAdapter.ExerciseViewHolder) mRecyclerView.findViewHolderForAdapterPosition(i);

                if (vh != null) {

                    Log.d(TAG, "=====================updateButtonState=====================");

                    Button addFavoriteBtn = vh.mAddFavButton;
                    if (addFavoriteBtn != null) {
                        vh.toggleButtonDisable(false);

                    } else {

                        Log.e(TAG, "ERROR:::UpdateButtonState:" + "unable to get the addFavoriteButton?");
                    }
                } else {
                    Log.e(TAG, "updateButtonState() is called but VH is null!");
                }

            }

        } else {
            Log.e(TAG, "ERROR:::updateButtonState() is called but adapter is null!");
        }

    }

    private void updateButtonState(boolean toggle, String exerciseName) {
        if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
            Log.e(TAG, "updateButtonState() is called!");
            int itemCount = mRecyclerView.getAdapter().getItemCount();
            for (int i = 0; i < itemCount; i++) {
                ExerciseListAdapter.ExerciseViewHolder vh = (ExerciseListAdapter.ExerciseViewHolder) mRecyclerView.findViewHolderForAdapterPosition(i);

                if (vh != null) {
                    if (vh.mExerciseName.getText().toString().compareTo(exerciseName) == 0) {
                        Log.d(TAG, "=====================updateButtonState=====================");
                        Log.d(TAG, "=====findViewHolderForAdapterPosition?=====" + i);
                        Log.d(TAG, "VH is NOT Null - get the button and toggle it!");
                        Log.d(TAG, "Cursor passin's exercise name:" + exerciseName);
                        Log.d(TAG, "VH properties:" + vh.mExerciseName.getText().toString());
                        Log.d(TAG, "VH properties:" + vh.mExerciseDesc.getText().toString());
                        Button addFavoriteBtn = vh.mAddFavButton;
                        if (addFavoriteBtn != null) {
                            Log.e(TAG, ">>>>> UpdateButtonState:" + toggle);
                            vh.toggleButtonDisable(toggle);
                        } else {

                            Log.e(TAG, "ERROR:::UpdateButtonState:" + "unable to get the addFavoriteButton?");
                        }
                    }
                } else {
                    Log.e(TAG, "updateButtonState() is called but VH is null!");
                }
            }

        } else {
            Log.e(TAG, "ERROR:::updateButtonState() is called but adapter is null!");
        }
    }

    public void reloadDataAllList(String catName) {

        // TODO: need to refresh the list after a list is deleted
        Log.e(TAG, "reload the list!");
        Uri queryURI = ExerciseContract.ExerciseEntry.CONTENT_URI_ALL;
                /* Sort order: Ascending by exercise id */
        String sortOrder = ExerciseContract.ExerciseEntry.EXERCISE_ID + " ASC";
        String selectionByCategoryName = ExerciseContract.ExerciseEntry.CATEGORY + " = ?";
        if (getContext() != null && getContext().getContentResolver() != null) {
            Cursor newCursor = getActivity().getContentResolver().query(queryURI, ExerciseSwipeViewActivity.FAV_EXERCISE_PROJECTION, selectionByCategoryName, new String[]{catName}, sortOrder);
            if (newCursor != null) {
                Log.d(TAG, "Assert latest data count:" + newCursor.getCount());
                mAdapter.swapCursor(newCursor);
                if (newCursor.getCount() == 0) {
                    // TODO: show no data
                    showData(false);
                } else {
                    showData(true);
                }
            } else {

                Log.e(TAG, "ERROR:::unable to get the cursor!");
            }
        } else {
            Log.e(TAG, "ERROR::: Why unable to get ContentResolver!");
        }
    }

    // After data changes, we need to reload the list
    public void reloadDataFavoriteList(String catName) {

        // TODO: need to refresh the list after a list is deleted
        Log.e(TAG, "====================Reload the favorite list================");
        Uri queryURI = ExerciseContract.ExerciseEntry.CONTENT_URI_ALL;
                /* Sort order: Ascending by exercise id */
        String favSortOrder = ExerciseContract.ExerciseEntry.EXERCISE_ID + " ASC";
        String selectionByCategoryName = ExerciseContract.ExerciseEntry.CATEGORY + " = ?";
        String selectionByFavorite = ExerciseContract.ExerciseEntry.EXERCISE_FAVORITE + " = ?";
        String multiSelection = selectionByCategoryName + " and " + selectionByFavorite;

        if (getActivity() != null && getActivity().getContentResolver() != null) {
            //if (getContext() !=null && getContext().getContentResolver()!=null) {
            Cursor newCursor = getActivity().getContentResolver().query(queryURI, ExerciseSwipeViewActivity.EXERCISE_PROJECTION, multiSelection, new String[]{catName,"1"}, favSortOrder);
            if (newCursor != null) {
                Log.d(TAG, "Assert latest data count:" + newCursor.getCount());
                mAdapter.swapCursor(newCursor);
                if (newCursor.getCount() == 0) {
                    // TODO: show no data
                    showData(false);
                } else {
                    showData(true);
                }
            } else {

                Log.e(TAG, "unable to get the cursor!");
            }
        } else {
            // TODO:- Need to show Error Message!
            Log.e(TAG, "ERROR::: Why unable to get ContentResolver!");
        }
    }
}
