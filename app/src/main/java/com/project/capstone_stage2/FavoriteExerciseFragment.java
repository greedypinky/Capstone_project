package com.project.capstone_stage2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.capstone_stage2.util.ExerciseListAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavoriteExerciseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavoriteExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteExerciseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PAGE = "page";
    private static final String ARG_TITLE = "page_title";

    private String title;
    private int page;
    private RecyclerView mRecyclerView;
    private TextView mNoDataText;
    private ProgressBar mProgressIndicator;
    private boolean mNoData = true;
    private ExerciseListAdapter mAdapter;


    private OnFragmentInteractionListener mListener;

    public FavoriteExerciseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteExerciseFragment newInstance(int page, String title) {
        FavoriteExerciseFragment fragment = new FavoriteExerciseFragment();
        Bundle args = new Bundle();
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
        View rootView = inflater.inflate(R.layout.fragment_favorite_exercise, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fav_execise_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mNoDataText = (TextView) rootView.findViewById(R.id.fav_execise_no_data_error_text);
        mProgressIndicator = (ProgressBar) rootView.findViewById(R.id.fav_execise_loading_indicator);

        // if no data
        if (mNoData) {

            showErrorMessage();
        }

        return rootView;
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

    private void showErrorMessage(){

        if(mNoDataText != null) {

            mNoDataText.setVisibility(View.VISIBLE);
        }

        if(mRecyclerView != null) {
            mRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    private void hideErrorMessage(){

        if(mNoDataText != null) {

            mNoDataText.setVisibility(View.GONE);
        }

        if(mRecyclerView != null) {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}
