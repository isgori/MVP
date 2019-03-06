package com.example.mvp.data.local;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

public class UserContract {
    public static final String CONTENT_AUTHORITY = "com.example.mvp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_USER = "user";

    public static final class UserEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_USER;

        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_USER;

        public static final String TABLE_NAME = "users";
        public static final String NAME_COLUMN = "name_u";
        public static final String LAST_COLUMN = "last_u";
        public static final String JSON_COLUMN = "json_u";


        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


        public static ContentValues prepareNewUser (String name, String last, String json) {
            ContentValues userCV = new ContentValues();
            userCV.put(UserContract.UserEntry.NAME_COLUMN, name);
            userCV.put(UserEntry.LAST_COLUMN, last);
            userCV.put(UserEntry.JSON_COLUMN, json);
            return userCV;
        }
    }
}
