package com.example.doankotlin.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doankotlin.Adapter.RecentBuyAdapter
import com.example.doankotlin.Domain.Foods
import com.example.doankotlin.Domain.OrderDetails
import com.example.doankotlin.R
import com.example.doankotlin.databinding.ActivityRecentOrderItemsBinding

class RecentOrderItemsActivity : AppCompatActivity() {
    private val binding: ActivityRecentOrderItemsBinding by lazy {
        ActivityRecentOrderItemsBinding.inflate(layoutInflater)
    }
    private lateinit var allFoods: ArrayList<Foods>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val recentOrderItems = intent.getSerializableExtra("RecentBuyOrderItem") as ArrayList<OrderDetails>
        recentOrderItems.let { orderDetails ->
            if(orderDetails.isNotEmpty()){
                val recentOrderItem = orderDetails[0]

                allFoods = recentOrderItem.listFoods!!
            }
        }

        setAdapter()

        binding.backBtn.setOnClickListener { finish() }
    }

    private fun setAdapter() {
        val rv = binding.recentOrderView
        rv.layoutManager = LinearLayoutManager(this)
        val adapter = RecentBuyAdapter(this, allFoods)
        rv.adapter = adapter
    }
}