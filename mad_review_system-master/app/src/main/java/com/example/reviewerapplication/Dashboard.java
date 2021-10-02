package com.example.reviewerapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Dashboard extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button add_reviwes,add_favorites,add_items;
    private EditText no_of_reviews,no_of_favorites,no_of_items;
    private FirebaseFirestore db;
    private TextView usernames;
    private ImageView suggestionicon,userprofile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        no_of_reviews=findViewById(R.id.no_of_reviews);
        usernames=findViewById(R.id.usernames);
        no_of_favorites=findViewById(R.id.no_of_favorites);
        no_of_items=findViewById(R.id.no_of_items);
        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        add_reviwes=findViewById(R.id.add_reviwes);
        add_favorites=findViewById(R.id.add_favorites);
        add_items=findViewById(R.id.add_items);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        suggestionicon=findViewById(R.id.suggestionicon);
        userprofile=findViewById(R.id.userprofile);
        usernames.setText(currentUser.getDisplayName());
        userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent=new Intent(Dashboard.this,SignIn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        suggestionicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Dashboard.this,Feedback.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        add_reviwes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> review_data = new HashMap<>();
                review_data.put("review_count", no_of_reviews.getText().toString());
                review_data.put("uid", currentUser.getUid());
                addData(review_data,"reviews");
            }
        });
        add_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> review_data = new HashMap<>();
                review_data.put("favourite_count", no_of_favorites.getText().toString());
                review_data.put("uid", currentUser.getUid());
                addData(review_data,"favourites");
            }
        });
        add_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> review_data = new HashMap<>();
                review_data.put("itemsCount", no_of_favorites.getText().toString());
                review_data.put("uid", currentUser.getUid());
                addData(review_data,"items");
            }
        });
    }

    void addData(Map review_data,String name){

        try{

            db.collection(name)
                    .add(review_data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            no_of_reviews.setText("");
                            no_of_favorites.setText("");
                            no_of_items.setText("");
                            Toast.makeText(Dashboard.this,"Successfully added",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            no_of_reviews.setText("");
                            no_of_favorites.setText("");
                            no_of_items.setText("");
                            Toast.makeText(Dashboard.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }catch (Exception e){
            no_of_reviews.setText("");
            no_of_favorites.setText("");
            no_of_items.setText("");
            System.out.println("error"+e.getMessage());
        }
    }
}