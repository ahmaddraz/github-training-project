package com.example.drazcarsapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface BookingDao {
    @Insert
    void insertBooking(Booking booking);

    @Query("SELECT * FROM bookings ORDER BY id DESC")
    List<Booking> getAllBookings();

    @Delete
    void deleteBooking(Booking booking);
    @Insert
    void addFavorite(Favorite favorite);
    @Query("DELETE FROM favorites WHERE carId = :cId")
    void removeFavorite(int cId);
    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE carId = :cId)")
    boolean isFavorite(int cId);
    @Query("SELECT carId FROM favorites")
    List<Integer> getAllFavoriteIds();
}