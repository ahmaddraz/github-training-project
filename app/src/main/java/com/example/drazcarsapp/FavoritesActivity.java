package com.example.drazcarsapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CarAdapter adapter;
    private List<Car> favoriteCarsList = new ArrayList<>();
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        recyclerView = findViewById(R.id.rv_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = AppDatabase.getInstance(this);

        loadFavorites();
    }

    private void loadFavorites() {
        // 1. جلب إيميل المستخدم المسجل حالياً
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String currentUserEmail = prefs.getString("user_email", "");

        List<Car> allCars = getAllAvailableCars();

        Executors.newSingleThreadExecutor().execute(() -> {
            // 2. استخدم favoriteDao بدلاً من bookingDao
            // وجلبنا القائمة بناءً على إيميل المستخدم
            List<Favorite> favorites = db.favoriteDao().getFavoritesByUser(currentUserEmail);

            // تحويل قائمة الـ Favorites إلى قائمة IDs لسهولة المقارنة
            List<Integer> favIds = new ArrayList<>();
            if (favorites != null) {
                for (Favorite f : favorites) {
                    favIds.add(f.getCarId());
                }
            }

            favoriteCarsList.clear();
            for (Car car : allCars) {
                if (favIds.contains(car.getId())) {
                    favoriteCarsList.add(car);
                }
            }

            runOnUiThread(() -> {
                if (favoriteCarsList.isEmpty()) {
                    Toast.makeText(this, "قائمة المفضلة فارغة", Toast.LENGTH_SHORT).show();
                }
                // تحديث الـ Adapter بالبيانات الجديدة
                adapter = new CarAdapter(favoriteCarsList);
                recyclerView.setAdapter(adapter);
            });
        });
    }

    private List<Car> getAllAvailableCars() {
        List<Car> list = new ArrayList<>();
        list.add(new Car(1, "بي إم دبليو M4", "600 شيكل", R.drawable.bmw_car, "متاحة", "فاخرة"));
        list.add(new Car(2, "تويوتا لاندكروزر", "800 شيكل", R.drawable.menu_gallery, "متاحة", "SUV"));
        list.add(new Car(3, "هيونداي اكسنت", "150 شيكل", R.drawable.hyundai_accent, "متاحة", "اقتصادية"));
        list.add(new Car(4, "كيا كيرنز", "300 شيكل", R.drawable.kiac_arens, "متاحة", "عائلية"));
        list.add(new Car(5, "تسلا موديل Y", "500 شيكل", R.drawable.tesla_model_y, "متاحة", "كهربائية"));
        return list;
    }
}