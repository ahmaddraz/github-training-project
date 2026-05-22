package com.example.drazcarsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvCategories, rvCars;
    private TextView tvWelcome;
    private EditText etSearch;
    private ImageView imgUserHome;
    private BottomNavigationView bottomNavigationView;

    private List<Car> allCarsList = new ArrayList<>();
    private List<Car> filteredCarsList = new ArrayList<>();
    private CarAdapter carAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        loadUserData();
        setupCarsData(); // تم تحديثها بالـ IDs
        setupCategories();
        setupSearch();
        setupBottomNavigation(); // تم تحديثها لفتح المفضلة
    }

    private void initViews() {
        rvCategories = findViewById(R.id.rv_categories);
        rvCars = findViewById(R.id.rv_home_cars);
        tvWelcome = findViewById(R.id.tv_welcome_name);
        etSearch = findViewById(R.id.et_home_search);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        imgUserHome = findViewById(R.id.img_user_home);

        if (getSupportActionBar() != null) getSupportActionBar().hide();
    }

    private void loadUserData() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String email = prefs.getString("user_email", "");

        new Thread(() -> {
            User user = AppDatabase.getInstance(this).userDao().getUserByEmail(email);
            if (user != null) {
                runOnUiThread(() -> {
                    tvWelcome.setText("أهلاً بك يا " + user.getName());
                    if (user.getImagePath() != null && !user.getImagePath().isEmpty() && imgUserHome != null) {
                        try {
                            imgUserHome.setImageURI(Uri.parse(user.getImagePath()));
                        } catch (Exception e) {
                            imgUserHome.setImageResource(R.drawable.app_logo);
                        }
                    }
                });
            }
        }).start();
    }

    private void setupCategories() {
        List<String> categories = new ArrayList<>();
        categories.add("الكل");
        categories.add("عائلية");
        categories.add("SUV");
        categories.add("اقتصادية");
        categories.add("فاخرة");
        categories.add("كهربائية");

        rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        CategoryAdapter adapter = new CategoryAdapter(categories, category -> {
            filterCarsByCategory(category);
        });
        rvCategories.setAdapter(adapter);
    }

    private void setupCarsData() {
        allCarsList.clear();
        // تم إضافة الـ ID كأول متغير لكل سيارة لحل خطأ الـ Constructor
        allCarsList.add(new Car(1, "بي إم دبليو M4", "600 شيكل", R.drawable.bmw_car, "متاحة", "فاخرة"));
        allCarsList.add(new Car(2, "تويوتا لاندكروزر", "800 شيكل", R.drawable.menu_gallery, "متاحة", "SUV"));
        allCarsList.add(new Car(3, "هيونداي اكسنت", "150 شيكل", R.drawable.hyundai_accent, "متاحة", "اقتصادية"));
        allCarsList.add(new Car(4, "كيا كيرنز", "300 شيكل", R.drawable.kiac_arens, "متاحة", "عائلية"));
        allCarsList.add(new Car(5, "تسلا موديل Y", "500 شيكل", R.drawable.tesla_model_y, "متاحة", "كهربائية"));

        filteredCarsList.clear();
        filteredCarsList.addAll(allCarsList);

        rvCars.setLayoutManager(new LinearLayoutManager(this));
        carAdapter = new CarAdapter(filteredCarsList);
        rvCars.setAdapter(carAdapter);
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCarsBySearch(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterCarsByCategory(String category) {
        filteredCarsList.clear();
        if (category.equals("الكل")) {
            filteredCarsList.addAll(allCarsList);
        } else {
            for (Car car : allCarsList) {
                if (car.getCategory().equalsIgnoreCase(category)) {
                    filteredCarsList.add(car);
                }
            }
        }
        carAdapter.notifyDataSetChanged();
    }

    private void filterCarsBySearch(String query) {
        filteredCarsList.clear();
        for (Car car : allCarsList) {
            if (car.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredCarsList.add(car);
            }
        }
        carAdapter.notifyDataSetChanged();
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_bookings) {
                startActivity(new Intent(this, MyBookingsActivity.class));
                return true;
            } else if (id == R.id.nav_favorites) { // <--- تم تغيير الاسم هنا ليطابق الـ XML الخاص بك
                startActivity(new Intent(this, FavoritesActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }
}