package com.project.capstone_stage2.util;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.URL;
import android.content.ContentValues;

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
    private static final String EXERCISES ="exercises"; //exercises
    private static final String CATEGORY ="category";
    private static final String CATEGORY_DESCRIPTION ="category description";
    private static final String ID ="id";
    private static final String NAME ="name";
    private static final String DESCRIPTION ="description";
    private static final String IMAGE ="image";
    private static final String VIDEO ="video";
    private static final String STEPS ="steps";
    private static final String STEP_DESCRIPTION ="stepDescription";

    public static ContentValues[] fetchJSONData(String json) {
        ContentValues[] exerciseContentValues = null;
        JSONArray exercises = null;
        try {

           // create the JSON object
           JSONObject jsonObject = new JSONObject(json);
           // get the exercises array
           exercises = jsonObject.getJSONArray(EXERCISES);
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
                   stepDesc = stepDesc + step.getString(STEP_DESCRIPTION);
                   Log.d(TAG,"Step Description is:" + stepDesc);
               }

               // from here how can we add the data back to the DB
               ContentValues exerciseValues = new ContentValues();
               // put the [ Column, Value ]
               /*
               builder2.append("CREATE TABLE IF NOT EXISTS " + FAV_TABLE)
                       .append(" (")
                       .append(ExerciseContract.ExerciseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,")
                       .append(ExerciseContract.ExerciseEntry.CATEGORY +  TEXT_NOT_NULL + ",")
                       .append(ExerciseContract.ExerciseEntry.CATEGORY_DESC +  TEXT_NOT_NULL + ",")
                       .append(ExerciseContract.ExerciseEntry.EXERCISE_ID + TEXT_NOT_NULL + ",")
                       .append(ExerciseContract.ExerciseEntry.EXERCISE_NAME +  TEXT_NOT_NULL + ",")
                       .append(ExerciseContract.ExerciseEntry.EXERCISE_DESCRIPTION +  TEXT_NOT_NULL + ",")
                       .append(ExerciseContract.ExerciseEntry.EXERCISE_STEPS +  TEXT_NOT_NULL + ",")
                       .append(ExerciseContract.ExerciseEntry.EXERCISE_IMAGE +  TEXT_NOT_NULL + ",")
                       .append(ExerciseContract.ExerciseEntry.EXERCISE_VIDEO +  TEXT_NOT_NULL)
                       .append(");");
                       */
               exerciseValues.put(ExerciseContract.ExerciseEntry.CATEGORY, category);
               exerciseValues.put(ExerciseContract.ExerciseEntry.CATEGORY_DESC, category_description);
               exerciseValues.put(ExerciseContract.ExerciseEntry.EXERCISE_ID, id);
               exerciseValues.put(ExerciseContract.ExerciseEntry.EXERCISE_NAME, name);
               exerciseValues.put(ExerciseContract.ExerciseEntry.EXERCISE_DESCRIPTION, description);
               exerciseValues.put(ExerciseContract.ExerciseEntry.EXERCISE_STEPS, stepDesc);
               exerciseValues.put(ExerciseContract.ExerciseEntry.EXERCISE_IMAGE, "");
               exerciseValues.put(ExerciseContract.ExerciseEntry.EXERCISE_VIDEO, "");

               // add into the ContentValues array
               exerciseContentValues[i] = exerciseValues;

           }

       } catch (Exception e) {
           e.printStackTrace();
       }

        return exerciseContentValues;

    }


}
