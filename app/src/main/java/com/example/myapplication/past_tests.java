package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class past_tests extends AppCompatActivity {
    ArrayList<String> list;
    static ArrayAdapter<String> adapter;

    @Override
    public void onBackPressed() {
        Intent fac = new Intent(past_tests.this, Faculty.class);
        startActivity(fac);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_tests);
        list = new ArrayList<String>();
        ListView view = findViewById(R.id.list);
        FirebaseDatabase.getInstance().getReference().child("Tests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.getChildrenCount()!=0) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren())
                        list.add(snapshot1.child("Title").getValue().toString() + " - " + snapshot1.getKey().toString());
                    adapter = new ArrayAdapter<String>(past_tests.this,android.R.layout.simple_list_item_1,list);
                    view.setAdapter(adapter);
                }else{
                    finish();
                    Toast.makeText(past_tests.this,"No past tests",Toast.LENGTH_LONG).show();
                }
                view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view1, int position, long id) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(past_tests.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                        dialog.setTitle("Are you sure you want to delete this test?");
                        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String code = view.getItemAtPosition(position).toString().split(" - ")[1].toString();
                                FirebaseDatabase.getInstance().getReference().child("Tests").child(code).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        list.remove(position);
                                        parent.removeViewInLayout(view1);
                                        adapter.notifyDataSetChanged();
                                        FirebaseDatabase.getInstance().getReference().child("codes").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                            @Override
                                            public void onSuccess(DataSnapshot dataSnapshot) {
                                                for(DataSnapshot snap : dataSnapshot.getChildren()) {
                                                    if (snap.getValue().toString().equals(code))
                                                        snap.getRef().removeValue();
                                                }
                                            }
                                        });
                                        FirebaseDatabase.getInstance().getReference().child("Questions").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                            @Override
                                            public void onSuccess(DataSnapshot dataSnapshot) {
                                                for(DataSnapshot snap : dataSnapshot.getChildren()) {
                                                    if (snap.getKey().toString().equals(code))
                                                        snap.getRef().removeValue();
                                                }
                                            }
                                        });
                                        FirebaseDatabase.getInstance().getReference().child("Users").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                            @Override
                                            public void onSuccess(DataSnapshot dataSnapshot) {
                                                for(DataSnapshot d : dataSnapshot.getChildren()){
                                                    try {
                                                        for (DataSnapshot s : d.child("Tests attempted").getChildren()) {
                                                            if (s.getValue().toString().equals(code))
                                                                s.getRef().removeValue();
                                                        }
                                                    }catch(Exception e){}
                                                }
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(past_tests.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).show();
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }
}
