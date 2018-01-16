package com.project.capstone_stage2;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.project.capstone_stage2.R;
import com.project.capstone_stage2.sync.ExerciseDataSyncTask;
import com.project.capstone_stage2.util.CategoryListAdapter;
import com.project.capstone_stage2.util.EndPointsAsyncTask;

import java.io.InputStream;

// Widget Example
// https://github.com/udacity/AdvancedAndroid_MyGarden
// image resource
// çsapling-plant-growing-seedling-154734/ https://pixabay.com/en/cactus-cacti-plant-thorns-spiky-152378/ https://pixabay.com/en/the-background-background-design-352165/

/**
1. Add the google cloud module from Android Studio : but where is it??
2. go to Google Developer Console to create a new project
 https://console.developers.google.com/
 use marukotest888 account to create the project
 capstoneproject-189106
 **/

// Main activity includes the CardView Layout for the activity
public class MainActivity extends AppCompatActivity implements CategoryListAdapter.CardViewOnClickListener,EndPointsAsyncTask.AsyncResponse {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView = null;
    private CategoryListAdapter mListAdapter = null;
    private String mEXERCISE_DATA_FROM_ENDPOINT = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.category_recycler_view);
        mListAdapter = new CategoryListAdapter(this);
        // set card adapter
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        boolean useEndPoint = true;
        getResponseFromEndPoint(useEndPoint);
    }

    // Implement the callback method so that when clicking on ViewHolder Item, it will handle the navigation properly.
    @Override
    public void onClickCategory(CategoryListAdapter.ExerciseCategory category) {
        // TODO: please add back the Intent and StartActivity with the intent!!
        Toast.makeText(this,"Start activity for " + category.getCategoryName(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this,ExerciseSwipeViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name",category.getCategoryName());
        bundle.putString("desc",category.getCategoryDesc());
        bundle.putInt("image", category.getImage());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void getResponseFromEndPoint(boolean useEndPoint) {

        if (useEndPoint) {
         /*
            Introduce a project dependency between your Java library and your GCE module,
            and modify the GCE starter code to pull jokes from your Java library.
            Create an Async task to retrieve jokes.
            Make the button kick off a task to retrieve a joke,
            then launch the activity from your Android Library to display it.
          */
            new EndPointsAsyncTask(this).execute(new Pair<Context, String>(this, "Manfred"));


        } else {

            //getJSONFromAsset();
        }
    }

    @Override
    public void processFinish(String result) {

        Log.d(TAG,"Google Endpoint API response:-" + result);
        if("Connection refused".equals(result)) {
            // use local mock json data from the Assets folder
            //mEXERCISE_DATA_FROM_ENDPOINT = getJSONFromAsset();
        } else {
            mEXERCISE_DATA_FROM_ENDPOINT = result;
        }

        // TODO: 2) Initialize the database by IntentService
        Log.d(TAG,"Sync DB data by IntentService");
        // ExerciseDataSyncTask.startImmediateSync(this, EXERCISE_DATA_FROM_ENDP
        ExerciseDataSyncTask.initialize(this, mEXERCISE_DATA_FROM_ENDPOINT);


    }

//    public String getJSONFromAsset() {
//        String json = null;
//        try {
//
//            InputStream is = getAssets().open("exercise.json");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            json = new String(buffer, "UTF-8");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//        return json;
//    }

}
