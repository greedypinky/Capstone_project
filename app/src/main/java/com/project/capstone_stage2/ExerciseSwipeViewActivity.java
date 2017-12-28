package com.project.capstone_stage2;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.project.capstone_stage2.util.CategoryListAdapter;
import com.project.capstone_stage2.util.ExerciseListAdapter;
import com.squareup.picasso.Picasso;

/**
 * ExerciseSwipeViewActivity
 * includes the TabView layout: All and Favorite execise
 */
public class ExerciseSwipeViewActivity extends AppCompatActivity implements ExerciseListAdapter.ItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = ExerciseSwipeViewActivity.class.getSimpleName();
    private static final int LOADER_ID = 1000;
    LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;
    private static String mExceriseCategoryName = "";
    private static String mExceriseCategoryDesc = "";
    private static int mExceriseCategoryImage = 0;
    private Fragment mFragmentSwipeView;
    public static final String CATEGORY_NAME_KEY = "name";
    public static final String CATEGORY_DESC_KEY = "desc";
    public static final String CATEGORY_IMAGE_KEY = "image";

    // View Objects
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolBar;
    private ImageView mToolBarImage;
    //private FloatingActionButton mShareFAB;
    private String mImageURL;
    private Bitmap mBitmap;
    private CoordinatorLayout mCoordinatorLayout;

    private ViewPager mViewPager;
    FragmentPagerAdapter mAdapterViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_swipe_view);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.swipe_view_coordinateLayout);
        mToolBarImage = (ImageView) findViewById(R.id.swipe_view_category_image);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.swipe_view_collapsingtoolbarlayout);
        mToolBar = (Toolbar) findViewById(R.id.swipe_view_toolbar);
        // set toolbar
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //mViewPager = findViewById(R.id.exercise_pager);
        //mAdapterViewPager = new SwipeViewFragment.MyFragmentPagerAdapter(getFragmentManager());
        //mViewPager.setAdapter(mAdapterViewPager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {

            // TODO: add back implementation for SavedInstanceState for this view
            if(savedInstanceState.containsKey(CATEGORY_NAME_KEY)) {
                mExceriseCategoryName = savedInstanceState.getString(CATEGORY_NAME_KEY);
            }
            if(savedInstanceState.containsKey(CATEGORY_DESC_KEY)) {
                mExceriseCategoryDesc = savedInstanceState.getString(CATEGORY_DESC_KEY);
            }
            if(savedInstanceState.containsKey(CATEGORY_IMAGE_KEY)) {
                mExceriseCategoryImage = savedInstanceState.getInt(CATEGORY_IMAGE_KEY);
            }

        } else {
            // get information from intent
            Intent intent = getIntent();
            if (intent != null && intent.getExtras() != null) {
                 Bundle bundle = intent.getExtras();
                 mExceriseCategoryName = bundle.getString("name");
                 mExceriseCategoryDesc = bundle.getString("desc");
                 mExceriseCategoryImage = bundle.getInt("image");
            }

        }

        Picasso.with(getApplicationContext()).load(mExceriseCategoryImage).into(mToolBarImage);

        // https://www.grokkingandroid.com/using-loaders-in-android/
        //loaderCallbacks = ExerciseSwipeViewActivity.this;
        //getSupportLoaderManager().initLoader(LOADER_ID, null, loaderCallbacks);

        // get the fragment that includes the ViewPager for AllExercise and FavoriteExercise
        mFragmentSwipeView = fragmentManager.findFragmentById(R.id.swipe_view_fragment);


        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public void onClickListener() {
        // TODO: add the action to take when the view holder is clicked
    }

    // Loader's Callback method
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<Cursor>(getApplicationContext()) {
            @Override
            public Cursor loadInBackground() {
                // TODO: add the load in background login
                return null;
            }
        };
    }
    // Loader's Callback method
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data!=null) {


        }

    }
    // Loader's Callback method
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * onOptionsItemSelected - will be called when a toolbar menu is selected
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // add this back to end the current activity
            case android.R.id.home:
                Log.d(TAG, "home/up button is clicked");
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO: add back the save state
        outState.putString(CATEGORY_NAME_KEY, mExceriseCategoryName);
        outState.putString(CATEGORY_DESC_KEY, mExceriseCategoryDesc);
        outState.putInt(CATEGORY_IMAGE_KEY, mExceriseCategoryImage);

    }

    /**
     * checkNetworkConnectivity
     * @return
     */
    private boolean checkNetworkConnectivity() {
        // https://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if(activeNetwork != null) {
            activeNetwork.isConnectedOrConnecting();
            return true;
        } else {
            return false;
        }
    }
}
