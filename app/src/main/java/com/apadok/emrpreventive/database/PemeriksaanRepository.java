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

    public PemeriksaanEntity getPemeriksaan(int noteId) throws ExecutionException, InterruptedException {
        return new getNoteAsync(mPemeriksaanDao).execute(noteId).get();
    }

    public void insertNote(PemeriksaanEntity note) {
        new insertNotesAsync(mPemeriksaanDao).execute(note);
    }

    public void updateNote(PemeriksaanEntity note) {
        new updateNotesAsync(mPemeriksaanDao).execute(note);
    }

    public void deleteNote(PemeriksaanEntity note) {
        new deleteNotesAsync(mPemeriksaanDao).execute(note);
    }

    public void deleteAllNotes() {
        new deleteAllNotesAsync(mPemeriksaanDao).execute();
    }

    /**
     * NOTE: all write operations should be done in background thread,
     * otherwise the following error will be thrown
     * `java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.`
     */

    private static class getNoteAsync extends AsyncTask<Integer, Void, PemeriksaanEntity> {

        private PemeriksaanDao mPemeriksaanDaoAsync;

        getNoteAsync(PemeriksaanDao animalDao) {
            mPemeriksaanDaoAsync = animalDao;
        }

        @Override
        protected PemeriksaanEntity doInBackground(Integer... ids) {
            return mPemeriksaanDaoAsync.getPemeriksaanById(ids[0]);
        }
    }

    private static class insertNotesAsync extends AsyncTask<PemeriksaanEntity, Void, Long> {

        private PemeriksaanDao mPemeriksaanDaoAsync;

        insertNotesAsync(PemeriksaanDao PemeriksaanDao) {
            mPemeriksaanDaoAsync = PemeriksaanDao;
        }

        @Override
        protected Long doInBackground(PemeriksaanEntity... notes) {
            long id = mPemeriksaanDaoAsync.insert(notes[0]);
            return id;
        }
    }

    private static class updateNotesAsync extends AsyncTask<PemeriksaanEntity, Void, Void> {

        private PemeriksaanDao mPemeriksaanDaoAsync;

        updateNotesAsync(PemeriksaanDao PemeriksaanDao) {
            mPemeriksaanDaoAsync = PemeriksaanDao;
        }

        @Override
        protected Void doInBackground(PemeriksaanEntity... notes) {
            mPemeriksaanDaoAsync.update(notes[0]);
            return null;
        }
    }

    private static class deleteNotesAsync extends AsyncTask<PemeriksaanEntity, Void, Void> {

        private PemeriksaanDao mPemeriksaanDaoAsync;

        deleteNotesAsync(PemeriksaanDao PemeriksaanDao) {
            mPemeriksaanDaoAsync = PemeriksaanDao;
        }

        @Override
        protected Void doInBackground(PemeriksaanEntity... notes) {
            mPemeriksaanDaoAsync.delete(notes[0]);
            return null;
        }
    }

    private static class deleteAllNotesAsync extends AsyncTask<PemeriksaanEntity, Void, Void> {

        private PemeriksaanDao mPemeriksaanDaoAsync;

        deleteAllNotesAsync(PemeriksaanDao PemeriksaanDao) {
            mPemeriksaanDaoAsync = PemeriksaanDao;
        }

        @Override
        protected Void doInBackground(PemeriksaanEntity... notes) {
            mPemeriksaanDaoAsync.deleteAll();
            return null;
        }
    }
}
