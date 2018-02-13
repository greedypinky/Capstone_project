package com.project.capstone_stage2;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.widget.RemoteViews;

import com.project.capstone_stage2.dbUtility.ExerciseContract;
import com.project.capstone_stage2.util.Exercise;
import com.project.capstone_stage2.util.ListViewWidgetService;

import java.util.ArrayList;

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

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
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
            Log.d(TAG,"onHandleIntent" );
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
    public static void updateExerciseAppWidget(Context context,AppWidgetManager appWidgetManager,
                                               int appWidgetId, ArrayList<Exercise> exercises) {

        Log.d(TAG,"updateExerciseAppWidget:" + context.getPackageName());
        if (exercises!=null) {
            Log.d(TAG, "Exercise Data count:-" + exercises.size());
        }
//            RemoteViews views = new RemoteViews(t
// his.getPackageName(),
//                    R.layout.layout_appwidget_large);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.content_widget);
        remoteViews.setTextViewText(R.id.widgetTitleLabel,"hello!");
        // Trigger listview item click
        Intent startListViewServiceIntent = new Intent(context, ListViewWidgetService.class);
        startListViewServiceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        startListViewServiceIntent.putParcelableArrayListExtra(WIDGET_EXERCISE_DATA, exercises);
        //startListViewServiceIntent.putExtra(WIDGET_EXERCISE_DATA, "");
        // TODO: populate the data - need to implement the data correctly
        remoteViews.setRemoteAdapter(R.id.widget_exercise_listview, startListViewServiceIntent);

        // template to handle the click listener for each item
        Intent clickIntentTemplate = new Intent(context, MainActivity.class);
        PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(clickIntentTemplate)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.widget_exercise_listview, clickPendingIntentTemplate);

//            Intent startActivityIntent = new Intent(context,MainActivity.class);
//
//            PendingIntent startActivityPendingIntent = PendingIntent.getActivity(context, 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            // set the pendingIntent for each of the List item
//            remoteViews.setPendingIntentTemplate(R.id.widget_ListView, startActivityPendingIntent);

        // The empty view is displayed when the collection has no items.

        // It should be in the same layout used to instantiate the RemoteViews  object above.

        //remoteViews.setEmptyView(R.id.widget_ListView, R.id.widget_empty_view);
        Log.d(TAG, "call appWidgetManager.updateAppWidget");
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }


}

