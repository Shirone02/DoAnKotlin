package com.example.doankotlin.Activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doankotlin.Adapter.DeliveryAdapter
import com.example.doankotlin.Domain.OrderDetails
import com.example.doankotlin.databinding.ActivityOutForDeliveryBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class OutForDeliveryActivity : BaseActivity() {
    private val binding: ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }
    private  var listOfCompleteOrderList: ArrayList<OrderDetails> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener { finish() }

        retrieveCompleteOrderDetail()

    }

    private fun retrieveCompleteOrderDetail() {
        val completeOrderReference = database.reference.child("CompletedOrder")
            .orderByChild("currentItem")

        completeOrderReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfCompleteOrderList.clear()

                for(orderSnapshot in snapshot.children){
                    val completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.let{
                        listOfCompleteOrderList.add(it)
                    }
                }
                listOfCompleteOrderList.reverse()
                setDataIntoRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun setDataIntoRecyclerView() {
        val customerName = mutableListOf<String>()
        val moneyStatus = mutableListOf<Boolean>()

        for (order in listOfCompleteOrderList){
            order.userName?.let {
                customerName.add(it)
            }

            moneyStatus.add(order.paymentReceived)
        }

        val adapter = DeliveryAdapter(this, customerName, moneyStatus)
        binding.deliveryRecyclerView.adapter = adapter
        binding.deliveryRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}