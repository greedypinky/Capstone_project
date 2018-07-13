package com.project.capstone_stage2.sync;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.project.capstone_stage2.dbUtility.ExerciseContract;
import com.project.capstone_stage2.util.RemoteEndPointUtil;
import java.util.concurrent.TimeUnit;

public class ExerciseDataSyncTask {
    private static final String TAG = ExerciseDataSyncTask.class.getSimpleName();
    /*
    * Interval at which to sync with latest data from the backend. Use TimeUnit for convenience, rather than
    * writing out a bunch of multiplication ourselves.
    */
//     private static final int SYNC_INTERVAL_HOURS = 3;
//     private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
//     private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;

    private static boolean sInitialized = false;
    private static final String SYNC_TAG = "exercise-sync";
    public static final String SYNC_EXERCISE_DATA = "exercise-json";

    // add Synchronized
    // synchronized public static void syncData(Context context,boolean alreadyHasData)
    synchronized public static void syncData(Context context, String exerciseJSON) {
        try {
            // TODO: add back the logic
            // 1. Use the EndPointSyncTask.execute to get the JSON data
            // 2. Parse the JSON data and put them into ContentValues[]
            // 3. So the bulk insert into the database
            ContentValues[] contentValues = RemoteEndPointUtil.fetchJSONData(exerciseJSON);
            if (contentValues != null && contentValues.length > 0) {

                // Then we update the database
                ContentResolver contentResolver = context.getContentResolver();
                // delete all the old data in DB first
                //contentResolver.delete(ExerciseContract.ExerciseEntry.CONTENT_URI_ALL, null,null);
                // bulk insert again with the latest data from server
                contentResolver.bulkInsert(ExerciseContract.ExerciseEntry.CONTENT_URI_ALL, contentValues);

            }
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    /**
     * Schedules a repeating sync up data by FirebaseJobDispatcher.
     *
     * @param context Context used to create the GooglePlayDriver that powers the
     *                FirebaseJobDispatcher
     */
    synchronized public static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context) {

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Long duration = TimeUnit.DAYS.toSeconds(7);
        int oneWeekDuration = duration.intValue();
        // Create the Job to periodically sync the exercise data from the endpoint server
        Job syncExerciseDataJob = dispatcher.newJobBuilder()
                .setService(ExerciseFireBaseJobservice.class) // use the JobService to sync the data
                .setTag(SYNC_TAG) // job's identifier
                .setConstraints(Constraint.ON_ANY_NETWORK) // job can run on any network
                .setLifetime(Lifetime.FOREVER) // run forever
                .setRecurring(true) // tell the job to recur so that it will run again to sync the data again
                /*
                 * We want the data to be synced every 3 to 4 hours. The first argument for
                 * Trigger's static executionWindow method is the start of the time frame when the
                 * sync should be performed. The second argument is the latest point in time at
                 * which the data should be synced. Please note that this end time is not
                 * guaranteed, but is more of a guideline for FirebaseJobDispatcher to go off of.
                 */
//                .setTrigger(Trigger.executionWindow(
//                        10,
//                        30))

                .setTrigger(Trigger.executionWindow(0, oneWeekDuration))
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setReplaceCurrent(true) // replace the job if one already exists.
                .setConstraints(Constraint.ON_ANY_NETWORK)  //Run this job only when the network is avaiable.
                .build(); // build the job when job configurations are set

        //schedule the Job to get Exercise Data from the backend
        Log.d(TAG, " dispatcher.schedule(syncExerciseDataJob)!");
        dispatcher.schedule(syncExerciseDataJob);
    }

    /**
     * Creates periodic sync tasks and checks to see if an immediate sync is required. If an
     * immediate sync is required, this method will take care of making sure that sync occurs.
     *
     * @param context Context that will be passed to other methods and used to access the
     *                ContentResolver
     */
    synchronized public static void initialize(@NonNull final Context context, final String exerciseData) {

        /*
         * Only perform initialization once per app lifetime. If initialization has already been
         * performed, we have nothing to do in this method.
         */
        if (sInitialized) return;

        sInitialized = true;

        /*
         * This method call triggers Sunshine to create its task to synchronize weather data
         * periodically.
         */
        Log.d(TAG, "scheduleFirebaseJobDispatcherSync!");
        // Context is the MainActivity
        scheduleFirebaseJobDispatcherSync(context);

        /*
         * We need to check to see if our ContentProvider has data to display in our forecast
         * list. However, performing a query on the main thread is a bad idea as this may
         * cause our UI to lag. Therefore, we create a thread in which we will run the query
         * to check the contents of our ContentProvider.
         */
        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {

                /* URI for every row of weather data in our weather table*/
                //Uri forecastQueryUri = WeatherContract.WeatherEntry.CONTENT_URI;
                Uri allExerciseURI = ExerciseContract.ExerciseEntry.CONTENT_URI_ALL;

                /*
                 * Since this query is going to be used only as a check to see if we have any
                 * data (rather than to display data), we just need to PROJECT the ID of each
                 * row. In our queries where we display data, we need to PROJECT more columns
                 * to determine what weather details need to be displayed.
                 */
                String[] projectionColumns = {ExerciseContract.ExerciseEntry._ID};

                /* Here, we perform the query to check to see if we have any weather data */
                Cursor cursor = context.getContentResolver().query(
                        allExerciseURI,
                        projectionColumns,
                        null,
                        null,
                        null);

                // if unable to get data from the existing database, sync the data immediately (not by the scheduling job)
                if (null == cursor || cursor.getCount() == 0) {
                    // this will start the Intent Service to sync the data the first time
                    startImmediateSync(context, exerciseData);
                } else {
                    // close the Cursor to avoid memory leaks!
                    cursor.close();
                }


            }
        });

        // start the thread is prepared to check if the database is empty
        checkForEmpty.start();
    }

    /**
     * Helper method to perform a sync immediately using an IntentService for asynchronous
     * execution.
     *
     * @param context The Context used to start the IntentService for the sync.
     */
    public static void startImmediateSync(@NonNull final Context context, @NonNull final String exerciseData) {
        Intent intentToSyncByIntentService = new Intent(context, ExerciseSyncIntentService.class);
        // TODO: Add the intent to pass the ExerciseData
        Bundle extras = new Bundle();
        extras.putString(SYNC_EXERCISE_DATA, exerciseData);
        intentToSyncByIntentService.putExtras(extras);
        intentToSyncByIntentService.setAction(ExerciseSyncIntentService.ACTION_SYNC);
        context.startService(intentToSyncByIntentService);
    }

}
