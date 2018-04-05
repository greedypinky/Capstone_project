package com.project.capstone_stage2.sync;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class ExerciseSyncIntentService extends IntentService {

    // IntentService can perform ACTION_SYNC action to sync data to the DB
    public static final String ACTION_SYNC = "com.project.capstone_stage2.sync.action.SYNC_DATA";

    public ExerciseSyncIntentService() {
        super("ExerciseSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SYNC.equals(action)) {
                // call the ExerciseDataSyncTask's method
                // need to get the exercise json from the intent by the right key!
                String exerciseJSON = intent.getStringExtra(ExerciseDataSyncTask.SYNC_EXERCISE_DATA);
                // call the actual syncData task to help sync up data to the Exercise Table
                ExerciseDataSyncTask.syncData(this, exerciseJSON);
            }
        }
    }

//    /**
//     * Handle action Foo in the provided background thread with the provided
//     * parameters.
//     */
//    private void handleActionFoo(String param1, String param2) {
//        // TODO: Handle action Foo
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
//
//    /**
//     * Handle action Baz in the provided background thread with the provided
//     * parameters.
//     */
//    private void handleActionBaz(String param1, String param2) {
//        // TODO: Handle action Baz
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
}
