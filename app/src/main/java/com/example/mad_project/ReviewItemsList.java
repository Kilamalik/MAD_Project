package com.example.mad_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReviewItemsList extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    MyAdapter myAdapter;
    ArrayList<ItemClass> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_items_list);

        recyclerView = findViewById(R.id.itemList);
        database = FirebaseDatabase.getInstance("https://test-6eb79-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Items");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*ItemClass i1 = new ItemClass("NIKE", "SHOES", "NO");
        ItemClass i2 = new ItemClass("NIKE", "SHOES", "NO");*/

        list = new ArrayList<ItemClass>();
        myAdapter = new MyAdapter(this, list);
        recyclerView.setAdapter(myAdapter);

        database.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ItemClass item = dataSnapshot.getValue(ItemClass.class);
                    list.add(item);
                    item.display();
                }

                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void back(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void reviewPage(View view) {
        Intent intent = new Intent(this, ReviewItem.class);
        startActivity(intent);
    }

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