package com.example.mvp.home;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.mvp.data.local.UserContract;
import com.example.mvp.data.local.UserProvider;
import com.example.mvp.data.remote.randomapi.RandomAPI;
import com.example.mvp.data.remote.randomapi.RandomAPIService;
import com.example.mvp.data.remote.randomapi.UserRemoteRepository;
import com.example.mvp.data.remote.randomapi.to.Result;
import com.example.mvp.util.NetworkError;
import com.example.mvp.util.NetworkManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomePresenter implements HomeContract.Presenter {

    public static final String TAG="HomePresenter_TAG";
    private UserRemoteRepository userRemoteRepository;

    public HomePresenter(HomeContract.View homeView,ContentResolver contentResolver) {
        /*this.homeView = homeView;
        NetworkManager networkManager = new NetworkManager();
        randomAPIService = new RandomAPIService(networkManager.provideRandomAPI(RandomAPI.BASE_URL));*/
        userRemoteRepository = new UserRemoteRepository(homeView,contentResolver);
    }

    @Override
    public void fetchUsers(int count) {
        /*randomAPIService.fetchRandomUsers(100, new RandomAPIService.RandomUsersCallback() {
            @Override
            public void OnUsersLoaded(List<Result> results) {
                homeView.showUsers(results);
            }

            @Override
            public void OnAPIError(NetworkError error) {
                homeView.showError(error);
            }
        });*/
        userRemoteRepository.fetchUsers(count);
    }

    @Override
    public void tearDown() {
        //homeView = null;
    }

    @Override
    public void saveData(final List<Result> results) {

        /*final List<Result> resultsF = results;
        final ContentResolver contentResolverF = contentResolver;

        new Runnable(){
            @Override
            public void run() {
                for (int i = 0; i < resultsF.size(); i++) {
                    ContentValues userCV = UserContract.UserEntry.prepareNewUser(
                            resultsF.get(i).getName().getFirst(), results.get(i).getName().getLast(), resultsF.get(i).toString());
                    Uri newUser = contentResolverF.insert(UserContract.UserEntry.CONTENT_URI, userCV);
                    if (newUser == null){
                        homeView.showError(NetworkError.BAD_REQUEST);
                        Log.d(TAG, "run: " + NetworkError.BAD_REQUEST.toString());
                    }
                }
                Log.d(TAG, "run: Finish");
            }
        }.run();*/

        userRemoteRepository.saveUsers(results);
    }

    @Override
    public List<Result> getData(ContentResolver contentResolver) {
            return userRemoteRepository.getUsers();
    }
}
