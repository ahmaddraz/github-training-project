package com.example.drazcarsapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.concurrent.Executors;

public class MyBookingsActivity extends AppCompatActivity {

    private RecyclerView rvMyBookings;
    private BookingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle("حجوزاتي");

        rvMyBookings = findViewById(R.id.rv_my_bookings);
        rvMyBookings.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadBookings();
    }

    private void loadBookings() {
        Executors.newSingleThreadExecutor().execute(() -> {

            List<Booking> bookingList = AppDatabase.getInstance(this).bookingDao().getAllBookings();

            runOnUiThread(() -> {
                if (bookingList != null && !bookingList.isEmpty()) {
                    adapter = new BookingAdapter(bookingList);
                    rvMyBookings.setAdapter(adapter);
                } else {

                }
            });
        });
    }
}