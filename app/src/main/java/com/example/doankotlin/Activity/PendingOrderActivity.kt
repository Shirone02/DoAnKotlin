package com.example.doankotlin.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doankotlin.Adapter.PendingOrderAdapter
import com.example.doankotlin.Domain.Foods
import com.example.doankotlin.Domain.OrderDetails

import com.example.doankotlin.databinding.ActivityPendingOrderBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class PendingOrderActivity : BaseActivity() ,PendingOrderAdapter.OnItemClicked {
    private val binding: ActivityPendingOrderBinding by lazy {
        ActivityPendingOrderBinding.inflate(layoutInflater)
    }
    private var listOfName: MutableList<String> = mutableListOf()
    private var listOfTotalPrice: MutableList<String> = mutableListOf()
    private var listOfImageFirstFoodOrder: MutableList<String> = mutableListOf()
    private var quantity: Int = 0
    private val list: ArrayList<Foods> = ArrayList()
    private var listOfOrderItem: ArrayList<OrderDetails> = ArrayList()
    private lateinit var databaseOrderDetails: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        databaseOrderDetails = database.reference.child("OrderDetails")

        getOrderDetails()

        binding.backBtn.setOnClickListener {
            finish()
        }

    }

    private fun getOrderDetails() {
        databaseOrderDetails.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children){
                    val orderDetails = orderSnapshot.getValue(OrderDetails::class.java)
                    orderDetails?.let {
                        listOfOrderItem.add(it)
                    }
                }

                if(listOfOrderItem.isEmpty()){
                    binding.emptyTxt.visibility = View.VISIBLE
                    binding.scrollview.visibility = View.GONE
                } else {
                    binding.emptyTxt.visibility = View.INVISIBLE
                    binding.scrollview.visibility = View.VISIBLE
                    addDataToListForRecyclerView()
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun addDataToListForRecyclerView() {
        for (orderItem in listOfOrderItem){
            val food = Foods()
            orderItem.userName?.let { listOfName.add(it) }
            orderItem.totalPrice?.let { listOfTotalPrice.add(it)}
            //orderItem.listFoods?.size.let { quantity = it!! }
            orderItem.listFoods!![0].imagePath.let { listOfImageFirstFoodOrder.add(it) }
            list.add(food)
        }
        setAdapter()
    }

    private fun setAdapter(){
        binding.pendingOrderView.layoutManager = LinearLayoutManager(this)
        val adapter = PendingOrderAdapter(this, listOfName, listOfTotalPrice, listOfImageFirstFoodOrder, this)
        binding.pendingOrderView.adapter = adapter
    }

    override fun onItemClickedListener(position: Int) {
        val intent = Intent(this, OrderDetailsActivity::class.java)
        val userOrderDetails = listOfOrderItem[position]
        intent.putExtra("UserOrderDetails", userOrderDetails)
        startActivity(intent)
    }

    override fun onItemAcceptClickedListener(position: Int) {
        val childItemPushKey = listOfOrderItem[position].itemPushKey
        val clickItemOrderReference = childItemPushKey?.let {
            database.reference.child("OrderDetails").child(it)
        }
        clickItemOrderReference?.child("orderAccepted")?.setValue(true)
        updateOrderAcceptStatus(position)
    }

    private fun updateOrderAcceptStatus(position: Int) {
        val userIdOfClickItem = listOfOrderItem[position].userUid
        val pushKeyOfClickItem = listOfOrderItem[position].itemPushKey
        val buyHistoryReference = database.reference.child("Users")
            .child(userIdOfClickItem!!).child("BuyHistory").child(pushKeyOfClickItem!!)
        buyHistoryReference.child("orderAccepted").setValue(true)
        databaseOrderDetails.child(pushKeyOfClickItem).child("orderAccepted").setValue(true)
    }

    override fun onItemDispatchClickedListener(position: Int) {
        val dispatchItemPushKey = listOfOrderItem[position].itemPushKey
        val dispatchItemOrderReference = database.reference.child("CompletedOrder").child(dispatchItemPushKey!!)
        dispatchItemOrderReference.setValue(listOfOrderItem[position]).addOnSuccessListener {
            deleteThisItemFromOrderDetails(dispatchItemPushKey)
        }
    }

    private fun deleteThisItemFromOrderDetails(dispatchItemPushKey: String) {
        val orderDetailsItemReference = database.reference.child("OrderDetails")
            .child(dispatchItemPushKey)

        orderDetailsItemReference.removeValue().addOnSuccessListener {
            Toast.makeText(this, "Đơn được gửi đi", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener {
                Toast.makeText(this, "Đơn hàng chưa được gửi đi", Toast.LENGTH_SHORT).show()
            }
    }
}