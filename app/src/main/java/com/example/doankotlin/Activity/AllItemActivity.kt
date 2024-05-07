package com.example.doankotlin.Activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doankotlin.Adapter.FoodListAdapter
import com.example.doankotlin.Domain.Foods
import com.example.doankotlin.databinding.ActivityAllItemBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AllItemActivity : BaseActivity() {
    private val binding: ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }


    private var adapterListFood: RecyclerView.Adapter<*>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener { finish() }
        initList()
    }

    private fun initList() {
        val myRef = database.getReference("Foods")
        binding.progressBar2.visibility = View.VISIBLE
        val list = ArrayList<Foods>()


        myRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (issue in snapshot.children) {
                        list.add(issue.getValue(Foods::class.java)!!)
                    }
                    if (list.isNotEmpty()) {
                        binding.foodListView.setLayoutManager(GridLayoutManager(this@AllItemActivity, 2))
                        adapterListFood = FoodListAdapter(list)
                        binding.foodListView.setAdapter(adapterListFood)
                    }
                    binding.progressBar2.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}