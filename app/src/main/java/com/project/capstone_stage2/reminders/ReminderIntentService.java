package com.project.capstone_stage2.reminders;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.project.capstone_stage2.MainActivity;
import com.project.capstone_stage2.R;

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
    public static final String CHANNEL_ID = "reminder_channel_id";
    public static final int REMINDER_NOTIFICATOIN_ID = 1000;

    public ReminderIntentService() {
        super("ReminderIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SEND_REMINDER.equals(action)) {
                handleActionReminder();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionReminder() {
        // TODO: AlarmManager will call this IntentService to create notification to do exercise
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID);
        builder.setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_context))
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setChannelId(CHANNEL_ID)
                .setAutoCancel(true)
                .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis());


        // if the device is on Oreo platform or up
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // TODO: require to use channel in the setting for this notification
            CharSequence name = getString(R.string.app_name);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }


        // TODO: create channel for Oreo devices
        // use NotificationManager to add the channel and notify
        notificationManager.notify(REMINDER_NOTIFICATOIN_ID,builder.build());

    }


}
