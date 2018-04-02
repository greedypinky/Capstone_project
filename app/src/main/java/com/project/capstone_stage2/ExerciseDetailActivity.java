package com.project.capstone_stage2;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.project.capstone_stage2.util.ExerciseUtil;
import com.project.capstone_stage2.util.NetworkUtil;

public class ExerciseDetailActivity extends AppCompatActivity {

    public static final String TAG = ExerciseDetailActivity.class.getSimpleName();
    public static final String EXERCISE_CATEGORY = "exerciseCategory";
    public static final String EXERCISE_NAME = "exerciseName";
    public static final String EXERCISE_KEY = "exerciseID";
    public static final String EXERCISE_STEPS = "exerciseSteps";
    public static final String EXERCISE_VIDEO_URL = "exerciseVideoURL";

    private FloatingActionButton mFAB = null;
    private String mExerciseCategory = null;
    private String mExerciseID = null;
    private String mExerciseName = null;
    private String mExerciseSteps = null;
    private String mExerciseVideoUrl = null;
    ExerciseDetailFragment mDetailFragment;
    private CoordinatorLayout mCoordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.detail_coordinateLayout);

        // TODO: need to implement the detail view
        // Detail view is defined in a fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        mDetailFragment = (ExerciseDetailFragment)fragmentManager.findFragmentById(R.id.fragment_steps_detail);
        // retrieve data from the intent
        Intent intent = getIntent();
        // Add this check to avoid exception
        if (intent != null && intent.getExtras() != null) {
            mDetailFragment.setFragmentData(intent.getExtras());
            //mVideoStepFragment.setStepData(mCurrentStep);
            // TODO: Set the ExerciseID !
            mExerciseCategory = intent.getExtras().getString(EXERCISE_CATEGORY);
            mExerciseID = intent.getExtras().getString(EXERCISE_KEY);
            mExerciseName = intent.getExtras().getString(EXERCISE_NAME);
            mExerciseSteps = intent.getExtras().getString(EXERCISE_STEPS);
            mExerciseVideoUrl = intent.getExtras().getString(EXERCISE_VIDEO_URL);
            Log.d(TAG, "From intent-Get the current ExerciseID");
            // Alternative way is to test the local mp4 files stored under the assets/raw
            // String localUrlPath="android.resource://"+getPackageName()+"/"+R.raw.ur file name;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mFAB = (FloatingActionButton) findViewById(R.id.detail_fab_btn);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: add the share action here
                if (!NetworkUtil.isNetworkConnected(ExerciseDetailActivity.this)) {
//                    Snackbar sb = Snackbar.make(mCoordinatorLayout, getString(R.string.no_network), Snackbar.LENGTH_LONG);
//                    View sbView = sb.getView();
//                    sbView.setElevation(getResources().getDimension(R.dimen.sb_elevation));
//                    sb.show();

                    Snackbar noNetworkSnackBar = NetworkUtil.makeSnackbar(mCoordinatorLayout, getString(R.string.no_network_connection), true);
                    noNetworkSnackBar.getView().setElevation(getResources().getDimension(R.dimen.sb_elevation));
                    noNetworkSnackBar.show();

                } else {
                    ExerciseUtil.onShareClick(mExerciseCategory, mExerciseName, mExerciseSteps, mExerciseVideoUrl, getApplicationContext(), mDetailFragment);
                }
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       //  outState.putParcelable(RECIPE_KEY, mRecipe);
        // when in the landscape mode
//        if(mTwoPane) {
//            outState.putLong(VIDEO_POSITION_KEY, mVideoPosition);
//            outState.putInt(CURRENT_WINDOW_POSITION_KEY, mCurrentwindowIndex);
//        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        // mDetailFragment.setData(mExerciseVideoUrl);
        // mDetailFragment.setData(mExerciseVideoUrl);
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


}
