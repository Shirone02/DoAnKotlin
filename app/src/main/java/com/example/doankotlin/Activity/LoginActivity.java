package com.example.doankotlin.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doankotlin.MainActivity;
import com.example.doankotlin.R;
import com.example.doankotlin.databinding.ActivityLoginBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends BaseActivity {

    ActivityLoginBinding binding;
    EditText email, password;
    boolean valid = true;
    Button loginBtn;
    TextView gotoRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        email = findViewById(R.id.userEdt);
        password = findViewById(R.id.passEdt);
        loginBtn = findViewById(R.id.loginBtn);
        gotoRegister = findViewById(R.id.tvGotoRegister);

        setVariable();

    }

    private void setVariable() {
        binding.loginBtn.setOnClickListener(v -> {
            checkField(email);
            checkField(password);

            if (valid) {
                String email = binding.userEdt.getText().toString();
                String password = binding.passEdt.getText().toString();
                if (!email.isEmpty() && !password.isEmpty()) {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {
                        if (task.isSuccessful()) {
                            checkUserAccessLevel(task.getResult().getUser().getUid());
                        } else {
                            Toast.makeText(LoginActivity.this, "Xác thực lỗi", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Vui lòng điền email và mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }

        });

        binding.tvGotoRegister.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignupActivity.class)));
    }

    private void checkUserAccessLevel(String uid) {
        DatabaseReference myRef = database.getReference("Users").child(uid);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("TAG", "onDataChange: " + snapshot.getValue());

                if (snapshot.exists()) {
                    if (snapshot.hasChild("isAdmin")) {
                        startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                        finish();
                    }

                    if (snapshot.hasChild("isUser")) {
                        startActivity(new Intent(getApplicationContext(), MainJavaActivity.class));
                        finish();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public boolean checkField(@NonNull EditText textField) {
        if (textField.getText().toString().isEmpty()) {
            textField.setError("Error");
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }

}