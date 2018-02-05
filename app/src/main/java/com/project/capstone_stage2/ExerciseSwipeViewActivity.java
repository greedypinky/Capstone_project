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
import com.project.capstone_stage2.util.FavExerciseListAdapter;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;

/**
 * ExerciseSwipeViewActivity
 * includes the TabView layout: All and Favorite execise
 *
 */
public class ExerciseSwipeViewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        EndPointsAsyncTask.AsyncResponse, AllExerciseFragment.OnFragmentInteractionListener {

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
    private boolean mTwoPaneMode = false;
    ExerciseDetailFragment mDetailFragment; // show this Detail Fragment on right if in 2 pane mode.

    public static String[] EXERCISE_PROJECTION = {
            ExerciseContract.ExerciseEntry.CATEGORY,
            ExerciseContract.ExerciseEntry.CATEGORY_DESC,
            ExerciseContract.ExerciseEntry.EXERCISE_ID,
            ExerciseContract.ExerciseEntry.EXERCISE_NAME,
            ExerciseContract.ExerciseEntry.EXERCISE_DESCRIPTION,
            ExerciseContract.ExerciseEntry.EXERCISE_STEPS,
            ExerciseContract.ExerciseEntry.EXERCISE_IMAGE,
            ExerciseContract.ExerciseEntry.EXERCISE_VIDEO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_swipe_view);

        loaderCallbacks =  ExerciseSwipeViewActivity.this;
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.swipe_view_coordinateLayout);
        mToolBarImage = (ImageView) findViewById(R.id.swipe_view_category_image);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.swipe_view_collapsingtoolbarlayout);
        mToolBar = (Toolbar) findViewById(R.id.swipe_view_toolbar);
        // set toolbar
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mDetailFragment = (ExerciseDetailFragment) fragmentManager.findFragmentById(R.id.fragment_steps_detail_land);
        if (mDetailFragment != null) {
            Log.d(TAG, "sw600dp Two pane mode is detected!");
            mTwoPaneMode = true;
            // Right pane - Show the Place holder string when no exercise is selected
            mDetailFragment.showGetStartPlaceHolderStr(true);
        } else {
            mTwoPaneMode = false;
            Log.d(TAG, "Only one pane is detected!");
            // will use the default image for now
            int defaultImage = R.drawable.exercise_default;
            //Picasso.with(getApplicationContext()).load(mExceriseCategoryImage).into(mToolBarImage);
            if (mToolBarImage!=null) {
                Picasso.with(getApplicationContext()).load(defaultImage).into(mToolBarImage);
            }
        }

        // init the ViewPager
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
                Log.d(TAG,String.format("Category name:%s CategoryDesc:%s CategoryImage:%s",
                        mExceriseCategoryName,mExceriseCategoryDesc,mExceriseCategoryImage));
            }

        }

        //example http://saulmm.github.io/mastering-coordinator
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
                switch (tabPosition)
                {
                    case "0":
                        // not only do we need to load the data but also need to get the fragment instance?
                        if (mFragment1 == null) {

                            Log.d(TAG,"when tab again, the fragment1 is null!");
                            mFragment1 = (AllExerciseFragment) mAdapterViewPager.getItem(0);
                        }
                        getSupportLoaderManager().initLoader(ALL_EXERCISE_DB_DATA_LOADER_ID,null, loaderCallbacks);

                        break;
                    case "1":
                        // query the favorite
                        if (mFragment2 == null) {

                            Log.d(TAG,"when tab again, the fragment2 is null!");
                            mFragment2 = (FavoriteExerciseFragment) mAdapterViewPager.getItem(1);
                        }

                        getSupportLoaderManager().initLoader(FAVORITE_EXERCISE_DB_DATA_LOADER_ID,null, loaderCallbacks);
                        break;
                    default:
                        Log.d(TAG,"invalid tab position!");

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        // https://www.grokkingandroid.com/using-loaders-in-android/
        //loaderCallbacks = ExerciseSwipeViewActivity.this;
        //getSupportLoaderManager().initLoader(LOADER_ID, null, loaderCallbacks);

        // get the fragment that includes the ViewPager for AllExercise and FavoriteExercise
        // mFragmentSwipeView = fragmentManager.findFragmentById(R.id.swipe_view_fragment);


        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // TODO: add back the data initialization from web to the DB

        //ExerciseDataSyncTask.initialize(this);

        // TODO: 1) call the EndPoint to get back the JSON String!
        // getResponseFromEndPoint(true);

        // String response = "";
        // Log.d(TAG,"Google Endpoint API response:-" + response);

        // TODO: get the JSON data, then starts the Sync task

        Log.d(TAG,"onCreate() initLoader to get the DB Cursor for all exercises");
        getSupportLoaderManager().initLoader(ALL_EXERCISE_DB_DATA_LOADER_ID, null, loaderCallbacks);
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
    public CursorLoader onCreateLoader(int loaderId, Bundle args) {

        Log.d(TAG,"onCreateLoader:-" + loaderId);
        switch (loaderId) {
            case ALL_EXERCISE_DB_DATA_LOADER_ID:
                CursorLoader loader = null;
                /* URI for all rows of all exercise data in table */
                Uri queryUri = ExerciseContract.ExerciseEntry.CONTENT_URI_ALL;
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

                loader = new CursorLoader(this,
                        queryUri,
                        EXERCISE_PROJECTION,
                        selectionByCategoryName,
                        new String[] {mExceriseCategoryName},
                        sortOrder);

                // Get the data from here instead of the onCreate method
                // getResponseFromEndPoint(true);
//
//                loader = new CursorLoader(this,
//                        queryUri,
//                        EXERCISE_PROJECTION,
//                        null,
//                        null,
//                        sortOrder);
                Log.d(TAG,"loader:-" + loader);
                return loader;
            case FAVORITE_EXERCISE_DB_DATA_LOADER_ID:
                CursorLoader loader2 = null;
                Uri queryUri2 = ExerciseContract.ExerciseEntry.CONTENT_URI_FAV;
                /* Sort order: Ascending by exercise id */
                String favSortOrder = ExerciseContract.ExerciseEntry.EXERCISE_ID + " ASC";
                String selectionByCategoryName2 = ExerciseContract.ExerciseEntry.CATEGORY + "=?";
//                loader2 = new CursorLoader(this,
//                        queryUri2,
//                        EXERCISE_PROJECTION,
//                        selectionByCategoryName2,
//                        new String[] {mExceriseCategoryName},
//                        favSortOrder);
                loader2 = new CursorLoader(this,
                        queryUri2,
                        EXERCISE_PROJECTION,
                        null,
                        null,favSortOrder);
                Log.d(TAG, "loader2 Get uri:- " + loader2.getUri() );
                return loader2;
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }
    // Loader's Callback method
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int loaderID = loader.getId();
        Log.d(TAG,"onLoadFinished by loader ID:-" + loaderID);
        if (data != null) {
            Log.e(TAG,"onLoadFinished - I got the data!");
            mDataCursor = data;
            Log.d(TAG, "onLoadFinished loader Get uri:- " + ((CursorLoader)loader).getUri() );
            mDataCursor.moveToFirst();
            if (loaderID == ALL_EXERCISE_DB_DATA_LOADER_ID) {

                Log.d(TAG,"All items in the cursor:" + mDataCursor.getCount());
                data.moveToFirst();
                //mFragment1.updateAdapterData(mDataCursor);
                // TODO:- at this point sometimes Fragment is null
                if (mFragment1 == null) {
                    Log.d(TAG,"when tab again, the fragment1 is null!");
                    mFragment1 = (AllExerciseFragment) mAdapterViewPager.getItem(0);
                }
                Log.d(TAG,"onLoadFinished:Update Adapter for All Fragment if mFragment1 is not null!");
                mFragment1.updateAdapterData(data);
                mFragment1.setPaneMode(mTwoPaneMode);

            } else if (loaderID == FAVORITE_EXERCISE_DB_DATA_LOADER_ID) {
                Log.d(TAG,"Update Adapter for Favorite Fragment");
                Log.d(TAG,"favorite items in the cursor:" + mDataCursor.getCount());
                // mFragment2.updateAdapterData(mDataCursor);
                data.moveToFirst();
                if (mFragment2 == null) {
                    Log.d(TAG,"when tab again, the fragment2 is null!");
                    mFragment2 = (FavoriteExerciseFragment) mAdapterViewPager.getItem(1);
                }
                Log.d(TAG,"onLoadFinish - Favorite case what is the count??" + data.getCount());
                mFragment2.updateAdapterData(data);
                mFragment2.setPaneMode(mTwoPaneMode);
            }

//            mForecastAdapter.swapCursor(data);
//            if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
//            mRecyclerView.smoothScrollToPosition(mPosition);
//            if (data.getCount() != 0) showWeatherDataView();
        } else {
            Log.e(TAG,"onLoadFinished - No Data!!");
        }

    }
    // Loader's Callback method
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        /*
         * Since this Loader's data is now invalid, we need to clear the Adapter that is
         * displaying the data.
         */

        mDataCursor = null;
        int loaderID = loader.getId();
        Log.d(TAG, "onLoadFinished by loader ID:-" + loaderID);

        if (loaderID == ALL_EXERCISE_DB_DATA_LOADER_ID) {
            Log.d(TAG, "AllExerciseFragment : Set cursor to null");
            mFragment1.resetAdapterData();
        } else {
            Log.d(TAG, "FavoriteExerciseFragment : Set cursor to null");
            mFragment2.resetAdapterData();
        }

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
//        loaderCallbacks =  ExerciseSwipeViewActivity.this;
//        Log.d(TAG,"initLoader to get the DB Cursor for all exercises");
//        getSupportLoaderManager().initLoader(ALL_EXERCISE_DB_DATA_LOADER_ID, null, loaderCallbacks);

    }

    // Fragment callback to pass back the exercise details to start the video
    @Override
    public void twoPaneModeOnClick(String exerciseID, String steps, String videoURL) {
        Log.d(TAG, "twoPaneModeOnClick - will call the  mDetailFragment.setFragmentData");
        Bundle bundle = new Bundle();
        bundle.putString(ExerciseDetailActivity.EXERCISE_KEY, exerciseID);
        bundle.putString(ExerciseDetailActivity.EXERCISE_STEPS, steps);
        bundle.putString(ExerciseDetailActivity.EXERCISE_VIDEO_URL, videoURL);
        if (mDetailFragment!=null) {

            mDetailFragment.setFragmentData(bundle);
            mDetailFragment.showVideo(true);
        }

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
                    mFragment1 = AllExerciseFragment.newInstance(0, getString(R.string.all_exercise), mTwoPaneMode);
                    // TODO : assign the result to mDataCursor
                    //fragment1.updateAdapterData(mDataCursor);
                    return mFragment1;
                case 1: // Fragment # 1 - This will show second Fragment different title
                    mFragment2 = FavoriteExerciseFragment.newInstance(1, getString(R.string.favorite_exercise), mTwoPaneMode);
                    // TODO : assign the result to mDataCursor
                    //fragment2.updateAdapterData(mDataCursor);
                    return mFragment2;

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
