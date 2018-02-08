package com.project.capstone_stage2;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.widget.RemoteViews;

import com.project.capstone_stage2.dbUtility.ExerciseContract;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

/**
 * Implementation of App Widget functionality.
 */
public class MyExerciseAppWidget extends AppWidgetProvider {

    private static String TAG = MyExerciseAppWidget.class.getSimpleName();

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
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
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
                    updateNewsAppWidget(appWidgetManager, incomingAppWidgetId,
                            intent);
                } catch (NullPointerException e) {
                }
            }

        }

        public void updateNewsAppWidget(AppWidgetManager appWidgetManager,
                                        int appWidgetId, Intent intent) {
            Log.v("String package name", this.getPackageName());
//            RemoteViews views = new RemoteViews(t
// his.getPackageName(),
//                    R.layout.layout_appwidget_large);

            RemoteViews views = null;
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }


}

