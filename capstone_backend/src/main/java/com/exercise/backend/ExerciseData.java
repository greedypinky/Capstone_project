package com.exercise.backend;
import com.exercise.backend.Category;

/**
 * The object model for the data we are sending through endpoints
 */
public class ExerciseData {

    private String exerciseByCategoryJSON = null;

    public String getData() {

        return exerciseByCategoryJSON;
    }

    public void setData(String categoryName)
    {
        exerciseByCategoryJSON = Category.getHashMap().get(categoryName);

    }
}