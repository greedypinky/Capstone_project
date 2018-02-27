package com.project.capstone_stage2.dbUtility;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


public class ExerciseContentProvider extends ContentProvider {

    private String TAG = ExerciseContentProvider.class.getSimpleName();

    // UriMatcher Code
    private static final int FAV_EXERCISE = 1;
    private static final int FAV_EXERCISE_WITH_ID = 2;
    private static final int FAV_EXERCISE_SEARCH_ID = 3;
    private static final int ALL_EXERCISE = 4;
    private static final int ALL_EXERCISE_WITH_ID = 5;
    private static final int ALL_EXERCISE_SEARCH_ID = 6;
    private UriMatcher uriMatcher = buildUriMatch();
    private DBHelper dbHelper;


    private static UriMatcher buildUriMatch() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = ExerciseContract.CONTENT_AUTHORITY;
        // String tableName = ExerciseContract.ExerciseEntry.TABLE_EXERCISE;
        matcher.addURI(authority, ExerciseContract.ExerciseEntry.TABLE_ALL, ALL_EXERCISE);
        matcher.addURI(authority, ExerciseContract.ExerciseEntry.TABLE_ALL + "/#", ALL_EXERCISE_WITH_ID);
        matcher.addURI(authority, ExerciseContract.ExerciseEntry.TABLE_EXERCISE, FAV_EXERCISE);
        matcher.addURI(authority, ExerciseContract.ExerciseEntry.TABLE_EXERCISE + "/#", FAV_EXERCISE_WITH_ID);
        return matcher;

    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(this.getContext());
        if(dbHelper!=null)
            return true;
        else
            return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        // TODO: reason why we cannot query the data from loadermanager because we forgot to implement the QUERY Method !!

        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);
        Cursor cursor = null;

        switch (match) {
            case FAV_EXERCISE:
                Log.d(TAG,"QUERY:FAVORITE EXERCISE Query from " + ExerciseContract.ExerciseEntry.TABLE_EXERCISE);
                cursor = db.query(ExerciseContract.ExerciseEntry.TABLE_EXERCISE ,projection, selection, selectionArgs,null,null, sortOrder);
                if(cursor!=null) {
                   int count = cursor.getCount();
                    Log.d(TAG,"cursor count:" + count);
                }
                return cursor;
            case FAV_EXERCISE_WITH_ID:
                Log.d(TAG,"QUERY:FAVORITE EXERCISE ID Query from " + ExerciseContract.ExerciseEntry.TABLE_EXERCISE);
                cursor = db.query(ExerciseContract.ExerciseEntry.TABLE_EXERCISE ,projection, selection, selectionArgs,null,null, sortOrder);
                return cursor;
            case ALL_EXERCISE:
                Log.d(TAG,"QUERY:ALL EXERCISE Query from " + ExerciseContract.ExerciseEntry.TABLE_ALL);
                cursor = db.query(ExerciseContract.ExerciseEntry.TABLE_ALL,projection, selection, selectionArgs,null,null, sortOrder);
                if(cursor!=null) {
                    int count = cursor.getCount();
                    Log.d(TAG,"cursor count:" + count);
                }
                return cursor;

            case ALL_EXERCISE_WITH_ID:
                return null;
            default:
                throw new UnsupportedOperationException("Unable to update table by uri:" + uri);

        }



    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder, @Nullable CancellationSignal cancellationSignal) {
        return super.query(uri, projection, selection, selectionArgs, sortOrder, cancellationSignal);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
      int type =  uriMatcher.match(uri);
      switch(type) {

          //vnc.android.cursor.dir
          case FAV_EXERCISE:
              return ExerciseContract.ExerciseEntry.CONTENT_DIR_FAV;

          //vnc.android.cursor.item
          case FAV_EXERCISE_WITH_ID:
              return ExerciseContract.ExerciseEntry.CONTENT_ITEM_FAV;

          case ALL_EXERCISE:
              return ExerciseContract.ExerciseEntry.CONTENT_DIR_ALL;

          case ALL_EXERCISE_WITH_ID:
              return ExerciseContract.ExerciseEntry.CONTENT_ITEM_ALL;

          default:
              throw new UnsupportedOperationException("Unable to get type because of unknown uri:" + uri);
      }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        Uri insertUri = null;
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);

        Log.d(TAG,"insert by uri: " + uri);
        Log.d(TAG,"matcher: " + match);

        switch (match) {
           case FAV_EXERCISE:
             //  db.beginTransaction();
             //  try {
                   // use the insert method to insert
                   long id = db.insert(ExerciseContract.ExerciseEntry.TABLE_EXERCISE, null,
                           contentValues);
                   // if id is return
                   if (id > 0) {
                       // build the insert URI with the returned ID
                       insertUri = ExerciseContract.ExerciseEntry.buildFavoriteExerciseUriWithId(id);
                       //return insertUri;
                   } else {
                       throw new android.database.SQLException("unable to insert into table:" +
                               ExerciseContract.ExerciseEntry.TABLE_EXERCISE);
                   }
                   //db.setTransactionSuccessful();
             //  } finally {
             //      db.endTransaction();
             //  }
               break;
           default:
               throw new UnsupportedOperationException("insert fail-unsupported url");
       }

       return insertUri;

    }

    /*
    1-06 13:18:49.261 2545-3779/com.project.capstone_stage2 E/SQLiteDatabase: Error inserting name=Squat1 categoryDesc=Squat steps=step1step2 description= category=Squat exerciseID=1 image= video=
    android.database.sqlite.SQLiteException: table AllExercise has no column named categoryDesc (code 1): , while compiling:
    INSERT INTO AllExercise(name,categoryDesc,steps,description,category,exerciseID,image,video)
    VALUES (?,?,?,?,?,?,?,?)
   */
    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        // TODO: Add back bulkInsert implementation
        Uri insertUri = null;
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Log.d(TAG,"insert by uri: " + uri);
        Log.d(TAG,"matcher: " + match);
        int rowsInserted = 0;

        // db.query("")

        switch (match) {
            case FAV_EXERCISE:
//                db.beginTransaction();
//                try {
//                    for (ContentValues value : values) {
//                        // insert a row and return the id of the row
//                        /*
//                        builder.append("CREATE TABLE IF NOT EXISTS " + ALL_TABLE)
//                                .append(" (")
//                                .append(ExerciseContract.ExerciseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,")
//                                .append(ExerciseContract.ExerciseEntry.CATEGORY +  TEXT_NOT_NULL + ",")
//                                .append(ExerciseContract.ExerciseEntry.CATEGORY_DESC +  TEXT_NOT_NULL + ",")
//                                .append(ExerciseContract.ExerciseEntry.EXERCISE_ID + TEXT_NOT_NULL + ",")
//                                .append(ExerciseContract.ExerciseEntry.EXERCISE_NAME +  TEXT_NOT_NULL + ",")
//                                .append(ExerciseContract.ExerciseEntry.EXERCISE_DESCRIPTION +  TEXT_NOT_NULL + ",")
//                                .append(ExerciseContract.ExerciseEntry.EXERCISE_STEPS +  TEXT_NOT_NULL + ",")
//                                .append(ExerciseContract.ExerciseEntry.EXERCISE_IMAGE +  TEXT_NOT_NULL + ",")
//                                .append(ExerciseContract.ExerciseEntry.EXERCISE_VIDEO +  TEXT_NOT_NULL)
//                                .append(");");
//                                */
//
//                        long _id = db.insert(ExerciseContract.ExerciseEntry.TABLE_EXERCISE,null,
//                            value);
//
//                        Log.d(TAG, "bulk insert ContentValues:" + _id);
//                        if (_id != -1) {
//                            rowsInserted++;
//                            Log.d(TAG, "total bulk insert num:" + rowsInserted);
//                        }
//                    }
//                    db.setTransactionSuccessful();
//                } finally {
//                    db.endTransaction();
//                }
//
//                if (rowsInserted > 0) {
//                    // need to notify the data change is happnened to URI uri
//                    getContext().getContentResolver().notifyChange(uri, null);
//                }
//
//                Log.d(TAG,"bulk insert:" + rowsInserted);
//                return rowsInserted; // need to return the number of rows that have been inserted

                return -1;

            case ALL_EXERCISE:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(ExerciseContract.ExerciseEntry.TABLE_ALL, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                //throw new UnsupportedOperationException("Unable to delete table by uri:" + uri);
                return super.bulkInsert(uri, values);
        }

       // return deleteRowsNum ;
/*
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case CODE_WEATHER:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long weatherDate =
                                value.getAsLong(WeatherContract.WeatherEntry.COLUMN_DATE);
                        if (!SunshineDateUtils.isDateNormalized(weatherDate)) {
                            throw new IllegalArgumentException("Date must be normalized to insert");
                        }

                        long _id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }

        */


    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String whereClause, @Nullable String[] whereArgs) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);
        int deleteRowsNum = 0;

        Log.d(TAG,"delete by uri: " + uri);
        Log.d(TAG,"matcher: " + match);
        Log.d(TAG,"where clause: " + whereClause);

        switch (match) {
            case FAV_EXERCISE:
                deleteRowsNum  = db.delete(ExerciseContract.ExerciseEntry.TABLE_EXERCISE, whereClause, whereArgs);
                break;
            case ALL_EXERCISE:
                deleteRowsNum  = db.delete(ExerciseContract.ExerciseEntry.TABLE_ALL, whereClause, whereArgs);
                break;
            case FAV_EXERCISE_WITH_ID:
                if (whereArgs != null) {
                    Log.d(TAG, "delete row with id: " + whereArgs[0]);
                    deleteRowsNum = db.delete(ExerciseContract.ExerciseEntry.TABLE_EXERCISE, whereClause,
                            whereArgs);
                } else {
                    deleteRowsNum = 0;
                }
                break;
            default:
                throw new UnsupportedOperationException("Unable to delete table by uri:" + uri);
        }
        Log.d(TAG,"delete number of rows:" + deleteRowsNum);
        return deleteRowsNum ;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String whereClause, @Nullable String[] whereArgs) {

        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);
        int updatedRowsNum = 0;

        if (contentValues == null) {
            throw new IllegalArgumentException("ContentValues cannot be null!");
        }

        switch (match) {
            case FAV_EXERCISE:
                db.beginTransaction();
                try {
                    updatedRowsNum = db.update(ExerciseContract.ExerciseEntry.TABLE_EXERCISE, contentValues, whereClause, whereArgs);
                    //db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
            case FAV_EXERCISE_WITH_ID:
                db.beginTransaction();
                try {
                    updatedRowsNum = db.update(ExerciseContract.ExerciseEntry.TABLE_EXERCISE,
                            contentValues,
                            ExerciseContract.ExerciseEntry._ID + " =?", // baseColumn's ID
                            new String[]{String.valueOf(ContentUris.parseId(uri))});
                    //db.setTransactionSuccessful();
                } finally {

                    db.endTransaction();
                }
                break;
            case ALL_EXERCISE:
                db.beginTransaction();
                try {
                    Log.d(TAG, "update all exercise");
                    Log.d(TAG, "contentValues:" + contentValues.toString());
                    Log.d(TAG, "where " + ExerciseContract.ExerciseEntry.EXERCISE_ID + " = " + whereArgs[0]);
                    updatedRowsNum = db.update(ExerciseContract.ExerciseEntry.TABLE_ALL,
                            contentValues,
                            ExerciseContract.ExerciseEntry.EXERCISE_ID + " =?", // baseColumn's ExerciseID, not _id
                             whereArgs);
                    //db.setTransactionSuccessful();
                } finally {

                    db.endTransaction();
                }
                break;
            default:
                throw new UnsupportedOperationException("Unable to update table by uri:" + uri);

       }

       if (updatedRowsNum > 0) {
            // notify the change
           getContext().getContentResolver().notifyChange(uri,null);
       }
       return updatedRowsNum;
    }

}
