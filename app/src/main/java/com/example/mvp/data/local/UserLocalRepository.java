package com.example.mvp.data.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.example.mvp.data.UsersRepository;
import com.example.mvp.data.remote.randomapi.RandomAPI;
import com.example.mvp.data.remote.randomapi.RandomAPIService;
import com.example.mvp.data.remote.randomapi.to.Result;
import com.example.mvp.home.HomeContract;
import com.example.mvp.util.NetworkError;
import com.example.mvp.util.NetworkManager;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class UserLocalRepository implements UsersRepository {

    // provider
    public static final String TAG="HomePresenter_TAG";
    private HomeContract.View homeView;
    private RandomAPIService randomAPIService;
    private String [] columns = {UserContract.UserEntry._ID,UserContract.UserEntry.NAME_COLUMN,UserContract.UserEntry.LAST_COLUMN,UserContract.UserEntry.JSON_COLUMN};
    private ContentResolver contentResolver;

    public UserLocalRepository(HomeContract.View homeView, ContentResolver contentResolver) {
        this.homeView = homeView;
        this.contentResolver=contentResolver;
        NetworkManager networkManager = new NetworkManager();
        randomAPIService = new RandomAPIService(networkManager.provideRandomAPI(RandomAPI.BASE_URL));
    }

    @Override
    public void fetchUsers(int count) {
        randomAPIService.fetchRandomUsers(count, new RandomAPIService.RandomUsersCallback() {
            @Override
            public void OnUsersLoaded(List<Result> results) {
                homeView.showUsers(results);
            }

            @Override
            public void OnAPIError(NetworkError error) {
                homeView.showError(error);
            }
        });
    }

    @Override
    public void fetchUser(int idUser) {
        List<Result> list = new ArrayList<>();
        try {
            Cursor query = contentResolver.query(
                    UserContract.UserEntry.buildMovieUri(idUser),
                    columns,null,null,null
            );


            if (query!= null   ){
                query.moveToPosition(0);
                String json = query.getString(query.getColumnIndexOrThrow(UserContract.UserEntry.JSON_COLUMN));

                if (json!= null){
                    Gson gson = new Gson();
                    JSONArray jsonArray = new JSONArray(json);
                    Result result  = gson.fromJson(jsonArray.getJSONObject(0).toString(), Result.class);
                    list.add(result);
                }

            }

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    @Override
    public void saveUsers(List<Result> lists) {

        for (int i = 0; i < lists.size(); i++) {
            Gson gson = new Gson();
            String jsonS = gson.toJson(lists);
            ContentValues userCV = UserContract.UserEntry.prepareNewUser(
                    lists.get(i).getName().getFirst(), lists.get(i).getName().getLast(), jsonS);
            Uri newUser = contentResolver.insert(UserContract.UserEntry.CONTENT_URI, userCV);
            if (newUser == null) {
                homeView.showError(NetworkError.BAD_REQUEST);
            }
        }
    }

    @Override
    public void updateUser(Result entity,int id) {

        try {
            ContentValues contentValues = new ContentValues();
            Gson gson = new Gson();
            String jsonS = gson.toJson(entity);
            contentValues.put(UserContract.UserEntry.NAME_COLUMN,entity.getName().getFirst());
            contentValues.put(UserContract.UserEntry.LAST_COLUMN,entity.getName().getLast());
            contentValues.put(UserContract.UserEntry.JSON_COLUMN,jsonS);
            int query = contentResolver.update(
                    UserContract.UserEntry.buildMovieUri(id),
                    contentValues,
                    null,
                    null
            );

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteUser(Result entity,int id) {

        try {
            contentResolver.delete(UserContract.UserEntry.CONTENT_URI,  UserContract.UserEntry._ID+" = " + id  , null);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteUsers() {
        contentResolver.delete(UserContract.UserEntry.CONTENT_URI, null  , null);
    }

    @Override
    public List<Result> getUsers() {
        List<Result> list = new ArrayList<>();
        try {
            Cursor query = contentResolver.query(
                    UserContract.UserEntry.CONTENT_URI,
                    columns,null,null,null
            );

            int cantidad = query.getCount();
            for (int i = 0; i < cantidad; i++) {
                query.moveToPosition(i);
                String json = query.getString(query.getColumnIndexOrThrow(UserContract.UserEntry.JSON_COLUMN));
                if (json!= null){
                    Gson gson = new Gson();
                    JSONArray jsonArray = new JSONArray(json);
                    Result result  = gson.fromJson(jsonArray.getJSONObject(0).toString(), Result.class);
                    list.add(result);
                }

            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return  list;
    }

    @Override
    public void tearDown() {
        homeView =null;
    }
}
