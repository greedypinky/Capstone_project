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
import com.project.capstone_stage2.util.EndPointsAsyncTask;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;

//https://developer.android.com/guide/topics/appwidgets/index.html#Configuring
public class WidgetConfigActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,EndPointsAsyncTask.AsyncResponse {

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

                for(int i = 0; i < mRadioGrp.getChildCount() ; i++) {
                    if (((RadioButton) mRadioGrp.getChildAt(i)).isChecked()) {
                        String selectedCategory = (String) ((RadioButton) mRadioGrp.getChildAt(i)).getText();
                        mSelectedExceriseCategory = selectedCategory;
                        break;
                    }
                }

                // TODO: get the database cursor by category ?
                // use LoaderManager to get the data in a seperate thread
                loaderCallbacks = WidgetConfigActivity.this; // this activity implements the callbacks method
                Log.d(TAG, "getSupportLoaderManager - get ricipe data!");
                getSupportLoaderManager().initLoader(LOADER_ID, null, loaderCallbacks);

            }
        });

        // set the exercise categories string to the radio button text
        for (String cat:categories) {
          RadioButton radioButton = new RadioButton(this);
          radioButton.setText(cat);
          mRadioGrp.addView(radioButton);

        }

        // fetch the JSON data from the API endpoint
        fetchDataFromEndPoint();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
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
                ExerciseSwipeViewActivity.EXERCISE_PROJECTION,
                selectionByCategoryName,
                new String[] {mSelectedExceriseCategory},
                sortOrder);

        return loader;
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor data) {
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

// TODO: pass the data cursor to the updateWidget to populate the list.
     // Call BakingAppWidget.udpateAppWidget method directly to update the widget
//        BakingAppWidget.updateAppWidget(getApplicationContext(), appWidgetManager,
//                mAppWidgetId, mCursor);

        Intent startService = new Intent(WidgetConfigActivity.this,
                MyExerciseAppWidget.UpdateWidgetService.class);
        startService.putExtra(
                EXTRA_APPWIDGET_ID, mAppWidgetId);
//        startService.put(
//               EXERCISE_CURSOR, data);
        startService.setAction("FROM CONFIGURATION ACTIVITY");
        setResult(RESULT_OK, startService);
        startService(startService);

//        Intent intent = new Intent();
//        intent.putExtra(EXTRA_APPWIDGET_ID,mAppWidgetId);
//        setResult(RESULT_OK, intent);
        finish();

    }


    // 1. fetch JSON exercise data from EndPoint
    private void fetchDataFromEndPoint() {
        // fetch the exercise json data from the endpoint
        new EndPointsAsyncTask(this).execute(new Pair<Context, String>(this, "Manfred"));
    }

    // implement the callback result passed by the EndPoint
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
}