package com.example.mvp.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mvp.data.local.UserContract;
import com.example.mvp.data.remote.randomapi.RandomAPIService;
import com.example.mvp.data.remote.randomapi.to.Result;
import com.example.mvp.home.HomeContract;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class AsynMethods {


    public static final String TAG = "HomePresenter_TAG";

}
