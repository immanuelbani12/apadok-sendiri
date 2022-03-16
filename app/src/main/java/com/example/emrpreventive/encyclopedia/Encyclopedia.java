package com.example.emrpreventive.encyclopedia;

import android.os.Parcel;
import android.os.Parcelable;

public class Encyclopedia implements Parcelable {
    String id_artikel;
    String judul_artikel;
    String isi_artikel;
    String kategori_artikel;
    String link_artikel;
    String created_at;
    String updated_at;

    protected Encyclopedia(Parcel in) {
        id_artikel = in.readString();
        judul_artikel = in.readString();
        isi_artikel = in.readString();
        kategori_artikel = in.readString();
        link_artikel = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
    }

    public Encyclopedia(String id_artikel, String judul_artikel, String isi_artikel, String kategori_artikel, String link_artikel, String created_at, String updated_at ) {
        this.id_artikel = id_artikel;
        this.judul_artikel = judul_artikel;
        this.isi_artikel = isi_artikel;
        this.kategori_artikel = kategori_artikel;
        this.link_artikel = link_artikel;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_artikel);
        dest.writeString(judul_artikel);
        dest.writeString(isi_artikel);
        dest.writeString(kategori_artikel);
        dest.writeString(link_artikel);
        dest.writeString(created_at);
        dest.writeString(updated_at);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Encyclopedia> CREATOR = new Creator<Encyclopedia>() {
        @Override
        public Encyclopedia createFromParcel(Parcel in) {
            return new Encyclopedia(in);
        }

        @Override
        public Encyclopedia[] newArray(int size) {
            return new Encyclopedia[size];
        }
    };

    public String getId_artikel() {
        return id_artikel;
    }

    public void setId_artikel(String id_artikel) {
        this.id_artikel = id_artikel;
    }

    public String getJudul_artikel() {
        return judul_artikel;
    }

    public void setJudul_artikel(String judul_artikel) {
        this.judul_artikel = judul_artikel;
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

    public String getLink_artikel() {
        return link_artikel;
    }

    public void setLink_artikel(String link_artikel) {
        this.link_artikel = link_artikel;
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
