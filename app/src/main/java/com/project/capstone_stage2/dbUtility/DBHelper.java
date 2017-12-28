package com.project.capstone_stage2.dbUtility;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getSimpleName();
    private static final String DB_NAME = "exercise.db";
    private static final String ALL_TABLE = ExerciseContract.ExerciseEntry.TABLE_ALL;
    private static final String FAV_TABLE = ExerciseContract.ExerciseEntry.TABLE_EXERCISE;
    private static final int VERSION = 1;

    private static final String TEXT_NOT_NULL = " TEXT NOT NULL";
    private static final String INTEGER_NOT_NULL = " INTEGER NOT NULL";

    /**
     * Constructor
     * @param context
     */
    public DBHelper(Context context) {
        // CursorFactory is null
        super(context,DB_NAME,null,VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        StringBuilder builder = new StringBuilder();
        // create table tablename ID INTEGER PRIMARY KEY AUTOINCREMENT (COL1, COL2, COL3);
        builder.append("CREATE TABLE IF NOT EXISTS " + ALL_TABLE)
                .append(" (")
                .append(ExerciseContract.ExerciseEntry.EXERCISE_ID)
                .append("INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(ExerciseContract.ExerciseEntry.EXERCISE_NAME +  TEXT_NOT_NULL + ",")
                .append(ExerciseContract.ExerciseEntry.EXERCISE_DESCRIPTION +  TEXT_NOT_NULL + ",")
                .append(ExerciseContract.ExerciseEntry.EXERCISE_STEPS +  TEXT_NOT_NULL + ",")
                .append(ExerciseContract.ExerciseEntry.EXERCISE_IMAGE +  TEXT_NOT_NULL + ",")
                .append(ExerciseContract.ExerciseEntry.EXERCISE_VIDEO +  TEXT_NOT_NULL + ",")
                .append(");");

        final String SQL_CREATE_TABLE1 = builder.toString();
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE1);

        StringBuilder builder2 = new StringBuilder();
        // create table tablename ID INTEGER PRIMARY KEY AUTOINCREMENT (COL1, COL2, COL3);
        builder2.append("CREATE TABLE IF NOT EXISTS " + FAV_TABLE)
                .append(" (")
                .append(ExerciseContract.ExerciseEntry.EXERCISE_ID)
                .append("INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(ExerciseContract.ExerciseEntry.EXERCISE_NAME +  TEXT_NOT_NULL + ",")
                .append(ExerciseContract.ExerciseEntry.EXERCISE_DESCRIPTION +  TEXT_NOT_NULL + ",")
                .append(ExerciseContract.ExerciseEntry.EXERCISE_STEPS +  TEXT_NOT_NULL + ",")
                .append(ExerciseContract.ExerciseEntry.EXERCISE_IMAGE +  TEXT_NOT_NULL + ",")
                .append(ExerciseContract.ExerciseEntry.EXERCISE_VIDEO +  TEXT_NOT_NULL + ",")
                .append(");");

        final String SQL_CREATE_TABLE2 = builder2.toString();
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");
        // Drop the table1
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ALL_TABLE);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                ALL_TABLE + "'");

        // Drop the table2
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FAV_TABLE);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                FAV_TABLE + "'");

        // re-create database
        onCreate(sqLiteDatabase);
    }
}
