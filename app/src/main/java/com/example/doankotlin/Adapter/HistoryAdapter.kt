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


class HistoryAdapter(private val listFoods: ArrayList<Foods>,
                     private val context: Context): RecyclerView.Adapter<HistoryAdapter.viewholder>(){
    inner class viewholder(itemView: View): RecyclerView.ViewHolder(itemView)  {
        var title: TextView
        var price: TextView
        var image: ImageView

        init {
            title = itemView.findViewById(R.id.titleTxt)
            price = itemView.findViewById(R.id.priceTxt)
            image = itemView.findViewById(R.id.img)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.viewholder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.viewholder_history, parent, false)
        return viewholder(inflate)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.title.text = listFoods[position].title
        holder.price.text = listFoods[position].price.toString() + "đ"
        Glide.with(holder.itemView.context)
            .load(listFoods[position].imagePath)
            .transform(CenterCrop(), RoundedCorners(30))
            .into(holder.image)


    }

    override fun getItemCount(): Int {
        return listFoods.size
    }
}