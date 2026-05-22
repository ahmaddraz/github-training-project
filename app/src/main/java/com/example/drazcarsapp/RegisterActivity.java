package com.example.drazcarsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        EditText etName = findViewById(R.id.et_name);
        EditText etEmail = findViewById(R.id.et_email);
        EditText etPass = findViewById(R.id.et_password);
        EditText etPhone = findViewById(R.id.et_phone);
        Button btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String pass = etPass.getText().toString().trim();
            String phone = etPhone != null ? etPhone.getText().toString().trim() : "";

            if(name.isEmpty() || email.isEmpty() || pass.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "يرجى ملأ جميع الحقول بما فيها رقم الهاتف", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                try {
                    // التعديل هنا: أضفنا "" كقيمة افتراضية لمسار الصورة (ImagePath)
                    // الترتيب حسب الكونسرتكتور في User: (الاسم، الإيميل، الباسورد، مسار الصورة، الهاتف)
                    User user = new User(name, email, pass, "", phone);

                    AppDatabase.getInstance(this).userDao().register(user);

                    SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("user_email", email);
                    editor.putString("user_name", name);
                    editor.apply();

                    runOnUiThread(() -> {
                        Toast.makeText(this, "تم إنشاء الحساب بنجاح ✅", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> Toast.makeText(this, "خطأ: الإيميل مسجل مسبقاً أو حدثت مشكلة", Toast.LENGTH_SHORT).show());
                }
            }).start();
        });
    }
}