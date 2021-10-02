package com.example.reviewerapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        Intent intent = new Intent(this, RetrieveYourReviews.class);
        startActivity(intent);
    }

}