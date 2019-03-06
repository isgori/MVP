package com.example.mvp.data.local;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class UserProvider extends ContentProvider {

    private static final int USER = 100;
    private static final int USER_ID = 101;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private UsersDBHelper usersDBHelper;

    @Override
    public boolean onCreate() {
        usersDBHelper = new UsersDBHelper(getContext());
        return true;
    }

    public static UriMatcher buildUriMatcher() {
        String content = UserContract.CONTENT_AUTHORITY;

        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(content, UserContract.PATH_USER, USER);
        matcher.addURI(content, UserContract.PATH_USER+"/#", USER_ID);

        return matcher;
    }

    @Nullable
    @Override
    public String getType( @NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case USER:
                return UserContract.UserEntry.CONTENT_TYPE;
            case USER_ID:
                return UserContract.UserEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query( @NonNull Uri uri,  @Nullable String[] projection,  @Nullable String selection,  @Nullable String[] selectionArgs,  @Nullable String sortOrder) {
        final SQLiteDatabase db = usersDBHelper.getWritableDatabase();
        Cursor queryCursor;

        switch (uriMatcher.match(uri)) {
            case USER:
                queryCursor = db.query(
                        UserContract.UserEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case USER_ID:
                long _id = ContentUris.parseId(uri);
                queryCursor = db.query(
                        UserContract.UserEntry.TABLE_NAME,
                        projection,
                        UserContract.UserEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw  new UnsupportedOperationException("Unknown URI " + uri);

        }
        queryCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return queryCursor;
    }

    @Nullable
    @Override
    public Uri insert( @NonNull Uri uri,  @Nullable ContentValues values) {
        final SQLiteDatabase db = usersDBHelper.getWritableDatabase();
        long _id;
        Uri returnUri;

        switch (uriMatcher.match(uri)) {
            case USER:
                _id = db.insert(
                        UserContract.UserEntry.TABLE_NAME,
                        null,
                        values
                );
                if(_id > 0) {
                    returnUri = UserContract.UserEntry.buildMovieUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI " + uri);
        }

        return returnUri;
    }

    @Override
    public int delete( @NonNull Uri uri,  @Nullable String selection,  @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = usersDBHelper.getWritableDatabase();
        int deletedRqws;

        switch (uriMatcher.match(uri)) {
            case USER:
                deletedRqws = db.delete(
                        UserContract.UserEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI " + uri);
        }

        if(selection == null || deletedRqws != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deletedRqws;
    }

    @Override
    public int update( @NonNull Uri uri,  @Nullable ContentValues values,  @Nullable String selection,  @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = usersDBHelper.getWritableDatabase();
        int updatedRows;

        switch (uriMatcher.match(uri)) {
            case USER:
                updatedRows = db.update(
                        UserContract.UserEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI " + uri);
        }

        if(updatedRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updatedRows;
    }
}
