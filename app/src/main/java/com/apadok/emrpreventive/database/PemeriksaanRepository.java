package com.apadok.emrpreventive.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.apadok.emrpreventive.database.dao.PemeriksaanDao;
import com.apadok.emrpreventive.database.entity.PemeriksaanEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class PemeriksaanRepository {

    private PemeriksaanDao mPemeriksaanDao;
    private LiveData<List<PemeriksaanEntity>> mAllPemeriksaans;
    private PemeriksaanEntity mLatestPemeriksaans;

    public PemeriksaanRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mPemeriksaanDao = db.pemeriksaanDao();
        mAllPemeriksaans = mPemeriksaanDao.getAllPemeriksaans();
        mLatestPemeriksaans = mPemeriksaanDao.getPemeriksaanLatest();
    }

    public PemeriksaanEntity getmLatestPemeriksaan() {
        return mLatestPemeriksaans;
    }

    public LiveData<List<PemeriksaanEntity>> getAllPemeriksaans() {
        return mAllPemeriksaans;
    }

    public PemeriksaanEntity getPemeriksaan(int pemeriksaanId) throws ExecutionException, InterruptedException {
        return new getpemeriksaanAsync(mPemeriksaanDao).execute(pemeriksaanId).get();
    }

    public void insertpemeriksaan(PemeriksaanEntity pemeriksaan) {
        new insertpemeriksaansAsync(mPemeriksaanDao).execute(pemeriksaan);
    }

    public void updatepemeriksaan(PemeriksaanEntity pemeriksaan) {
        new updatepemeriksaansAsync(mPemeriksaanDao).execute(pemeriksaan);
    }

    public void deletepemeriksaan(PemeriksaanEntity pemeriksaan) {
        new deletepemeriksaansAsync(mPemeriksaanDao).execute(pemeriksaan);
    }

    public void deleteAllpemeriksaans() {
        new deleteAllpemeriksaansAsync(mPemeriksaanDao).execute();
    }

    /**
     * pemeriksaan: all write operations should be done in background thread,
     * otherwise the following error will be thrown
     * `java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.`
     */

    private static class getpemeriksaanAsync extends AsyncTask<Integer, Void, PemeriksaanEntity> {

        private PemeriksaanDao mPemeriksaanDaoAsync;

        getpemeriksaanAsync(PemeriksaanDao animalDao) {
            mPemeriksaanDaoAsync = animalDao;
        }

        @Override
        protected PemeriksaanEntity doInBackground(Integer... ids) {
            return mPemeriksaanDaoAsync.getPemeriksaanById(ids[0]);
        }
    }

    private static class insertpemeriksaansAsync extends AsyncTask<PemeriksaanEntity, Void, Long> {

        private PemeriksaanDao mPemeriksaanDaoAsync;

        insertpemeriksaansAsync(PemeriksaanDao PemeriksaanDao) {
            mPemeriksaanDaoAsync = PemeriksaanDao;
        }

        @Override
        protected Long doInBackground(PemeriksaanEntity... pemeriksaans) {
            long id = mPemeriksaanDaoAsync.insert(pemeriksaans[0]);
            return id;
        }
    }

    private static class updatepemeriksaansAsync extends AsyncTask<PemeriksaanEntity, Void, Void> {

        private PemeriksaanDao mPemeriksaanDaoAsync;

        updatepemeriksaansAsync(PemeriksaanDao PemeriksaanDao) {
            mPemeriksaanDaoAsync = PemeriksaanDao;
        }

        @Override
        protected Void doInBackground(PemeriksaanEntity... pemeriksaans) {
            mPemeriksaanDaoAsync.update(pemeriksaans[0]);
            return null;
        }
    }

    private static class deletepemeriksaansAsync extends AsyncTask<PemeriksaanEntity, Void, Void> {

        private PemeriksaanDao mPemeriksaanDaoAsync;

        deletepemeriksaansAsync(PemeriksaanDao PemeriksaanDao) {
            mPemeriksaanDaoAsync = PemeriksaanDao;
        }

        @Override
        protected Void doInBackground(PemeriksaanEntity... pemeriksaans) {
            mPemeriksaanDaoAsync.delete(pemeriksaans[0]);
            return null;
        }
    }

    private static class deleteAllpemeriksaansAsync extends AsyncTask<PemeriksaanEntity, Void, Void> {

        private PemeriksaanDao mPemeriksaanDaoAsync;

        deleteAllpemeriksaansAsync(PemeriksaanDao PemeriksaanDao) {
            mPemeriksaanDaoAsync = PemeriksaanDao;
        }

        @Override
        protected Void doInBackground(PemeriksaanEntity... pemeriksaans) {
            mPemeriksaanDaoAsync.deleteAll();
            return null;
        }
    }
}
