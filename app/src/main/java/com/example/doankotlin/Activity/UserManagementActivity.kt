package com.example.doankotlin.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doankotlin.Adapter.UserCardAdapter
import com.example.doankotlin.Domain.User
import com.example.doankotlin.databinding.ActivityUserManagementBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class UserManagementActivity : BaseActivity() {
    private val binding: ActivityUserManagementBinding by lazy {
        ActivityUserManagementBinding.inflate(layoutInflater)
    }

    private lateinit var adapter: UserCardAdapter
    private var userList: ArrayList<User> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.userListView.layoutManager = LinearLayoutManager(this)
        adapter = UserCardAdapter(userList)
        binding.userListView.adapter = adapter
        initListUser()
        binding.backBtn.setOnClickListener { finish() }
    }

    private fun initListUser() {
        val userId = mAuth.currentUser?.uid?:""
        val userRef = database.getReference("Users").orderByChild("name")
        userRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userList.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)
                    user?.let {
                        userList.add(it)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("UserManagementActivity", "Failed to read user data", databaseError.toException())
            }
        })
    }


}