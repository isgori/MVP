package com.example.mvp.data.remote.randomapi;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mvp.data.UsersRepository;
import com.example.mvp.data.local.UserContract;
import com.example.mvp.data.remote.randomapi.to.Result;
import com.example.mvp.home.HomeContract;
import com.example.mvp.util.AsynMethods;
import com.example.mvp.util.NetworkError;
import com.example.mvp.util.NetworkManager;

import java.util.List;

public class UserRemoteRepository implements UsersRepository {

    private HomeContract.View homeView;
    private RandomAPIService randomAPIService;
    private String [] columns = {UserContract.UserEntry._ID,UserContract.UserEntry.NAME_COLUMN,UserContract.UserEntry.LAST_COLUMN,UserContract.UserEntry.JSON_COLUMN};
    private ContentResolver contentResolver;

    public UserRemoteRepository(HomeContract.View homeView, ContentResolver contentResolver) {
        this.homeView = homeView;
        this.contentResolver=contentResolver;
        NetworkManager networkManager = new NetworkManager();
        randomAPIService = new RandomAPIService(networkManager.provideRandomAPI(RandomAPI.BASE_URL));
    }

    @Override
    public void fetchUsers(int count) {
        new AsynMethods.fetchUsersAsyncTask(randomAPIService,homeView).execute(count);
    }

    @Override
    public void fetchUser(int idUser) {
        new AsynMethods.fetchUserAsyncTask(contentResolver,homeView).execute(idUser);
    }

    @Override
    public void saveUsers(List<Result> lists) {
        new AsynMethods.saveUsersAsyncTask(contentResolver,homeView).execute(lists);
    }

    @Override
    public void updateUser(int user) {

    }

    @Override
    public void deleteUser(int user) {
        new AsynMethods.deleteUserAsyncTask(contentResolver).execute(user);

    }

    @Override
    public void deleteUsers() {
        new AsynMethods.deleteAllUsersAsyncTask(contentResolver).execute();

    }

    @Override
    public List<Result> getUsers() {
        AsynMethods.getUsersAsyncTask holis = new AsynMethods.getUsersAsyncTask(contentResolver,homeView);
        holis.execute();
       return holis.getData();
    }

}
