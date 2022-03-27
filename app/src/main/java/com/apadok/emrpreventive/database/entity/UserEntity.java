package com.apadok.emrpreventive.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Blob;
import java.util.Date;


@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey
    int userid;

    @ColumnInfo(name = "username")
    String username;

    @ColumnInfo(name = "clinicname")
    String clinicname;

    @ColumnInfo(name = "token")
    String token;

    @ColumnInfo(name = "cliniclogo")
    Blob cliniclogo;

    @ColumnInfo(name = "isonline")
    Boolean isonline;

    @ColumnInfo(name = "tokendate")
    Date tokendate;

    public UserEntity(int userid, String username, String clinicname, String token, Blob cliniclogo) {
        this.userid = userid;
        this.username = username;
        this.clinicname = clinicname;
        this.token = token;
        this.cliniclogo = cliniclogo;
    }

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

    public Blob getCliniclogo() {
        return cliniclogo;
    }

    public void setCliniclogo(Blob cliniclogo) {
        this.cliniclogo = cliniclogo;
    }

    public Boolean getIsonline() {
        return isonline;
    }

    public void setIsonline(Boolean isonline) {
        this.isonline = isonline;
    }

    public Date getTokendate() {
        return tokendate;
    }

    public void setTokendate(Date tokendate) {
        this.tokendate = tokendate;
    }
}

