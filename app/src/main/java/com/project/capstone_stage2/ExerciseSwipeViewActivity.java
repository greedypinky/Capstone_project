package com.project.capstone_stage2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.capstone_stage2.dbUtility.ExerciseContract;
import com.project.capstone_stage2.sync.ExerciseDataSyncTask;
import com.project.capstone_stage2.util.CategoryListAdapter;
import com.project.capstone_stage2.util.EndPointsAsyncTask;
import com.project.capstone_stage2.util.ExerciseListAdapter;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;

/**
 * ExerciseSwipeViewActivity
 * includes the TabView layout: All and Favorite execise
 */
public class ExerciseSwipeViewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, EndPointsAsyncTask.AsyncResponse {

    private static final String TAG = ExerciseSwipeViewActivity.class.getSimpleName();
    private static final int ALL_EXERCISE_DB_DATA_LOADER_ID = 1000;
    private static final int FAVORITE_EXERCISE_DB_DATA_LOADER_ID = 2000;
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
    private final static int FRAGMENT_NUM = 2;
    private static Cursor mDataCursor;

    private String mEXERCISE_DATA_FROM_ENDPOINT = null;

    private AllExerciseFragment mFragment1;
    private FavoriteExerciseFragment mFragment2;

    public static String[] EXERCISE_PROJECTION = {
             ExerciseContract.ExerciseEntry.CATEGORY,
                    ExerciseContract.ExerciseEntry.CATEGORY_DESC,
            ExerciseContract.ExerciseEntry.EXERCISE_ID,
            ExerciseContract.ExerciseEntry.EXERCISE_NAME,
            ExerciseContract.ExerciseEntry.EXERCISE_DESCRIPTION,
            ExerciseContract.ExerciseEntry.EXERCISE_IMAGE,
            ExerciseContract.ExerciseEntry.EXERCISE_VIDEO
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_swipe_view);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.swipe_view_coordinateLayout);
        //mToolBarImage = (ImageView) findViewById(R.id.swipe_view_category_image);
        //mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.swipe_view_collapsingtoolbarlayout);
        mToolBar = (Toolbar) findViewById(R.id.swipe_view_toolbar);
        // set toolbar
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager = findViewById(R.id.exercise_pager);
        mAdapterViewPager = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapterViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                // TODO: how can we update the data ?
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // need to update the view page ?
                String tabPosition = String.valueOf(tab.getPosition());
                String tabText = (String) tab.getText();
                CharSequence debugmsg = "tab position:" + tabPosition + " tab text:" + tabText;
                Toast.makeText(getApplicationContext(), debugmsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


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

        // Picasso.with(getApplicationContext()).load(mExceriseCategoryImage).into(mToolBarImage);

        // https://www.grokkingandroid.com/using-loaders-in-android/
        //loaderCallbacks = ExerciseSwipeViewActivity.this;
        //getSupportLoaderManager().initLoader(LOADER_ID, null, loaderCallbacks);

        // get the fragment that includes the ViewPager for AllExercise and FavoriteExercise
       // mFragmentSwipeView = fragmentManager.findFragmentById(R.id.swipe_view_fragment);


        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // TODO: add back the data initialization from web to the DB

        //ExerciseDataSyncTask.initialize(this);

        // TODO: 1) call the EndPoint to get back the JSON String!
        getResponseFromEndPoint(true);

        // String response = "";
        // Log.d(TAG,"Google Endpoint API response:-" + response);

        // TODO: get the JSON data, then starts the Sync task


    }

    private void getResponseFromEndPoint(boolean useEndPoint) {

        if (useEndPoint) {
            /*
               Introduce a project dependency between your Java library and your GCE module,
               and modify the GCE starter code to pull jokes from your Java library.
               Create an Async task to retrieve jokes.
               Make the button kick off a task to retrieve a joke,
               then launch the activity from your Android Library to display it.
             */
            new EndPointsAsyncTask(this).execute(new Pair<Context, String>(this, "Manfred"));


        } else {

            getJSONFromAsset();
        }
    }

    public String getJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("exercise.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    // Loader's Callback method
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        switch (loaderId) {

            case ALL_EXERCISE_DB_DATA_LOADER_ID:
                /* URI for all rows of all exercise data in table */
                Uri forecastQueryUri = ExerciseContract.ExerciseEntry.CONTENT_URI_ALL;
                /* Sort order: Ascending by exercise id */
                String sortOrder = ExerciseContract.ExerciseEntry.EXERCISE_ID + " ASC";
                /*
                 * A SELECTION in SQL declares which rows you'd like to return. In our case, we
                 * want all weather data from today onwards that is stored in our weather table.
                 * We created a handy method to do that in our WeatherEntry class.
                 */
                // TODO: Selection we need to filter by the Category Name
                // String selection = WeatherContract.WeatherEntry.getSqlSelectForTodayOnwards();
                // SQL's where clause - where category = SQUAT or PULL or PUSH
                String selectionByCategoryName = ExerciseContract.ExerciseEntry.CATEGORY + "=?";

                return new CursorLoader(this,
                        forecastQueryUri,
                        EXERCISE_PROJECTION,
                        selectionByCategoryName,
                        new String[] {mExceriseCategoryName},
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }
    // Loader's Callback method
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null) {
            int loaderID = loader.getId();
            Log.d(TAG,"onLoadFinished by loader ID:-" + loaderID);
            mDataCursor = data;
            if (loaderID == ALL_EXERCISE_DB_DATA_LOADER_ID) {
                mFragment1.updateAdapterData(data);
            } else {
                mFragment2.updateAdapterData(data);
            }

//            mForecastAdapter.swapCursor(data);
//            if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
//            mRecyclerView.smoothScrollToPosition(mPosition);
//            if (data.getCount() != 0) showWeatherDataView();
        }

    }
    // Loader's Callback method
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
 /*
         * Since this Loader's data is now invalid, we need to clear the Adapter that is
         * displaying the data.
         */
        // mForecastAdapter.swapCursor(null);
        mDataCursor = null;
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
        // TODO: DO we save which tab is on focus before

    }

    /**
     * checkNetworkConnectivity
     * @return
     */
    private boolean checkNetworkConnectivity() {
        // https://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null) {
            activeNetwork.isConnectedOrConnecting();
            return true;
        } else {
            return false;
        }
    }

    // EndPointsAsyncTask callback to get the result from EndPoint
    @Override
    public void processFinish(String result) {

        if("Connection refused".equals(result)) {
            mEXERCISE_DATA_FROM_ENDPOINT  = getJSONFromAsset();
        } else {
            mEXERCISE_DATA_FROM_ENDPOINT = result;
        }

        Log.d(TAG,"Google Endpoint API response:-" + result);

        // TODO: 2) Initialize the database by IntentService
        Log.d(TAG,"Sync DB data by IntentService");
        // ExerciseDataSyncTask.startImmediateSync(this, EXERCISE_DATA_FROM_ENDPOINT);
        ExerciseDataSyncTask.initialize(this, mEXERCISE_DATA_FROM_ENDPOINT);

        // TODO: 3) Call the loader manager to load the cursor and pass the cursor to view pager ??
        // mViewPager.getAdapter().
        loaderCallbacks =  ExerciseSwipeViewActivity.this;
        Log.d(TAG,"initLoader to get the DB Cursor for all exercises");
        getSupportLoaderManager().initLoader(ALL_EXERCISE_DB_DATA_LOADER_ID, null, loaderCallbacks);
    }

    // swipe across a collection of Fragment objects
    // https://guides.codepath.com/android/Google-Play-Style-Tabs-using-TabLayout
    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {


        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return FRAGMENT_NUM;
            // return 2;
        }

        @Override
        public void startUpdate(ViewGroup container) {
            super.startUpdate(container);
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
                    AllExerciseFragment fragment1 = AllExerciseFragment.newInstance(0, "All Exercise Page # 1");
                    // TODO : assign the result to mDataCursor
                    //fragment1.updateAdapterData(mDataCursor);
                    return fragment1;
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    FavoriteExerciseFragment fragment2 = FavoriteExerciseFragment.newInstance(1, "Favorite Exercise Page # 2");
                    // TODO : assign the result to mDataCursor
                    //fragment2.updateAdapterData(mDataCursor);
                    return fragment2;

                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {

            if (position == 0) {
                return getString(R.string.all_exercise);
            } else if (position == 1) {
                return getString(R.string.favorite_exercise);
            } else {

                return null;
            }
        }
    }
}
