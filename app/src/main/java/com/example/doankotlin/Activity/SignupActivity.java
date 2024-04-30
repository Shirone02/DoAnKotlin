package com.example.doankotlin.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends BaseActivity {

    ActivitySignupBinding binding;
    EditText emailEdt, passwordEdt;
    boolean valid = true;

    CheckBox isAdminBox, isUserBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVariable();
    }

    private void setVariable() {
        binding.cbUser.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                binding.cbQTV.setChecked(false);
            }
        });

        binding.cbQTV.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                binding.cbUser.setChecked(false);
            }
        });

        binding.signupBtn.setOnClickListener(v -> {
            String email = binding.userEdt.getText().toString();
            String password = binding.passEdt.getText().toString();

            checkField(binding.userEdt);
            checkField(binding.passEdt);

            if(!(binding.cbQTV.isChecked() || binding.cbUser.isChecked())){
                Toast.makeText(this, "Vui lòng chọn quyền truy cập", Toast.LENGTH_SHORT).show();
                return;
            }

            if (valid) {
                if (password.length() < 6) {
                    Toast.makeText(SignupActivity.this, "Độ dài mật khẩu phải có ít nhất 6 kí tự", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(this, "Tài khoản được tạo thành công", Toast.LENGTH_SHORT).show();
                        DatabaseReference myRef = database.getReference("Users").child(user.getUid());
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("Email", email);
                        userInfo.put("Password", password);
                        if(binding.cbQTV.isChecked()){
                            userInfo.put("isAdmin", "1");
                        }else if(binding.cbUser.isChecked()){
                            userInfo.put("isUser", "1");
                        }

                        myRef.setValue(userInfo);
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SignupActivity.this, "Tạo tài khoản lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });

        binding.tvGotoLogin.setOnClickListener(v -> startActivity(new Intent(SignupActivity.this, LoginActivity.class)));
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