package com.project.capstone_stage2.reminders;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ReminderIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_SEND_REMINDER= "com.google.developer.capstone.action.REMINDERS";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.project.capstone_stage2.reminders.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.project.capstone_stage2.reminders.extra.PARAM2";

    public ReminderIntentService() {
        super("ReminderIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
//    public static void startActionFoo(Context context, String param1, String param2) {
//        Intent intent = new Intent(context, ReminderIntentService.class);
//        intent.setAction(ACTION_FOO);
//        intent.putExtra(EXTRA_PARAM1, param1);
//        intent.putExtra(EXTRA_PARAM2, param2);
//        context.startService(intent);
//    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SEND_REMINDER.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionReminder(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionReminder(String param1, String param2) {
        // TODO: AlarmManager will call this IntentService to create notification to do exercise

    }


}
