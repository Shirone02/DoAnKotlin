package com.example.doankotlin.Activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doankotlin.Adapter.DeleteFoodAdapter
import com.example.doankotlin.Domain.Foods
import com.example.doankotlin.databinding.ActivityDeleteFoodBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class DeleteFoodActivity : BaseActivity() {
    var binding: ActivityDeleteFoodBinding? = null
    private var adapterDeleteFood: RecyclerView.Adapter<*>? = null
    private val categoryId = 0
    private val categoryName: String? = null
    private val searchText: String? = null
    private val isSearch = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteFoodBinding.inflate(layoutInflater)
        setContentView(binding!!.getRoot())
        intentExtra
        initList()
    }

    private fun initList() {
        val myRef = database.getReference("Foods")
        val list = ArrayList<Foods?>()

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (issue in snapshot.getChildren()) {
                        list.add(issue.getValue(Foods::class.java))
                    }
                    if (list.isNotEmpty()) {
                        binding!!.foodListView.setLayoutManager(
                            LinearLayoutManager(
                                this@DeleteFoodActivity,
                            )
                        )
                        adapterDeleteFood = DeleteFoodAdapter(list)
                        binding!!.foodListView.setAdapter(adapterDeleteFood)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DeleteFoodActivity", "Failed to delete food", error.toException())
            }
        })
    }

    private val intentExtra: Unit
        get() {
            binding!!.backBtnDeleteFood.setOnClickListener { v -> finish() }
        }
}
