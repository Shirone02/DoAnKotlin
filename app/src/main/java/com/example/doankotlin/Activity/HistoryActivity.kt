package com.example.doankotlin.Activity


import android.content.Intent
import android.net.Uri
import android.os.Bundle

import android.util.Log
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.doankotlin.Adapter.HistoryAdapter
import com.example.doankotlin.Domain.Foods
import com.example.doankotlin.Domain.OrderDetails
import com.example.doankotlin.databinding.ActivityHistoryBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class HistoryActivity : BaseActivity() {
    private lateinit var userId: String
    private val listOfOrderItems: ArrayList<OrderDetails> = ArrayList()
    private lateinit var  historyAdapter: HistoryAdapter
    private val binding: ActivityHistoryBinding by lazy {
        ActivityHistoryBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.recentBuy.visibility = View.INVISIBLE
        binding.backBtn.setOnClickListener { finish() }

        retrieveBuyHistory()

        binding.recentBuy.setOnClickListener {
            seeItemsRecentBuy()
        }

        binding.receivedButton.setOnClickListener {
            updateOrderStatus()
        }
    }

    private fun updateOrderStatus() {
        val itemPushKey = listOfOrderItems[0].itemPushKey
        val completeOrderReference = database.reference.child("CompletedOrder").child(itemPushKey!!)
        completeOrderReference.child("paymentReceived").setValue(true)
    }

    private fun seeItemsRecentBuy() {
        listOfOrderItems.firstOrNull()?.let { recentBuy ->
            val intent = Intent(this, RecentOrderItemsActivity::class.java)
            intent.putExtra("RecentBuyOrderItem", listOfOrderItems)
            startActivity(intent)
        }
    }

    private fun retrieveBuyHistory() {
        userId = mAuth.currentUser?.uid?:""
        val buyItemReference: DatabaseReference =
            database.reference.child("Users").child(userId).child("BuyHistory")
        val shortingQuery = buyItemReference.orderByChild("currentTime")

        shortingQuery.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapshot in snapshot.children){
                    val buyHistoryItem = buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let{
                        listOfOrderItems.add(it)
                    }
                }
                listOfOrderItems.reverse()
                if(listOfOrderItems.isNotEmpty()){
                    setDataRecentBuyItem()
                    setPreviousBuyItemsRecyclerView()
                } else {
                    binding.emptyTxt.visibility = View.VISIBLE
                    binding.scrollview.visibility = View.GONE
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun setDataRecentBuyItem() {
        binding.recentBuy.visibility = View.VISIBLE
        val recentOrderItem = listOfOrderItems.firstOrNull()
        recentOrderItem?.let {
            with(binding){
                titleBuy.text = it.listFoods!![0].title
                priceBuy.text = it.listFoods!![0].price.toString() + "đ"
                val image = it.listFoods!![0].imagePath
                val uri = Uri.parse(image)
                Glide.with(this@HistoryActivity).load(uri).into(imgBuy)

                val isOrderIsAccepted = listOfOrderItems[0].orderAccepted
                if(isOrderIsAccepted){
                    receivedButton.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setPreviousBuyItemsRecyclerView() {
        val listFoods: ArrayList<Foods> = ArrayList()

        for (i in 1 until listOfOrderItems.size) {
            val food = Foods()
            listOfOrderItems[i].listFoods!![0].title?.let {
                food.title = it
                listOfOrderItems[i].listFoods!![0].price.let {
                    food.price = it
                    listOfOrderItems[i].listFoods!![0].imagePath?.let {
                        food.imagePath = it
                    }
                    listFoods.add(food)
                    Log.d("TAG", "Data: $food")
                }
                val rv = binding.historyView
                rv.layoutManager = LinearLayoutManager(this)
                historyAdapter = HistoryAdapter(listFoods, this)
                rv.adapter = historyAdapter
            }
        }
    }
}
