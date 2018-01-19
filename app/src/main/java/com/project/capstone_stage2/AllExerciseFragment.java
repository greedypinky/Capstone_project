package com.project.capstone_stage2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.project.capstone_stage2.dbUtility.ExerciseContract;
import com.project.capstone_stage2.util.ExerciseListAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllExerciseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllExerciseFragment extends Fragment implements ExerciseListAdapter.ExerciseItemOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    private static final String TAG = AllExerciseFragment.class.getSimpleName();
    private static final String ARG_PAGE = "page";
    private static final String ARG_TITLE = "page_title";

    private String title;
    private int page;

    private RecyclerView mRecyclerView;
    private TextView mNoDataText;
    private ProgressBar mProgressIndicator;
    private boolean mHasData = false;
    private ExerciseListAdapter mAdapter;


    private OnFragmentInteractionListener mListener;

    public AllExerciseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment AllExerciseFragment.
     */
    // TODO: Rename and change types and number of parameter

    public static AllExerciseFragment newInstance(int page, String title) {
        AllExerciseFragment fragment = new AllExerciseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_all_exercise, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.all_execise_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ExerciseListAdapter(getContext(),this);
        mRecyclerView.setAdapter(mAdapter);
        mNoDataText = (TextView) rootView.findViewById(R.id.all_execise_no_data_error_text);
        mProgressIndicator = (ProgressBar) rootView.findViewById(R.id.all_execise_loading_indicator);

        // if no data
        if (!mHasData) {

            showErrorMessage();
        }
        return rootView;
    }

    /**
     * Call by SwipeView activity to update the cursor
     * @param cursor
     */
    public void updateAdapterData(Cursor cursor) {
        // TODO: update the cursor
        // TODO: question - when do we need to close the cursor ??
        if(cursor != null) {
            mAdapter.setAdapterData(cursor);
            mHasData = true;
            hideErrorMessage();
        } else {
            mHasData = false;
            showErrorMessage();
        }
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

    // This is a callback method of ExerciseListAdapter
    @Override
    public void onClickExercise() {
        // TODO: implement how to handle when the Exercise Item is selected!
        Toast.makeText(getContext(),"navigate to Detail view", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onShareClick() {
        Toast.makeText(getContext(),"share content!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAddFavClick(Cursor cursor) {

        // https://developer.android.com/guide/topics/providers/content-provider-basics.html
        // select the row from All Exercise and add the parameter to Favorite Exercise
        int id_index = cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_ID);
        int name_index = cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_NAME);
        String exeID = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_ID));
        String exeName = cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_NAME));
        //long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);

        Toast.makeText(getContext(),"add favorite:- " + exeID + ":" + exeName, Toast.LENGTH_LONG).show();
        // Defines an object to contain the new values to insert
          ContentValues mNewValues = new ContentValues();
        mNewValues.put(ExerciseContract.ExerciseEntry.EXERCISE_ID,cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_ID)));
        mNewValues.put(ExerciseContract.ExerciseEntry.EXERCISE_NAME,cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_ID)));
        mNewValues.put(ExerciseContract.ExerciseEntry.EXERCISE_DESCRIPTION,cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_ID)));
        mNewValues.put(ExerciseContract.ExerciseEntry.EXERCISE_STEPS,cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_ID)));
        mNewValues.put(ExerciseContract.ExerciseEntry.EXERCISE_IMAGE,cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_ID)));
        mNewValues.put(ExerciseContract.ExerciseEntry.EXERCISE_VIDEO,cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_ID)));
        mNewValues.put(ExerciseContract.ExerciseEntry.CATEGORY,cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.CATEGORY)));
        mNewValues.put(ExerciseContract.ExerciseEntry.CATEGORY_DESC,cursor.getString(cursor.getColumnIndex(ExerciseContract.ExerciseEntry.CATEGORY_DESC)));

        Uri insertUri =  getActivity().getContentResolver()
                     .insert(ExerciseContract.ExerciseEntry.CONTENT_URI_FAV, mNewValues);

        Log.d(TAG,"insert favorite exercise result uri: " + insertUri);
    }


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

    private void showErrorMessage() {
        mNoDataText.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void hideErrorMessage() {
        mNoDataText.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
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
}
