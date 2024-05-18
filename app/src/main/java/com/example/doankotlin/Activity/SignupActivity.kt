package com.example.doankotlin.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Toast
import com.example.doankotlin.databinding.ActivitySignupBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

class SignupActivity : BaseActivity() {
    var binding: ActivitySignupBinding? = null
    var emailEdt: EditText? = null
    var passwordEdt: EditText? = null
    var valid = true
    var isAdminBox: CheckBox? = null
    var isUserBox: CheckBox? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding!!.getRoot())
        setVariable()
    }

    private fun setVariable() {
        binding!!.cbUser.setOnCheckedChangeListener { buttonView: CompoundButton, isChecked: Boolean ->
            if (buttonView.isChecked) {
                binding!!.cbQTV.setChecked(false)
            }
        }
        binding!!.cbQTV.setOnCheckedChangeListener { buttonView: CompoundButton, isChecked: Boolean ->
            if (buttonView.isChecked) {
                binding!!.cbUser.setChecked(false)
            }
        }
        binding!!.signupBtn.setOnClickListener { v: View? ->
            val email = binding!!.userEdt.getText().toString()
            val password = binding!!.passEdt.getText().toString()
            checkField(binding!!.userEdt)
            checkField(binding!!.passEdt)
            if (!(binding!!.cbQTV.isChecked || binding!!.cbUser.isChecked)) {
                Toast.makeText(this, "Vui lòng chọn quyền truy cập", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (valid) {
                if (password.length < 6) {
                    Toast.makeText(this@SignupActivity, "Độ dài mật khẩu phải có ít nhất 6 kí tự", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this@SignupActivity) { task->
                        if (task.isSuccessful) {

                            mAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                                Toast.makeText(this, "Vui lòng xác nhận email của bạn", Toast.LENGTH_SHORT).show()

                                val user = mAuth.currentUser
                                Toast.makeText(this, "Tài khoản được tạo thành công", Toast.LENGTH_SHORT).show()
                                val myRef = database.getReference("Users").child(user!!.uid)
                                val userInfo: MutableMap<String, Any> = HashMap()
                                userInfo["Email"] = email
                                userInfo["Password"] = password
                                if (binding!!.cbQTV.isChecked) {
                                    userInfo["isAdmin"] = "1"
                                } else if (binding!!.cbUser.isChecked) {
                                    userInfo["isUser"] = "0"
                                }
                                myRef.setValue(userInfo)
                                startActivity(Intent(applicationContext, LoginActivity::class.java))
                                finish()

                            }?.addOnFailureListener {
                                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                            }

                        } else {
                            Toast.makeText(this@SignupActivity, "Tạo tài khoản lỗi", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        binding!!.tvGotoLogin.setOnClickListener { v: View? ->
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
            finish()
        }
    }

    fun checkField(textField: EditText): Boolean {
        if (textField.getText().toString().isEmpty()) {
            textField.error = "Error"
            valid = false
        } else {
            valid = true
        }
        return valid
    }
}