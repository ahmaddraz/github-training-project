package com.example.drazcarsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPhone;
    private Button btnUpdate, btnLogout;
    private ImageView imgProfile;
    private User currentUser;
    private String selectedImageUri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etName = findViewById(R.id.et_profile_name);
        etEmail = findViewById(R.id.et_profile_email);
        etPhone = findViewById(R.id.et_profile_phone);
        btnUpdate = findViewById(R.id.btn_update_profile);
        btnLogout = findViewById(R.id.btn_logout);
        imgProfile = findViewById(R.id.img_profile_pic);

        if (getSupportActionBar() != null) getSupportActionBar().hide();
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userEmail = prefs.getString("user_email", "");

        if (!userEmail.isEmpty()) {
            loadUserData(userEmail);
        } else {
            Toast.makeText(this, "لم يتم العثور على بيانات الجلسة", Toast.LENGTH_SHORT).show();
            finish();
        }
        imgProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, 101);
        });
        btnUpdate.setOnClickListener(v -> updateProfile());
        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                selectedImageUri = uri.toString();
                imgProfile.setImageURI(uri);
            }
        }
    }

    private void loadUserData(String email) {
        new Thread(() -> {
            currentUser = AppDatabase.getInstance(this).userDao().getUserByEmail(email);
            runOnUiThread(() -> {
                if (currentUser != null) {
                    etName.setText(currentUser.getName());
                    etEmail.setText(currentUser.getEmail());
                    etPhone.setText(currentUser.getPhoneNumber());
                    etEmail.setEnabled(false);
                    if (currentUser.getImagePath() != null && !currentUser.getImagePath().isEmpty()) {
                        imgProfile.setImageURI(Uri.parse(currentUser.getImagePath()));
                    }
                }
            });
        }).start();
    }

    private void updateProfile() {
        if (currentUser == null) return;

        String newName = etName.getText().toString().trim();
        String newPhone = etPhone.getText().toString().trim();

        if (newName.isEmpty() || newPhone.isEmpty()) {
            Toast.makeText(this, "يرجى ملء الحقول", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            currentUser.setName(newName);
            currentUser.setPhoneNumber(newPhone);

            if (!selectedImageUri.isEmpty()) {
                currentUser.setImagePath(selectedImageUri);
            }

            AppDatabase.getInstance(this).userDao().updateUser(currentUser);
            runOnUiThread(() -> Toast.makeText(this, "تم التحديث بنجاح ✅", Toast.LENGTH_SHORT).show());
        }).start();
    }
}