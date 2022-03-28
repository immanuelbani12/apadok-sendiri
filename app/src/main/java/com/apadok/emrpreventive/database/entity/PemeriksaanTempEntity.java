package com.apadok.emrpreventive.database.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pemeriksaantemps")
public class PemeriksaanTempEntity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    int id_pemeriksaan_temp;

    @ColumnInfo(name = "data_pemeriksaan")
    String data_pemeriksaan;

    @ColumnInfo(name = "is_sent")
    boolean is_sent;

    public int getId_pemeriksaan_temp() {
        return id_pemeriksaan_temp;
    }

    public void setId_pemeriksaan_temp(int id_pemeriksaan_temp) {
        this.id_pemeriksaan_temp = id_pemeriksaan_temp;
    }

    public String getData_pemeriksaan() {
        return data_pemeriksaan;
    }

    public void setData_pemeriksaan(String data_pemeriksaan) {
        this.data_pemeriksaan = data_pemeriksaan;
    }

    public boolean isIs_sent() {
        return is_sent;
    }

    public void setIs_sent(boolean is_sent) {
        this.is_sent = is_sent;
    }

    public PemeriksaanTempEntity(@NonNull int id_pemeriksaan_temp, String data_pemeriksaan, boolean is_sent) {
        this.id_pemeriksaan_temp = id_pemeriksaan_temp;
        this.data_pemeriksaan = data_pemeriksaan;
        this.is_sent = is_sent;
    }
}
