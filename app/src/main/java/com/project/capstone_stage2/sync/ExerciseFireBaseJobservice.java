package com.project.capstone_stage2.sync;


import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;
import com.project.capstone_stage2.util.EndPointsAsyncTask;

public class ExerciseFireBaseJobservice extends JobService {

    private static String TAG = ExerciseFireBaseJobservice.class.getSimpleName();
    private static AsyncTask<Void, Void, Void> mFetchDataTask;
    private static AsyncTask<Pair<Context, String>, Void, String> mFetchFromEndPointTask;

    /**
     * The entry point to your Job. Implementations should offload work to another thread of
     * execution as soon as possible.
     * <p>
     * This is called by the Job Dispatcher to tell us we should start our job. Keep in mind this
     * method is run on the application's main thread, so we need to offload work to a background
     * thread.
     *
     * @return whether there is more work remaining.
     */
//    @Override
//    public boolean onStartJob(final JobParameters job) {
//        final boolean needsReschedule = false;
//
//        Context context = getApplicationContext();
//        ExerciseDataSyncTask.scheduleFirebaseJobDispatcherSync(context);
//        Log.d(TAG, "Congrats! your job dispatcher is working - doInBackground is called!");
//        mFetchFromEndPointTask = new EndPointsAsyncTask(true).execute(new Pair<Context, String>(this, "Manfred"));
//        return true;
//    }
    @Override
    public boolean onStartJob(final JobParameters job) {
        final boolean needsReschedule = false;
        mFetchDataTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                // Context context = getApplicationContext();
                // ExerciseDataSyncTask.scheduleFirebaseJobDispatcherSync(context);
                Log.d(TAG, "Congrats! your job dispatcher is working - doInBackground is called!");
                jobFinished(job, needsReschedule);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.d(TAG, "Congrats! onPostExecute() is called!");
                jobFinished(job, needsReschedule);
            }


        };

        mFetchDataTask.execute();
        return true;
    }

    /**
     * Called when the scheduling engine has decided to interrupt the execution of a running job,
     * most likely because the runtime constraints associated with the job are no longer satisfied.
     *
     * @return whether the job should be retried
     * @see com.firebase.jobdispatcher.Job.Builder#setRetryStrategy(RetryStrategy)
     **/

    @Override
    public boolean onStopJob(JobParameters job) {
//        if (mFetchDataTask!=null) {
//            // cancel the task
//            mFetchDataTask.cancel(true);
//        }
        if (mFetchFromEndPointTask != null) {
            mFetchFromEndPointTask.cancel(true);
        }
        return true;
    }
}
