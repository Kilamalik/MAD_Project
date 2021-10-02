package com.example.reviewerapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.reviewerapplication.Adapters.YourReviewsAdapter;
import com.example.reviewerapplication.Models.ReviewClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RetrieveYourReviews extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    YourReviewsAdapter adapter;
    ArrayList<ReviewClass> list;
    String userName = "Sajeed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_your_reviews);

        recyclerView = findViewById(R.id.yourReviewsList);
        database = FirebaseDatabase.getInstance("https://reviewerapplication-185f7-default-rtdb.firebaseio.com/").getReference("Reviews");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<ReviewClass>();
        adapter = new YourReviewsAdapter(this, list);
        recyclerView.setAdapter(adapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                while (!list.isEmpty()) list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ReviewClass review = dataSnapshot.getValue(ReviewClass.class);
                    if (review.user.equals(userName)) {
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

    public void back(View view) {
        Intent intent = new Intent(this, Review.class);
        startActivity(intent);
    }
}