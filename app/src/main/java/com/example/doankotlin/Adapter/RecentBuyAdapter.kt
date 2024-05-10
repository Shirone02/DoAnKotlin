package com.example.doankotlin.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.doankotlin.Domain.Foods
import com.example.doankotlin.R

class RecentBuyAdapter(private var context: Context,
                       private val listFoods: ArrayList<Foods>):
    RecyclerView.Adapter<RecentBuyAdapter.viewholder>() {
    inner class viewholder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var price: TextView
        var image: ImageView
        var quantity: TextView
        init {
            title = itemView.findViewById(R.id.nameCustomer)
            price = itemView.findViewById(R.id.priceBuyTxt)
            image = itemView.findViewById(R.id.imgBuyItem)
            quantity = itemView.findViewById(R.id.quantityBuyTxt)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentBuyAdapter.viewholder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.recent_buy_item, parent, false)
        return viewholder(inflate)
    }

    override fun onBindViewHolder(holder: RecentBuyAdapter.viewholder, position: Int) {
        holder.title.text = listFoods[position].title
        holder.price.text = listFoods[position].price.toString() + "đ"
        Glide.with(holder.itemView.context)
            .load(listFoods[position].imagePath)
            .transform(CenterCrop(), RoundedCorners(30))
            .into(holder.image)
        holder.quantity.text = listFoods[position].numberInCart.toString()
    }

    override fun getItemCount(): Int {
        return listFoods.size
    }
}