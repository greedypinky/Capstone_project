package com.project.capstone_stage2.pref;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.project.capstone_stage2.R;
import com.project.capstone_stage2.reminders.AlarmReceiver;
import com.project.capstone_stage2.reminders.ReminderIntentService;


public class ExercisePreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static String TAG = ExercisePreferenceFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Preference onChangePref = (Preference) findPreference(key);

        // TODO: add back if we have the change, we re-schedule the notifcations
        // if user turn the reminder switch on, we also need to reschedule the time again
        if (key.equals(getString(R.string.pref_key_reminder))) {
            boolean reminder = sharedPreferences.getBoolean(getString(R.string.pref_key_reminder), false);
            // re-schedule the alarm
            if (reminder) {
                Log.d(TAG, "Reminder Setting is ON !!");
                Intent intentToAlarmReceiver = new Intent(getActivity(), AlarmReceiver.class);
                // forgot the set action to the intent otherwise, intent filter won't work
                intentToAlarmReceiver.setAction(ReminderIntentService.ACTION_SEND_REMINDER);
                getActivity().sendBroadcast(intentToAlarmReceiver);
            }
        }

        // If use select a new time, we also need to reschedule the timer again
        if (key.equals(getString(R.string.pref_key_time))) {
            String alarmTime = sharedPreferences.getString(getString(R.string.pref_key_time), "12:00");
            Log.d(TAG, "Need to re-schedule Alarm Time at: " + alarmTime);


            Preference pref = findPreference(key);
            if (pref instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) pref;
                int index = listPreference.findIndexOfValue(listPreference.getValue());
                String entry = listPreference.getEntries()[index].toString();
                pref.setSummary(getString(R.string.pref_settime_summary) + " at " + entry);
            }
            // re-schedule the alarm
            Intent intentToAlarmReceiver = new Intent(getActivity(), AlarmReceiver.class);
            intentToAlarmReceiver.setAction(ReminderIntentService.ACTION_SEND_REMINDER);

            getActivity().sendBroadcast(intentToAlarmReceiver);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

    }
}
