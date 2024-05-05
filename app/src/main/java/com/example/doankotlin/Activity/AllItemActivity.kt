package com.example.doankotlin.Activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.doankotlin.Domain.Foods
import com.example.doankotlin.databinding.ActivityAllItemBinding

class AllItemActivity : BaseActivity() {
    private val binding: ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }


    private val adapterListFood: RecyclerView.Adapter<*>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initList()
    }

    private fun initList() {
        val myRef = database.getReference("Foods").orderByChild("Title")
        binding.progressBar2.visibility = View.VISIBLE
        val list = ArrayList<Foods>()
    }
}