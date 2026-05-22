package com.example.drazcarsapp;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.concurrent.Executors;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookingList;

    public BookingAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.tvCarName.setText(booking.getCarName());
        holder.tvDates.setText("من: " + booking.getStartDate() + " إلى: " + booking.getEndDate());
        holder.tvPrice.setText(booking.getTotalPrice());
        holder.tvStatus.setText(booking.getStatus() != null ? booking.getStatus() : "نشط");

        // لون الحالة
        holder.tvStatus.setBackgroundColor(Color.parseColor("#4CAF50"));
        holder.tvStatus.setTextColor(Color.WHITE);
        holder.btnDelete.setOnClickListener(v -> {
            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase.getInstance(v.getContext()).bookingDao().deleteBooking(booking);
                new Handler(Looper.getMainLooper()).post(() -> {
                    int currentPosition = holder.getAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        bookingList.remove(currentPosition);
                        notifyItemRemoved(currentPosition);
                        notifyItemRangeChanged(currentPosition, bookingList.size());

                        Toast.makeText(v.getContext(), "تم حذف الحجز بنجاح", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });
    }

    @Override
    public int getItemCount() {
        return bookingList != null ? bookingList.size() : 0;
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvCarName, tvDates, tvPrice, tvStatus;
        ImageView btnDelete;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCarName = itemView.findViewById(R.id.tv_res_car_name);
            tvDates = itemView.findViewById(R.id.tv_res_dates);
            tvPrice = itemView.findViewById(R.id.tv_res_total_price);
            tvStatus = itemView.findViewById(R.id.tv_res_status);
            btnDelete = itemView.findViewById(R.id.btn_delete_booking);
        }
    }
}
