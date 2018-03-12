package com.project.capstone_stage2.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.project.capstone_stage2.R;
import com.project.capstone_stage2.dbUtility.ExerciseContract;

import static android.content.ContentValues.TAG;

/**
 * Created by ritalaw on 2018-03-03.
 */

public class ExerciseUtil {


    // public void updateAllExerciseFavoriteCol(Uri updateURI,String exerciseID, ContentValues contentValues){
    public static void updateAllExerciseFavoriteCol(Fragment fragment, Uri updateURI, ContentValues contentValues, String exeID, String category){
        // TODO: need to refresh the list after a list is deleted
        //Log.e(TAG, "reload the list after removal of the item!");
        // Uri updateURI = ExerciseContract.ExerciseEntry.CONTENT_URI_ALL;
        //String whereClause = ExerciseContract.ExerciseEntry._ID + " = ?";
        String whereClause = ExerciseContract.ExerciseEntry.EXERCISE_ID + " = ? AND " + ExerciseContract.ExerciseEntry.CATEGORY + " = ?";
        //String whereClause = ExerciseContract.ExerciseEntry.EXERCISE_ID + " = ?";
        //int updateRow = getActivity().getContentResolver().update(updateURI, contentValues, whereClause, new String[]{id});
        //int updateRow = getActivity().getContentResolver().update(updateURI, contentValues, whereClause, new String[]{exeID});

        int updateRow = fragment.getActivity().getContentResolver().update(updateURI, contentValues, whereClause, new String[]{exeID,category});
        Log.e(fragment.getTag(), "updateAllExerciseFavoriteCol #of row:" + updateRow);
    }

    public static void toggleButtonDisable(boolean disable, Button favButton){
        if (disable) {
            Log.d(TAG, "expect to setEnabled false!");
            Log.d(TAG, "disable the Add Button by flag:- " + !disable);
            favButton.setAlpha(0.5f);
            favButton.setEnabled(!disable); // set false

            Log.d(TAG, "what is the result , is the button Enabled?" + favButton.isEnabled());

        } else {
            Log.d(TAG, "Enable the Add Button!");
            favButton.setAlpha(1f);
            favButton.setEnabled(!disable); // set true
        }
    }

    public Cursor checkAlreadyInsertAsFavorite(Fragment fragment,String exerciseID) {
        Uri uri = ExerciseContract.ExerciseEntry.CONTENT_URI_FAV;
        String[] projections = {ExerciseContract.ExerciseEntry.EXERCISE_ID, ExerciseContract.ExerciseEntry.EXERCISE_NAME};
        String selection = ExerciseContract.ExerciseEntry.EXERCISE_ID + "=?";
        String[] selectionArgs = {exerciseID};
        try {
            Cursor cursor = fragment.getActivity().getContentResolver().query(uri,projections,selection,selectionArgs,null);
            //if (cursor.getCount() == 1) {
            if (cursor.getCount() > 0) {
                Log.d(TAG,"Assert if favorite is added ::: checkAlreadyInsertAsFavorite:" + cursor.getCount());
                return cursor;
            }  else {
                Log.d(TAG,"Error:::unable to query the added favorite exercise!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "checkAlreadyInsert:Unable to query the inserted data using id:");
        }
        return null;
    }


    public static void onShareClick(String cat,String exeName,String exeSteps,String exeVideoURL,Context context,Fragment fragment) {
        // Add analytics to track which exercise is shared
        //Toast.makeText(getContext(),"share content!", Toast.LENGTH_LONG).show();
        Bundle bundle = new Bundle();

        StringBuilder builder = new StringBuilder();
        builder.append("Exercise Category:" + cat + "\n")
               .append("Exercise Name:" + exeName + "\n")
                .append("Exercise Steps:" + exeSteps + "\n");
        if (exeVideoURL!=null && !exeVideoURL.isEmpty()) {
            builder.append("Please check out our exercise by this link: " + exeVideoURL + "\n");
        }

        // TODO: add analytics when share button is clicked
        /*        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Share Exercise Name:" + exeName)
                .setAction("Share")
                .build());*/

        context.startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(fragment.getActivity())
                .setType("text/plain")
                .setChooserTitle(context.getString(R.string.share_exercise_title))
                .setText(builder.toString())
                .getIntent(), context.getString(R.string.share_exercise_sendTo)));
    }




}
