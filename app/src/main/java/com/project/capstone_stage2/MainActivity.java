package com.project.capstone_stage2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.project.capstone_stage2.R;
import com.project.capstone_stage2.util.CategoryListAdapter;

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
public class MainActivity extends AppCompatActivity implements CategoryListAdapter.CardViewOnClickListener {

    private RecyclerView mRecyclerView = null;
    private CategoryListAdapter mListAdapter = null;

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

}
