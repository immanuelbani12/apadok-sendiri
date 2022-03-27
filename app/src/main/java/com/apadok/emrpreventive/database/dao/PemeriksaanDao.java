package com.apadok.emrpreventive.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.apadok.emrpreventive.database.entity.PemeriksaanEntity;
import com.apadok.emrpreventive.database.entity.UserEntity;

import java.util.List;

public interface PemeriksaanDao {
    @Query("SELECT * FROM pemeriksaans ORDER BY id_pemeriksaan DESC")
    LiveData<List<PemeriksaanEntity>> getAllPemeriksaans();

    @Query("SELECT * FROM pemeriksaans ORDER BY id_pemeriksaan DESC LIMIT 1")
    PemeriksaanEntity getPemeriksaanLatest();

    @Query("SELECT * FROM pemeriksaans WHERE id_pemeriksaan=:id")
    PemeriksaanEntity getPemeriksaanById(int id);

    @Insert
    long insert(PemeriksaanEntity pemeriksaaan);

    @Update
    void update(PemeriksaanEntity pemeriksaaan);

    @Delete
    void delete(PemeriksaanEntity pemeriksaaan);

    @Query("DELETE FROM pemeriksaans")
    void deleteAll();
}
