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

    @ColumnInfo(name = "hasil_kardiovaskular")
    String hasil_kardiovaskular;

    @ColumnInfo(name = "hasil_stroke")
    String hasil_stroke;

    @ColumnInfo(name = "created_at")
    String created_at;

    @ColumnInfo(name = "updated_at")
    String updated_at;

    @ColumnInfo(name = "kadar_gula")
    String kadar_gula;

    @ColumnInfo(name = "tekanan_darah")
    String tekanan_darah;

    @ColumnInfo(name = "kadar_kolesterol")
    String kadar_kolesterol;


    protected PemeriksaanEntity(Parcel in) {
        id_pemeriksaan = in.readString();
        id_user = in.readString();
        hasil_diabetes = in.readString();
        hasil_kardiovaskular = in.readString();
        hasil_stroke = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        kadar_gula = in.readString();
        tekanan_darah = in.readString();
        kadar_kolesterol = in.readString();
    }

    public PemeriksaanEntity(@NonNull String id_pemeriksaan, String id_user, String hasil_diabetes, String hasil_kardiovaskular, String hasil_stroke, String created_at, String updated_at, String kadar_gula, String tekanan_darah, String kadar_kolesterol) {
        this.id_pemeriksaan = id_pemeriksaan;
        this.id_user = id_user;
        this.hasil_diabetes = hasil_diabetes;
        this.hasil_kardiovaskular = hasil_kardiovaskular;
        this.hasil_stroke = hasil_stroke;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.kadar_gula = kadar_gula;
        this.tekanan_darah = tekanan_darah;
        this.kadar_kolesterol = kadar_kolesterol;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_pemeriksaan);
        dest.writeString(id_user);
        dest.writeString(hasil_diabetes);
        dest.writeString(hasil_kardiovaskular);
        dest.writeString(hasil_stroke);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeString(kadar_gula);
        dest.writeString(tekanan_darah);
        dest.writeString(kadar_kolesterol);
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

    @NonNull
    public String getId_pemeriksaan() {
        return id_pemeriksaan;
    }

    public void setId_pemeriksaan(@NonNull String id_pemeriksaan) {
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

    public String getHasil_kardiovaskular() {
        return hasil_kardiovaskular;
    }

    public void setHasil_kardiovaskular(String hasil_kardiovaskular) {
        this.hasil_kardiovaskular = hasil_kardiovaskular;
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

    public String getKadar_gula() {
        return kadar_gula;
    }

    public void setKadar_gula(String kadar_gula) {
        this.kadar_gula = kadar_gula;
    }

    public String getTekanan_darah() {
        return tekanan_darah;
    }

    public void setTekanan_darah(String tekanan_darah) {
        this.tekanan_darah = tekanan_darah;
    }

    public String getKadar_kolesterol() {
        return kadar_kolesterol;
    }

    public void setKadar_kolesterol(String kadar_kolesterol) {
        this.kadar_kolesterol = kadar_kolesterol;
    }
}
