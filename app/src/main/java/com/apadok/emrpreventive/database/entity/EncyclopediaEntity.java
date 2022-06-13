package com.apadok.emrpreventive.database.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class EncyclopediaEntity implements Parcelable {
    String id_artikel;
    String id_login;
    String judul_artikel;
    String gambar_artikel;
    String isi_artikel;
    String kategori_artikel;
    String jenis_artikel;
    String created_at;
    String updated_at;

    protected EncyclopediaEntity(Parcel in) {
        id_artikel = in.readString();
        id_login = in.readString();
        judul_artikel = in.readString();
        gambar_artikel = in.readString();
        isi_artikel = in.readString();
        kategori_artikel = in.readString();
        jenis_artikel = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_artikel);
        dest.writeString(id_login);
        dest.writeString(judul_artikel);
        dest.writeString(gambar_artikel);
        dest.writeString(isi_artikel);
        dest.writeString(kategori_artikel);
        dest.writeString(jenis_artikel);
        dest.writeString(created_at);
        dest.writeString(updated_at);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EncyclopediaEntity> CREATOR = new Creator<EncyclopediaEntity>() {
        @Override
        public EncyclopediaEntity createFromParcel(Parcel in) {
            return new EncyclopediaEntity(in);
        }

        @Override
        public EncyclopediaEntity[] newArray(int size) {
            return new EncyclopediaEntity[size];
        }
    };

    public EncyclopediaEntity(String id_artikel, String id_login, String judul_artikel, String gambar_artikel, String isi_artikel, String kategori_artikel, String jenis_artikel, String created_at, String updated_at) {
        this.id_artikel = id_artikel;
        this.id_login = id_login;
        this.judul_artikel = judul_artikel;
        this.gambar_artikel = gambar_artikel;
        this.isi_artikel = isi_artikel;
        this.kategori_artikel = kategori_artikel;
        this.jenis_artikel = jenis_artikel;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId_artikel() {
        return id_artikel;
    }

    public void setId_artikel(String id_artikel) {
        this.id_artikel = id_artikel;
    }

    public String getId_login() {
        return id_login;
    }

    public void setId_login(String id_login) {
        this.id_login = id_login;
    }

    public String getJudul_artikel() {
        return judul_artikel;
    }

    public void setJudul_artikel(String judul_artikel) {
        this.judul_artikel = judul_artikel;
    }

    public String getGambar_artikel() {
        return gambar_artikel;
    }

    public void setGambar_artikel(String gambar_artikel) {
        this.gambar_artikel = gambar_artikel;
    }

    public String getIsi_artikel() {
        return isi_artikel;
    }

    public void setIsi_artikel(String isi_artikel) {
        this.isi_artikel = isi_artikel;
    }

    public String getKategori_artikel() {
        return kategori_artikel;
    }

    public void setKategori_artikel(String kategori_artikel) {
        this.kategori_artikel = kategori_artikel;
    }

    public String getJenis_artikel() {
        return jenis_artikel;
    }

    public void setJenis_artikel(String jenis_artikel) {
        this.jenis_artikel = jenis_artikel;
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
