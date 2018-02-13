package com.project.capstone_stage2.util;
import android.os.Parcel;
import android.os.Parcelable;

public class Exercise implements Parcelable{
    private String mExerciseName = null;
    private String mExerciseDesc = null;
    private String mExerciseImageURI = null;
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

    // Constructor
    public Exercise(String name, String desc, String imageURI) {
        mExerciseName = name;
        mExerciseDesc = desc;
        mExerciseImageURI = imageURI;
    }

    public Exercise() {

    }

    public Exercise(Parcel in ) {
        mExerciseName = in.readString();
        mExerciseDesc = in.readString();
        mExerciseImageURI = in.readString();
    }

    public String getExerciseDesc() {
        return mExerciseDesc;
    }

    public void setExerciseDesc(String exerciseDesc) {
        this.mExerciseDesc = exerciseDesc;
    }

    public String getExerciseImageURI() {
        return mExerciseImageURI;
    }

    public void setExerciseImageURI(String exerciseImageURI) {
        this.mExerciseImageURI = exerciseImageURI;
    }


    public String getExerciseName() {
        return mExerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.mExerciseName = exerciseName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mExerciseName);
        parcel.writeString(mExerciseDesc);
        parcel.writeString(mExerciseImageURI);
    }

}


