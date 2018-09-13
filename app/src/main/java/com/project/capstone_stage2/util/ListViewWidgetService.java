package com.project.capstone_stage2.util;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.project.capstone_stage2.MyExerciseAppWidget;
import com.project.capstone_stage2.R;
import com.project.capstone_stage2.WidgetConfigActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

//http://www.worldbestlearningcenter.com/answers/1524/using-listview-in-home-screen-widget-in-android
public class ListViewWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewRemoteViewsFactory(this.getApplicationContext(), intent);

    }

    class ListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private String TAG = ListViewRemoteViewsFactory.class.getSimpleName();
        private Context mContext = null;
        private Intent intentWithData = null;

        // private ArrayList<Exercise> mExercisesData = new ArrayList<Exercise>();
        private ArrayList<HashMap<String, String>> mExercisesData = new ArrayList<HashMap<String, String>>();

        // startListViewServiceIntent.putParcelableArrayListExtra(WIDGET_EXERCISE_DATA, exercises);
        // TODO: question is where should we get back the data?
        public ListViewRemoteViewsFactory(Context context, Intent intent) {

            mContext = context;
            intentWithData = intent;
            if (intentWithData != null) {

                Bundle bundle = intentWithData.getExtras();
                Log.d(TAG, "Get exercise Arraylist");

                //mExercisesData =  bundle.getParcelableArrayList(MyExerciseAppWidget.WIDGET_EXERCISE_DATA);
                mExercisesData = (ArrayList<HashMap<String, String>>) bundle.getSerializable(MyExerciseAppWidget.WIDGET_EXERCISE_DATA);
                int widgetID = bundle.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
                Log.d(TAG, "widget id is:" + widgetID);
                Log.d(TAG, "what is the size of exercise data?:" + mExercisesData.size());

            }

        }

        // Initialize the data set.

        public void onCreate() {

            Log.d(TAG, "onCreate()");
            // In onCreate() you set up any connections / cursors to your data source. Heavy lifting,

            // for example downloading or creating content etc, should be deferred to onDataSetChanged()

            // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.

            //intentWithData.getParcelableArrayListExtra(MyExerciseAppWidget.WIDGET_EXERCISE_DATA);

        }

        // Given the position (index) of a WidgetItem in the array, use the item's text value in

        // combination with the app widget item XML file to construct a RemoteViews object.

        public RemoteViews getViewAt(int position) {
            Log.d(TAG, "======== getViewAt position:" + position + "========");
            // Construct a RemoteViews item based on the app widget item XML file, and set the
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_exercise_item);

            HashMap<String, String> hmap = mExercisesData.get(position);
            String name = "";
            String desc = "";
            String imageurl = "";
            String videourl = "";
            name = hmap.get(WidgetConfigActivity.NAME_KEY);
            desc = hmap.get(WidgetConfigActivity.DESC_KEY);
            imageurl = hmap.get(WidgetConfigActivity.IMAGE_KEY);
            videourl = hmap.get(WidgetConfigActivity.VIDEO_KEY);
            Log.d(TAG, String.format(" name [%s], desc [%s], imageURL [%s]   ", name, desc, imageurl));

            // set the values for remote views
            rv.setTextViewText(R.id.widget_execise_name, name);
            rv.setTextViewText(R.id.widget_execise_desc, desc);

            try {
                // TODO : in real, we get the URI and set the image by PICASSO?
                // Picasso will handle loading the images on a background thread, image decompression and caching the images.
                // Bitmap bm = Picasso.with(mContext).load(data.getExerciseImageURI()).get();
                // Bitmap bm = Picasso.with(mContext).load(imageurl).get();
                // for now, we can use a default icon to set the image
                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.nao_squat01);
                //rv.setImageViewResource(R.id.widget_execise_image,bm);
                //rv.setImageViewBitmap(R.id.widget_execise_image, bm);
                if (imageurl != null) {
                    Log.d(TAG, "try to set the image to remote view");
                    rv.setImageViewUri(R.id.widget_execise_image, Uri.parse(imageurl));
                } else {
                    // set default image
                    // rv.setImageViewBitmap(R.id.widget_execise_image, bm);
                    //rv.setViewVisibility();
                    rv.setViewVisibility(R.id.widget_invalidImageText, View.VISIBLE);
                    rv.setViewVisibility(R.id.widget_execise_name, View.GONE);
                }
                if (videourl !=null) {

                } else {

                }
            } catch (Exception ioe) {
                ioe.printStackTrace();
            }

            // end feed row

            // Next, set a fill-intent, which will be used to fill in the pending intent template

            // that is set on the collection view in ListViewWidgetProvider.

//            Bundle extras = new Bundle();
//
//            extras.putInt("ITEM_POS", position);
//
//            Intent fillInIntent = new Intent();
//
//            fillInIntent.putExtra("DATA",mExercisesData);
//
//            fillInIntent.putExtras(extras);

            // Make it possible to distinguish the individual on-click

            // action of a given item

            //rv.setOnClickFillInIntent(R.id.widget_execise_name, fillInIntent);

            // Return the RemoteViews object.

            return rv;

        }

        public int getCount() {

            if(mExercisesData != null) {
                Log.d(TAG, mExercisesData.size() + "");
                return mExercisesData.size();
            } else return 0;
        }

        // when will this be triggered?
        public void onDataSetChanged() {
            Log.d(TAG, "onDataSetChanged!");
        }

        public int getViewTypeCount() {

            return 1;

        }

        public long getItemId(int position) {

            return position;

        }

        public void onDestroy() {

            mExercisesData.clear();

        }

        public boolean hasStableIds() {

            return true;

        }

        public RemoteViews getLoadingView() {

            return null;

        }

    }

}
