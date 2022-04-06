package com.apadok.emrpreventive.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.apadok.emrpreventive.database.dao.PemeriksaanDao;
import com.apadok.emrpreventive.database.dao.UserDao;
import com.apadok.emrpreventive.database.entity.PemeriksaanEntity;
import com.apadok.emrpreventive.database.entity.UserEntity;

@Database(entities = {UserEntity.class, PemeriksaanEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract PemeriksaanDao pemeriksaanDao();


    private static AppDatabase INSTANCE;

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "apadok_db")
                            .build();

                }
            }
        }
        return INSTANCE;
    }
}