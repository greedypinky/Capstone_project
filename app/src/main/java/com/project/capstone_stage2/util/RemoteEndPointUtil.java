package com.project.capstone_stage2.util;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.URL;

import android.content.ContentValues;


import com.project.capstone_stage2.ExerciseSwipeViewActivity;
import com.project.capstone_stage2.dbUtility.ExerciseContract;

/*
       {

        "exercises": [
            {
                "category": "Squat",
                "category description": "Squat",
                "id": 1,
                "name":"Situp",
                "description": "",
                "image": "imageurl",
                "video": "videourl",
                "steps": [
                    {
                    "stepDescription": "step1"

                    },
                    {
                    "stepDescription": "step2"

                    }
                 ]
          }

        ]

        }
*/

public class RemoteEndPointUtil {

    private static final String TAG = "RemoteEndpointUtil";
    private static final String EXERCISES = "exercises"; //exercises
    private static final String CATEGORY = "category";
    private static final String CATEGORY_DESCRIPTION = "category description";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String IMAGE = "image";
    private static final String VIDEO = "video";
    private static final String STEPS = "steps";
    private static final String STEP_DESCRIPTION = "stepDescription";

    public static ContentValues[] fetchJSONData(String json) {
        ContentValues[] exerciseContentValues = null;
        JSONArray exercises = null;
        try {

            Log.e(TAG,"parse the JSON data:" + json);
            // create the JSON object
            //JSONObject jsonObject = new JSONObject(json);
            // get the exercises array

            exercises = new JSONArray(json);
            exerciseContentValues = new ContentValues[exercises.length()];

            for (int i = 0; i < exercises.length(); i++) {

                String category = "";
                String category_description = "";
                String id = "";
                String name = "";
                String description = "";
                String image = "";
                String video = "";
                String steps = "";
                String stepDesc = "";

                JSONObject exercise = (JSONObject) exercises.get(i);
                // get 1 exercise out and get the properties
                category = exercise.getString(CATEGORY);
                category_description = exercise.getString(CATEGORY_DESCRIPTION);
                id = exercise.getString(ID);
                name = exercise.getString(NAME);
                description = exercise.getString(DESCRIPTION);
                image = exercise.getString(IMAGE);
                video = exercise.getString(VIDEO);
                JSONArray stepsArray = exercise.getJSONArray(STEPS);
                //String stepDesc = "";
                for (int s = 0; s < stepsArray.length(); s++) {
                    JSONObject step = (JSONObject) stepsArray.get(s);
                    stepDesc = stepDesc + step.getString(STEP_DESCRIPTION) + "\n";
                    Log.d(TAG, "Step Description is:" + stepDesc);
                }

                // from here how can we add the data back to the DB
                ContentValues exerciseValues = new ContentValues();
                // put the [ Column, Value ]
                exerciseValues.put(ExerciseContract.ExerciseEntry.CATEGORY, category);
                exerciseValues.put(ExerciseContract.ExerciseEntry.CATEGORY_DESC, category_description);
                exerciseValues.put(ExerciseContract.ExerciseEntry.EXERCISE_ID, id);
                exerciseValues.put(ExerciseContract.ExerciseEntry.EXERCISE_NAME, name);
                exerciseValues.put(ExerciseContract.ExerciseEntry.EXERCISE_DESCRIPTION, description);
                exerciseValues.put(ExerciseContract.ExerciseEntry.EXERCISE_STEPS, stepDesc);
                exerciseValues.put(ExerciseContract.ExerciseEntry.EXERCISE_IMAGE, image);
                exerciseValues.put(ExerciseContract.ExerciseEntry.EXERCISE_VIDEO, video);
                exerciseValues.put(ExerciseContract.ExerciseEntry.EXERCISE_FAVORITE, "0");

                // add into the ContentValues array
                exerciseContentValues[i] = exerciseValues;

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,"Unable to parse the JSON data:" + e.getMessage());
        }

        return exerciseContentValues;

    }

    public static boolean insertNewExerciseToDB(Context context, ContentValues[] values) {
        Cursor existingDbCursor = null;
        boolean result = true;
        try {
            Uri queryURI = ExerciseContract.ExerciseEntry.CONTENT_URI_ALL;
            /* Sort order: Ascending by exercise id */
            String sortOrder = ExerciseContract.ExerciseEntry.EXERCISE_ID + " ASC";
            String selectionByExerciseID = ExerciseContract.ExerciseEntry.EXERCISE_ID + " = ?";
            String selectionByFavorite = ExerciseContract.ExerciseEntry.EXERCISE_FAVORITE + " = ?";

            for (int i=0; i<values.length; i++) {
                String exerciseID  = values[i].getAsString(ExerciseContract.ExerciseEntry.EXERCISE_ID);
                String exerciseName  = values[i].getAsString(ExerciseContract.ExerciseEntry.EXERCISE_NAME);
                existingDbCursor = context.getContentResolver().query(queryURI, ExerciseSwipeViewActivity.EXERCISE_PROJECTION, selectionByFavorite, new String[]{exerciseID}, sortOrder);
                    if (existingDbCursor.getCount() == 0) {

                        //TODO: insert data into DB
                        Uri insertUri = context.getContentResolver()
                                    .insert(ExerciseContract.ExerciseEntry.CONTENT_URI_ALL, values[i]);
                        Log.d(TAG, "=============Insert new exercise's result uri: " + insertUri + "=============");
                        if (insertUri == null) {
                            result = false;
                            break;
                        }
                    } else {
                        Log.e(TAG, String.format("Exercise %s with exerciseID %s is already existed in the local database!", exerciseName, exerciseID));
                    }
            }

        } finally {
            // close the cursor to avoid memory leak
            existingDbCursor.close();
        }
        return result;
    }

}
