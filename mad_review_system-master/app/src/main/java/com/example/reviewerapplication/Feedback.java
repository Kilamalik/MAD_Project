package com.example.reviewerapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
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

public class Feedback extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText feedbackTextMultiLine,email_ID_to_submit;
    private CheckBox No,checkBox2;
    private Button button;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        db=FirebaseFirestore.getInstance();
        ratingBar=findViewById(R.id.ratingBar);
        feedbackTextMultiLine=findViewById(R.id.feedbackTextMultiLine);
        No=findViewById(R.id.No);
        checkBox2=findViewById(R.id.checkBox2);
        email_ID_to_submit=findViewById(R.id.email_ID_to_submit);
        button=findViewById(R.id.button);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float getrating = ratingBar.getRating();

                Map<String, Object> feedback_data = new HashMap<>();
                feedback_data.put("rating",getrating);
                feedback_data.put("uid", currentUser.getUid());
                feedback_data.put("suggestion",feedbackTextMultiLine.getText().toString());
                feedback_data.put("email",email_ID_to_submit.getText().toString());
                if(checkBox2.isChecked()){
                    feedback_data.put("isUserLike",false);
                }else if(No.isChecked()){
                    feedback_data.put("isUserLike",true);
                }
                addData(feedback_data,"feedback");
            }
        });

    }

    void addData(Map review_data, String name){

        try{

            db.collection(name)
                    .add(review_data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            Toast.makeText(Feedback.this,"Successfully added",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(Feedback.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }catch (Exception e){

            System.out.println("error"+e.getMessage());
        }
    }
}