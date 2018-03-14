package com.project.capstone_stage2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
    private static final String HAS_DATA_KEY= "has_data";

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
    }

    public AllExerciseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment AllExerciseFragment.
     */
    // TODO: Rename and change types and number of parameter

    public static AllExerciseFragment newInstance(int page, String title,boolean twoPane) {
        Log.d(TAG,"when new fragment newInstance, what is the paneMode?" + twoPane);
        AllExerciseFragment fragment = new AllExerciseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        fragment.setPaneMode(twoPane);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page = getArguments().getInt(ARG_PAGE);
            title = getArguments().getString(ARG_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        mTwoPane = getResources().getBoolean(R.bool.has_two_panes);
        Log.d(TAG, "###### onCreateView IS IT TWO PANE ?? #### " + mTwoPane);

        // Obtain the shared Tracker instance.
        //AnalyticsApplication application = (AnalyticsApplication) getApplication();
        //mTracker = application.getDefaultTracker();

        Log.d(TAG,"onCreateView is called!");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_all_exercise, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.all_execise_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setHasFixedSize(true);
        Log.d(TAG,"onCreateView:-create new instance for Recycler Adapter");
        // THINK ABOUT if we need to have 2 fragment
        // actually the only difference is the adapter, do you think we need seperate Fragment class?
        boolean isAllFragment = true;
        mAdapter = new ExerciseListAdapter(getContext(),this, isAllFragment);
        mRecyclerView.setAdapter(mAdapter);
        mNoDataText = (TextView) rootView.findViewById(R.id.all_exercise_no_data_error_text);
        mProgressIndicator = (ProgressBar) rootView.findViewById(R.id.all_exercise_loading_indicator);

        if (savedInstanceState!=null) {
            if(savedInstanceState.containsKey(HAS_DATA_KEY)) {
                mHasData = savedInstanceState.getBoolean(HAS_DATA_KEY);
            }
        }
        if (mHasData) {
            showData(true);
        } else {
            showData(false);
        }

        return rootView;
    }

    public void showData(boolean hasData) {
        if(hasData) {
            Log.d(TAG, "show the recycler view!");
            mProgressIndicator.setVisibility(View.GONE);
            mNoDataText.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mHasData = true;
        } else {
            Log.d(TAG, "hide the recycler view!");
            mProgressIndicator.setVisibility(View.GONE);
            mNoDataText.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            mHasData = false;
        }

    }

    /**
     * Call by SwipeView activity to update the cursor
     * @param cursor
     */
    public void updateAdapterData(Cursor cursor) {
        if(cursor != null) {
            Log.d(TAG,">>>>>updateAdapterData!!");
            // TODO: how come quickly click on tab page the mAdapter is not ready ??
            if (cursor != null && !cursor.isClosed() && cursor.getCount() > 0) {
                Log.d(TAG,"Cursor has data!");
                if(mAdapter != null) {
                    mCursor = cursor;
                    mAdapter.setAdapterData(mCursor);
                    mHasData = true;
                    // Show the Recycler view
                    showData(true);
                    // Set the Add Favorite button state
                    // checkIsFavorite(cursor);

                } else {
                    Log.e(TAG,"Error:Adapter is null ? why?");
                    // showData(false);
                }
            }
        } else {
            Log.d(TAG,"Cursor has No data!");
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
        Log.d(TAG,">>>> DEBUG::: setPaneMode:" + mode);
        mTwoPane = mode;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Context context) {

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
        super.onDetach();
        //mListener = null;
    }

    // This is a callback method of ExerciseListAdapter
    // when click it will launch the Exercise Detail Activity
    @Override
    public void onClickExercise(Cursor cursor) {
        Log.d(TAG, "onClickExercise: what is the pane mode?" + mTwoPane);
        Toast.makeText(getContext(), "navigate to Detail view", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onClickExercise:check 2 Pane mode is:" + mTwoPane);
        Toast.makeText(getContext(), "2 pane mode:" + mTwoPane, Toast.LENGTH_LONG).show();
        String exeCategory = null;
        String exeName = null;
        String exeID = null;
        String exeVideoURL = null;
        String exeSteps = null;

        if (cursor!=null && !cursor.isClosed()) {
            exeCategory = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.CATEGORY));
            exeName = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_NAME));
            exeID = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_ID));
            exeVideoURL = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_VIDEO));
            exeSteps = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_STEPS));
        }

        // One-Pane
        if (!mTwoPane) {
            Toast.makeText(getContext(), "navigate to Detail view", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getContext(), ExerciseDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(ExerciseDetailActivity.EXERCISE_CATEGORY, exeCategory);
            bundle.putString(ExerciseDetailActivity.EXERCISE_KEY, exeID);
            bundle.putString(ExerciseDetailActivity.EXERCISE_NAME, exeName);
            bundle.putString(ExerciseDetailActivity.EXERCISE_STEPS, exeSteps);
            bundle.putString(ExerciseDetailActivity.EXERCISE_VIDEO_URL, exeVideoURL);
            intent.putExtras(bundle);
            startActivity(intent);
            // Two-Pane
        } else {
            // when in Two pane!
            // Activity need to implement the Fragment listener to pass back the information to show the video
            // show the video on the right pane
            // Pass the information back to the Parent Activity to play the video on the Right pane.
            // but now we cannot play this because we dont know when it is a 2 pane mode!
             mListener.twoPaneModeOnClick(exeID,exeSteps,exeVideoURL);
            Toast.makeText(getContext(), "Callback action for 2pane mode, what the hell!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onShareClick(Cursor cursor) {
        // Add analytics to track which exercise is shared
/*        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Exercise Name:" + exeName)
                .setAction("Share")
                .build());*/

        Toast.makeText(getContext(),"share content!", Toast.LENGTH_LONG).show();
        Bundle bundle = new Bundle();
        String exeName = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_NAME));
        String exeVideoURL = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_VIDEO));
        String exeSteps = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_STEPS));

        StringBuilder builder = new StringBuilder();
        builder.append("Exercise Name:" + exeName + "\n")
                .append("Exercise Steps:" + exeSteps + "\n");
        if(exeVideoURL!=null && !exeVideoURL.isEmpty()) {
                builder.append("Please check out our exercise by this link: " + exeVideoURL + "\n");
        }

        startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                .setType("text/plain")
                .setChooserTitle(getString(R.string.share_exercise_title))
                .setText(builder.toString())
                .getIntent(), getString(R.string.share_exercise_sendTo)));
    }

    // Call back method for ExerciseListAdapter to pass in the cursor
    @Override
    public boolean onAddFavClick(Cursor cursor) {
        // https://developer.android.com/guide/topics/providers/content-provider-basics.html
        // select the row from All Exercise and add the parameter to Favorite Exercise
        int id = cursor.getColumnIndex(ExerciseContract.ExerciseEntry._ID);
        int name_index = cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_NAME);
        String exeID = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_ID));
        String exeName = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_NAME));
        String category = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.CATEGORY));

        // Add analytics to track which exercise is add as favorite
/*
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Exercise Name:" + exeName)
                .setAction("Add favorite")
                .build());
*/

        Log.d(TAG,"insert favorite index0: " + cursor.getString(0) );
        Log.d(TAG,"insert favorite index1: " + cursor.getString(1) );
        Log.d(TAG,"insert favorite index2: " + cursor.getString(2) );
        Log.d(TAG,"insert favorite index3: " + cursor.getString(3) );
        Log.d(TAG,"insert favorite index4: " + cursor.getString(4) );
        Log.d(TAG,"insert favorite index5: " + cursor.getString(5) );
        Log.d(TAG,"insert favorite index6: " + cursor.getString(6) );
        Log.d(TAG,"insert favorite index0: " + cursor.getString(7) );
        Log.d(TAG,"insert favorite index0: " + cursor.getString(8) );

        Toast.makeText(getContext(), "add favorite:- " + exeID + ":" + exeName, Toast.LENGTH_LONG).show();
        // CREATE TABLE AllExercise (_id INTEGER PRIMARY KEY AUTOINCREMENT,category TEXT NOT NULL,categoryDesc TEXT NOT NULL,exerciseID TEXT NOT NULL,name TEXT NOT NULL,description TEXT NOT NULL,steps TEXT NOT NULL,image TEXT NOT NULL,video TEXT NOT NULL);

        if (cursor != null || !cursor.isClosed()) {
            // Defines an object to contain the new values to insert
            ContentValues mNewValues = new ContentValues();

/// 01-23 00:07:05.885 2637-2637/com.project.capstone_stage2 E/CursorWindow: Failed to read row 0, column 7 from a CursorWindow which has 2 rows, 7 columns.
            mNewValues.put(ExerciseContract.ExerciseEntry.CATEGORY, cursor.getString(1));
            mNewValues.put(ExerciseContract.ExerciseEntry.CATEGORY_DESC, cursor.getString(2));
            mNewValues.put(ExerciseContract.ExerciseEntry.EXERCISE_ID, cursor.getString(3));
            mNewValues.put(ExerciseContract.ExerciseEntry.EXERCISE_NAME, cursor.getString(4));
            mNewValues.put(ExerciseContract.ExerciseEntry.EXERCISE_DESCRIPTION, cursor.getString(5));
            mNewValues.put(ExerciseContract.ExerciseEntry.EXERCISE_STEPS, cursor.getString(6));
            //mNewValues.put(ExerciseContract.ExerciseEntry.EXERCISE_STEPS, "no steps");
            mNewValues.put(ExerciseContract.ExerciseEntry.EXERCISE_IMAGE, cursor.getString(7));
            mNewValues.put(ExerciseContract.ExerciseEntry.EXERCISE_VIDEO, cursor.getString(8));

            Uri insertUri = getActivity().getContentResolver()
                    .insert(ExerciseContract.ExerciseEntry.CONTENT_URI_FAV, mNewValues);
            Log.d(TAG, "insert favorite exercise result uri: " + insertUri);
        } else {
            Toast.makeText(getContext(), "Error:Cursor is not valid!", Toast.LENGTH_LONG).show();
        }

        if (checkAlreadyInsertAsFavorite(exeID)) {
            //mFavMap.put(exeName,true);
            // TODO: disable the ADD Favorite button!
            ContentValues contentValues = new ContentValues();
            // set the favorite flag = 1
            contentValues.put(ExerciseContract.ExerciseEntry.EXERCISE_FAVORITE,1);
            // TODO: update the AllExercise table's favorite flag
             Uri uri = ExerciseContract.ExerciseEntry.CONTENT_URI_ALL;
            // Uri uri = ExerciseContract.ExerciseEntry.buildAllExerciseUriWithId(id);
            updateAllExerciseFavoriteCol(uri,contentValues,exeID,category);
            //updateAllExerciseFavoriteCol(exeID, contentValues);
            return true;
        }

        return false;
    }

    @Override
    public boolean onRemoveFavClick(Cursor cursor) {
        return false;
    }

    // LoaderManager's Callback methods
    // TODO: 1) add back the login to get the data from backend API!
    // TODO: 2) parse the JSON and save into the DB (not to the Exercise Object Array)
    // TODO: 3) set the Content Provider Cursor to the Recycler View's Adapter to show the data

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

    //D/AllExerciseFragment: insert favorite exercise result uri: content://com.project.capstone_stage2.app.provider/FavoriteExercise/1
//02-18 18:28:48.324 2832-2832/com.project.capstone_stage2 D/ExerciseContentProvider: QUERY:FAVORITE EXERCISE Query from FavoriteExercise
//02-18 18:28:48.325 2832-2832/com.project.capstone_stage2 D/ExerciseContentProvider: cursor count:0
//            02-18 18:28:48.325 2832-2832/com.project.capstone_stage2 D/ContentValues: addFavorite flag:false
    public boolean checkAlreadyInsertAsFavorite(String exerciseID) {
        Uri uri = ExerciseContract.ExerciseEntry.CONTENT_URI_FAV;
        String[] projections = {ExerciseContract.ExerciseEntry.EXERCISE_ID, ExerciseContract.ExerciseEntry.EXERCISE_NAME};
        String selection = ExerciseContract.ExerciseEntry.EXERCISE_ID + "=?";
        String[] selectionArgs = {exerciseID};
        try {
            Cursor cursor = getActivity().getContentResolver().query(uri,projections,selection,selectionArgs,null);
            //if (cursor.getCount() == 1) {
            if (cursor.getCount() > 0) {
                Log.d(TAG,"Assert if favorite is added ::: checkAlreadyInsertAsFavorite:" + cursor.getCount());
                return true;
            }  else {
                Log.d(TAG,"Error:::unable to query the added favorite exercise!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "checkAlreadyInsert:Unable to query the inserted data using id:");
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean(HAS_DATA_KEY,mHasData);

    }

    // TODO: should we move to an Util class
   // public void updateAllExerciseFavoriteCol(Uri updateURI,String exerciseID, ContentValues contentValues){
    public void updateAllExerciseFavoriteCol(Uri updateURI, ContentValues contentValues, String exeID, String category){
        // TODO: need to refresh the list after a list is deleted
        Log.e(TAG, "update AllExercise Table's favorite flag!");
        Log.e(TAG, "ContentValues:" + contentValues);
        // Uri updateURI = ExerciseContract.ExerciseEntry.CONTENT_URI_ALL;
        //String whereClause = ExerciseContract.ExerciseEntry._ID + " = ?";
        String whereClause = ExerciseContract.ExerciseEntry.EXERCISE_ID + " = ? AND " + ExerciseContract.ExerciseEntry.CATEGORY + " = ?";
        //String whereClause = ExerciseContract.ExerciseEntry.EXERCISE_ID + " = ?";
        //int updateRow = getActivity().getContentResolver().update(updateURI, contentValues, whereClause, new String[]{id});
        //int updateRow = getActivity().getContentResolver().update(updateURI, contentValues, whereClause, new String[]{exeID});

        int updateRow = getActivity().getContentResolver().update(updateURI, contentValues, whereClause, new String[]{exeID,category});
        Log.e(TAG, "updateAllExerciseFavoriteCol #of row:" + updateRow);
    }

    private ExerciseListAdapter getAdapter() {
        return mAdapter;
    }

    public void checkIsFavorite(Cursor cursor) {

        if (cursor!=null && !cursor.isClosed()) {
            Log.d(TAG, "checkIsFavorite - what is the cursor size?" + cursor.getCount());
            Log.d(TAG, "Loop through cursor and toggle the button!");
            cursor.moveToPosition(-1);
            Log.d(TAG, "while loop=====================================================");
            while (cursor.moveToNext()) {

                String exerciseName = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_NAME));
                int favorite = cursor.getInt(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_FAVORITE));
                Log.e(TAG, ">>>>> Curosr 's ExerciseName:" + exerciseName);
                Log.e(TAG, ">>>>> Cursor 's favorite flag:" + favorite);
                boolean toggle = (favorite == 1) ? true : false;
                Log.e(TAG, ">>>>> if favorite ==1 set to true,else false:" + toggle);
                updateButtonState(toggle,exerciseName);

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
        if (getActivity()!= null && getActivity().getContentResolver() != null) {
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


    private void resetButtonStateToEnabled() {
        if (mRecyclerView!=null && mRecyclerView.getAdapter()!=null) {
            Log.e(TAG,"updateButtonState() is called!");
            int itemCount = mRecyclerView.getAdapter().getItemCount();
            for (int i=0; i<itemCount;i++) {
                ExerciseListAdapter.ExerciseViewHolder vh = (ExerciseListAdapter.ExerciseViewHolder) mRecyclerView.findViewHolderForAdapterPosition(i);

                if (vh != null ) {

                        Log.e(TAG, "=====================updateButtonState=====================");

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
            Log.e(TAG,"ERROR:::updateButtonState() is called but adapter is null!");
        }

    }

    private void updateButtonState(boolean toggle,String exerciseName){
        if (mRecyclerView!=null && mRecyclerView.getAdapter()!=null) {
            Log.e(TAG,"updateButtonState() is called!");
            int itemCount = mRecyclerView.getAdapter().getItemCount();
            for (int i=0; i<itemCount;i++) {
                ExerciseListAdapter.ExerciseViewHolder vh = (ExerciseListAdapter.ExerciseViewHolder) mRecyclerView.findViewHolderForAdapterPosition(i);

                if (vh != null ) {
                    if (vh.mExerciseName.getText().toString().compareTo(exerciseName) == 0) {
                        Log.e(TAG, "=====================updateButtonState=====================");
                        Log.e(TAG, "=====findViewHolderForAdapterPosition?=====" + i);
                        Log.e(TAG, "VH is NOT Null - get the button and toggle it!");
                        Log.e(TAG, "Cursor passin's exercise name:" + exerciseName);
                        Log.e(TAG, "VH properties:" + vh.mExerciseName.getText().toString());
                        Log.e(TAG, "VH properties:" + vh.mExerciseDesc.getText().toString());
                        Button addFavoriteBtn = vh.mAddFavButton;
                        if (addFavoriteBtn != null) {
                            Log.e(TAG, ">>>>> UpdateButtonState:" + toggle);
                            //ExerciseUtil.toggleButtonDisable(toggle, addFavoriteBtn);
                            //mRecyclerView.getAdapter().to
                            vh.toggleButtonDisable(toggle);
                            //found = true;
                        } else {

                            Log.e(TAG, "ERROR:::UpdateButtonState:" + "unable to get the addFavoriteButton?");
                        }
                    }
                } else {
                    Log.e(TAG, "updateButtonState() is called but VH is null!");
                }
            }

        } else {
            Log.e(TAG,"ERROR:::updateButtonState() is called but adapter is null!");
        }
    }

    public void reloadData(String catName){

        // TODO: need to refresh the list after a list is deleted
        Log.e(TAG, "reload the list!");
        Uri queryURI = ExerciseContract.ExerciseEntry.CONTENT_URI_ALL;
                /* Sort order: Ascending by exercise id */
        String sortOrder = ExerciseContract.ExerciseEntry.EXERCISE_ID + " ASC";
        String selectionByCategoryName = ExerciseContract.ExerciseEntry.CATEGORY + " = ?";
        if (getContext() !=null && getContext().getContentResolver()!=null) {
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

                Log.e(TAG, "unable to get the cursor!");
            }
        } else {

            // TODO:- Need to show Error Message!
            //Toast.makeText(getContext(),"Unable to get data! Please Try again!",Toast.LENGTH_LONG).show();
            Log.e(TAG, "ERROR::: Why unable to get ContentResolver!");
            // showData(false);
        }
    }
}
