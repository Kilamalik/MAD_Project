package com.example.reviewerapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.reviewerapplication.Models.ReviewClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReviewItem extends AppCompatActivity {

    ImageView itemImage;
    TextView itemName, itemCategory, itemDescription;
    String category = "category", imageURL = "imageURL", itemNameString = "itemName", description = "description";

    EditText detailed, oneWord;
    RadioGroup radioGroup;

    Intent intent;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_item);

        detailed = findViewById(R.id.editDetailedText);
        oneWord = findViewById(R.id.editShortText);
        radioGroup = findViewById(R.id.ratingGroup);

        itemImage = findViewById(R.id.itemImageReview);
        itemName = findViewById(R.id.itemNameReview);
        itemCategory = findViewById(R.id.itemCategoryReview);
        itemDescription = findViewById(R.id.itemDescriptionReview);

        intent = getIntent();

        itemName.setText(intent.getStringExtra(itemNameString));
        itemCategory.setText(intent.getStringExtra(category));
        itemDescription.setText(intent.getStringExtra(description));
        Glide.with(this)
                .asBitmap().load(intent.getStringExtra(imageURL))
                .into(itemImage);
    }

    public void submit(View view) {
        ReviewClass review;
        String user = "Sajeed";
        String itemName = intent.getStringExtra(itemNameString);
        String detailedText = detailed.getText().toString();
        String shortText = oneWord.getText().toString();

        int selectedID = radioGroup.getCheckedRadioButtonId();

        if(selectedID == -1) {
            Toast toast = Toast.makeText(this, "Please select a rating", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            RadioButton rb = findViewById(selectedID);
            String rating = rb.getText().toString();

            if(detailedText.isEmpty() || shortText.isEmpty()) {
                Toast toast = Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT);
                toast.show();
            }
            else {
                review = new ReviewClass(detailedText, shortText, rating, user, itemName);
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://reviewerapplication-185f7-default-rtdb.firebaseio.com/");
                DatabaseReference myRef = database.getReference("Reviews");

                myRef.child(itemName + "-" + user).setValue(review);

                Toast toast = Toast.makeText(this, "Your review has been added. Adding another review to the same item will overwrite your previous one.", Toast.LENGTH_LONG);
                toast.show();

                detailed.setText("");
                oneWord.setText("");
                radioGroup.clearCheck();
            }
        }
    }

    public void back(View view) {
        Intent intent = new Intent(this, ReviewItemsList.class);
        startActivity(intent);
    }

    public void toAllReviews(View view) {
        Intent secondIntent = new Intent(this, RetrieveAllReviews.class);
        secondIntent.putExtra(itemNameString, intent.getStringExtra(itemNameString));
        startActivity(secondIntent);
    }
}