package com.apadok.emrpreventive.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey
    @NonNull
    int id_user;

    @ColumnInfo(name = "nama_user")
    String nama_user;

    @ColumnInfo(name = "kode_group")
    String kode_group;

    @ColumnInfo(name = "id_klinik")
    String id_klinik;

    @ColumnInfo(name = "role")
    String role;

    @ColumnInfo(name = "nama_klinik")
    String nama_klinik;

    @ColumnInfo(name = "token")
    String token;

    @ColumnInfo(name = "cliniclogo", typeAffinity = ColumnInfo.BLOB)
    byte[] cliniclogo;

    @ColumnInfo(name = "isonline")
    Boolean isonline;


    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getNama_user() {
        return nama_user;
    }

    public void setNama_user(String nama_user) {
        this.nama_user = nama_user;
    }

    public String getNama_klinik() {
        return nama_klinik;
    }

    public void setNama_klinik(String nama_klinik) {
        this.nama_klinik = nama_klinik;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getIsonline() {
        return isonline;
    }

    public void setIsonline(Boolean isonline) {
        this.isonline = isonline;
    }

    public byte[] getCliniclogo() {
        return cliniclogo;
    }

    public void setCliniclogo(byte[] cliniclogo) {
        this.cliniclogo = cliniclogo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getKode_group() {
        return kode_group;
    }

    public void setKode_group(String kode_group) {
        this.kode_group = kode_group;
    }

    public String getId_klinik() {
        return id_klinik;
    }

    public void setId_klinik(String id_klinik) {
        this.id_klinik = id_klinik;
    }

    public UserEntity(int id_user, String nama_user, String kode_group, String id_klinik, String role, String nama_klinik, String token, byte[] cliniclogo, Boolean isonline) {
        this.id_user = id_user;
        this.nama_user = nama_user;
        this.kode_group = kode_group;
        this.id_klinik = id_klinik;
        this.role = role;
        this.nama_klinik = nama_klinik;
        this.token = token;
        this.cliniclogo = cliniclogo;
        this.isonline = isonline;
    }
}

