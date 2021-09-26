package com.example.reviewerapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText inputname,newinputemail,inputpassword,inputphone;
    private Button create_btn;
    private TextView navigation_note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        inputname=findViewById(R.id.inputname);
        newinputemail=findViewById(R.id.newinputemail);
        inputpassword=findViewById(R.id.inputpassword);
        inputphone=findViewById(R.id.inputphone);
        navigation_note=findViewById(R.id.navigation_note);

        navigation_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUp.this,SignIn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        create_btn=findViewById(R.id.create_btn);
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputname.getText().toString().isEmpty()){
                    Toast.makeText(SignUp.this, "Username is required", Toast.LENGTH_SHORT).show();
                }
                else if(newinputemail.getText().toString().isEmpty()){
                    Toast.makeText(SignUp.this, "Email is required", Toast.LENGTH_SHORT).show();
                }else if(inputpassword.getText().toString().isEmpty()){
                    Toast.makeText(SignUp.this, "password is required", Toast.LENGTH_SHORT).show();
                }else if(inputpassword.getText().toString().length() <6)
                {
                    Toast.makeText(SignUp.this, "password length must be greater than 6 characters!", Toast.LENGTH_SHORT).show();
                }else{
                    signUp(newinputemail.getText().toString(),inputpassword.getText().toString());
                }
            }
        });

    }

    void signUp(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(inputname.getText().toString())
                                    .setPhotoUri(Uri.parse("https://firebasestorage.googleapis.com/v0/b/desktop-application-46c8f.appspot.com/o/avataaars.svg?alt=media&token=057fa52c-79e9-4115-bad3-2151e7318ab6"))
                                    .build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                UserModel userModel = new UserModel(user.getDisplayName(), user.getEmail(), user.getPhoneNumber());
                                                FirebaseDatabase.getInstance().getReference("Users")
                                                        .child(FirebaseAuth.getInstance().getUid())
                                                        .setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Toast.makeText(SignUp.this, "Account Sucessfully created", Toast.LENGTH_SHORT).show();
                                                            Intent intent=new Intent(SignUp.this,SignIn.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                        else{
                                                            Toast.makeText(SignUp.this,"Register unsucessfuly",Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });






                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(SignUp.this, "Error something wrong!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }
}