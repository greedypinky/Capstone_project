package com.project.capstone_stage2.util;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/*
        "category": "Squat",
        "category description": "Squat",
        "id": 2,
        "name": "Split Squat",
        "description": "Level: Intermediate | Equipment: Bench, Dumbbells | Body parts: Glutes, Hamstrings, Quads",
        "image": "http://atlasonlinefitness.com/wp-content/uploads/2018/03/nao-squat02.png",
        "video": "bpRK5bHLIYA",
        "steps": [
        {
        "stepDescription": "Step1:Hold a dumbbell in each hand with your arms fully extended at your sides and your palms facing each other. With your feet hip-width apart, place the instep of your rear foot on a bench. Your feet should be approximately three feet apart."
        },
        {
        "stepDescription": "Step2:Lower your hips toward the floor so that your rear knee comes close to the floor. Pause and drive through your front heel to return to the starting position."
        }
        ]
*/
public class Exercise implements Parcelable{


    private String mID = null;
    private String mCategory = null;
    private String mCategoryDesc = null;
    private String mExerciseName = null;
    private String mExerciseDesc = null;
    private String mImageURI = null;

    private String mVideoURL = null;
    private String mSteps = null;

    public static final Parcelable.Creator<Exercise> CREATOR = new Parcelable.Creator<Exercise>()  {
        @Override
        public Exercise createFromParcel(Parcel parcel) {
            return new Exercise(parcel);
        }

        @Override
        public Exercise[] newArray(int i) {
            return new Exercise[i];
        }
    };

    // Default Constructor
    public Exercise() {
    }

    // Constructor
    public Exercise(String id,String category, String categoryDesc, String name, String desc, String imageURI, String videoURL) {
        mID = id;
        mCategory = category;
        mCategoryDesc = desc;
        mExerciseName = name;
        mExerciseDesc = desc;
        mImageURI = imageURI;
        mVideoURL = videoURL;
    }

    public Exercise(Parcel in) {
        mID = in.readString();
        mCategory = in.readString();
        mCategoryDesc = in.readString();
        mExerciseName = in.readString();
        mExerciseDesc = in.readString();
        mImageURI = in.readString();
        mVideoURL = in.readString();
        mSteps = in.readString();
        //in.readStringList(mSteps);
    }

    public String getExerciseDesc() {
        return mExerciseDesc;
    }

    public void setExerciseDesc(String exerciseDesc) {
        this.mExerciseDesc = exerciseDesc;
    }

    public String getImageURI() {
        return mImageURI;
    }

    public void setImageURI(String exerciseImageURI) {
        this.mImageURI = exerciseImageURI;
    }


    public String getExerciseName() {
        return mExerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.mExerciseName = exerciseName;
    }

    public String getID() {
        return mID;
    }

    public void setID(String mID) {
        this.mID = mID;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public String getCategoryDesc() {
        return mCategoryDesc;
    }

    public void setCategoryDesc(String mCategoryDesc) {
        this.mCategoryDesc = mCategoryDesc;
    }

    public String getVideoURL() {
        return mVideoURL;
    }

    public void setVideoURL(String mVideoURL) {
        this.mVideoURL = mVideoURL;
    }

    public String getSteps() {
        return mSteps;
    }

    public void setSteps(String mSteps) {
        this.mSteps = mSteps;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mID);
        parcel.writeString(mCategory);
        parcel.writeString(mCategoryDesc);
        parcel.writeString(mExerciseName);
        parcel.writeString(mExerciseDesc);
        parcel.writeString(mImageURI);
        parcel.writeString(mVideoURL);
        parcel.writeString(mSteps);
    }

}


