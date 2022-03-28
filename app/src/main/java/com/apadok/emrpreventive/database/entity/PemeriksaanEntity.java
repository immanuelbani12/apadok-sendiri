package com.apadok.emrpreventive.database.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pemeriksaans")
public class PemeriksaanEntity implements Parcelable {
    @PrimaryKey
    @NonNull
    String id_pemeriksaan;

    @ColumnInfo(name = "id_user")
    String id_user;

    @ColumnInfo(name = "hasil_diabetes")
    String hasil_diabetes;

    @ColumnInfo(name = "hasil_kolesterol")
    String hasil_kolesterol;

    @ColumnInfo(name = "hasil_stroke")
    String hasil_stroke;

    @ColumnInfo(name = "created_at")
    String created_at;

    @ColumnInfo(name = "updated_at")
    String updated_at;

    protected PemeriksaanEntity(Parcel in) {
        id_pemeriksaan = in.readString();
        id_user = in.readString();
        hasil_diabetes = in.readString();
        hasil_kolesterol = in.readString();
        hasil_stroke = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
    }

    public PemeriksaanEntity(@NonNull String id_pemeriksaan, String id_user, String hasil_diabetes, String hasil_kolesterol, String hasil_stroke, String created_at, String updated_at) {
        this.id_pemeriksaan = id_pemeriksaan;
        this.id_user = id_user;
        this.hasil_diabetes = hasil_diabetes;
        this.hasil_kolesterol = hasil_kolesterol;
        this.hasil_stroke = hasil_stroke;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_pemeriksaan);
        dest.writeString(id_user);
        dest.writeString(hasil_diabetes);
        dest.writeString(hasil_kolesterol);
        dest.writeString(hasil_stroke);
        dest.writeString(created_at);
        dest.writeString(updated_at);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PemeriksaanEntity> CREATOR = new Creator<PemeriksaanEntity>() {
        @Override
        public PemeriksaanEntity createFromParcel(Parcel in) {
            return new PemeriksaanEntity(in);
        }

        @Override
        public PemeriksaanEntity[] newArray(int size) {
            return new PemeriksaanEntity[size];
        }
    };

    public String getId_pemeriksaan() {
        return id_pemeriksaan;
    }

    public void setId_pemeriksaan(String id_pemeriksaan) {
        this.id_pemeriksaan = id_pemeriksaan;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getHasil_diabetes() {
        return hasil_diabetes;
    }

    public void setHasil_diabetes(String hasil_diabetes) {
        this.hasil_diabetes = hasil_diabetes;
    }

    public String getHasil_kolesterol() {
        return hasil_kolesterol;
    }

    public void setHasil_kolesterol(String hasil_kolesterol) {
        this.hasil_kolesterol = hasil_kolesterol;
    }

    public String getHasil_stroke() {
        return hasil_stroke;
    }

    public void setHasil_stroke(String hasil_stroke) {
        this.hasil_stroke = hasil_stroke;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
