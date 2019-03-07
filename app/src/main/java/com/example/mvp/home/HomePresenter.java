package com.example.mvp.home;

import android.content.ContentResolver;
import com.example.mvp.data.local.UserLocalRepository;
import com.example.mvp.data.remote.randomapi.to.Result;
import java.util.List;

public class HomePresenter implements HomeContract.Presenter {

    public static final String TAG="HomePresenter_TAG";
    private UserLocalRepository userLocalRepository;

    public HomePresenter(HomeContract.View homeView,ContentResolver contentResolver) {
        /*this.homeView = homeView;
        NetworkManager networkManager = new NetworkManager();
        randomAPIService = new RandomAPIService(networkManager.provideRandomAPI(RandomAPI.BASE_URL));*/
        userLocalRepository = new UserLocalRepository(homeView,contentResolver);
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
        userLocalRepository.fetchUsers(count);
    }

    @Override
    public void fetchUser(int _id) {
        userLocalRepository.fetchUser(_id);
    }

    @Override
    public void deleteUser(Result entity,int _id) {
        userLocalRepository.deleteUser(entity,_id);

    }

    @Override
    public void updateUser(Result entity,int _id) {
        userLocalRepository.updateUser(entity,_id);
    }

    @Override
    public void deleteUsers() {
        userLocalRepository.deleteUsers();
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

        userLocalRepository.saveUsers(results);
    }

    @Override
    public List<Result> getData(ContentResolver contentResolver) {
            return userLocalRepository.getUsers();
    }
}
