package com.example.doankotlin.Activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.doankotlin.R
import com.example.doankotlin.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class LoginActivity : BaseActivity() {
    var binding: ActivityLoginBinding? = null
    var email: TextInputEditText? = null
    var password: TextInputEditText? = null
    var valid = true
    var loginBtn: Button? = null
    var gotoRegister: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding!!.getRoot())
        email = findViewById(R.id.userEdt)
        password = findViewById(R.id.passEdt)
        loginBtn = findViewById(R.id.loginBtn)
        gotoRegister = findViewById(R.id.tvGotoRegister)
        setVariable()
        binding!!.password.isHelperTextEnabled = false
        binding!!.passEdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length == 0) {
                    binding!!.password.error = "*Không được để trống"
                } else {
                    binding!!.password.error = null
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding!!.userEdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length == 0) {
                    binding!!.user.error = "*Không được để trống"
                } else {
                    binding!!.user.error = null
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding!!.tvForgotPassword.setOnClickListener {
            startActivity( Intent(this, ResetPasswordActivity::class.java))
            finish()
        }
    }

    private fun setVariable() {
        binding!!.loginBtn.setOnClickListener { v: View? ->
            checkField(email!!)
            checkField(password!!)
            if (valid) {
                val email = binding!!.userEdt.getText().toString()
                val password = binding!!.passEdt.getText().toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this@LoginActivity) { task ->
                            if (task.isSuccessful) {
                                val verification = mAuth.currentUser?.isEmailVerified
                                if(verification == true){
                                    checkUserAccessLevel(task.result.user!!.uid)
                                } else {
                                    Toast.makeText(this, "Vui lòng xác nhận email của bạn", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this@LoginActivity, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this@LoginActivity, "Vui lòng điền email và mật khẩu", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding!!.tvGotoRegister.setOnClickListener { v: View? ->
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
            finish()
        }
    }

    private fun checkUserAccessLevel(uid: String) {
        val myRef = database.getReference("Users").child(uid)
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("TAG", "onDataChange: " + snapshot.value)
                if (snapshot.exists()) {
                    if (snapshot.hasChild("isAdmin")) {
                        startActivity(Intent(applicationContext, AdminActivity::class.java))
                        finish()
                    }
                    if (snapshot.hasChild("isUser")) {
                        startActivity(Intent(applicationContext, MainJavaActivity::class.java))
                        finish()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun checkField(textField: TextInputEditText): Boolean {
        valid = if (textField.getText().toString().isEmpty()) {
            //textField.setError("Error");
            false
        } else {
            true
        }
        return valid
    }
}