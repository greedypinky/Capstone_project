package com.project.capstone_stage2;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.project.capstone_stage2.R;
import com.project.capstone_stage2.sync.ExerciseDataSyncTask;
import com.project.capstone_stage2.util.CategoryListAdapter;
import com.project.capstone_stage2.util.EndPointsAsyncTask;

import java.io.InputStream;

// Widget Example
// https://github.com/udacity/AdvancedAndroid_MyGarden
// image resource
// çsapling-plant-growing-seedling-154734/ https://pixabay.com/en/cactus-cacti-plant-thorns-spiky-152378/ https://pixabay.com/en/the-background-background-design-352165/

/**
1. Add the google cloud module from Android Studio : but where is it??
2. go to Google Developer Console to create a new project
 https://console.developers.google.com/
 use marukotest888 account to create the project
 capstoneproject-189106
 **/

// Main activity includes the CardView Layout for the activity
public class MainActivity extends AppCompatActivity implements CategoryListAdapter.CardViewOnClickListener,EndPointsAsyncTask.AsyncResponse {


    // App ID: ca-app-pub-3160158119336562~7703910721
    /* Follow the SDK integration guide. Specify ad type, size, and placement when you integrate the code.
     App ID: ca-app-pub-3160158119336562~7703910721
     Ad unit ID: ca-app-pub-3160158119336562/5237884703
    */
    // ca-app-pub-3160158119336562~4874289441
    private static final String AD_MOB_APP_ID = "ca-app-pub-3160158119336562~4874289441";
    private static final String AD_MOB_UNIT_ID = "ca-app-pub-3160158119336562/6706245862";
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView = null;
    private CategoryListAdapter mListAdapter = null;
    private String mEXERCISE_DATA_FROM_ENDPOINT = null;
    private InterstitialAd mInterstitial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.category_recycler_view);
        mListAdapter = new CategoryListAdapter(this);
        // set card adapter
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        boolean useEndPoint = true;
        // This the ads view
        AdView mAdView = (AdView) findViewById(R.id.adView);

        getResponseFromEndPoint(useEndPoint);
    }

    // Implement the callback method so that when clicking on ViewHolder Item, it will handle the navigation properly.
    @Override
    public void onClickCategory(CategoryListAdapter.ExerciseCategory category) {
        // before showing the Exercise, lets show the ads here
        showAds();

        // TODO: please add back the Intent and StartActivity with the intent!!
        Toast.makeText(this,"Start activity for " + category.getCategoryName(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this,ExerciseSwipeViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name",category.getCategoryName());
        bundle.putString("desc",category.getCategoryDesc());
        bundle.putInt("image", category.getImage());
        intent.putExtras(bundle);
        startActivity(intent);
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

            //getJSONFromAsset();
        }
    }

    @Override
    public void processFinish(String result) {

        Log.d(TAG,"Google Endpoint API response:-" + result);
        if("Connection refused".equals(result)) {
            // use local mock json data from the Assets folder
            //mEXERCISE_DATA_FROM_ENDPOINT = getJSONFromAsset();
        } else {
            mEXERCISE_DATA_FROM_ENDPOINT = result;
        }

        // TODO: 2) Initialize the database by IntentService
        Log.d(TAG,"Sync DB data by IntentService");
        // ExerciseDataSyncTask.startImmediateSync(this, EXERCISE_DATA_FROM_ENDP
        ExerciseDataSyncTask.initialize(this, mEXERCISE_DATA_FROM_ENDPOINT);


    }

    // at least it works when click on back
    private void showAds() {
        Log.d(TAG,"Show the TestAds from AdMob");
        // Only show when it is a FREE application
        if (mInterstitial!=null && mInterstitial.isLoaded()) {
            mInterstitial.show();
        } else {
            initAds();
            if ( mInterstitial!=null && mInterstitial.isLoaded()) {
                mInterstitial.show();
            }
        }
    }

    private void initAds () {

        // initialize Mobile Ads SDK with the AdMob App ID
        MobileAds.initialize(this, AD_MOB_APP_ID);
        mInterstitial = new InterstitialAd(this);
        mInterstitial.setAdUnitId(AD_MOB_UNIT_ID); // TODO: replace ID please

        // AdView mAdView = (AdView) root.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        // mAdView.loadAd(adRequest);
        mInterstitial.loadAd(adRequest);
    }

}
