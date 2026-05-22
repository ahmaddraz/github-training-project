package com.example.drazcarsapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.concurrent.Executors;

public class BookingActivity extends AppCompatActivity {

    private TextView tvCarName, tvCarPrice, tvTotalDays, tvFinalPrice;
    private Button btnStartDate, btnEndDate, btnConfirm;
    private ImageView btnBack;

    private Calendar startCalendar = Calendar.getInstance();
    private Calendar endCalendar = Calendar.getInstance();
    private int dailyPrice = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        initViews();
        String carName = getIntent().getStringExtra("car_name");
        String carPriceString = getIntent().getStringExtra("car_price");

        if (carName != null) {
            tvCarName.setText(carName);
        }

        if (carPriceString != null) {
            try {
                dailyPrice = Integer.parseInt(carPriceString.replaceAll("[^0-9]", ""));
                tvCarPrice.setText("السعر اليومي: " + carPriceString);
            } catch (Exception e) {
                dailyPrice = 200;
                tvCarPrice.setText("السعر اليومي: " + carPriceString);
            }
        }

        btnBack.setOnClickListener(v -> {
            finish();
        });

        btnStartDate.setOnClickListener(v -> showDatePicker(true));
        btnEndDate.setOnClickListener(v -> showDatePicker(false));
        btnConfirm.setOnClickListener(v -> saveBookingToDatabase(carName));
    }

    private void initViews() {
        tvCarName = findViewById(R.id.tv_booking_car_name);
        tvCarPrice = findViewById(R.id.tv_booking_car_price);
        tvTotalDays = findViewById(R.id.tv_total_days);
        tvFinalPrice = findViewById(R.id.tv_final_price);
        btnStartDate = findViewById(R.id.btn_start_date);
        btnEndDate = findViewById(R.id.btn_end_date);
        btnConfirm = findViewById(R.id.btn_confirm_booking);
        btnBack = findViewById(R.id.btn_back_booking);
    }

    private void showDatePicker(boolean isStart) {
        Calendar cal = isStart ? startCalendar : endCalendar;
        DatePickerDialog datePicker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            cal.set(year, month, dayOfMonth);
            String dateStr = dayOfMonth + "/" + (month + 1) + "/" + year;

            if (isStart) {
                btnStartDate.setText("الاستلام: " + dateStr);
            } else {
                btnEndDate.setText("الإرجاع: " + dateStr);
            }
            calculateTotal();
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        datePicker.show();
    }

    private void calculateTotal() {
        long diff = endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis();
        if (diff > 0) {
            long days = diff / (24 * 60 * 60 * 1000);
            if (days == 0) days = 1;

            tvTotalDays.setText(days + " أيام");
            long total = days * dailyPrice;
            tvFinalPrice.setText(total + " شيكل");
        } else {
            tvTotalDays.setText("0 أيام");
            tvFinalPrice.setText("0 شيكل");
        }
    }

    private void saveBookingToDatabase(String carName) {
        String startDateStr = btnStartDate.getText().toString();
        String endDateStr = btnEndDate.getText().toString();
        String totalPrice = tvFinalPrice.getText().toString();

        if (!startDateStr.contains("/") || !endDateStr.contains("/")) {
            Toast.makeText(this, "يرجى تحديد تواريخ الاستلام والإرجاع", Toast.LENGTH_SHORT).show();
            return;
        }

        if (endCalendar.getTimeInMillis() <= startCalendar.getTimeInMillis()) {
            Toast.makeText(this, "تاريخ الإرجاع يجب أن يكون بعد تاريخ الاستلام", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                String cleanStart = startDateStr.replace("الاستلام: ", "").trim();
                String cleanEnd = endDateStr.replace("الإرجاع: ", "").trim();
                Booking newBooking = new Booking(carName, cleanStart, cleanEnd, totalPrice, "user@example.com", "نشط");
                AppDatabase.getInstance(this).bookingDao().insertBooking(newBooking);

                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(this, "تم الحجز بنجاح!", Toast.LENGTH_LONG).show();
                    finish();
                });
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(this, "حدث خطأ أثناء الحفظ", Toast.LENGTH_SHORT).show());
            }
        });
    }
}


