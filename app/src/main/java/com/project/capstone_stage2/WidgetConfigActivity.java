package com.project.capstone_stage2;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

//https://developer.android.com/guide/topics/appwidgets/index.html#Configuring
public class WidgetConfigActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private static String TAG = WidgetConfigActivity.class.getSimpleName();
    private int mAppWidgetId = -1;
    private Cursor mCursor = null;
    LoaderManager.LoaderCallbacks loaderCallbacks;
    private int LOADER_ID = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if they press the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.activity_widget_config);


        // get the App Widget ID from the Intent that launched the Activity:
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            // If they gave us an intent without the widget id, end the activity.
            if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                Log.e(TAG, "Invalid App widget ID!");
                finish();
            }
        }


        // use LoaderManager to get the data in a seperate thread
        loaderCallbacks = WidgetConfigActivity.this; // this activity implements the callbacks method
        Log.d(TAG, "getSupportLoaderManager - get ricipe data!");
        getSupportLoaderManager().initLoader(LOADER_ID, null, loaderCallbacks);
//
//
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addAppWidgetWithDesiredRecipe(selectedRecipeIndex);
//                //addAppWidgetWithDesiredRecipe(pos);
//                Log.d(TAG,"OnCreate - Update Widget based on Activity configuration");
//            }
//        });

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    // https://github.com/appium/android-apidemos/blob/master/src/io/appium/android/apis/appwidget/ExampleAppWidgetConfigure.java
    private void addAppWidgetWithDesiredRecipe(int position) {
        Log.d(TAG, "addAppWidgetWithDesiredRecipe for recipe in position:" + position);

        // When the configuration is complete, get an instance of the AppWidgetManager
//        mAppWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
//
//        Recipe selectedRecipe = mRecipes.get(position);
//        Log.d(TAG, "addAppWidgetWithDesiredRecipe for selected recipe :" + selectedRecipe.getName());
//
     // Call BakingAppWidget.udpateAppWidget method directly to update the widget
//        BakingAppWidget.updateAppWidget(getApplicationContext(), appWidgetManager,
//                mAppWidgetId, mCursor);
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();

    }

}