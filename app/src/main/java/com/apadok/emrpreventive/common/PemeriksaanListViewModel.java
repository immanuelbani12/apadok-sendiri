package com.apadok.emrpreventive.common;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.apadok.emrpreventive.database.PemeriksaanRepository;
import com.apadok.emrpreventive.database.entity.PemeriksaanEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class PemeriksaanListViewModel extends AndroidViewModel {

    private PemeriksaanRepository mRepository;
    private LiveData<List<PemeriksaanEntity>> pemeriksaans;

    public PemeriksaanListViewModel(@NonNull Application application) {
        super(application);

        mRepository = new PemeriksaanRepository(application);
    }

    public LiveData<List<PemeriksaanEntity>> getNotes() {
        if (pemeriksaans == null) {
            pemeriksaans = mRepository.getAllPemeriksaans();
        }

        return pemeriksaans;
    }

    public PemeriksaanEntity getNote(int id) throws ExecutionException, InterruptedException {
        return mRepository.getPemeriksaan(id);
    }

    public PemeriksaanEntity getLatestPemeriksaan() throws ExecutionException, InterruptedException {
        return mRepository.getmLatestPemeriksaan();
    }

    public void insertNote(PemeriksaanEntity pem) {
        mRepository.insertpemeriksaan(pem);
    }

    public void updateNote(PemeriksaanEntity pem) {
        mRepository.updatepemeriksaan(pem);
    }

    public void deleteNote(PemeriksaanEntity pem) {
        mRepository.deletepemeriksaan(pem);
    }

    public void deleteAllNotes() {
        mRepository.deleteAllpemeriksaans();
    }
}
