package com.example.reviewerapplication.ui.dashboard;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reviewerapplication.ui.feedback.FeedbackFragment;
import com.example.reviewerapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DashboardFragment extends Fragment {
    private FirebaseAuth mAuth;
    private Button add_reviwes,add_favorites,add_items;
    private EditText no_of_reviews,no_of_favorites,no_of_items;
    private FirebaseFirestore db;
    private TextView usernames;
    private ImageView suggestionicon,userprofile;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        no_of_reviews=root.findViewById(R.id.no_of_reviews);
//        usernames=root.findViewById(R.id.usernames);
        no_of_favorites=root.findViewById(R.id.no_of_favorites);
        no_of_items=root.findViewById(R.id.no_of_items);
        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        add_reviwes=root.findViewById(R.id.add_reviwes);
        add_favorites=root.findViewById(R.id.add_favorites);
        add_items=root.findViewById(R.id.add_items);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        suggestionicon=root.findViewById(R.id.suggestionicon);
        suggestionicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), FeedbackFragment.class);
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

        return root;
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
                            Toast.makeText(getContext(),"Successfully added",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            no_of_reviews.setText("");
                            no_of_favorites.setText("");
                            no_of_items.setText("");
                            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
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