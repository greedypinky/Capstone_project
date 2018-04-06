package com.project.capstone_stage2;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.project.capstone_stage2.dbUtility.ExerciseContract;
import com.project.capstone_stage2.util.Exercise;
import com.project.capstone_stage2.util.ListViewWidgetService;

import java.util.ArrayList;
import java.util.HashMap;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

/**
 * Implementation of App Widget functionality.
 */
public class MyExerciseAppWidget extends AppWidgetProvider {

    private static String TAG = MyExerciseAppWidget.class.getSimpleName();
    public static String WIDGET_EXERCISE_DATA = "exercise_array";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_exercise_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // TODO: need to add back my code here
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    // if we plan to use IntentService to update the widget
    public static class UpdateWidgetService extends IntentService {
        public UpdateWidgetService() {
            // only for debug purpose
            super("UpdateWidgetService");

        }

        @Override
        protected void onHandleIntent(Intent intent) {

            String mSelectedExceriseCategory = null;
            Log.d(TAG, "onHandleIntent");
            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(UpdateWidgetService.this);

            int incomingAppWidgetId = intent.getIntExtra(EXTRA_APPWIDGET_ID,
                    INVALID_APPWIDGET_ID);
            String mSelectedExerciseCategory = intent.getStringExtra(WidgetConfigActivity.SELECTED_CATEGORY);

            if (incomingAppWidgetId != INVALID_APPWIDGET_ID && mSelectedExceriseCategory != null) {
                try {

                } catch (NullPointerException e) {
                }
            }
        }
    }

    //https://www.sitepoint.com/killer-way-to-show-a-list-of-items-in-android-collection-widget/
//    public static void updateExerciseAppWidget(Context context,AppWidgetManager appWidgetManager,
//                                               int appWidgetId, ArrayList<Exercise> exercises) {
    public static void updateExerciseAppWidget(Context context, AppWidgetManager appWidgetManager,
                                               int appWidgetId, ArrayList<HashMap<String, String>> exercises) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.content_widget);
        Log.d(TAG, "updateExerciseAppWidget:" + context.getPackageName());

        if (exercises != null && exercises.size() == 0) {
            Log.d(TAG, "No Exercise Data!");
            // should set the No Exercise Data place holder Text if there is no exercise data!
            remoteViews.setTextViewText(R.id.widget_empty_textview, context.getString(R.string.widget_no_data));
            remoteViews.setViewVisibility(R.id.widget_empty_textview, View.VISIBLE);
        } else {

            Log.d(TAG, "Have Exercise Data!");

            // TODO: DEBUG by finding AppWidgetHostView
            // AppWidgetHostView: android.os.BadParcelableException: ClassNotFoundException when unmarshalling: com.project.capstone_stage2.util.Exercise
            // remoteViews.setTextViewText(R.id.widgetTitleLabel,"hello!");
            // Trigger listview item click
            Intent startListViewServiceIntent = new Intent(context, ListViewWidgetService.class);
            Bundle bundle = new Bundle();
            //bundle.putParcelableArrayList(WIDGET_EXERCISE_DATA, exercises);

            bundle.putSerializable(WIDGET_EXERCISE_DATA, exercises);
            bundle.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            startListViewServiceIntent.putExtras(bundle);

            // TODO: populate the data - need to implement the data correctly
            remoteViews.setRemoteAdapter(R.id.widget_exercise_listview, startListViewServiceIntent);

            //     template to handle the click listener for each item
//        Intent clickIntentTemplate = new Intent(context, MainActivity.class);
//        PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
//                .addNextIntentWithParentStack(clickIntentTemplate)
//                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteViews.setPendingIntentTemplate(R.id.widget_exercise_listview, clickPendingIntentTemplate);

        }
        Log.d(TAG, "call appWidgetManager.updateAppWidget");
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }
}

