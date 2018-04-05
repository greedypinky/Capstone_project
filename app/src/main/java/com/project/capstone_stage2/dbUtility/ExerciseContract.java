package com.project.capstone_stage2.dbUtility;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


public class ExerciseContract {

    public static final String CONTENT_AUTHORITY = "com.project.capstone_stage2.app.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class ExerciseEntry implements BaseColumns {

        public static final String TABLE_ALL = "AllExercise";
        public static final String TABLE_EXERCISE = "FavoriteExercise";
        // declare the columns of the table
        public static final String EXERCISE_ID = "exerciseID"; // unique exercise id
        public static final String CATEGORY = "category";
        public static final String CATEGORY_DESC = "categoryDesc";
        public static final String EXERCISE_NAME = "name";
        public static final String EXERCISE_DESCRIPTION = "description";
        public static final String EXERCISE_STEPS = "steps";
        public static final String EXERCISE_IMAGE = "image";
        public static final String EXERCISE_VIDEO = "video";
        public static final String EXERCISE_FAVORITE = "favorite";

        // define the content uri build by the content authority + table name
        public static final Uri CONTENT_URI_ALL = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_ALL).build();
        public static final Uri CONTENT_URI_FAV = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_EXERCISE).build();
        // define the type of the table URI : dir
        // vnc.android.cursor.dir
        public static final String CONTENT_DIR_FAV = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_EXERCISE;
        // define the type of the table URI : by item
        // vnc.android.cursor.item
        public static final String CONTENT_ITEM_FAV = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_EXERCISE;

        // define the type of the All Exercise table URI : dir
        // vnd.android.cursor.dir/<Content authority>/<tablename>
        public static final String CONTENT_DIR_ALL = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_ALL;
        // define the type of the All Exercise table URI : by item
        // vnd.android.cursor.item/<Content authority>/<tablename>
        public static final String CONTENT_ITEM_ALL = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_ALL;


        /**
         * buildAllExerciseUriWithId
         *
         * @param id
         * @return
         */
        public static Uri buildAllExerciseUriWithId(long id) {

            return ContentUris.withAppendedId(CONTENT_URI_ALL, id);
        }

        /**
         * buildFavoriteExerciseUriWithId
         *
         * @param id
         * @return
         */
        public static Uri buildFavoriteExerciseUriWithId(long id) {

            return ContentUris.withAppendedId(CONTENT_URI_FAV, id);
        }


    }

}
