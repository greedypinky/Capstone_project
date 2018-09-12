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
        if (dbHelper != null)
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
                Log.d(TAG, "QUERY:FAVORITE EXERCISE Query from " + ExerciseContract.ExerciseEntry.TABLE_EXERCISE);
                cursor = db.query(ExerciseContract.ExerciseEntry.TABLE_EXERCISE, projection, selection, selectionArgs, null, null, sortOrder);
                if (cursor != null) {
                    int count = cursor.getCount();
                    Log.d(TAG, "cursor count:" + count);
                }
                break;
                //return cursor;
            case FAV_EXERCISE_WITH_ID:
                Log.d(TAG, "QUERY:FAVORITE EXERCISE ID Query from " + ExerciseContract.ExerciseEntry.TABLE_EXERCISE);
                cursor = db.query(ExerciseContract.ExerciseEntry.TABLE_EXERCISE, projection, selection, selectionArgs, null, null, sortOrder);
                //return cursor;
                break;
            case ALL_EXERCISE:
                Log.d(TAG, "QUERY:ALL EXERCISE Query from " + ExerciseContract.ExerciseEntry.TABLE_ALL);
                cursor = db.query(ExerciseContract.ExerciseEntry.TABLE_ALL, projection, selection, selectionArgs, null, null, sortOrder);
                if (cursor != null) {
                    int count = cursor.getCount();
                    Log.d(TAG, "cursor count:" + count);
                }
                break;
               // return cursor;

            case ALL_EXERCISE_WITH_ID:
                //return null;
                break;
            default:
                throw new UnsupportedOperationException("Unable to update table by uri:" + uri);



        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;


    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder, @Nullable CancellationSignal cancellationSignal) {
        return super.query(uri, projection, selection, selectionArgs, sortOrder, cancellationSignal);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int type = uriMatcher.match(uri);
        switch (type) {

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

        Log.d(TAG, "insert by uri: " + uri);
        Log.d(TAG, "matcher: " + match);

        switch (match) {
           // case FAV_EXERCISE:
            case ALL_EXERCISE:
                db.beginTransaction();
                try {
                    // use the insert method to insert
                    long id = db.insert(ExerciseContract.ExerciseEntry.TABLE_ALL, null,
                            contentValues);
                    // if id is return
                    if (id > 0) {
                        // build the insert URI with the returned ID
                        insertUri = ExerciseContract.ExerciseEntry.buildAllExerciseUriWithId(id);
                        db.setTransactionSuccessful();
                        //return insertUri;
                        Log.d(TAG,"insert:notifyChanges!");
                        getContext().getContentResolver().notifyChange(uri,null);
                    } else {
                        throw new android.database.SQLException("unable to insert into table:" +
                                ExerciseContract.ExerciseEntry.TABLE_ALL);
                    }

                } finally {
                    db.endTransaction();
                }
                break;
            default:
                throw new UnsupportedOperationException("ERROR:::Insert failed:unsupported url");
        }

        return insertUri;

    }


    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        // TODO: Add back bulkInsert implementation
        Uri insertUri = null;
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Log.d(TAG, "insert by uri: " + uri);
        Log.d(TAG, "matcher: " + match);
        int rowsInserted = 0;

        switch (match) {
            case FAV_EXERCISE:
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
                    Log.d(TAG, String.format("bulk insert:notifyChanges of %d of rows is inserted!", rowsInserted));
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                //throw new UnsupportedOperationException("Unable to delete table by uri:" + uri);
                return super.bulkInsert(uri, values);
        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String whereClause, @Nullable String[] whereArgs) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);
        int deleteRowsNum = 0;

        Log.d(TAG, "delete by uri: " + uri);
        Log.d(TAG, "matcher: " + match);
        Log.d(TAG, "where clause: " + whereClause);

        switch (match) {
            case FAV_EXERCISE:
                deleteRowsNum = db.delete(ExerciseContract.ExerciseEntry.TABLE_EXERCISE, whereClause, whereArgs);
                break;
            case ALL_EXERCISE:
                deleteRowsNum = db.delete(ExerciseContract.ExerciseEntry.TABLE_ALL, whereClause, whereArgs);
                break;
            case FAV_EXERCISE_WITH_ID:
                db.beginTransaction();
                try {
                    if (whereArgs != null) {
                        Log.d(TAG, "delete row with id: " + whereArgs[0]);
                        deleteRowsNum = db.delete(ExerciseContract.ExerciseEntry.TABLE_EXERCISE, whereClause,
                                whereArgs);
                        db.setTransactionSuccessful();
                    } else {
                        deleteRowsNum = 0;
                    }

                } finally {
                    db.endTransaction();
                }
                break;
            default:
                throw new UnsupportedOperationException("Unable to delete table by uri:" + uri);
        }
        Log.d(TAG, "delete number of rows:" + deleteRowsNum);
        if (deleteRowsNum > 0) {
            Log.d(TAG, String.format("Delete :notifyChanges of %d of rows is deleted!!", deleteRowsNum));
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return deleteRowsNum;
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
                    db.setTransactionSuccessful();
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
                    db.setTransactionSuccessful();
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
                            whereClause, // baseColumn's ExerciseID, not _id
                            whereArgs);

                    // try to execute the SQL directly
//                   int fav = contentValues.getAsInteger(ExerciseContract.ExerciseEntry.EXERCISE_FAVORITE);
//                   // update AllExercise set favorite = '1'  where exerciseID = '1';
//                   String command =
//                           "update " + ExerciseContract.ExerciseEntry.TABLE_ALL + " set " + ExerciseContract.ExerciseEntry.EXERCISE_FAVORITE + " = '" + Integer.toString(fav) + "' "+ " where " + ExerciseContract.ExerciseEntry.EXERCISE_ID + " = '" + whereArgs[0] + "'";
//                   Log.d(TAG,">>>>>>> Update sql command is:" + command);
//                    db.execSQL(command);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
            default:
                throw new UnsupportedOperationException("Unable to update table by uri:" + uri);

        }

        if (updatedRowsNum > 0) {
            // notify the change
            Log.d(TAG, String.format("Update :notifyChanges of %d of rows is updated!!", updatedRowsNum));
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updatedRowsNum;
    }

}
