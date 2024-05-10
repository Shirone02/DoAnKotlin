package com.example.doankotlin.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast

import com.example.doankotlin.Domain.Foods
import com.example.doankotlin.Domain.OrderDetails
import com.example.doankotlin.R
import com.example.doankotlin.databinding.ActivityPayOutBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class PayOutActivity : BaseActivity() {
    lateinit var binding: ActivityPayOutBinding
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var phone: String
    private lateinit var totalAmount: String
    private lateinit var listFoods: ArrayList<Foods>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseReference = database.getReference()

        setUserData()

        val intent = intent
        listFoods = intent.getSerializableExtra("listFood") as ArrayList<Foods>

        totalAmount = calculateTotalAmount().toString() + "đ"
        binding.totalAmount.isEnabled = false
        binding.totalAmount.setText(totalAmount)
        binding.backButton.setOnClickListener {
            //startActivity(Intent(this, CartActivity::class.java))
            finish()
        }
        binding.orderButton.setOnClickListener {
            name = binding.name.text.toString().trim()
            address = binding.address.text.toString().trim()
            phone = binding.phone.text.toString().trim()
            if(name.isBlank() && address.isBlank() && phone.isBlank()){
                Toast.makeText(this, "Vui lòng nhập thông tin",Toast.LENGTH_SHORT).show()
            } else {
                placeOrder()
            }

        }
    }

    private fun placeOrder() {
        userId = mAuth.currentUser?.uid?:""
        val time = System.currentTimeMillis()
        val itemPushKey = databaseReference.child("OrderDetails").push().key
        val orderDetails = OrderDetails(userId, name, listFoods,address, totalAmount, phone, time, itemPushKey,false)
        val orderReference = databaseReference.child("OrderDetails").child(itemPushKey!!)
        orderReference.setValue(orderDetails).addOnSuccessListener {
            Toast.makeText(this,"Đặt hàng thành công", Toast.LENGTH_SHORT).show()
            removeItemFromCart()
            addOrderToHistory(orderDetails)
            startActivity(Intent(this, MainJavaActivity::class.java))
        } .addOnFailureListener {
            Toast.makeText(this, "Đặt hàng thất bại",Toast.LENGTH_SHORT).show()
        }
    }

    private fun addOrderToHistory(orderDetails: OrderDetails) {
        databaseReference.child("Users").child(userId).child("BuyHistory")
            .child(orderDetails.itemPushKey!!).setValue(orderDetails).addOnSuccessListener {

            }
    }


    private fun removeItemFromCart() {
        val cartItemsReference = databaseReference.child("Users").child(userId).child("CartItems")
        cartItemsReference.removeValue()
    }

    private fun calculateTotalAmount(): Int {
        var totalAmount = 0
        for(i in 0 until listFoods.size){
            var price = listFoods[i].price
            var quantity = listFoods[i].numberInCart
            totalAmount += price * quantity
        }
        return totalAmount
    }

    private fun setUserData() {
        val user = mAuth.currentUser
        if (user != null) {
            val userId = user.uid
            val userReference = databaseReference.child("Users").child(userId)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val names = snapshot.child("Email").getValue(String::class.java) ?: ""
                        val addresses = snapshot.child("Address").getValue(String::class.java) ?: ""
                        val phones = snapshot.child("Phone").getValue(String::class.java) ?: ""
                        binding.apply {
                            name.setText(names)
                            address.setText(addresses)
                            phone.setText(phones)
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }
}