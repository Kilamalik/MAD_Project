package com.example.mad_project;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;

public class YourReviewsAdapter extends RecyclerView.Adapter<YourReviewsAdapter.YourReviewsViewHolder> {

    Context context;
    ArrayList<ReviewClass> list = new ArrayList<>();
    DatabaseReference database = FirebaseDatabase.getInstance("https://test-6eb79-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Items");
    DatabaseReference updatedDatabase = FirebaseDatabase.getInstance("https://test-6eb79-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Reviews");
    ItemClass reviewItem;

    public YourReviewsAdapter(Context context, ArrayList<ReviewClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public YourReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.your_reviews_layout, parent, false);
        return new YourReviewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull YourReviewsViewHolder holder, int position) {
        ReviewClass review = list.get(position);
        String itemNameFromReview = review.getItemName();

        // Retrieve the item from the users review
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ItemClass item = dataSnapshot.getValue(ItemClass.class);
                    if(item.itemName.equals(itemNameFromReview)) {
                        reviewItem = item;
                        Glide.with(context)
                                .asBitmap().load(reviewItem.getImageURL())
                                .into(holder.itemImage);
                        holder.itemNameView.setText(itemNameFromReview);
                        holder.categoryView.setText(reviewItem.getCategory());
                        holder.ratingView.setText(review.getRating());
                        holder.oneWordView.setText(review.getShortReview());
                        holder.detailedView.setText(review.getDetailedReview());

                        holder.editButtonView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final DialogPlus dialogPlus = DialogPlus.newDialog(context)
                                        .setContentHolder(new ViewHolder(R.layout.updade_layout))
                                        .setExpanded(true, 1500)
                                        .create();

                                //dialogPlus.show();

                                View dialogView = dialogPlus.getHolderView();

                                EditText oneWordDialogView = dialogView.findViewById(R.id.oneWordUpdate);
                                EditText detailedDialogView = dialogView.findViewById(R.id.detailedUpdate);
                                EditText ratingDialogView = dialogView.findViewById(R.id.ratingUpdate);

                                Button updateDialogButton = dialogView.findViewById(R.id.buttonUpdate);

                                oneWordDialogView.setText(review.getShortReview());
                                detailedDialogView.setText(review.getDetailedReview());
                                ratingDialogView.setText(review.getRating());

                                dialogPlus.show();

                                updateDialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String oneWord, detailed, rating;
                                        oneWord = oneWordDialogView.getText().toString();
                                        detailed = detailedDialogView.getText().toString();
                                        rating = ratingDialogView.getText().toString();

                                        if(oneWord.isEmpty() || detailed.isEmpty()) {
                                            Toast toast = Toast.makeText(oneWordDialogView.getContext(), "Please fill in all of the fields", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                        else if(rating.isEmpty() || Integer.parseInt(rating)<0 || Integer.parseInt(rating)>5) {
                                            Toast toast = Toast.makeText(oneWordDialogView.getContext(), "Rating is missing or contains invalid values", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                        else {
                                            ReviewClass updatedReview = new ReviewClass(detailed, oneWord, rating, review.getUser(), itemNameFromReview);
                                            updatedDatabase.child(itemNameFromReview + "-" + review.getUser()).setValue(updatedReview);

                                            Toast toast = Toast.makeText(oneWordDialogView.getContext(), "Your review has been updated", Toast.LENGTH_SHORT);
                                            toast.show();

                                            dialogPlus.dismiss();
                                        }
                                    }
                                });

                            }
                        });

                        holder.deleteButtonView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Are you Sure?");

                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        updatedDatabase.child(itemNameFromReview + "-" + review.getUser()).removeValue();
                                    }
                                });

                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast toast = Toast.makeText(context, "Deletion Cancelled", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                });

                                builder.show();
                            }
                        });

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount()  {
        return list.size();
    }

    public static class YourReviewsViewHolder extends RecyclerView.ViewHolder{

        ImageView itemImage;
        TextView itemNameView, categoryView, ratingView, oneWordView, detailedView;
        Button editButtonView;
        ImageButton deleteButtonView;

        public YourReviewsViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.itemImage);
            itemNameView = itemView.findViewById(R.id.itemNameCard);
            categoryView = itemView.findViewById(R.id.categoryCard);
            ratingView = itemView.findViewById(R.id.yourReviewRating);
            oneWordView = itemView.findViewById(R.id.yourOneWord);
            detailedView = itemView.findViewById(R.id.yourDetailedReview);
            editButtonView = itemView.findViewById(R.id.editButton);
            deleteButtonView = itemView.findViewById(R.id.deleteButton);

        }
    }

}
