package com.apadok.emrpreventive.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.apadok.emrpreventive.database.entity.UserEntity;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users ORDER BY id_user DESC")
    List<UserEntity> getAllUsers();

    @Query("SELECT * FROM users WHERE id_user=:id")
    UserEntity getUserById(int id);

    @Query("SELECT * FROM users LIMIT 1")
    UserEntity getUser();

    @Insert
    long insert(UserEntity user);

    @Update
    void update(UserEntity user);

    @Delete
    void delete(UserEntity user);

    @Query("DELETE FROM users")
    void deleteAll();
}
