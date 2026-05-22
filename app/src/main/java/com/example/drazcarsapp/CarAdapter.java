package com.example.drazcarsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.concurrent.Executors;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private List<Car> carList;
    private AppDatabase db;

    public CarAdapter(List<Car> carList) {
        this.carList = carList;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car, parent, false);
        db = AppDatabase.getInstance(parent.getContext());
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = carList.get(position);
        Context context = holder.itemView.getContext();

        // جلب إيميل المستخدم الحالي من SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String currentUserEmail = prefs.getString("user_email", "");

        holder.tvName.setText(car.getName());
        holder.tvPrice.setText(car.getPrice());
        holder.tvStatus.setText(car.getStatus());
        holder.imgCar.setImageResource(car.getImageResId());

        // التحقق مما إذا كانت السيارة في المفضلة لهذا المستخدم تحديداً
        Executors.newSingleThreadExecutor().execute(() -> {
            boolean isFav = db.favoriteDao().isFavorite(currentUserEmail, car.getId());
            new Handler(Looper.getMainLooper()).post(() -> {
                holder.btnFav.setImageResource(isFav ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
            });
        });

        // عند الضغط على زر النجمة
        holder.btnFav.setOnClickListener(v -> {
            Executors.newSingleThreadExecutor().execute(() -> {
                if (db.favoriteDao().isFavorite(currentUserEmail, car.getId())) {
                    // إذا كانت موجودة، نقوم بحذفها
                    db.favoriteDao().deleteByCarId(currentUserEmail, car.getId());
                    new Handler(Looper.getMainLooper()).post(() -> holder.btnFav.setImageResource(android.R.drawable.btn_star_big_off));
                } else {
                    // إذا لم تكن موجودة، نقوم بإضافتها مع الإيميل (هنا تم حل مشكلة الـ Constructor)
                    db.favoriteDao().addFavorite(new Favorite(car.getId(), currentUserEmail));
                    new Handler(Looper.getMainLooper()).post(() -> holder.btnFav.setImageResource(android.R.drawable.btn_star_big_on));
                }
            });
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("carName", car.getName());
            intent.putExtra("carPrice", car.getPrice());
            intent.putExtra("carImage", car.getImageResId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return carList.size(); }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCar, btnFav;
        TextView tvName, tvPrice, tvStatus;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCar = itemView.findViewById(R.id.img_car);
            tvName = itemView.findViewById(R.id.tv_car_name);
            tvPrice = itemView.findViewById(R.id.tv_car_price);
            tvStatus = itemView.findViewById(R.id.tv_car_status);
            btnFav = itemView.findViewById(R.id.btn_fav);
        }
    }
}