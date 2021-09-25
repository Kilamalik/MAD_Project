package com.example.mad_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void itemListPage(View view) {
        Intent intent = new Intent(this, ReviewItemsList.class);
        startActivity(intent);
    }

    public void yourReviewsPage(View view) {
        Intent intent = new Intent(this, RetrieveYourReviews.class);
        startActivity(intent);
    }

    /*
    public void back(View view) {
        Intent intent = new Intent(this, page.class);
        startActivity(intent);
    }


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