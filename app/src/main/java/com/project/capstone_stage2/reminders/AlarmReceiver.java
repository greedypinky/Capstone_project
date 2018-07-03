package com.project.capstone_stage2.reminders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.project.capstone_stage2.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {

    private static String TAG = AlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
        if(intent.getAction().equals(ReminderIntentService.ACTION_SEND_REMINDER)) {
            // trigger the action after Alarm is received.
            scheduleTime(context);
        }

    }

    public static void scheduleTime(Context context) {

        // Require the Alarm Manager to help use to trigger the Alarm at the preferred setting time
        AlarmManager alarmManager =  AlarmManagerProvider.getAlarmManager(context);
        String keyReminder = context.getString(R.string.pref_key_reminder);
        String keySettime = context.getString(R.string.pref_key_time);

        // use PreferenceManager to get the Default SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isSetReminder = sharedPreferences.getBoolean(keyReminder,false);

        // prepare the intent that includes the IntentSerivce to build notification
        // but we need to wrap the intent inside the PendingIntent

        Intent intent = new Intent(context, ReminderIntentService.class);
        PendingIntent triggerNotificationPendingIntent = PendingIntent.getService(context,0,intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (isSetReminder) {

            // if the switch is ON, we need to trigger the notification by Alarm Manager
            Calendar now = Calendar.getInstance();
            Calendar startTime = Calendar.getInstance();
            long startTimeInMillis = startTime.getTimeInMillis();
            try {
                String alarmTime = sharedPreferences.getString("pref_key_time", "12:00");
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                // parse will return the Date object
                startTime.setTime(dateFormat.parse(alarmTime));
            } catch (ParseException e) {
                Log.w(TAG,"unable to determina alarm start time");
                return;
            }

            // if the current time is after the starting time
            if (now.after(startTime)) {
                // Prospone the alarm time to be 1 day later
                startTime.add(Calendar.DATE,1);
                Log.d(TAG,"Schedule the alarm tomorrow");
            } else {

                Log.d(TAG,"Schedule the alarm today");
            }


            // This will schedule repeatedly
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, startTime.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, triggerNotificationPendingIntent);

        } else {
            // cancel the operation which is set inside the PendingIntent
         alarmManager.cancel(triggerNotificationPendingIntent);
        }


    }
}
