package com.example.doankotlin.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doankotlin.Adapter.SearchListAdapter;
import com.example.doankotlin.Domain.Foods;
import com.example.doankotlin.R;
import com.example.doankotlin.databinding.ActivityLoginBinding;
import com.example.doankotlin.databinding.ActivitySearchBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity {

    ActivitySearchBinding binding;
    ArrayList<Foods> foodsArrayList;
    SearchListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.searchView.clearFocus();

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);

                return true;
            }
        });
        binding.foodListViewSearch.setLayoutManager(new LinearLayoutManager(this));
        binding.foodListViewSearch.setHasFixedSize(true);

        binding.backBtn.setOnClickListener(v -> finish());


        getData();
    }

    private void filterList(String newText) {
        ArrayList<Foods> filteredList = new ArrayList<>();
        for (Foods food: foodsArrayList){
            if(food.getTitle().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(food);
            }
        }

        if(filteredList.isEmpty()){
            Toast.makeText(this, "Không có đồ ăn", Toast.LENGTH_SHORT).show();
            binding.emptyTxtSearch.setVisibility(View.VISIBLE);
            binding.scrollviewSearch.setVisibility(View.INVISIBLE);
        } else {
            adapter.setFilteredList(filteredList);
            binding.emptyTxtSearch.setVisibility(View.INVISIBLE);
            binding.scrollviewSearch.setVisibility(View.VISIBLE);
        }
    }

    private void getData() {
        DatabaseReference myRef = database.getReference("Foods");
        foodsArrayList = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for(DataSnapshot issue: snapshot.getChildren()){
                        foodsArrayList.add(issue.getValue(Foods.class));
                    }
                    if(!foodsArrayList.isEmpty()){
                        adapter = new SearchListAdapter(foodsArrayList);
                        binding.foodListViewSearch.setAdapter(adapter);
                        binding.emptyTxtSearch.setVisibility(View.INVISIBLE);
                        binding.scrollviewSearch.setVisibility(View.VISIBLE);
                    }
                    else {
                        binding.emptyTxtSearch.setVisibility(View.VISIBLE);
                        binding.scrollviewSearch.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}