package com.example.doankotlin.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.doankotlin.R
import com.example.doankotlin.databinding.ActivityResetPasswordBinding

class ResetPasswordActivity : BaseActivity() {

    var binding: ActivityResetPasswordBinding?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.resetPasswordBtn.setOnClickListener {
            val forgotEmail = binding!!.forgotEmail.text.toString()

            mAuth.sendPasswordResetEmail(forgotEmail).addOnSuccessListener {
                Toast.makeText(this, "Đặt lại mật khẩu thành công. Vui lòng kiểm tra email của bạn", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()

            }.addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        binding!!.backBtn.setOnClickListener { finish() }

    }
}