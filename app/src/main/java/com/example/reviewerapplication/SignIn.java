package com.example.reviewerapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SignIn extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView bottom_note, forgotPassword;
    private EditText email_ID,password_id;
    private Button signIn_btn ,admin_btn;
    private ProgressBar progressBar;
    String emailPattern = "[a-zA-Z0-9]._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        bottom_note=findViewById(R.id.bottom_note);
        email_ID=findViewById(R.id.email_ID);
        password_id=findViewById(R.id.password_id);
        signIn_btn=findViewById(R.id.signIn_btn);
        admin_btn=findViewById(R.id.adminsignin);
        forgotPassword= findViewById(R.id.forgot_pwd);
        progressBar = findViewById(R.id.progressBar);

        signIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password_id.getText().toString().isEmpty()){
                    Toast.makeText(SignIn.this, "password is required", Toast.LENGTH_SHORT).show();
                }else if(email_ID.getText().toString().isEmpty())
                {
                    Toast.makeText(SignIn.this, "Email is required", Toast.LENGTH_SHORT).show();
                   
                }else{
                    signInUser(email_ID.getText().toString(),password_id.getText().toString());
                }
                if(password_id.length() < 6){
                    password_id.setError("Password Must be > = 6 Characters");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

            }
        });

        admin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignIn.this,SignUp.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        bottom_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignIn.this,SignUp.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignIn.this,ResetPassword.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }

    void signInUser(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        UserModel userModel = new UserModel(user.getDisplayName(), user.getEmail(), user.getPhoneNumber());
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getUid())
                                .setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(SignIn.this, "Successfully login", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(SignIn.this,Profile_SetUP.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Register unsucessfuly",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });



                    } else {
                        Toast.makeText(SignIn.this, "Error Something wrong!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }


}