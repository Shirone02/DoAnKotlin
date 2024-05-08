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
    private var binding: ActivityCartBinding? = null
    private var tax = 0.0
    private lateinit var cartAdapter: CartAdapter
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding!!.getRoot())
        setVariable()
        calculateCart()
        initList()


    }

    private fun initList() {
        /*if (managmentCart!!.getListCart("CartList").isEmpty()) {
            binding!!.emptyTxt.visibility = View.VISIBLE
            binding!!.scrollviewCart.visibility = View.GONE
        } else {
            binding!!.emptyTxt.visibility = View.GONE
            binding!!.scrollviewCart.visibility = View.VISIBLE
        }
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding!!.cardView.setLayoutManager(linearLayoutManager)
        adapter = CartAdapter(managmentCart!!.getListCart("CartList"), this) { calculateCart() }
        binding!!.cardView.setAdapter(adapter)*/

        retrieveCartItems()
    }

    private fun retrieveCartItems() {
        userId = mAuth.currentUser?.uid ?: ""
        val foodReference: DatabaseReference =
            database.reference.child("Users").child(userId).child("CartItems")

        val list: ArrayList<Foods> = ArrayList()

        foodReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val cartItems = foodSnapshot.getValue(CartItems::class.java)
                    val foodItem = Foods()
                    cartItems?.Title.let { foodItem.title = it }
                    cartItems?.Price.let { foodItem.price = it!! }
                    cartItems?.ImagePath.let { foodItem.imagePath = it }
                    cartItems?.Quantity.let { foodItem.numberInCart = it!! }

                    list.add(foodItem)
                    setAdapter()

                }
            }

            private fun setAdapter() {
                cartAdapter = CartAdapter(list, this@CartActivity) { calculateCart() }
                binding!!.cardView.layoutManager =
                    LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
                binding!!.cardView.adapter = cartAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CartActivity, "Lỗi", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun calculateCart() {
        val percentTax = 0.04 // percent 4%
        val delivery = 1000.0 // 1000 VND
        val receivedData = intent.getDoubleExtra("feeEachItem", 0.0)
        tax =
            (Math.round( receivedData * percentTax * 100.0) / 100).toDouble()
        val total =
            (Math.round((receivedData + tax + delivery) * 100) / 100).toDouble()
        val itemTotal = (Math.round((receivedData * 100).toDouble()) / 100).toDouble()
        binding!!.totalFeeTxt.text = itemTotal.toString() + "đ"
        binding!!.taxTxt.text = tax.toString() + "đ"
        binding!!.deliveryTxt.text = delivery.toString() + "đ"
        binding!!.totalTxt.text = total.toString() + "đ"
    }

    private fun setVariable() {
        binding!!.backBtn.setOnClickListener { finish() }
        binding!!.orderBtn.setOnClickListener {
            getOrderItemDetail()
        }

        /*binding!!.orderBtn.setOnClickListener {
            val orderList = managmentCart!!.getListCart("CartList")
            val user = mAuth.currentUser
            val foodInfo: MutableMap<String, Any> = HashMap()
            for (i in orderList.indices) {
                val myRef = database.getReference("Orders").child(user!!.uid).child(i.toString())
                val food = orderList[i]
                //myRef.child(String.valueOf(food.getId()));
                foodInfo["Title"] = food.title
                foodInfo["Quantity"] = food.numberInCart
                foodInfo["Total"] = food.price * food.numberInCart
                foodInfo["ImagePath"] = food.imagePath
                myRef.setValue(foodInfo)
                Toast.makeText(this@CartActivity, "Đặt hàng thành công", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "Data: $foodInfo")
            }
        }*/
    }

    private fun getOrderItemDetail() {
        val  orderReference: DatabaseReference = database.reference.child("Users").child(userId).child("CartItems")

        val listFood: ArrayList<Foods> = ArrayList()

        orderReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val orderItem = foodSnapshot.getValue(CartItems::class.java)
                    val foods = Foods()
                    orderItem?.Title.let { foods.title = it }
                    orderItem?.Price.let { foods.price = it!! }
                    orderItem?.Quantity.let { foods.numberInCart = it!! }
                    orderItem?.ImagePath.let { foods.imagePath = it }

                    listFood.add(foods)
                    orderNow(listFood)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CartActivity,"Lỗi order",Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun orderNow(listFood: ArrayList<Foods>) {
        val intent = Intent(this@CartActivity, PayOutActivity::class.java)
        intent.putExtra("listFood", listFood)
        startActivity(intent)
    }

}