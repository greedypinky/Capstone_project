package com.project.capstone_stage2.util;


import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.widget.Toast;

import com.exercise.backend.MyEndpoint;
import com.exercise.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import com.exercise.backend.*;
import com.google.api.server.spi.config.ApiMethod;

import javax.inject.Named;

public class EndPointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
    private static MyApi myApiService = null;
    private Context context;
    private boolean runLocal = true;
    // class that implement GCE_EndpointsAsyncTask.AsyncResponse to get the result of the AsyncTask
    public AsyncResponse delegate = null; // activity will implement this method

    // callback interface
    public interface AsyncResponse {
        // callback method to pass the endpoint async task result
        void processFinish(String result);
    }

    public EndPointsAsyncTask(AsyncResponse asyncResponse) {

        delegate = asyncResponse;
    }

    @Override
    protected String doInBackground(Pair<Context, String>... params) {

        MyApi.Builder builder = null;
        if (myApiService == null) {  // Only do this once
            if (runLocal) {
                builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl("http://localhost:8080/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
            } else {

                //  Go to https://console.cloud.google.com/ to check the deployed link
//                builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
//                        .setRootUrl("https://telljokeproject.appspot.com/_ah/api/");

            }

            myApiService = builder.build();
        }

        context = params[0].first;
        String name = params[0].second;

//        @ApiMethod(name = "getExerciseData")
//        public ExerciseData getExerciseData(@Named("categoryName") String categoryName) {
//            ExerciseData response = new ExerciseData();
//            // TODO: get the Data and set it into the Response
//            response.setData(categoryName);
//
//            return response;
//        }


        try {
           return myApiService.getExerciseData(name).execute().getData();

        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        // TODO: add back passing result to the AsyncResponse (delegate)

    }
}