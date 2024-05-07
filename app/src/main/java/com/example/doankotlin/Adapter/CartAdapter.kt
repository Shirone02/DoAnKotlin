package com.example.doankotlin.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.doankotlin.Activity.CartActivity
import com.example.doankotlin.Domain.Foods
import com.example.doankotlin.Helper.ChangeNumberItemsListener
import com.example.doankotlin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartAdapter (
    private var list: ArrayList<Foods>,
    private val context: Context,
    changeNumberItemsListener: ChangeNumberItemsListener
) : RecyclerView.Adapter<CartAdapter.viewholder>() {

    private var changeNumberItemsListener: ChangeNumberItemsListener
    private val auth = FirebaseAuth.getInstance()
    init {
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid?:""

        this.changeNumberItemsListener = changeNumberItemsListener
        cartItemsReferences = database.reference.child("Users").child(userId).child("CartItems")
    }
    companion object{
        private lateinit var cartItemsReferences: DatabaseReference
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.viewholder_cart, parent, false)
        return viewholder(inflate)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.title.text = list[position].title
        holder.feeEachItem.text =
            (list[position].numberInCart * list[position].price).toString() + "đ"
        holder.totalEachItem.text =
            list[position].numberInCart.toString() + "*" + (list[position].price.toString() + "đ")
        holder.num.text = list[position].numberInCart.toString() + ""
        Glide.with(holder.itemView.context)
            .load(list[position].imagePath)
            .transform(CenterCrop(), RoundedCorners(30))
            .into(holder.pic)

        holder.plusItem.setOnClickListener {
            list[position].numberInCart += 1
            notifyDataSetChanged()
            changeNumberItemsListener.change()
            changeNumberItem(position, list[position].numberInCart )
        }

        holder.minusItem.setOnClickListener {
            if (list[position].numberInCart == 1) {
                //list.removeAt(position)
                deleteItem(position)
            } else {
                list[position].numberInCart -= 1
                changeNumberItem(position, list[position].numberInCart )
            }
            notifyDataSetChanged()
            changeNumberItemsListener.change()
        }

        Intent(context, CartActivity::class.java)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var feeEachItem: TextView
        var plusItem: TextView
        var minusItem: TextView
        var pic: ImageView
        var totalEachItem: TextView
        var num: TextView

        init {
            title = itemView.findViewById(R.id.titleTxt)
            pic = itemView.findViewById(R.id.pic)
            feeEachItem = itemView.findViewById(R.id.feeEachItem)
            plusItem = itemView.findViewById(R.id.plusCartBtn)
            minusItem = itemView.findViewById(R.id.minusCartBtn)
            totalEachItem = itemView.findViewById(R.id.totalEachItem)
            num = itemView.findViewById(R.id.numberItemTxt)
        }
    }
    private fun changeNumberItem(position: Int, quantity: Int){
        val positionRetrieve = position
        getUniqueKeyAtPosition(positionRetrieve){uniqueKey ->
            if(uniqueKey != null){
                cartItemsReferences.child(uniqueKey).child("quantity").setValue(quantity)
            }
        }
    }

    private fun deleteItem(position: Int){
        val positionRetrieve = position
        getUniqueKeyAtPosition(positionRetrieve){uniqueKey ->
            if(uniqueKey != null){
                removeItem(position, uniqueKey)
            }
        }
    }

    private fun removeItem(position: Int, uniqueKey: String) {
        if (uniqueKey != null){
            cartItemsReferences.child(uniqueKey).removeValue().addOnSuccessListener {
                list.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position,list.size)
            }.addOnFailureListener {
                Toast.makeText(context, "Xoá lỗi",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getUniqueKeyAtPosition(positionRetrieve: Int, onComplete:(String?) -> Unit) {
        cartItemsReferences.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var uniqueKey:String? = null
                snapshot.children.forEachIndexed { index, dataSnapshot ->
                    if(index == positionRetrieve){
                        uniqueKey = dataSnapshot.key
                        return@forEachIndexed
                    }
                }
                onComplete(uniqueKey)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}
