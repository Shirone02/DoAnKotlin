package com.example.doankotlin.Adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.doankotlin.R

class DeliveryAdapter(private val context: Context,
                      private var customerNames: MutableList<String>,
                      private var moneyStatus: MutableList<Boolean>)
    : RecyclerView.Adapter <DeliveryAdapter.viewholder>(){
    inner class viewholder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var customerName: TextView
        var received: TextView
        var orderStatus: CardView
        init {
            customerName = itemView.findViewById(R.id.nameCustomer)
            received = itemView.findViewById(R.id.tvReceived)
            orderStatus = itemView.findViewById(R.id.orderStatus)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryAdapter.viewholder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.delivery_item, parent, false)
        return viewholder(inflate)
    }

    override fun onBindViewHolder(holder: DeliveryAdapter.viewholder, position: Int) {
        holder.customerName.text = customerNames[position]
        if(moneyStatus[position] == true){
            holder.received.text = "Đã nhận"
        } else {
            holder.received.text = "Chưa nhận"
        }
        val colorMap = mapOf(true to Color.GREEN, false to Color.RED)
        holder.received.setTextColor(colorMap[moneyStatus[position]]?:Color.BLACK)
        holder.orderStatus.backgroundTintList = ColorStateList.valueOf(colorMap[moneyStatus[position]]?:Color.BLACK)
    }

    override fun getItemCount(): Int {
       return customerNames.size
    }

}
