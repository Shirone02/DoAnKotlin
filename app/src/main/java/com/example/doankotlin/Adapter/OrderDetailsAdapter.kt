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
import kotlin.io.path.fileVisitor

class OrderDetailsAdapter(private val context: Context,
    private var listFoods: ArrayList<Foods>):
    RecyclerView.Adapter<OrderDetailsAdapter.viewholder>() {
    inner class viewholder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var foodName: TextView
        var foodQuantity: TextView
        var foodPrice: TextView
        var imgPath: ImageView

        init {
            foodName = itemView.findViewById(R.id.foodNameTxt)
            foodQuantity = itemView.findViewById(R.id.foodQuantity)
            foodPrice = itemView.findViewById(R.id.foodPriceTxt)
            imgPath = itemView.findViewById(R.id.imgPath)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): OrderDetailsAdapter.viewholder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.order_detail_item, parent, false)
        return viewholder(inflate)
    }

    override fun onBindViewHolder(holder: OrderDetailsAdapter.viewholder, position: Int) {
        holder.foodName.text = listFoods[position].title
        holder.foodPrice.text = listFoods[position].price.toString() +"đ"
        holder.foodQuantity.text = listFoods[position].numberInCart.toString()
        Glide.with(holder.itemView.context)
            .load(listFoods[position].imagePath)
            .transform(CenterCrop(), RoundedCorners(30))
            .into(holder.imgPath)
    }

    override fun getItemCount(): Int {
        return listFoods.size
    }
}