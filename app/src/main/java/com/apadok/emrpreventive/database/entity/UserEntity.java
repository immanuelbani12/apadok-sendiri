package com.apadok.emrpreventive.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey
    int id_user;

    @ColumnInfo(name = "nama_user")
    String nama_user;

    @ColumnInfo(name = "kode_user")
    String kode_user;

    @ColumnInfo(name = "id_institusi")
    String id_institusi;

    @ColumnInfo(name = "role")
    String role;

    @ColumnInfo(name = "nama_institusi")
    String nama_institusi;

    @ColumnInfo(name = "token")
    String token;

    @ColumnInfo(name = "logo_institusi", typeAffinity = ColumnInfo.BLOB)
    byte[] logo_institusi;

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

    public String getKode_user() {
        return kode_user;
    }

    public void setKode_user(String kode_user) {
        this.kode_user = kode_user;
    }

    public String getId_institusi() {
        return id_institusi;
    }

    public void setId_institusi(String id_institusi) {
        this.id_institusi = id_institusi;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNama_institusi() {
        return nama_institusi;
    }

    public void setNama_institusi(String nama_institusi) {
        this.nama_institusi = nama_institusi;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public byte[] getLogo_institusi() {
        return logo_institusi;
    }

    public void setLogo_institusi(byte[] logo_institusi) {
        this.logo_institusi = logo_institusi;
    }

    public Boolean getIsonline() {
        return isonline;
    }

    public void setIsonline(Boolean isonline) {
        this.isonline = isonline;
    }

    public UserEntity(int id_user, String nama_user, String kode_user, String id_institusi, String role, String nama_institusi, String token, byte[] logo_institusi, Boolean isonline) {
        this.id_user = id_user;
        this.nama_user = nama_user;
        this.kode_user = kode_user;
        this.id_institusi = id_institusi;
        this.role = role;
        this.nama_institusi = nama_institusi;
        this.token = token;
        this.logo_institusi = logo_institusi;
        this.isonline = isonline;
    }
}

