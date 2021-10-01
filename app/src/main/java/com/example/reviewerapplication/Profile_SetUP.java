package com.example.reviewerapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.reviewerapplication.ui.dashboard.DashboardFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Profile_SetUP extends AppCompatActivity {

    private Button skip_btn,addimage_btn;
    private EditText username;
    private FirebaseAuth mAuth;

    ImageView addImage,profileImage;
    DatabaseReference mDatabaseReference;
    FirebaseDatabase mFirebaseInstance;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    EditText name, email, phone;

    private Uri selectedImageUri;
    private String ImageURIacessToken;
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "SelectImageActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        firebaseStorage= FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        skip_btn=findViewById(R.id.skip_btn);
        mAuth = FirebaseAuth.getInstance();
        username=findViewById(R.id.username);
        addimage_btn=findViewById(R.id.addimage_btn);
        FirebaseUser user = mAuth.getCurrentUser();

        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile_SetUP.this, SideNavigationBarActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        addimage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(username.getText().toString())
                        .build();
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Profile_SetUP.this, "Sucessfully updated", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });

        username.setText((user.getDisplayName()));

        if (ContextCompat.checkSelfPermission(Profile_SetUP.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 100);

        if (ContextCompat.checkSelfPermission(Profile_SetUP.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 101);

        addImage = findViewById(R.id.add_image);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        profileImage = findViewById(R.id.profile_image);

        FirebaseApp.initializeApp(Profile_SetUP.this);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseInstance.getReference("Users");
        mDatabaseReference.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if(dataSnapshot.exists()){
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    name = findViewById(R.id.username);
                    name.setText(userModel.getName());

                    email = findViewById(R.id.email);
                    email.setText(userModel.getEmail());

                    phone = findViewById(R.id.phoneNo);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError){
                // TODO: Implement this method
                Toast.makeText(Profile_SetUP.this, databaseError.getDetails()+" "+databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }

        });

        profileImage =  findViewById(R.id.profile_image);
        storageReference.child("Images").child(mAuth.getUid()).child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageURIacessToken=uri.toString();
                Glide.with(profileImage)
                        .load(uri)
                        .into(profileImage);
            }
        });

        FirebaseApp.initializeApp(this);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseInstance.getReference("Users");
        mDatabaseReference.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if(dataSnapshot.exists()){
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    name = findViewById(R.id.username);
                    name.setText(userModel.getName());

                    email = findViewById(R.id.email);
                    email.setText(userModel.getEmail());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError){
                // TODO: Implement this method
                Toast.makeText(Profile_SetUP.this, databaseError.getDetails()+" "+databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }

        });

        Button editProfile = findViewById(R.id.addimage_btn);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateimagetostorage();
                UserModel userModel = new UserModel(name.getText().toString(), email.getText().toString(), phone.getText().toString());
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getUid())
                        .setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Profile_SetUP.this,"Edited sucessfuly",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(Profile_SetUP.this,"Register unsucessfuly",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        Button delete = findViewById(R.id.delete_btn);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getUid())
                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Profile_SetUP.this," Account deleted successfully",Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                            Intent intent=new Intent(Profile_SetUP.this,SignIn.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(Profile_SetUP.this," unsucessfuly",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }

    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (resultCode == RESULT_OK) {
                    if (requestCode == SELECT_PICTURE) {
                        // Get the url from data
                        selectedImageUri = data.getData();
                        if (null != selectedImageUri) {
                            // Get the path from the Uri
                            String path = getPathFromURI(selectedImageUri);
                            Log.i(TAG, "Image Path : " + path);
                            // Set the image in ImageView
                            findViewById(R.id.profile_image).post(new Runnable() {
                                @Override
                                public void run() {
                                    ((ImageView) findViewById(R.id.profile_image)).setImageURI(selectedImageUri);
                                }
                            });

                        }
                    }
                }
            }
        }).start();
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private void updateimagetostorage() {

        StorageReference imageref = storageReference.child("Images").child(mAuth.getUid()).child("Profile Pic");

        Bitmap bitmap=null;
        try {
            if(null!=selectedImageUri)
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImageUri);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,25,byteArrayOutputStream);
        byte[] data=byteArrayOutputStream.toByteArray();
        ///putting image to storage

        UploadTask uploadTask=imageref.putBytes(data);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ImageURIacessToken=uri.toString();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getApplicationContext(),"URI get Failed",Toast.LENGTH_SHORT).show();
                    }


                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile_SetUP.this,"Image Not Updated",Toast.LENGTH_SHORT).show();
            }
        });


    }
}