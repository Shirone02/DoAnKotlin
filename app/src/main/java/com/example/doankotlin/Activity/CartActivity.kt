package com.example.doankotlin.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doankotlin.Adapter.CartAdapter
import com.example.doankotlin.Domain.CartItems
import com.example.doankotlin.Domain.Foods
import com.example.doankotlin.databinding.ActivityCartBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class CartActivity : BaseActivity() {
    private lateinit var binding: ActivityCartBinding
    private var tax = 0.0
    private lateinit var cartAdapter: CartAdapter
    private lateinit var userId: String
    private var receivedData = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setVariables()
        retrieveCartItems()
    }

    private fun retrieveCartItems() {
        userId = mAuth.currentUser?.uid ?: ""
        val cartReference: DatabaseReference =
            database.reference.child("Users").child(userId).child("CartItems")

        val cartItemList: ArrayList<Foods> = ArrayList()

        cartReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cartItemList.clear()
                receivedData = 0.0 // Reset receivedData
                for (cartSnapshot in snapshot.children) {
                    val cartItems = cartSnapshot.getValue(CartItems::class.java)
                    cartItems?.let {
                        val foodItem = Foods().apply {
                            title = it.Title
                            price = it.Price ?: 0
                            imagePath = it.ImagePath
                            numberInCart = it.Quantity ?: 0
                        }
                        cartItemList.add(foodItem)
                        receivedData += foodItem.price * foodItem.numberInCart
                    }
                }
                if(cartItemList.isEmpty()){
                    binding.emptyTxt.visibility = View.VISIBLE
                    binding.scrollviewCart.visibility = View.GONE
                } else {
                    binding.emptyTxt.visibility = View.GONE
                    binding.scrollviewCart.visibility = View.VISIBLE
                }
                setAdapter(cartItemList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CartActivity, "Error retrieving cart items", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setAdapter(cartItems: ArrayList<Foods>) {
        cartAdapter = CartAdapter(cartItems, this@CartActivity) {
            calculateCart()
        }
        binding.cardView.layoutManager = LinearLayoutManager(this@CartActivity)
        binding.cardView.adapter = cartAdapter

        // Calculate cart total initially
        calculateCart()
    }

    private fun calculateCart() {
        val percentTax = 0.04 // 4%
        val delivery = 1000.0 // 1000 VND

        tax = (Math.round(receivedData * percentTax * 100.0) / 100).toDouble()
        val total = (Math.round((receivedData + tax + delivery) * 100) / 100).toDouble()
        val itemTotal = (Math.round(receivedData * 100) / 100).toDouble()

        binding.totalFeeTxt.text = "${itemTotal}đ"
        binding.taxTxt.text = "${tax}đ"
        binding.deliveryTxt.text = "${delivery}đ"
        binding.totalTxt.text = "${total}đ"
    }

    private fun setVariables() {
        binding.backBtn.setOnClickListener { finish() }
        binding.orderBtn.setOnClickListener {
            // Directly pass cartItemList to orderNow function
            orderNow(cartAdapter.getCartItems())
        }
    }

    private fun orderNow(listFood: ArrayList<Foods>) {
        val intent = Intent(this@CartActivity, PayOutActivity::class.java)
        intent.putExtra("listFood", listFood)
        startActivity(intent)
    }
}