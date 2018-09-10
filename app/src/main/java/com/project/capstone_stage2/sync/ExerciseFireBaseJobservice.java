package com.project.capstone_stage2.sync;


import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;
import com.project.capstone_stage2.R;
import com.project.capstone_stage2.util.NetworkUtil;

import java.io.IOException;
import java.net.URL;

public class ExerciseFireBaseJobservice extends JobService {

    private static String TAG = ExerciseFireBaseJobservice.class.getSimpleName();
    private static AsyncTask<Void, Void, String> mFetchDataTask;
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
    @Override
    public boolean onStartJob(final JobParameters job) {
        final boolean needsReschedule = true;
        mFetchDataTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String response = null;
                try {
                    Log.d(TAG, "doInBackground() - set response from Firebase db!");
                    URL mFirebaseDbUrl = new URL(getString(R.string.firebaseproject) + getString(R.string.firebaseDbRefJson));
                    response = NetworkUtil.getResponseFromHttp(mFirebaseDbUrl);
                    if (response != null) {
                       ExerciseDataSyncTask.syncJobScheduleData(getApplicationContext(), response);
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    Log.e(TAG, ioe.getMessage());
                }

                Log.d(TAG, "FireBase job dispatcher's doInBackground() is called!");
                return response;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                Log.d(TAG, "FireBase job dispatcher's onPostExecute response:" + response);

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
        if (mFetchFromEndPointTask != null) {
            mFetchFromEndPointTask.cancel(true);
        }
        return true;
    }
}
