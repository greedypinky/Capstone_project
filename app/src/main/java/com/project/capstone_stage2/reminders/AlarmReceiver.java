package com.project.capstone_stage2.reminders;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import com.project.capstone_stage2.MainActivity;
import com.project.capstone_stage2.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
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
        intent.setAction(ReminderIntentService.ACTION_SEND_REMINDER);
        PendingIntent triggerNotificationPendingIntent = PendingIntent.getService(context,0,intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (isSetReminder) {
            // if the switch is ON, we need to trigger the notification by Alarm Manager
            // example:- http://droidmentor.com/schedule-notifications-using-alarmmanager/
            Calendar now = Calendar.getInstance();
            Calendar startTime = Calendar.getInstance();
            long startTimeInMillis = startTime.getTimeInMillis();
            try {
                String alarmTime = sharedPreferences.getString(context.getString(R.string.pref_key_time), "12:00");
                Log.d(TAG,"DEBUG:::What is sharedPreferences time?" + alarmTime);
                String hour = alarmTime.split(":")[0];
                String min = alarmTime.split(":")[1];

                Log.d(TAG,"DEBUG:::What is split hour?" + hour);
                Log.d(TAG,"DEBUG:::What is split mins?" + min);
                startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                startTime.set(Calendar.MINUTE, Integer.parseInt(min));
                startTime.set(Calendar.SECOND, 0);

            } catch (Exception e) {
                Log.e(TAG,"unable to determine/parse the Alarm start time, please check the time format!");
                return;
            }

            boolean triggerNextday = now.after(startTime);
            Log.d(TAG,"DEBUG:::What is the time now?" + now.getTime());
            Log.d(TAG,"DEBUG:::What is setting time ?" + startTime.getTime());
            Log.d(TAG,"DEBUG:::Do we set the alarm next day?" + triggerNextday);
            // if the current time is after the starting time
            if (now.after(startTime)) {
                // Prospone the alarm time to be 1 day later
                startTime.add(Calendar.DATE,1);
                // DEBUG:::What is setting time ?Thu Jan 01 12:00:00 PST 1970
                // TODO: fix this!! why the date became 1970 ? that is why the notification never gets TRIGGERED!
                Log.d(TAG,"Schedule the alarm tomorrow:" + startTime.getTime().toString());
            } else {
                Log.d(TAG,"Schedule the alarm today:" + startTime.getTime().toString());
            }

            // This will schedule repeatedly using Real Time Clock (UTC) instead of the System boot time
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, startTime.getTimeInMillis()+10000,
                    AlarmManager.INTERVAL_DAY, triggerNotificationPendingIntent);

        } else {
            // cancel the operation which is set inside the PendingIntent
            alarmManager.cancel(triggerNotificationPendingIntent);
        }


    }



}
