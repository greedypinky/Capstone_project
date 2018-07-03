package com.project.capstone_stage2.reminders;

import android.app.AlarmManager;
import android.content.Context;

/**
 * Created by ritalaw on 2018-06-26.
 */

public class AlarmManagerProvider {

    private static AlarmManager manager;

    // this method is for unit test?
    public static synchronized void injectAlarmManager(AlarmManager alarmManager) {
        if (manager != null) {
            throw new IllegalStateException("Alarm Manager Already Set");
        }

        manager = alarmManager;
    }

    // only access by one thread at a time to get
    // within package
    static synchronized AlarmManager getAlarmManager(Context context) {

        if (manager == null) {
          manager =  (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        return manager;
    }

}
