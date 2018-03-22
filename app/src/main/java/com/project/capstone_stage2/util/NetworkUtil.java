package com.project.capstone_stage2.util;


import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

public class NetworkUtil {
    /**
     * Checks if the device is currently connected to the network.
     * @param context Context this is being called from
     * @return Whether the network state is connected or not
     */
    public static boolean isNetworkConnected(
            Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo currentNetwork = connectivityManager.getActiveNetworkInfo();
        return currentNetwork != null && currentNetwork.isConnected();
    }

    public static Snackbar makeSnackbar(View view, String message, boolean dismissible) {
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.RED);
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);

        if (dismissible) {
            snackbar.setAction("X", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });
            snackbar.setActionTextColor(Color.WHITE);
        }

        return snackbar;
    }
}
