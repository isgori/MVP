package com.example.mvp.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UsersDBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "users.db";

    private static final String CREATE_USERS_TABLE = "CREATE TABLE "+ UserContract.UserEntry.TABLE_NAME + "( "
            + UserContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + UserContract.UserEntry.NAME_COLUMN + " TEXT, "
            + UserContract.UserEntry.LAST_COLUMN + " TEXT, "
            + UserContract.UserEntry.JSON_COLUMN + " TEXT);";

    private static final String DELETE_USERS = "DROP TABLE IF EXISTS " + UserContract.UserEntry.TABLE_NAME;

    public UsersDBHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_USERS);
        onCreate(db);
    }

}
