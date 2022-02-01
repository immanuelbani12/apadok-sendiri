package com.example.emrpreventive.shorting.screeninghistory;

import android.os.Parcel;
import android.os.Parcelable;

public class ScreeningHistory implements Parcelable {
    String id_pemeriksaan;
    String id_user;
    String hasil_diabetes;
    String hasil_kolesterol;
    String hasil_stroke;
    String create_at;
    String updated_at;

    protected ScreeningHistory(Parcel in) {
        id_pemeriksaan = in.readString();
        id_user = in.readString();
        hasil_diabetes = in.readString();
        hasil_kolesterol = in.readString();
        hasil_stroke = in.readString();
        create_at = in.readString();
        updated_at = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_pemeriksaan);
        dest.writeString(id_user);
        dest.writeString(hasil_diabetes);
        dest.writeString(hasil_kolesterol);
        dest.writeString(hasil_stroke);
        dest.writeString(create_at);
        dest.writeString(updated_at);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ScreeningHistory> CREATOR = new Creator<ScreeningHistory>() {
        @Override
        public ScreeningHistory createFromParcel(Parcel in) {
            return new ScreeningHistory(in);
        }

        @Override
        public ScreeningHistory[] newArray(int size) {
            return new ScreeningHistory[size];
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

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
