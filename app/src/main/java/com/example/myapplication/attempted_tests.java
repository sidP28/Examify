package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class attempted_tests extends AppCompatActivity {
    ArrayList<String> list;
    static ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attempted_tests);
        list = new ArrayList<String>();
        ListView view = findViewById(R.id.list1);
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("Tests attempted").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    if (task.getResult().getChildrenCount() != 0) {
                        for (DataSnapshot snapshot1 : task.getResult().getChildren())
                            list.add("Code - " + snapshot1.getValue().toString());
                        adapter = new ArrayAdapter<String>(attempted_tests.this, android.R.layout.simple_list_item_1, list);
                        view.setAdapter(adapter);
                    }else{
                        finish();
                        Toast.makeText(attempted_tests.this,"No past tests",Toast.LENGTH_LONG).show();
                    }
                }else{
                    finish();
                    Toast.makeText(attempted_tests.this,task.getException().toString(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}