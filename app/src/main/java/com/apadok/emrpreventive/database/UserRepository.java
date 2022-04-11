package com.apadok.emrpreventive.database;

import android.app.Application;
import android.os.AsyncTask;

import com.apadok.emrpreventive.database.dao.UserDao;
import com.apadok.emrpreventive.database.entity.UserEntity;

import java.util.concurrent.ExecutionException;

public class UserRepository {

    private UserDao mUserDao;
    private UserEntity mUsers;

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mUserDao = db.userDao();
        mUsers = mUserDao.getUser();
    }

    public UserEntity getmLoggedUser() {
        return mUsers;
    }

    public UserEntity getUser(int UserId) throws ExecutionException, InterruptedException {
        return new getUserAsync(mUserDao).execute(UserId).get();
    }

    public void insertUser(UserEntity User) {
        new insertUsersAsync(mUserDao).execute(User);
    }

    public void updateUser(UserEntity User) {
        new updateUsersAsync(mUserDao).execute(User);
    }

    public void deleteUser(UserEntity User) {
        new deleteUsersAsync(mUserDao).execute(User);
    }

    public void deleteAllUsers() {
        new deleteAllUsersAsync(mUserDao).execute();
    }

    /**
     * User: all write operations should be done in background thread,
     * otherwise the following error will be thrown
     * `java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.`
     */

    private static class getUserAsync extends AsyncTask<Integer, Void, UserEntity> {

        private UserDao mUserDaoAsync;

        getUserAsync(UserDao animalDao) {
            mUserDaoAsync = animalDao;
        }

        @Override
        protected UserEntity doInBackground(Integer... ids) {
            return mUserDaoAsync.getUserById(ids[0]);
        }
    }

    private static class insertUsersAsync extends AsyncTask<UserEntity, Void, Long> {

        private UserDao mUserDaoAsync;

        insertUsersAsync(UserDao UserDao) {
            mUserDaoAsync = UserDao;
        }

        @Override
        protected Long doInBackground(UserEntity... Users) {
            long id = mUserDaoAsync.insert(Users[0]);
            return id;
        }
    }

    private static class updateUsersAsync extends AsyncTask<UserEntity, Void, Void> {

        private UserDao mUserDaoAsync;

        updateUsersAsync(UserDao UserDao) {
            mUserDaoAsync = UserDao;
        }

        @Override
        protected Void doInBackground(UserEntity... Users) {
            mUserDaoAsync.update(Users[0]);
            return null;
        }
    }

    private static class deleteUsersAsync extends AsyncTask<UserEntity, Void, Void> {

        private UserDao mUserDaoAsync;

        deleteUsersAsync(UserDao UserDao) {
            mUserDaoAsync = UserDao;
        }

        @Override
        protected Void doInBackground(UserEntity... Users) {
            mUserDaoAsync.delete(Users[0]);
            return null;
        }
    }

    private static class deleteAllUsersAsync extends AsyncTask<UserEntity, Void, Void> {

        private UserDao mUserDaoAsync;

        deleteAllUsersAsync(UserDao UserDao) {
            mUserDaoAsync = UserDao;
        }

        @Override
        protected Void doInBackground(UserEntity... Users) {
            mUserDaoAsync.deleteAll();
            return null;
        }
    }
}
