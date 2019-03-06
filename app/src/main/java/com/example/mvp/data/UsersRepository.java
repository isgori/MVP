package com.example.mvp.data;

import com.example.mvp.data.remote.randomapi.to.Result;

import java.util.List;

public interface UsersRepository {

    /// Is going to check if we have users on the database, and then show those
    /// if there are no users then load them from the REST API
    /// Once loaded from the API they are cached into the Content Provider
    /// Which in turn saves them into a SQLite database

    void fetchUsers(int count);

    void fetchUser(int idUser);

    List<Result>  getUsers();


    void saveUsers(List<Result> lists);

    void updateUser(int id);

    void deleteUser(int id);

    void deleteUsers();

}
