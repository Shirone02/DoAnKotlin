package com.example.doankotlin.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doankotlin.MainActivity;
import com.example.doankotlin.R;
import com.example.doankotlin.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class SignupActivity extends BaseActivity {

    ActivitySignupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVariable();
    }

    private void setVariable() {
        binding.signupBtn.setOnClickListener(v -> {
            String email = binding.userEdt.getText().toString();
            String password = binding.passEdt.getText().toString();

            if(password.length()<6){
                Toast.makeText(SignupActivity.this, "Độ dài mật khẩu phải có ít nhất 6 kí tự", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignupActivity.this, task -> {
                if(task.isSuccessful()){
                    Log.i("TAG","onComplete: ");
                    startActivity(new Intent(SignupActivity.this, MainJavaActivity.class));
                } else {
                    Log.i("TAG","failure: "+task.getException());
                    Toast.makeText(SignupActivity.this,"Xác thực lỗi",Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}