package com.example.testapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel,MainAdapter.myViewHolder>{
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull MainModel model) {
  holder.ItemName.setText(model.getItemName());
  holder.ItemDescription.setText(model.getItemDescription());
  holder.ItemAuthor.setText(model.getItemAuthor());

        Glide.with(holder.img.getContext())
                .load(model.getUrl())
                .placeholder(R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(R.drawable.common_google_signin_btn_icon_dark)
                .into(holder.img);
     holder.btnApprove.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             final DialogPlus dialogPlus=DialogPlus.newDialog(holder.img.getContext())
                     .setContentHolder(new ViewHolder(R.layout.add_popup))
                     .setExpanded(true,1200)
                     .create();
             View view=dialogPlus.getHolderView();
             EditText itemname=view.findViewById(R.id.txtName);
             EditText itemdescription =view.findViewById(R.id.txDescription);
             EditText itemauthor=view.findViewById(R.id.txtAuthor);
             EditText surl=view.findViewById(R.id.txtImageUrl);

             Button btnAdd=view.findViewById(R.id.btnAdd);
             itemname.setText(model.getItemName());
             itemdescription.setText(model.getItemDescription());
             itemauthor.setText(model.getItemAuthor());
             surl.setText(model.getUrl());
             dialogPlus.show();
             btnAdd.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     Map<String,Object>map=new HashMap<>();
                     map.put("ItemAuthor",itemauthor.getText().toString());
                     map.put("ItemDescription",itemdescription.getText().toString());
                     map.put("ItemName",itemname.getText().toString());
                     map.put("url",surl.getText().toString());

                     FirebaseDatabase.getInstance().getReference().child("approved").push().setValue(map)
                             .addOnSuccessListener(new OnSuccessListener<Void>() {
                                 @Override
                                 public void onSuccess(Void unused) {
                                     Toast.makeText(holder.ItemName.getContext() ,"Added Successfully", Toast.LENGTH_SHORT).show();
                                 }
                             });

                 }
             });
         }
     });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus=DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true,1200)
                        .create();

               View view =dialogPlus.getHolderView();
                EditText itemname=view.findViewById(R.id.txtName);
                EditText itemdescription =view.findViewById(R.id.txDescription);
                EditText itemauthor=view.findViewById(R.id.txtAuthor);
                EditText surl=view.findViewById(R.id.txtImageUrl);

                Button btnUpdate=view.findViewById(R.id.btnUpdate);

                itemname.setText(model.getItemName());
                itemdescription.setText(model.getItemDescription());
                itemauthor.setText(model.getItemAuthor());
                surl.setText(model.getUrl());
                dialogPlus.show();
                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object>map=new HashMap<>();
                        map.put("ItemAuthor",itemauthor.getText().toString());
                        map.put("ItemDescription",itemdescription.getText().toString());
                        map.put("ItemName",itemname.getText().toString());
                        map.put("url",surl.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("items")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.ItemName.getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.ItemName.getContext(), "Error Updating", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                });
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(holder.ItemName.getContext());
                builder.setTitle("Are you Sure?");
                builder.setMessage("Items once deleted cannot be recovered!");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("items")
                                .child(getRef(position).getKey()).removeValue();


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.ItemName.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();

            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        CircleImageView img;
        TextView ItemName,ItemDescription,ItemAuthor;

        Button btnEdit,btnDelete,btnApprove;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img =(CircleImageView)itemView.findViewById(R.id.img1);
            ItemName=(TextView)itemView.findViewById(R.id.nametext);
            ItemDescription=(TextView)itemView.findViewById(R.id.descriptiontext);
            ItemAuthor=(TextView)itemView.findViewById(R.id.authortext);
            btnEdit=(Button)itemView.findViewById(R.id.btnEdit);
            btnDelete=(Button) itemView.findViewById(R.id.btnDelete);
            btnApprove=(Button) itemView.findViewById(R.id.btnApprove);
        }
    }
}
