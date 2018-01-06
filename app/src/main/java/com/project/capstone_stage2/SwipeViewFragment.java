package com.project.capstone_stage2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// Example >>> https://guides.codepath.com/android/viewpager-with-fragmentpageradapter
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SwipeViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SwipeViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PAGE = "page";
    private static final String ARG_TITLE = "page_title";
    private String title;
    private int page;
    private ViewPager mViewPager;
    FragmentPagerAdapter mAdapterViewPager;
    private final static int FRAGMENT_NUM = 2;

    private OnFragmentInteractionListener mListener;

    public SwipeViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            page = getArguments().getInt(ARG_PAGE);
//            title = getArguments().getString(ARG_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflates the layout for this fragment which contain the ViewPager
        View v = inflater.inflate(R.layout.fragment_swipe_view, container, false);
        mViewPager = v.findViewById(R.id.exercise_pager);
        mAdapterViewPager = new MyFragmentPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(mAdapterViewPager);
        return v;
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

    // swipe across a collection of Fragment objects
    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {

            return FRAGMENT_NUM;
        }

        @Override
        public void startUpdate(ViewGroup container) {
            super.startUpdate(container);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return super.isViewFromObject(view, object);
        }

        @Override
        public Parcelable saveState() {
            return super.saveState();
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            super.restoreState(state, loader);
        }

        @Override
        public long getItemId(int position) {


            return super.getItemId(position);
        }

        @Override
        public Fragment getItem(int position) {
            // TODO: add back how to get the Page's fragment by position
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return AllExerciseFragment.newInstance(0, "All Exercise Page # 1");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return FavoriteExerciseFragment.newInstance(1, "Favorite Exercise Page # 2");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {

            if (position == 1) {
                return getString(R.string.all_exercise);
            } else if (position == 2) {
                return getString(R.string.favorite_exercise);
            } else {

                return null;
            }
        }
    }
}
