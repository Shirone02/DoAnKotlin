package com.example.doankotlin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.doankotlin.Domain.Foods;
import com.example.doankotlin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DeleteFoodAdapter extends RecyclerView.Adapter<DeleteFoodAdapter.viewholder> {
    ArrayList<Foods> items;
    Context context;

    public DeleteFoodAdapter(ArrayList<Foods> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public DeleteFoodAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_delete_food, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeleteFoodAdapter.viewholder holder, int position) {
        holder.titleDF.setText(items.get(position).getTitle());
        holder.priceDF.setText(items.get(position).getPrice() + "đ");

        Glide.with(context)
                .load(items.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.picDF);
        holder.btnDF.setOnClickListener(v -> deleteItem(position));

//        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(context, DetailActivity.class);
//            intent.putExtra("object", items.get(position));
//            context.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView titleDF, priceDF;
        ImageView picDF;

        Button btnDF;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            titleDF = itemView.findViewById(R.id.titleDeleteFood);
            priceDF = itemView.findViewById(R.id.priceDeleteFood);
            picDF = itemView.findViewById(R.id.imgDeleteFood);
            btnDF = itemView.findViewById(R.id.btnDelete);
        }
    }

    public void deleteItem(int position) {
        // Get the item to delete
        Foods item = items.get(position);

        // Get the Firebase database reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Get the path to the item in the database
        String path =  "Foods/" + item.getId();

        // Delete the item from the database
        databaseReference.child(path).removeValue();

        // Remove the item from the list
        items.remove(position);

        // Notify the adapter that the item has been removed
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
        Toast.makeText(context, "Xoá thành công", Toast.LENGTH_SHORT).show();
    }
}
