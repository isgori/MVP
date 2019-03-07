package com.example.mvp.data;

import android.content.ContentValues;

import java.util.List;

public interface Repository <T, K> {


    List<T> getUsers();
    void saveUsers(List<T> entitys);
    void updateUser(T entity,int id);
    void deleteUser(T entity,int id);
    void deleteUsers();
}
