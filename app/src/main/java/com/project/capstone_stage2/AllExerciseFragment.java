package com.project.capstone_stage2;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.capstone_stage2.util.ExerciseListAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllExerciseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllExerciseFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
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
        mRecyclerView.setAdapter(mAdapter);
        mNoDataText = (TextView) rootView.findViewById(R.id.all_execise_no_data_error_text);
        mProgressIndicator = (ProgressBar) rootView.findViewById(R.id.all_execise_loading_indicator);

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

    private void showErrorMessage() {

        if(mNoDataText != null) {

            mNoDataText.setVisibility(View.VISIBLE);
        }

        if(mRecyclerView != null) {
            mRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    private void hideErrorMessage() {

        if(mNoDataText != null) {

            mNoDataText.setVisibility(View.GONE);
        }

        if(mRecyclerView != null) {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
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
