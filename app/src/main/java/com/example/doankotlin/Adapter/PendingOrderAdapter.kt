package com.example.doankotlin.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.doankotlin.R

class PendingOrderAdapter(private val context: Context,
                          private val customerNames: MutableList<String>,
                          private val quantities: MutableList<String>,
                          private val imagePaths: MutableList<String>,
                          private val itemClicked: OnItemClicked
): RecyclerView.Adapter<PendingOrderAdapter.viewholder>() {
    interface OnItemClicked{
        fun onItemClickedListener(position: Int)
        fun onItemAcceptClickedListener(position: Int)
        fun onItemDispatchClickedListener(position: Int)
    }
    inner class viewholder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var customerName: TextView
        var quantity: TextView
        var orderAcceptButton: Button
        var image: ImageView
        init {
            customerName = itemView.findViewById(R.id.nameCustomer)
            quantity = itemView.findViewById(R.id.quantityBuyTxt)
            orderAcceptButton = itemView.findViewById(R.id.acceptButton)
            image = itemView.findViewById(R.id.imgPath)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderAdapter.viewholder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.viewholder_pending_order, parent, false)
        return viewholder(inflate)
    }

    override fun onBindViewHolder(holder: PendingOrderAdapter.viewholder, position: Int) {
        var isAccepted = false
        holder.customerName.text = customerNames[position]
        holder.quantity.text = quantities[position]

        Glide.with(holder.itemView.context)
            .load(imagePaths[position])
            .transform(CenterCrop(), RoundedCorners(30))
            .into(holder.image)

        holder.orderAcceptButton.apply {
            if(!isAccepted){
                text = "Chấp nhận"
            } else {
                text = "Gửi đi"
            }
            setOnClickListener {
                if(!isAccepted){
                    text = "Gửi đi"
                    isAccepted = true
                    showToast("Đơn hàng được chấp nhận")
                    itemClicked.onItemAcceptClickedListener(position)
                } else {
                    customerNames.removeAt(position)
                    notifyItemRemoved(position)
                    showToast("Đơn hàng được gửi đi")
                    itemClicked.onItemDispatchClickedListener(position)
                }
            }
        }

        holder.itemView.setOnClickListener {
            itemClicked.onItemClickedListener(position)
        }
    }

    fun showToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    override fun getItemCount(): Int {
        return customerNames.size
    }
}