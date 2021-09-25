package com.example.mad_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RetrieveAllReviews extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    AllReviewsAdapter adapter;
    ArrayList<ReviewClass> list;

    Intent intent;
    String itemName2, category, description, imageURL;
    String getItemName2 = "itemName";

    TextView itemNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_all_reviews);

        intent = getIntent();
        itemName2 = intent.getStringExtra(getItemName2);
        System.out.println("Item name 2 is " + itemName2);

        itemNameView = findViewById(R.id.itemNameReviews);
        itemNameView.setText("All reviews for " + itemName2);

        recyclerView = findViewById(R.id.allReviewsList);
        database = FirebaseDatabase.getInstance("https://test-6eb79-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Reviews");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<ReviewClass>();
        adapter = new AllReviewsAdapter(this, list);
        recyclerView.setAdapter(adapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ReviewClass review = dataSnapshot.getValue(ReviewClass.class);
                    if(review.itemName.equals(itemName2)) {
                        list.add(review);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*public void back(View view) {
        Intent intent = new Intent(this, ReviewItem.class);
        startActivity(intent);
    }*/

    /*
    public void home(View view) {
        Intent intent = new Intent(this, page.class);
        startActivity(intent);
    }

    public void suggestions(View view) {
        Intent intent = new Intent(this, page.class);
        startActivity(intent);
    }

    public void you(View view) {
        Intent intent = new Intent(this, page.class);
        startActivity(intent);
    }
     */
}