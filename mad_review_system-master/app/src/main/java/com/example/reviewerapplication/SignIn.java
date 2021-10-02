package com.example.reviewerapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView bottom_note;
    private EditText email_ID,password_id;
    private Button signIn_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();
        bottom_note=findViewById(R.id.bottom_note);
        email_ID=findViewById(R.id.email_ID);
        password_id=findViewById(R.id.password_id);
        signIn_btn=findViewById(R.id.signIn_btn);

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
    }

    void signInUser(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignIn.this, "Successfully login", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(SignIn.this,Profile_SetUP.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignIn.this, "Error Something wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}