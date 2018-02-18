package com.project.capstone_stage2;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.project.capstone_stage2.util.ExerciseListAdapter;
import com.project.capstone_stage2.util.FavExerciseListAdapter;

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
        if (cursor != null && !cursor.isClosed()) {
            Log.d(TAG,"Cursor is not null, updateAdapterData!");
            if (mAdapter != null) {
                mCursor = cursor;
                mAdapter.setAdapterData(mCursor);
                mHasData = true;
                showData(true);
            } else {
                Log.d(TAG,"Adapter is null! Why?");
            }
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
    public void onClickExercise() {
        Toast.makeText(getContext(), "navigate to Detail view", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onShareClick() {
        Toast.makeText(getContext(), "Share!", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onRemoveFavClick(Cursor cursor) {
        return false;
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


    public void showData(boolean hasData){
        if(hasData) {
            mProgressIndicator.setVisibility(View.GONE);
            mNoDataText.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
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

//    public void showProgress(boolean showProgress) {
//        if (showProgress) {
//            mProgressIndicator.setVisibility(View.VISIBLE);
//        } else {
//            mProgressIndicator.setVisibility(View.GONE);
//        }
//    }
}
