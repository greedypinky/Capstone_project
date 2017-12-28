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


public class ExerciseContentProvider extends ContentProvider {

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
        String tableName = ExerciseContract.ExerciseEntry.TABLE_EXERCISE;
        matcher.addURI(authority, tableName, FAV_EXERCISE);
        matcher.addURI(authority,tableName + "/#", FAV_EXERCISE_WITH_ID);
        return null;

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

        return null;
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
          case FAV_EXERCISE:
              return ExerciseContract.ExerciseEntry.CONTENT_DIR_FAV;

          case FAV_EXERCISE_WITH_ID:
              return ExerciseContract.ExerciseEntry.CONTENT_ITEM_FAV;

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
        switch (match) {

           case FAV_EXERCISE:
               // use the insert method to insert
              long id = db.insert(ExerciseContract.ExerciseEntry.TABLE_EXERCISE,
               null,
               contentValues);
                // if id is return
               if (id > 0) {
                   // build the insert URI with the returned ID
                  insertUri =  ExerciseContract.ExerciseEntry.buildFavoriteExerciseUriWithId(id);
                  return insertUri;
               } else {

                   throw new android.database.SQLException("unable to insert into table:" +
                           ExerciseContract.ExerciseEntry.TABLE_EXERCISE);
               }

           default:
               throw new UnsupportedOperationException("insert fail-unsupported url: + url");

       }



    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        // TODO: Add back bulkInsert implementation
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

        return super.bulkInsert(uri, values);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String whereClause, @Nullable String[] whereArgs) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);
        int deleteRowsNum = 0;

        switch (match) {
            case FAV_EXERCISE:
                deleteRowsNum  = db.delete(ExerciseContract.ExerciseEntry.TABLE_EXERCISE, whereClause, whereArgs);
                break;

            case ALL_EXERCISE:
                deleteRowsNum  = db.delete(ExerciseContract.ExerciseEntry.TABLE_ALL, whereClause, whereArgs);
                break;
            case FAV_EXERCISE_WITH_ID:
                deleteRowsNum  = db.delete(ExerciseContract.ExerciseEntry.TABLE_EXERCISE,
                        ExerciseContract.ExerciseEntry._ID + " =?",
                         new String[] { String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new UnsupportedOperationException("Unable to delete table by uri:" + uri);
        }
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
                updatedRowsNum = db.update(ExerciseContract.ExerciseEntry.TABLE_EXERCISE, contentValues, whereClause, whereArgs);
                break;
            case FAV_EXERCISE_WITH_ID:
                updatedRowsNum = db.update(ExerciseContract.ExerciseEntry.TABLE_EXERCISE,
                         contentValues,
                        ExerciseContract.ExerciseEntry._ID + " =?", // baseColumn's ID
                         new String[] { String.valueOf(ContentUris.parseId(uri)) }
                         );
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
