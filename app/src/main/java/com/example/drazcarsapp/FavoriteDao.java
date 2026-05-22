package com.example.drazcarsapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface FavoriteDao {

    @Insert
    void addFavorite(Favorite favorite);

    @Delete
    void removeFavorite(Favorite favorite);

    // استعلام لجلب قائمة المفضلات لمستخدم معين
    @Query("SELECT * FROM favorites WHERE userEmail = :email")
    List<Favorite> getFavoritesByUser(String email);

    // استعلام للتأكد إذا كانت السيارة موجودة في المفضلة أم لا (لتغيير شكل القلب)
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE userEmail = :email AND carId = :carId)")
    boolean isFavorite(String email, int carId);

    @Query("DELETE FROM favorites WHERE userEmail = :email AND carId = :carId")
    void deleteByCarId(String email, int carId);
}