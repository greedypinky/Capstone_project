package com.project.capstone_stage2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.capstone_stage2.R;
import com.project.capstone_stage2.pref.ExercisePreferenceActivity;
import com.project.capstone_stage2.pref.ExercisePreferenceFragment;
import com.project.capstone_stage2.sync.ExerciseDataSyncTask;
import com.project.capstone_stage2.sync.ExerciseSyncIntentService;
import com.project.capstone_stage2.util.CategoryListAdapter;
import com.project.capstone_stage2.util.EndPointsAsyncTask;
import com.project.capstone_stage2.util.Exercise;
import com.project.capstone_stage2.util.NetworkUtil;
import com.project.capstone_stage2.reminders.ReminderIntentService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

// Widget Example
// https://github.com/udacity/AdvancedAndroid_MyGarden
// image resource
// çsapling-plant-growing-seedling-154734/ https://pixabay.com/en/cactus-cacti-plant-thorns-spiky-152378/ https://pixabay.com/en/the-background-background-design-352165/
// https://developer.android.com/training/keyboard-input/navigation.html

/**
 * 1. Add the google cloud module from Android Studio : but where is it??
 * 2. go to Google Developer Console to create a new project
 * https://console.developers.google.com/
 * use marukotest888 account to create the project
 * capstoneproject-189106
 **/

// Main activity includes the CardView Layout for the activity
public class MainActivity extends AppCompatActivity implements CategoryListAdapter.CardViewOnClickListener, EndPointsAsyncTask.AsyncResponse, LoaderManager.LoaderCallbacks {


    // App ID: ca-app-pub-3160158119336562~7703910721
    /* Follow the SDK integration guide. Specify ad type, size, and placement when you integrate the code.
     App ID: ca-app-pub-3160158119336562~7703910721
     Ad unit ID: ca-app-pub-3160158119336562/5237884703
    */
    // ca-app-pub-3160158119336562~4874289441
    private static final String AD_MOB_APP_ID = "ca-app-pub-3160158119336562~4874289441";
    private static final String AD_MOB_UNIT_ID = "ca-app-pub-3160158119336562/6706245862";
    private static final String KEY_LOADED_BY_ENDPOINT = "endpoint";
    private static final String KEY_LOADED_BY_FIREBASE_REALTIME_DB = "firebaserealtimedb";
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView = null;
    private CategoryListAdapter mListAdapter = null;
    private String mEXERCISE_DATA_FROM_ENDPOINT = null;
    private ProgressBar mProgressBar = null;
    private InterstitialAd mInterstitial;
    private Tracker mTracker; // https://developers.google.com/analytics/devguides/collection/android/v4/
    private static final String PREF_GETENDPOINT = "getendpoint";
    private static String mEndPointResponse = "";
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference exercisesRef = mDatabase.getReference().child("exercises");
    // References - https://firebase.google.com/docs/reference/rest/database/
    // API Usage - You can use any Firebase Database URL as a REST endpoint. All you need to do is append .json to the end of the URL and send a request from your favorite HTTPS client.
    // curl 'https://[PROJECT_ID].firebaseio.com/users/jack/name.json'
    // curl 'https://naotoexercise.firebaseio.com/exercises.json'
    private static URL mFirebaseDbUrl = null;
    private String mExerciseJson = "";
    private final static int LOADER_EXERCISE_FROM_FIREBASEDB_ID = 100;
    private LoaderManager.LoaderCallbacks<String> mLoaderManagerCallbacks = null;
    private boolean isDataLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "=============================onCreate()=============================");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            mFirebaseDbUrl = new URL(getString(R.string.firebaseproject) + getString(R.string.firebaseDbRefJson));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

        exercisesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               // Exercise addedExercise = dataSnapshot.getValue(Exercise.class);
                Log.d(TAG, "onChildAdded:New Exercise is added!");
                Log.d(TAG, "DataSnapshot:" + dataSnapshot.toString());

               // TODO: need to update the adapter as well
                /*


                 DataSnapshot:DataSnapshot { key = 4, value = {image=http://atlasonlinefitness.com/wp-content/uploads/2018/03/nao-push-01.jpg, name=Standing Dumbbell Press, category description=UpperBody Push Exercise, description=Level: Beginner | Body Parts: Shoulders | Equipment: Dumbbell, id=5, video=WjP7HwAq_vE, category=Push, steps={0={stepDescription=Step1:Standing with your feet shoulder width apart, take a dumbbell in each hand. Raise the dumbbells to head height, the elbows out and about 90 degrees. This will be your starting position.}, 1={stepDescription=Step2:Maintaining strict technique with no leg drive or leaning back, extend through the elbow to raise the weights together directly above your head.}, 2={stepDescription=Step3:Pause, and slowly return the weight to the starting position}}} }


                 */
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Log.d(TAG, "onChildChanged:Exercise is changed!");
                Log.d(TAG, "DataSnapshot:" + dataSnapshot.toString());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildChanged:Exercise is removed!");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildMoved!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled!");
            }
        });


        // https://developers.google.com/analytics/devguides/collection/android/v4/?hl=ja
        // https://github.com/googlesamples/google-services/blob/master/android/analytics/app/src/main/java/com/google/samples/quickstart/analytics/MainActivity.java#L72-L74
        // Obtain the shared Tracker instance.
        Log.d(TAG, "onCreate:- getApplication:" + getApplication().getApplicationInfo());
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        if (mTracker != null) {
            mTracker.setScreenName("Activity:" + TAG);
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }

        Log.d(TAG, "onCreate:- get Analytics Tracker:" + mTracker);

        mRecyclerView = (RecyclerView) findViewById(R.id.category_recycler_view);
        mListAdapter = new CategoryListAdapter(this, this.getApplicationContext());
        // set card adapter
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mProgressBar = (ProgressBar) findViewById(R.id.main_loading_indicator);
        mLoaderManagerCallbacks = MainActivity.this;

        boolean useEndPoint = true;
        // This the ads view
        AdView mAdView = (AdView) findViewById(R.id.adView);

        // check the network connectivity
        if (!NetworkUtil.isNetworkConnected(this)) {
            Snackbar noNetworkSnackBar = NetworkUtil.makeSnackbar(getRootView(), getString(R.string.no_network_connection), true);
            noNetworkSnackBar.show();
        } else {
            showProgressBar(true);

            // Lets check the Preferences if we get data from the Endpoint before?
            // getResponseFromEndPoint(useEndPoint);
            if (savedInstanceState != null && savedInstanceState.containsKey(KEY_LOADED_BY_FIREBASE_REALTIME_DB)) {
                if (savedInstanceState.getBoolean(KEY_LOADED_BY_FIREBASE_REALTIME_DB)) {
                   Log.d(TAG, "Exercise Data is retrieved once from firebase realtime db already!");
                }
            } else {
                getSupportLoaderManager().initLoader(LOADER_EXERCISE_FROM_FIREBASEDB_ID,null, mLoaderManagerCallbacks);
            }

        }
    }


    private void showProgressBar(boolean showProgress) {
        if (showProgress) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }


    private View getRootView() {
        final ViewGroup contentViewGroup = (ViewGroup) findViewById(android.R.id.content);
        View rootView = null;

        if (contentViewGroup != null)
            rootView = contentViewGroup.getChildAt(0);

        if (rootView == null)
            rootView = getWindow().getDecorView().getRootView();

        return rootView;
    }

    // Implement the callback method so that when clicking on ViewHolder Item, it will handle the navigation properly.
    @Override
    public void onClickCategory(CategoryListAdapter.ExerciseCategory category) {

        // https://github.com/googlesamples/google-services/blob/master/android/analytics/app/src/main/java/com/google/samples/quickstart/analytics/MainActivity.java#L153-L155
        // add analytics code to check which categories people tends to click on
        // [START]
        /*
        Log.i(TAG, "Clicks on category: " + category);
        mTracker.setScreenName("Category screen: " + category);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        */
        // [END]

        // before showing the Exercise, lets show the ads here
        Log.d(TAG, "onClickCategory --> Call ShowAds() to show the Ads");
        showAds();

        // TODO: please add back the Intent and StartActivity with the intent!!
        Intent intent = new Intent(this, ExerciseSwipeViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", category.getCategoryName());
        bundle.putString("desc", category.getCategoryDesc());
        bundle.putInt("image", category.getImage());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void getResponseFromEndPoint(boolean useEndPoint) {
         /*
            Introduce a project dependency between your Java library and your GCE module,
            and modify the GCE starter code to pull data from Java library.
            Create an Async task to retrieve data from endpoint API.
            Make the button kick off a task to retrieve a joke,
          */

        // first time to get the data from EndPoint
        Log.d(TAG, "onCreate()-Get Data from EndPoint!");
        new EndPointsAsyncTask(this).execute(new Pair<Context, String>(this, "Manfred"));

    }

    // Call back method to set the EndPoint API result from the Async Task
    @Override
    public void processFinish(String result) {
        Log.d(TAG, "Google Endpoint API response:-" + result);
        if ("Connection refused".equals(result)) {
            Log.e(TAG, "Google API EndPoint connection failed!");
        } else {
            mEXERCISE_DATA_FROM_ENDPOINT = result;
            // TODO: 2) Initialize the database by IntentService
            Log.d(TAG, "Get back JSON data from the Google BackEnd API-Sync DB data by IntentService");
            // ExerciseDataSyncTask.startImmediateSync(this, EXERCISE_DATA_FROM_ENDP
            // This will trigger the IntentService to update the JSON data to the DB
            ExerciseDataSyncTask.initialize(this, mEXERCISE_DATA_FROM_ENDPOINT);
            mEndPointResponse = result;
            isDataLoaded = true;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showProgressBar(false);
            }
        }, 1000);
    }

    // at least it works when click on back
    private void showAds() {
        Log.d(TAG, "Show the TestAds from AdMob");
        // Only show when it is a FREE application
        if (mInterstitial != null && mInterstitial.isLoaded()) {
            Log.d(TAG, "showAds > mInterstitial.show()");
            mInterstitial.show();
        } else {
            // init the Ads
            initAds();
            if (mInterstitial != null && mInterstitial.isLoaded()) {
                Log.d(TAG, "showAds > mInterstitial.show()");
                mInterstitial.show();
            }
        }
    }

    // TODO: add the ads
    // initialize the apps
    private void initAds() {
        // initialize Mobile Ads SDK with the AdMob App ID
        Log.d(TAG, "initAds: initialize Mobile Ads SDK with the AdMob App ID");
        MobileAds.initialize(this, AD_MOB_APP_ID);
        mInterstitial = new InterstitialAd(this);
        mInterstitial.setAdUnitId(AD_MOB_UNIT_ID); // TODO: replace valid ads ID please

        // AdView mAdView = (AdView) root.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitial.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Navigate to the Preference activity
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, ExercisePreferenceActivity.class);
            startActivity(intent);
            return true;
        }
        // for notification debugging
        /*
        if (item.getItemId() == R.id.action_notification) {
            // Intent intent = new Intent(this, ExercisePreferenceActivity.class);
            Intent intent = new Intent(this, ReminderIntentService.class);
            intent.setAction(ReminderIntentService.ACTION_SEND_REMINDER);
            startService(intent);
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }

    private static class MyAsyncTaskLoader extends AsyncTaskLoader {

        public MyAsyncTaskLoader(final Context context) {
            super(context);

        }

        @Nullable
        @Override
        public Object loadInBackground() {
            String response = null;
            try {
                Log.d(TAG, "loadInBackground is called!");
                response = NetworkUtil.getResponseFromHttp(mFirebaseDbUrl);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                Log.e(TAG, ioe.getMessage());
            }
            return response;
        }

        @Override
        protected boolean onCancelLoad() {
            return super.onCancelLoad();
        }

        @Override
        protected void onStartLoading() {

            forceLoad(); // loadInBackground

        }

    }

    // LoaderManager's callback methods
    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        Loader loader = null;
        if (id == LOADER_EXERCISE_FROM_FIREBASEDB_ID) {
            // TODO: define a static async task because....
            // The reason behind is that non-static inner classes hold a direct reference to outer class. When you declare an inner AsyncTask class,
            // it may live longer than the container class which is an activity now.
            loader = new MyAsyncTaskLoader(this);

        }
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {
      if (loader instanceof MyAsyncTaskLoader) {
          Log.d(TAG, "=========================onLoadFinished==========================");
          Log.d(TAG, "Get back JSON data from Firebase RealTime Database URL!");

          if (data != null) {
              mEXERCISE_DATA_FROM_ENDPOINT = (String) data;
              Log.d(TAG, "JSON data ---> " + mEXERCISE_DATA_FROM_ENDPOINT);
              // This will trigger the IntentService to update the JSON data to the DB if DB has no data
              Log.d(TAG, "Trigger immediate sync up data to local DB!");
              ExerciseDataSyncTask.initialize(this, mEXERCISE_DATA_FROM_ENDPOINT);
              isDataLoaded = true;
          }
          new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                  showProgressBar(false);
              }
          }, 1000);
      }

    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_LOADED_BY_FIREBASE_REALTIME_DB, isDataLoaded);
    }
}
