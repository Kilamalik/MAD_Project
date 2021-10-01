package com.example.reviewerapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Review extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
    }

    public void itemListPage(View view) {
        Intent intent = new Intent(this, ReviewItemsList.class);
        startActivity(intent);
    }

    public void yourReviewsPage(View view) {
        /*Intent intent = new Intent(this, RetrieveYourReviews.class);
        startActivity(intent);*/
    }

}