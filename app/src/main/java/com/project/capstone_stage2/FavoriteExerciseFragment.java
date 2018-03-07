package com.project.capstone_stage2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.project.capstone_stage2.dbUtility.ExerciseContract;
import com.project.capstone_stage2.util.CategoryListAdapter;
import com.project.capstone_stage2.util.ExerciseListAdapter;
import com.project.capstone_stage2.util.ExerciseUtil;
import com.project.capstone_stage2.util.FavExerciseListAdapter;

import java.util.function.LongFunction;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavoriteExerciseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavoriteExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteExerciseFragment extends Fragment implements FavExerciseListAdapter.ExerciseItemOnClickHandler {
    // TODO: Rename parameter arguments, choose names that match
    private static final String TAG = FavoriteExerciseFragment.class.getSimpleName();
    private static final String ARG_PAGE = "page";
    private static final String ARG_TITLE = "page_title";

    private String title;
    private int page;
    private RecyclerView mRecyclerView;
    private TextView mNoDataText;
    private ProgressBar mProgressIndicator;
    private boolean mHasData = false;
    private FavExerciseListAdapter mAdapter;
    // Try to use the same adapter instead of 2
    // private ExerciseListAdapter mAdapter;
    private boolean mTwoPane = false;
    private Cursor mCursor = null;


    private OnFragmentInteractionListener mListener;

    public FavoriteExerciseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteExerciseFragment newInstance(int page, String title, boolean twoPane) {
        FavoriteExerciseFragment fragment = new FavoriteExerciseFragment();
        Bundle args = new Bundle();
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

        Log.d(TAG,"onCreateView is called!");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favorite_exercise, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fav_execise_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setHasFixedSize(true);
        // fixed the nullpointerException by pass in context from constructor
         mAdapter = new FavExerciseListAdapter(getContext(),this);
        // mAdapter = new ExerciseListAdapter(getContext(), this, false);
        mRecyclerView.setAdapter(mAdapter);
        mNoDataText = (TextView) rootView.findViewById(R.id.fav_execise_no_data_error_text);
        mProgressIndicator = (ProgressBar) rootView.findViewById(R.id.fav_execise_loading_indicator);

        showData(false);
        return rootView;
    }

    /**
     * Call by SwipeView activity to update the cursor
     * @param cursor
     */
    public void updateAdapterData(Cursor cursor) {
        // TODO: update the cursor
        // TODO: question - when do we need to close the cursor ??
       // mAdapter.setAdapterData(cursor);
        // TODO: update the cursor
        // TODO: question - when do we need to close the cursor ??
        if (cursor != null && !cursor.isClosed() && cursor.getCount() > 0) {
            Log.d(TAG,"Cursor is not null and has data -> updateAdapterData!");
            if (mAdapter != null) {
                mCursor = cursor;
                mAdapter.setAdapterData(mCursor);
                mHasData = true;
                showData(true);
            } else {
                Log.d(TAG,"Adapter is null! Why?");
            }
        } else {
            Log.d(TAG,"Cursor has no data!");
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
        mTwoPane = mode;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // TODO: uncomment the callback listener if it is needed !
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // Implement the Callback methods
//    @Override
//    public void onClickExercise(Cursor cursor) {
//
//        Toast.makeText(getContext(),"FavoriteExercise - onClickExercise", Toast.LENGTH_LONG).show();
//
//    }
//
//    @Override
//    public void onShareClick() {
//        Toast.makeText(getContext(),"share content!", Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public boolean onAddFavClick(Cursor cursor) {
//        return false;
//    }
//
//    @Override
//    public boolean onRemoveFavClick(Cursor cursor) {
//        Toast.makeText(getContext(),"Remove ", Toast.LENGTH_LONG).show();
//        return false;
//    }

    @Override
    public void onClickExercise(Cursor cursor) {
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
        if (!mTwoPane ) {
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
            // mListener.twoPaneModeOnClick(exeID,exeSteps,exeVideoURL);
            Toast.makeText(getContext(), "Callback action for 2pane mode, what the hell!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onShareClick(Cursor cursor) {

        Toast.makeText(getContext(), "Share!", Toast.LENGTH_LONG).show();

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

    @Override
    public boolean onRemoveFavClick(Cursor cursor) {
        Log.d(TAG,"callback onRemoveFavClick");
        String id = cursor.getString(cursor.getColumnIndex("_id"));
        String exeID = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_ID));
        String exeName = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_NAME));
        String catName = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.CATEGORY));
        String  whereClause = ExerciseContract.ExerciseEntry._ID + " =?";
        Uri uri = ExerciseContract.ExerciseEntry.buildFavoriteExerciseUriWithId(Long.parseLong(id));
        int deleteRow = getActivity().getContentResolver().delete(uri, whereClause, new String[]{id});

        Log.e(TAG, "deleted item count:" + deleteRow);
        // TODO: need to refresh the list after a list is deleted
        Log.e(TAG, "reload the list after removal of the item!");
        reloadData(catName);

        Log.e(TAG, "update the favorite flag in the All table!");
        //
        ContentValues contentValues = new ContentValues();
        // set the favorite flag to false since the user remove from the favorite list
        contentValues.put(ExerciseContract.ExerciseEntry.EXERCISE_FAVORITE, 0);
        // TODO: not sure why but unable to update the flag
        //updateAllExerciseFavoriteCol(exeID, contentValues);
        Uri updateALlExerciseURI = ExerciseContract.ExerciseEntry.CONTENT_URI_ALL;
        ExerciseUtil.updateAllExerciseFavoriteCol(this,updateALlExerciseURI,contentValues,exeID,catName);

        if (deleteRow > 0) {
            return true;
        } else {
            return false;
        }
    }


    public void reloadData(String catName){

        // TODO: need to refresh the list after a list is deleted
        Log.e(TAG, "reload the list after removal of the item!");
        Uri queryURI = ExerciseContract.ExerciseEntry.CONTENT_URI_FAV;
                /* Sort order: Ascending by exercise id */
        String favSortOrder = ExerciseContract.ExerciseEntry.EXERCISE_ID + " ASC";
        String selectionByCategoryName = ExerciseContract.ExerciseEntry.CATEGORY + " = ?";
        Cursor newCursor = getActivity().getContentResolver().query(queryURI, ExerciseSwipeViewActivity.FAV_EXERCISE_PROJECTION,selectionByCategoryName, new String[]{catName},favSortOrder);
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
    }

//    public void updateAllExerciseFavoriteCol(String exerciseID, ContentValues contentValues){
//        // TODO: need to refresh the list after a list is deleted
//        Log.e(TAG, "reload the list after removal of the item!");
//        Uri updateURI = ExerciseContract.ExerciseEntry.CONTENT_URI_ALL;
//                /* Sort order: Ascending by exercise id */
//        String favSortOrder = ExerciseContract.ExerciseEntry.EXERCISE_ID + " ASC";
//        String whereClause = ExerciseContract.ExerciseEntry.EXERCISE_ID + " = ?";
//        int updateRow = getActivity().getContentResolver().update(updateURI, contentValues, whereClause, new String[]{exerciseID});
//
//    }


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
        void onFragmentInteraction(Uri uri);
    }


    public void showData(boolean hasData){
        if (hasData) {
            Log.d(TAG,"show the recycler view!");
            mProgressIndicator.setVisibility(View.GONE);
            mNoDataText.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG,"hide the recycler view!");
            mProgressIndicator.setVisibility(View.GONE);
            mNoDataText.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    public void showLoading() {
        /* Then, hide the weather data */
        mRecyclerView.setVisibility(View.GONE);
        mNoDataText.setVisibility(View.GONE);
        /* Finally, show the loading indicator */
        mProgressIndicator.setVisibility(View.VISIBLE);
    }


}
