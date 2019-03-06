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

    public static class saveUsersAsyncTask extends AsyncTask<List<Result>, Void, Void> {
        private HomeContract.View homeView;
        ContentResolver contentResolver;

        public saveUsersAsyncTask(ContentResolver contentResolver, HomeContract.View homeView) {
            this.contentResolver = contentResolver;
            this.homeView = homeView;
        }


        @Override
        protected Void doInBackground(List<Result>... lists) {
            final List<Result> resultsF = lists[0];

            for (int i = 0; i < resultsF.size(); i++) {
                Gson gson = new Gson();
                String jsonS = gson.toJson(resultsF);
                ContentValues userCV = UserContract.UserEntry.prepareNewUser(
                        resultsF.get(i).getName().getFirst(), resultsF.get(i).getName().getLast(), jsonS);
                Uri newUser = contentResolver.insert(UserContract.UserEntry.CONTENT_URI, userCV);
                if (newUser == null) {
                    homeView.showError(NetworkError.BAD_REQUEST);
                    Log.d(TAG, "run: " + NetworkError.BAD_REQUEST.toString());
                }
            }
            Log.d(TAG, "run: Finish");

            return null;
        }
    }

    public static class getUsersAsyncTask extends AsyncTask<List<Result>, Void, Void> {
        private HomeContract.View homeView;
        ContentResolver contentResolver;
        private String [] columns = {UserContract.UserEntry._ID,UserContract.UserEntry.NAME_COLUMN,UserContract.UserEntry.LAST_COLUMN,UserContract.UserEntry.JSON_COLUMN};
        List<Result> list = new ArrayList<>();
        public getUsersAsyncTask(ContentResolver contentResolver, HomeContract.View homeView) {
            this.contentResolver = contentResolver;
            this.homeView = homeView;
        }

        public List<Result> getData(){
            return list;
        }


        @Override
        protected Void doInBackground(List<Result>... lists) {

            try {
                Cursor query = contentResolver.query(
                        UserContract.UserEntry.CONTENT_URI,
                        columns,null,null,null
                );

                int cantidad = query.getCount();
                for (int i = 0; i < cantidad; i++) {
                    query.moveToPosition(i);
                    String id = query.getString(query.getColumnIndexOrThrow(UserContract.UserEntry._ID));
                    String name = query.getString(query.getColumnIndexOrThrow(UserContract.UserEntry.NAME_COLUMN));
                    String last = query.getString(query.getColumnIndexOrThrow(UserContract.UserEntry.LAST_COLUMN ));
                    String json = query.getString(query.getColumnIndexOrThrow(UserContract.UserEntry.JSON_COLUMN));

                    if (json!= null){
                        Gson gson = new Gson();
                        Log.d(TAG, "getData: " + json);
                        JSONArray jsonArray = new JSONArray(json);
                        Result result  = gson.fromJson(jsonArray.getJSONObject(0).toString(), Result.class);
                        list.add(result);
                    }

                }
                Log.d(TAG, "getData: " + cantidad);
            }catch (Exception ex)
            {
                ex.printStackTrace();
                Log.d(TAG, "getData: " + ex.getMessage());
            }

            return null;
        }
    }


    public static class deleteAllUsersAsyncTask extends AsyncTask<Void, Void, Void> {
        private HomeContract.View homeView;
        ContentResolver contentResolver;

        public deleteAllUsersAsyncTask(ContentResolver contentResolver, HomeContract.View homeView) {
            this.contentResolver = contentResolver;
            this.homeView = homeView;
        }


        @Override
        protected Void doInBackground(Void... lists) {

            contentResolver.delete(UserContract.UserEntry.CONTENT_URI, null, null);
            Log.d(TAG, "run: Finish");
            return null;
        }
    }


    public static class fetchUsersAsyncTask extends AsyncTask<Integer, Void, Void> {
        private HomeContract.View homeView;
        private RandomAPIService randomAPIService;

        public fetchUsersAsyncTask(RandomAPIService randomAPIService, HomeContract.View homeView) {
            this.randomAPIService = randomAPIService;
            this.homeView = homeView;
        }


        @Override
        protected Void doInBackground(Integer... count) {
            randomAPIService.fetchRandomUsers(count[0], new RandomAPIService.RandomUsersCallback() {
                @Override
                public void OnUsersLoaded(List<Result> results) {
                    homeView.showUsers(results);
                }

                @Override
                public void OnAPIError(NetworkError error) {
                    homeView.showError(error);
                }
            });
            return null;
        }
    }
}
