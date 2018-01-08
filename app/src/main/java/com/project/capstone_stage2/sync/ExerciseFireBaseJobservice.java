package com.project.capstone_stage2.sync;


import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;

public class ExerciseFireBaseJobservice extends JobService {

    private static AsyncTask<Void, Void, Void> mFetchDataTask;
    /**
     * The entry point to your Job. Implementations should offload work to another thread of
     * execution as soon as possible.
     *
     * This is called by the Job Dispatcher to tell us we should start our job. Keep in mind this
     * method is run on the application's main thread, so we need to offload work to a background
     * thread.
     *
     * @return whether there is more work remaining.
     */
    @Override
    public boolean onStartJob(final JobParameters job) {
        final boolean needsReschedule = false;
        mFetchDataTask = new AsyncTask<Void, Void , Void>() {
             @Override
             protected Void doInBackground(Void... voids) {
                 Context context = getApplicationContext();
                 ExerciseDataSyncTask.scheduleFirebaseJobDispatcherSync(context);

                 jobFinished(job,needsReschedule);
                 return null;
             }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                jobFinished(job,needsReschedule);
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

        if (mFetchDataTask!=null) {
            // cancel the task
            mFetchDataTask.cancel(true);
        }
        return true;
    }
}
