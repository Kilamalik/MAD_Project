package com.example.mad_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AllReviewsAdapter extends RecyclerView.Adapter<AllReviewsAdapter.AllReviewsViewHolder> {

    Context context;
    ArrayList<ReviewClass> list = new ArrayList<>();

    public AllReviewsAdapter(Context context, ArrayList<ReviewClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AllReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.all_reviews_layout, parent, false);
        return new AllReviewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AllReviewsViewHolder holder, int position) {
        ReviewClass review = list.get(position);
        holder.userNameView.setText(review.getUser());
        holder.detailedView.setText(review.getDetailedReview());
        holder.ratingView.setText(review.getRating());
        holder.oneWordView.setText(review.getShortReview());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class AllReviewsViewHolder extends RecyclerView.ViewHolder{

        TextView userNameView, detailedView, ratingView, oneWordView;

        public AllReviewsViewHolder(@NonNull View itemView) {
            super(itemView);

            userNameView = itemView.findViewById(R.id.itemNameCard);
            detailedView = itemView.findViewById(R.id.detailedReview);
            ratingView = itemView.findViewById(R.id.ratingReview);
            oneWordView = itemView.findViewById(R.id.oneWordReview);

        }
    }
}
