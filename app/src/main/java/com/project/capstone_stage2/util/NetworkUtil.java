package com.project.capstone_stage2.util;


import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.project.capstone_stage2.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtil {

    private final static String TAG = NetworkUtil.class.getSimpleName();
    private final static String DELIMITER = "//A";

    // prevent the creation of the NetworkUtil instance outside of the package
    private NetworkUtil(){
    }

    /**
     * Checks if the device is currently connected to the network.
     *
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

    /**
     * gerRespsonseFromHttp
     *
     * @param url
     * @return response
     * @throws IOException
     */
    public static String getResponseFromHttp(URL url) throws IOException {

        Log.d(TAG, "URL authority:" + url.getAuthority());
        Log.d(TAG, "Http URL connecting...to " + url.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream responseInputStream = conn.getInputStream();
        String response = null;

        try {

            Scanner scanner = new Scanner(responseInputStream);
            scanner.useDelimiter(DELIMITER);
            if (scanner.hasNext()) {
                response = scanner.next();
                Log.d(TAG, "response is " + response);
                return response;
            } else {
                return null;
            }

        } finally {
            conn.disconnect();
        }

    }
}
