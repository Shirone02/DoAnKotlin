package com.example.doankotlin.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doankotlin.Adapter.OrderDetailsAdapter
import com.example.doankotlin.Domain.Foods
import com.example.doankotlin.Domain.OrderDetails
import com.example.doankotlin.R
import com.example.doankotlin.databinding.ActivityOrderDetailsBinding

class OrderDetailsActivity : BaseActivity() {
    private val binding: ActivityOrderDetailsBinding by lazy {
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }
    private var userName: String?= null
    private var address:String?= null
    private var phoneNumber: String?= null
    private var totalPrice: String? = null
    private var listFood: ArrayList<Foods> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        getDataFromIntent()
    }

    private fun getDataFromIntent() {
        val receivedOrderDetails = intent.getSerializableExtra("UserOrderDetails") as OrderDetails
        receivedOrderDetails.let{orderDetails ->
                userName = receivedOrderDetails.userName
                listFood = receivedOrderDetails.listFoods!!
                address = receivedOrderDetails.address
                phoneNumber = receivedOrderDetails.phoneNumber
                totalPrice = receivedOrderDetails.totalPrice

                setUserDetail()
                setAdapter()
        }

    }

    private fun setAdapter() {
        binding.orderDetailsRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = OrderDetailsAdapter(this, listFood)
        binding.orderDetailsRecyclerView.adapter = adapter

    }

    private fun setUserDetail() {
        binding.name.text = userName
        binding.address.text = address
        binding.phone.text = phoneNumber
        binding.totalAmoutTxt.text = totalPrice

    }
}