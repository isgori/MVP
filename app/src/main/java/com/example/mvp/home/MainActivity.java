package com.example.mvp.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mvp.R;
import com.example.mvp.data.remote.randomapi.RandomAPI;
import com.example.mvp.data.remote.randomapi.RandomAPIService;
import com.example.mvp.data.remote.randomapi.to.ApiResponse;
import com.example.mvp.data.remote.randomapi.to.Result;
import com.example.mvp.util.ErrorManager;
import com.example.mvp.util.NetworkError;
import com.example.mvp.util.NetworkManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements HomeContract.View {

    private HomeContract.Presenter homePresenter;
    private UsersAdapter usersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homePresenter = new HomePresenter(this,getContentResolver());
        initViews();
        usersAdapter.updateUserDataSet(homePresenter.getData(getContentResolver()));
    }

    private void initViews() {
        RecyclerView usersRecyclerView = findViewById(R.id.usersRecyclerView);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersAdapter = new UsersAdapter();
        usersRecyclerView.setAdapter(usersAdapter);
    }

    @Override
    public void showUsers(List<Result> results) {
        usersAdapter.updateUserDataSet(results);
    }

    @Override
    public void showError(NetworkError error) {
        Toast.makeText(MainActivity.this, ErrorManager.handleError(error), Toast.LENGTH_SHORT).show();
    }

    public void doMagic(View view) {
        homePresenter.fetchUsers(1);
    }

    public void saveData(View view){ homePresenter.saveData(usersAdapter.getUserDataSet());}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        homePresenter.tearDown();
        homePresenter = null;
    }
}
