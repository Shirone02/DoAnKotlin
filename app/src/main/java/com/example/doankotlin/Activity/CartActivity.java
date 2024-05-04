package com.example.doankotlin.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doankotlin.Adapter.CartAdapter;
import com.example.doankotlin.Domain.Foods;
import com.example.doankotlin.Helper.ManagmentCart;
import com.example.doankotlin.databinding.ActivityCartBinding;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends BaseActivity {

    private ActivityCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;
    private double tax;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);

        setVariable();
        calculateCart();
        initList();
    }

    private void initList() {
        if(managmentCart.getListCart("CartList").isEmpty()){
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollviewCart.setVisibility(View.GONE);
        }else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollviewCart.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        binding.cardView.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(managmentCart.getListCart("CartList"), this, this::calculateCart);

        binding.cardView.setAdapter(adapter);
    }

    private void calculateCart() {
        double percentTax = 0.04; // percent 4%
        double delivery = 1000; // 1000 VND

        tax = Math.round(managmentCart.getTotalFee("CartList")*percentTax*100.0)/100;

        double total = Math.round((managmentCart.getTotalFee("CartList")+tax+delivery)*100)/100;
        double itemTotal = Math.round(managmentCart.getTotalFee("CartList")*100)/100;

        binding.totalFeeTxt.setText(itemTotal +"đ");
        binding.taxTxt.setText(tax+"đ");
        binding.deliveryTxt.setText(delivery+"đ");
        binding.totalTxt.setText(total + "đ");

    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
        binding.orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Foods> orderList = managmentCart.getListCart("CartList");
                FirebaseUser user = mAuth.getCurrentUser();

                Map<String, Object> foodInfo = new HashMap<>();
                for(int i=0; i<orderList.size();i++) {
                    DatabaseReference myRef = database.getReference("Orders").child(user.getUid()).child(String.valueOf(i));
                    Foods food = orderList.get(i);
                    //myRef.child(String.valueOf(food.getId()));
                    foodInfo.put("Title", food.getTitle());
                    foodInfo.put("Quantity", food.getNumberInCart());
                    foodInfo.put("Total", food.getPrice() * food.getNumberInCart());
                    foodInfo.put("ImagePath", food.getImagePath());

                    myRef.setValue(foodInfo);
                    Toast.makeText(CartActivity.this,"Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                    Log.d("TAG","Data: " + foodInfo);
                }
            }
        });
    }
}