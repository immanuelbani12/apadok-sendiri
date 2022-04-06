package com.apadok.emrpreventive.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey
    @NonNull
    int userid;

    @ColumnInfo(name = "username")
    String username;

    @ColumnInfo(name = "clinicname")
    String clinicname;

    @ColumnInfo(name = "token")
    String token;

    @ColumnInfo(name = "cliniclogo", typeAffinity = ColumnInfo.BLOB)
    byte[] cliniclogo;

    @ColumnInfo(name = "isonline")
    Boolean isonline;


    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClinicname() {
        return clinicname;
    }

    public void setClinicname(String clinicname) {
        this.clinicname = clinicname;
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

    public UserEntity(int userid, String username, String clinicname, String token, byte[] cliniclogo, Boolean isonline) {
        this.userid = userid;
        this.username = username;
        this.clinicname = clinicname;
        this.token = token;
        this.cliniclogo = cliniclogo;
        this.isonline = isonline;
    }
}

