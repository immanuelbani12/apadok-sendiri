package com.apadok.emrpreventive.common;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.apadok.emrpreventive.database.UserRepository;
import com.apadok.emrpreventive.database.entity.UserEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserListViewModel extends AndroidViewModel {

    private UserRepository mRepository;
    private LiveData<List<UserEntity>> Users;

    public UserListViewModel(@NonNull Application application) {
        super(application);

        mRepository = new UserRepository(application);
    }


    public UserEntity getUser(int id) throws ExecutionException, InterruptedException {
        return mRepository.getUser(id);
    }

    public UserEntity getLoggedUser() throws ExecutionException, InterruptedException {
        return mRepository.getmLoggedUser();
    }

    public void insertNote(UserEntity pem) {
        mRepository.insertUser(pem);
    }

    public void updateNote(UserEntity pem) {
        mRepository.updateUser(pem);
    }

    public void deleteNote(UserEntity pem) {
        mRepository.deleteUser(pem);
    }

    public void deleteAllNotes() {
        mRepository.deleteAllUsers();
    }
}
