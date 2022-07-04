package com.apadok.emrpreventive.database.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pemeriksaans")
public class PemeriksaanKebugaranEntity implements Parcelable {
    @PrimaryKey
    @NonNull
    String id_pemeriksaan_kebugaran;

    @ColumnInfo(name = "id_user")
    String id_user;

    @ColumnInfo(name = "score_kebugaran")
    String score_kebugaran;

    @ColumnInfo(name = "created_at")
    String created_at;

    @ColumnInfo(name = "updated_at")
    String updated_at;

    protected PemeriksaanKebugaranEntity(Parcel in) {
        id_pemeriksaan_kebugaran = in.readString();
        id_user = in.readString();
        score_kebugaran = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
    }

    public PemeriksaanKebugaranEntity(@NonNull String id_pemeriksaan, String id_user, String hasil_kebugaran, String score_kebugaran, String created_at, String updated_at) {
        this.id_pemeriksaan_kebugaran = id_pemeriksaan;
        this.id_user = id_user;
        this.score_kebugaran = score_kebugaran;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_pemeriksaan_kebugaran);
        dest.writeString(id_user);
        dest.writeString(score_kebugaran);
        dest.writeString(created_at);
        dest.writeString(updated_at);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PemeriksaanKebugaranEntity> CREATOR = new Creator<PemeriksaanKebugaranEntity>() {
        @Override
        public PemeriksaanKebugaranEntity createFromParcel(Parcel in) {
            return new PemeriksaanKebugaranEntity(in);
        }

        @Override
        public PemeriksaanKebugaranEntity[] newArray(int size) {
            return new PemeriksaanKebugaranEntity[size];
        }
    };

    @NonNull
    public String getId_pemeriksaan_kebugaran() {
        return id_pemeriksaan_kebugaran;
    }

    public void setId_pemeriksaan_kebugaran(@NonNull String id_pemeriksaan) {
        this.id_pemeriksaan_kebugaran = id_pemeriksaan;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
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

    public String getScore_kebugaran() {
        return score_kebugaran;
    }

    public void setScore_kebugaran(String score_kebugaran) {
        this.score_kebugaran = score_kebugaran;
    }
}
