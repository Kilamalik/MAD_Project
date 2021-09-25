package com.example.mad_project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<ItemClass> list = new ArrayList<>();

    public MyAdapter(Context context, ArrayList<ItemClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ItemClass item = list.get(position);
        //item.display();
        Glide.with(context)
                        .asBitmap().load(item.getImageURL())
                        .into(holder.itemImage);
        holder.itemNameView.setText(item.getItemName());
        holder.categoryView.setText(item.getCategory());
        holder.reviewsView.setText(String.valueOf(item.getReviews()));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {

            String category = "category", imageURL = "imageURL", itemName = "itemName", description = "description";

            @Override
            public void onClick(View view) {
                //Toast.makeText(context,item.getItemName(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ReviewItem.class);
                intent.putExtra(itemName, item.getItemName());
                intent.putExtra(category, item.getCategory());
                intent.putExtra(imageURL, item.getImageURL());
                intent.putExtra(description, item.getDescription());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView itemNameView, categoryView, reviewsView;
        ImageView itemImage;
        CardView parentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.itemImage);
            itemNameView = itemView.findViewById(R.id.itemNameCard);
            categoryView = itemView.findViewById(R.id.categoryCard);
            reviewsView = itemView.findViewById(R.id.ratingReview);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
