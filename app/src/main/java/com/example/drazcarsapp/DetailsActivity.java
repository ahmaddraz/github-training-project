package com.example.drazcarsapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast; // أضفت التوست للتأكد
import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // 1. تعريف العناصر
        ImageView imgCar = findViewById(R.id.img_details_car);
        TextView tvName = findViewById(R.id.tv_details_name);
        TextView tvPrice = findViewById(R.id.tv_details_price);
        Button btnBookNow = findViewById(R.id.btn_book_now);
        ImageView btnBack = findViewById(R.id.btn_back); // تعريف زر الرجوع

        String name = getIntent().getStringExtra("carName");
        String price = getIntent().getStringExtra("carPrice");
        int image = getIntent().getIntExtra("carImage", 0);

        tvName.setText(name);
        tvPrice.setText(price);
        imgCar.setImageResource(image);
        btnBack.setOnClickListener(v -> {
            finish();
        });
        btnBookNow.setOnClickListener(v -> {
            // تأكد أن BookingActivity منشأة عندك
            Intent intent = new Intent(DetailsActivity.this, BookingActivity.class);
            intent.putExtra("car_name", name);
            intent.putExtra("car_price", price);
            startActivity(intent);
        });
    }
}