package com.apadok.emrpreventive.user;

import android.os.Parcel;
import android.os.Parcelable;

public class NewUser implements Parcelable {

    String nama;
    String username;
    String kode_group;

    public NewUser(String nama, String username, String kode_group) {
        this.nama = nama;
        this.username = username;
        this.kode_group = kode_group;
    }

    protected NewUser(Parcel in) {
        nama = in.readString();
        username = in.readString();
        kode_group = in.readString();
    }

    public static final Creator<NewUser> CREATOR = new Creator<NewUser>() {
        @Override
        public NewUser createFromParcel(Parcel in) {
            return new NewUser(in);
        }

        @Override
        public NewUser[] newArray(int size) {
            return new NewUser[size];
        }
    };

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKode_group() {
        return kode_group;
    }

    public void setKode_group(String kode_group) {
        this.kode_group = kode_group;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nama);
        dest.writeString(username);
        dest.writeString(kode_group);
    }
}
