package com.example.doankotlin.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doankotlin.MainActivity;
import com.example.doankotlin.R;
import com.example.doankotlin.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {

    ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        setVariable();
        getWindow().setStatusBarColor(Color.parseColor("#FFE485"));
    }

    private void setVariable() {
        binding.loginBtn.setOnClickListener(v -> {
            if(mAuth.getCurrentUser() != null){
                startActivity(new Intent(IntroActivity.this, MainJavaActivity.class));
            } else {
                startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            }
        });

        binding.signupBtn.setOnClickListener(v -> startActivity(new Intent(IntroActivity.this, SignupActivity.class)));
    }
}