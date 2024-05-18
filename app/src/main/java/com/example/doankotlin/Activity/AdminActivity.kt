package com.example.doankotlin.Activity

import android.content.Intent
import android.os.Bundle
import com.example.doankotlin.databinding.ActivityAdminBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminActivity : BaseActivity() {

    private val binding: ActivityAdminBinding by lazy {
        ActivityAdminBinding.inflate(layoutInflater)
    }

    private lateinit var completedOrderReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.addMenu.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }

        binding.seeAll.setOnClickListener {
            val intent = Intent(this, AllItemActivity::class.java)
            startActivity(intent)
        }

        binding.userManagement.setOnClickListener {
            val intent = Intent(this, UserManagementActivity::class.java)
            startActivity(intent)
        }

        binding.pendingOrder.setOnClickListener {
            val intent = Intent(this, PendingOrderActivity::class.java)
            startActivity(intent)
        }

        binding.delivery.setOnClickListener {
            val intent = Intent(this, OutForDeliveryActivity::class.java)
            startActivity(intent)
        }

        binding.deleteItem.setOnClickListener {
            val intent = Intent(this, DeleteFoodActivity::class.java)
            startActivity(intent)
        }

        binding.logOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@AdminActivity, LoginActivity::class.java))
            finish()
        }


    }



}