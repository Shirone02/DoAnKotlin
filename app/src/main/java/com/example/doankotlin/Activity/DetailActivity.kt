package com.example.doankotlin.Activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.doankotlin.Domain.CartItems
import com.example.doankotlin.Domain.Foods
import com.example.doankotlin.Helper.ManagmentCart
import com.example.doankotlin.R
import com.example.doankotlin.databinding.ActivityDetailBinding
import java.lang.Double.parseDouble

class DetailActivity : BaseActivity() {
    var binding: ActivityDetailBinding? = null
    private var `object`: Foods? = null
    private var num = 1
    private var managmentCart: ManagmentCart? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding!!.getRoot())
        window.statusBarColor = getResources().getColor(R.color.black)
        intentExtra
        setVariable()
    }

    private fun setVariable() {
        managmentCart = ManagmentCart(this)
        binding!!.backBtn.setOnClickListener { v: View? -> finish() }
        Glide.with(this@DetailActivity)
            .load(`object`!!.imagePath)
            .into(binding!!.pic)
        binding!!.priceTxt.text = `object`!!.price.toString() + "đ"
        binding!!.titleTxt.text = `object`!!.title
        binding!!.descriptionTxt.text = `object`!!.description
        binding!!.rateTxt.text = `object`!!.star.toString() + " xếp hạng"
        binding!!.ratingBar.rating = `object`!!.star.toFloat()
        binding!!.totalTxt.text = (num * `object`!!.price).toString() + "đ"
        binding!!.timeTxt.text = `object`!!.timeValue.toString() + " phút"
        binding!!.plusBtn.setOnClickListener { v: View? ->
            num = num + 1
            binding!!.numTxt.text = "$num "
            binding!!.totalTxt.text = (num * `object`!!.price).toString() + "đ"
        }
        binding!!.minusBtn.setOnClickListener { v: View? ->
            if (num > 1) {
                num = num - 1
                binding!!.numTxt.text = num.toString() + ""
                binding!!.totalTxt.text = (num * `object`!!.price).toString() + "đ"
            }
        }
        binding!!.addBtn.setOnClickListener { v: View? ->
            /*object.setNumberInCart(num);
            managmentCart.insertFood("CartList",object);*/
            addItemToCart()
        }
    }

    private fun addItemToCart() {
        val myRef = database.reference
        val userId = mAuth.currentUser?.uid?: ""

        val cartItem = CartItems(`object`!!.title,`object`!!.price,`object`!!.imagePath , num)
        myRef.child("Users").child(userId).child("CartItems").push().setValue(cartItem).addOnSuccessListener {
            Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this, "Thêm không thành công", Toast.LENGTH_SHORT).show()
        }
    }

    private val intentExtra: Unit
        get() {
            `object` = intent.getSerializableExtra("object") as Foods?
        }
}