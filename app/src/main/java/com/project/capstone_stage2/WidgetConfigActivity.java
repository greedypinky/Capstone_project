package com.project.capstone_stage2;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.project.capstone_stage2.dbUtility.ExerciseContract;
import com.project.capstone_stage2.sync.ExerciseDataSyncTask;
import com.project.capstone_stage2.util.CategoryListAdapter;
import com.project.capstone_stage2.util.EndPointsAsyncTask;
import com.project.capstone_stage2.util.Exercise;
import com.project.capstone_stage2.util.NetworkUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;

//https://developer.android.com/guide/topics/appwidgets/index.html#Configuring
// UX
// https://stackoverflow.com/questions/27661305/material-design-suggestions-for-lists-with-avatar-text-and-icon
// https://material.io/guidelines/resources/sticker-sheets-icons.html#sticker-sheets-icons-components
public class WidgetConfigActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, EndPointsAsyncTask.AsyncResponse {

    private static String TAG = WidgetConfigActivity.class.getSimpleName();
    private int mAppWidgetId = -1;
    private Cursor mCursor = null;
    LoaderManager.LoaderCallbacks loaderCallbacks;
    private int LOADER_ID = 2000;
    private AppWidgetManager mAppWidgetManager = null;
    private RadioGroup mRadioGrp = null;
    private Button mAddWidgetButton = null;
    public static final String SELECTED_CATEGORY = "selected_category";
    public static final String EXERCISE_CURSOR = "exercise_cursor";

    private String mSelectedExceriseCategory = null;
    private String mEXERCISE_DATA_FROM_ENDPOINT = null;
    public static String NAME_KEY = "name";
    public static String DESC_KEY = "desc";
    public static String IMAGE_KEY = "image";
    public static String VIDEO_KEY = "video";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if they press the back button.
        setResult(RESULT_CANCELED);

        // get the App Widget ID from the Intent that launched the Activity:
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            // If they gave us an intent without the widget id, end the activity.
            if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                Log.e(TAG, "Invalid App widget ID!");
                finish();
            }
        }

        setContentView(R.layout.activity_widget_config);

        String[] categories = getResources().getStringArray(R.array.category_array);
        mRadioGrp = (RadioGroup) findViewById(R.id.radio_category_group);
        mAddWidgetButton = (Button) findViewById(R.id.addWidget);
        mAddWidgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < mRadioGrp.getChildCount(); i++) {
                    if (((RadioButton) mRadioGrp.getChildAt(i)).isChecked()) {
                        String selectedCategory = (String) ((RadioButton) mRadioGrp.getChildAt(i)).getText();
                        mSelectedExceriseCategory = selectedCategory;
                        Log.d(TAG, "What is the selected Category?" + mSelectedExceriseCategory);
                        break;
                    }
                }

                // TODO: get the database cursor by category ?
                // use LoaderManager to get the data in a seperate thread
                loaderCallbacks = WidgetConfigActivity.this; // this activity implements the callbacks method
                Log.d(TAG, "getSupportLoaderManager - get exercise data!");
                getSupportLoaderManager().initLoader(LOADER_ID, null, loaderCallbacks);

            }
        });

        // set the exercise categories string to the radio button text
        int i = 0;
        for (String cat : categories) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(cat);
            mRadioGrp.addView(radioButton);
            if (i == 0) {
                int id = radioButton.getId();
                mRadioGrp.check(id);
            }
            i++;
        }

        // fetch data from the FireBase RealTime db instead
//        String response = fetchDataFromFirebaseDB();
//        if (response!=null) {
//            ExerciseDataSyncTask.initialize(this, response);
//        }

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        Log.d(TAG, "onCreateLoader:" + id);
        Log.d(TAG, "Get the list from Execise Category:" + mSelectedExceriseCategory);
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
        String selectionByCategoryName = ExerciseContract.ExerciseEntry.CATEGORY + " =? ";

        loader = new CursorLoader(this,
                queryUri,
                ExerciseSwipeViewActivity.EXERCISE_PROJECTION,
                selectionByCategoryName,
                new String[]{mSelectedExceriseCategory},
                sortOrder);

        return loader;
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor data) {
        Log.d(TAG, "onLoadFinished");
        if (data!=null) {
            Log.d(TAG, "onLoadFinished: cursor count:" + data.getCount());
        }
        addAppWidget(data);

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    // https://github.com/appium/android-apidemos/blob/master/src/io/appium/android/apis/appwidget/ExampleAppWidgetConfigure.java
    private void addAppWidget(Cursor data) {
        Log.d(TAG, "addAppWidget");
        // When the configuration is complete, get an instance of the AppWidgetManager
        mAppWidgetManager = AppWidgetManager.getInstance(getApplicationContext());

        // TODO: Loop through cursor and put the info into the CategoryExercise POJO Class
        //ArrayList<Exercise> exerciseArrayList = new ArrayList<Exercise>();
        ArrayList<HashMap<String, String>> exerciseArrayList = new ArrayList<HashMap<String, String>>();

        if (data != null) {
            // data.moveToFirst();
            while (data.moveToNext()) {
                String name = data.getString(data.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_NAME));
                String desc = data.getString(data.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_DESCRIPTION));
                String imageURI = data.getString(data.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_IMAGE));
                String videoURI = data.getString(data.getColumnIndex(ExerciseContract.ExerciseEntry.EXERCISE_VIDEO));
                Log.d(TAG, String.format("name [ %s], desc [%s], imageURL [%s]", name, desc, imageURI));
                //exerciseArrayList.add(new Exercise(name,desc,imageURI));
                // exerciseArrayList.add(new Exercise(name));
                HashMap<String, String> hmap = new HashMap<String, String>();
                hmap.put(NAME_KEY, name);
                hmap.put(DESC_KEY, desc);
                hmap.put(IMAGE_KEY, imageURI);
                hmap.put(VIDEO_KEY, videoURI);
//                Log.d(TAG,"Widget - read data from cursor for name:" + name);
//                Log.d(TAG,"Widget - read data from cursor for desc:" + desc);
//                Log.d(TAG,"Widget - read data from cursor for imageurl:" + imageURI);
//                Log.d(TAG,"Widget - read data from cursor for videourl:" + videoURI);
                exerciseArrayList.add(hmap);
            }

        }

        Log.d(TAG, "addAppWidget:how many exercise ? " + exerciseArrayList.size());
        MyExerciseAppWidget.updateExerciseAppWidget(getApplicationContext(), mAppWidgetManager, mAppWidgetId, exerciseArrayList);

        Intent intent = new Intent();
        intent.putExtra(EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, intent);
        finish();

    }


    // 1. fetch JSON exercise data from EndPoint
    private void fetchDataFromEndPoint() {
        // fetch the exercise json data from the endpoint
        new EndPointsAsyncTask(this).execute(new Pair<Context, String>(this, "Manfred"));
    }

    private String fetchDataFromFirebaseDB() {
        String response = null;
        URL mFirebaseDbUrl;
        try {
            mFirebaseDbUrl = new URL(getString(R.string.firebaseproject) + getString(R.string.firebaseDbRefJson));
            Log.d(TAG, "loadInBackground is called!");
            response = NetworkUtil.getResponseFromHttp(mFirebaseDbUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }  catch (IOException ioe) {
            ioe.printStackTrace();
            Log.e(TAG, ioe.getMessage());
        }
        return response;
    }

    // implement the callback result passed by the EndPoint
    @Override
    public void processFinish(String result) {

        Log.d(TAG, "Google Endpoint API response:-" + result);
        if ("Connection refused".equals(result)) {
            // use local mock json data from the Assets folder
            //mEXERCISE_DATA_FROM_ENDPOINT = getJSONFromAsset();
            Log.d(TAG, "Connection refused! unable to get the data from the backend!");
        } else {
            mEXERCISE_DATA_FROM_ENDPOINT = result;
            // TODO: 2) Initialize the database by IntentService
            Log.d(TAG, "Sync DB data by IntentService");
            // ExerciseDataSyncTask.startImmediateSync(this, EXERCISE_DATA_FROM_ENDP
            ExerciseDataSyncTask.initialize(this, mEXERCISE_DATA_FROM_ENDPOINT);
        }

    }
}