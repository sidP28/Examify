package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class Student extends AppCompatActivity {
    FirebaseAuth firebase = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        FirebaseDatabase.getInstance().getReference().child("Users").child(firebase.getCurrentUser().getUid()).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful())
                    setTitle("Welcome "+task.getResult().getValue().toString());
                else
                    setTitle("Welcome to "+String.valueOf(R.string.app_name));
            }
        });
    }

    public void signout(View view) {
        firebase.signOut();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void test(View view) {
        Intent intent = new Intent(this,taketest.class);
        startActivity(intent);
    }

    public void stats(View view) {
        Intent intent = new Intent(this,attempted_tests.class);
        startActivity(intent);
    }

    public void profile(View view) {
        Intent profile = new Intent(Student.this,profile.class);
        startActivity(profile);
    }
}