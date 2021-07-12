package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

public class profile extends AppCompatActivity {

    ImageView dp,next,prev;
    TextView changedp,deldp,done,name,username,email,usertype,usernametaken;
    EditText newdetails;
    Button newdetailsbtn;
    View line;
    int count;
    StorageReference storeref;
    Uri selectedImageUri;
    int ids[] = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,R.drawable.f,R.drawable.g,R.drawable.h,R.drawable.i,R.drawable.j,R.drawable.k,R.drawable.l,R.drawable.m,R.drawable.n};

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        dp = findViewById(R.id.dp);
        changedp = findViewById(R.id.textView);
        deldp = findViewById(R.id.textView10);
        line = findViewById(R.id.line);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);
        done = findViewById(R.id.done);
        newdetails = findViewById(R.id.newdetails);
        newdetailsbtn = findViewById(R.id.newdetailsbtn);
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        usertype = findViewById(R.id.usertype);
        usernametaken = findViewById(R.id.usernametaken);
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("dp").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (String.valueOf(task.getResult().getValue())!="null"){
                    deldp.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                    changedp.setText("Change");
                    dp.setImageDrawable(getDrawable(Integer.parseInt(String.valueOf(task.getResult().getValue()))));
                }
            }
        });
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                name.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                username.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("usertype").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                usertype.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        changedp.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changedp.setTextAppearance(R.style.textclick);
                return false;
            }
        });
        deldp.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                deldp.setTextAppearance(R.style.textclick);
                return false;
            }
        });
        done.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                done.setTextAppearance(R.style.textclick);
                return false;
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void changedp(View view) {
        dp.setImageDrawable(getDrawable(R.drawable.a));
        changedp.setTextAppearance(R.style.normal);
        next.setVisibility(View.VISIBLE);
        count = 0;
        done.setVisibility(View.VISIBLE);
    }

    public void editname(View view) {
        newdetails.setText("");
        newdetails.setVisibility(View.VISIBLE);
        newdetailsbtn.setVisibility(View.VISIBLE);
        newdetails.setHint("Enter new name");
    }

    public void edituser(View view) {
        if(!past_tests.adapter.isEmpty()){
            Toast.makeText(profile.this,"Username can't be changed when you have a test created",Toast.LENGTH_LONG).show();
            return;
        }
        newdetails.setText("");
        newdetails.setVisibility(View.VISIBLE);
        newdetails.setHint("Enter new username");
        newdetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                Query uniqueuser = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("username").equalTo(newdetails.getText().toString());
                uniqueuser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount() > 0) {
                            usernametaken.setVisibility(View.VISIBLE);
                            newdetailsbtn.setVisibility(View.GONE);
                        } else {
                            usernametaken.setVisibility(View.GONE);
                            newdetailsbtn.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }

    public void editemail(View view) {
        newdetails.setText("");
        newdetails.setVisibility(View.VISIBLE);
        newdetailsbtn.setVisibility(View.VISIBLE);
        newdetails.setHint("Enter new mail");
    }

    public void newdetails(View view) {
        int e = 0;
        if(newdetails.getText().toString().isEmpty())
            e = 1;
        if(newdetails.getHint().toString()== "Enter new username" && newdetails.getText().toString().contains(" "))
            e = 1;
        if(newdetails.getHint().toString()== "Enter new name" && newdetails.getText().toString().trim().isEmpty())
            e = 1;
        if(e!=1) {
            switch (newdetails.getHint().toString()) {
                case "Enter new name":
                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue(newdetails.getText().toString());
                    break;
                case "Enter new username":
                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").setValue(newdetails.getText().toString());
                    break;
                case "Enter new mail":
                    FirebaseAuth.getInstance().getCurrentUser().updateEmail(newdetails.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful())
                                Toast.makeText(profile.this, "Couldn't update - " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    break;
                default:
                    break;
            }
            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    name.setText(String.valueOf(task.getResult().getValue()));
                }
            });
            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    username.setText(String.valueOf(task.getResult().getValue()));
                }
            });
            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("usertype").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    usertype.setText(String.valueOf(task.getResult().getValue()));
                }
            });
            email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            newdetails.setVisibility(View.GONE);
            newdetailsbtn.setVisibility(View.GONE);
        }else
            if(newdetails.getText().toString().isEmpty())
                newdetails.setError("Required");
            else {
                switch (newdetails.getHint().toString()) {
                    case "Enter new name":
                        if (newdetails.getText().toString().trim().isEmpty()) {
                            newdetails.setError("Enter some characters");
                        }
                        break;
                    case "Enter new username":
                        if (newdetails.getText().toString().contains(" ")) {
                            newdetails.setError("Spaces not allowed");
                        }
                        break;
                }
            }
        }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void deldp(View view) {
        deldp.setTextAppearance(R.style.normal);
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("dp").removeValue();
        deldp.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
        changedp.setText("Change profile picture");
        dp.setImageDrawable(getDrawable(R.drawable.profile_picture));
    }

    public void next(View view) {
        count++;
        dp.setImageDrawable(getDrawable(ids[count]));
        if(count==13)
            next.setVisibility(View.GONE);
        if(count==1)
            prev.setVisibility(View.VISIBLE);
    }

    public void back(View view) {
        count--;
        dp.setImageDrawable(getDrawable(ids[count]));
        if(count==0)
            prev.setVisibility(View.GONE);
        if(count==12)
            next.setVisibility(View.VISIBLE);
    }

    public void done(View view) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("dp").setValue(ids[count]);
        deldp.setVisibility(View.VISIBLE);
        line.setVisibility(View.VISIBLE);
        next.setVisibility(View.GONE);
        prev.setVisibility(View.GONE);
        changedp.setText("Change");
        done.setVisibility(View.GONE);
    }
}