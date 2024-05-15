package com.example.doankotlin.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doankotlin.Domain.User;
import com.example.doankotlin.R;
import java.util.ArrayList;
import java.util.Objects;

public class UserCardAdapter extends RecyclerView.Adapter<UserCardAdapter.UserCardViewHolder> {
    private ArrayList<User> userList;
    Context context;

    public UserCardAdapter(ArrayList<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_list_user, parent, false);
        return new UserCardViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserCardViewHolder holder, int position) {
        User user = userList.get(position);
        holder.orderNumberTextView.setText(String.valueOf(position + 1));
        holder.emailTextView.setText(user.getEmail());
        if (user.getIsUser()!= null) {
            if (user.getIsUser().equals("0") ) {
                holder.isUserTextView.setText("User");
            } else {
                holder.isUserTextView.setText("Admin");
            }
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserCardViewHolder extends RecyclerView.ViewHolder {
        TextView orderNumberTextView, emailTextView, isUserTextView;

        public UserCardViewHolder(@NonNull View itemView) {
            super(itemView);
            orderNumberTextView = itemView.findViewById(R.id.orderNumberTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            isUserTextView = itemView.findViewById(R.id.isUserTextView);
        }
    }
}
