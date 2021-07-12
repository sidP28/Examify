package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class taketest extends AppCompatActivity {
    EditText edit;
    LinearLayout code;
    ScrollView details;
    int count,count1;
    Button start,show;
    ProgressBar progress;
    static String codetxt;
    TextView inst,title,end,begin,faculty,dur,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taketest);
        edit = findViewById(R.id.editText);
        code = findViewById(R.id.code);
        details = findViewById(R.id.detailsview);
        count = 0;
        count1=0;
        show = findViewById(R.id.showcode);
        start = findViewById(R.id.starttest);
        progress = findViewById(R.id.progressBar2);
        inst = findViewById(R.id.textView12);
        title = findViewById(R.id.testtitle);
        end = findViewById(R.id.testend);
        begin = findViewById(R.id.teststart);
        faculty = findViewById(R.id.testfac);
        dur = findViewById(R.id.testdur);
        date = findViewById(R.id.testdate);
    }

    public void details(View view) {
        closeKeyboard();
        codetxt = edit.getText().toString();
        progress.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference().child("codes").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    count++;
                    if(d.getValue().toString().equals(codetxt)){
                        code.setVisibility(View.GONE);
                        edit.setText("");
                        show.setEnabled(true);
                        start.setEnabled(true);
                        FirebaseDatabase.getInstance().getReference().child("Tests").child(codetxt).get().addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(taketest.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }).addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                for (DataSnapshot d : dataSnapshot.getChildren()) {
                                    count1++;
                                    switch(d.getKey()){
                                        case "Date":
                                            date.setText(d.getValue().toString());
                                            break;
                                        case "Duration":
                                            long hr = (long)d.getValue()/3600000;
                                            long min = ((long)d.getValue()%3600000)/60000;
                                            dur.setText(hr+" hours "+min+" mins");
                                            break;
                                        case "End":
                                            end.setText(d.getValue().toString());
                                            break;
                                        case "Start":
                                            begin.setText(d.getValue().toString());
                                            break;
                                        case "Title":
                                            title.setText(d.getValue().toString());
                                            break;
                                    }
                                }
                            }
                        });
                        FirebaseDatabase.getInstance().getReference().child("Tests").child(codetxt).child("Faculty").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if(task.isSuccessful())
                                    faculty.setText(task.getResult().getValue().toString()+" (Username)");
                                else {
                                    Toast.makeText(taketest.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
                        FirebaseDatabase.getInstance().getReference().child("Tests").child(codetxt).child("Instructions").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(Task<DataSnapshot> task) {
                                String instructions = "";
                                int count2 = 0;
                                if(task.isSuccessful()) {
                                    if (task.getResult().getChildrenCount() != 0) {
                                        for (DataSnapshot d : task.getResult().getChildren()) {
                                            count2++;
                                            if(count2!=task.getResult().getChildrenCount())
                                                instructions += "- " + d.getValue() + "\n";
                                            else
                                                instructions += "- " + d.getValue();
                                        }
                                        inst.setText(instructions);
                                    }else{
                                        inst.setText("No instructions added");
                                    }
                                }
                                else {
                                    Toast.makeText(taketest.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
                        details.setVisibility(View.VISIBLE);
                        break;
                    }
                    if(count==dataSnapshot.getChildrenCount())
                        Toast.makeText(taketest.this,"Invalid test code", Toast.LENGTH_SHORT).show();
                }
                progress.setVisibility(View.GONE);
            }
        });
    }
    private void closeKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void start(View view) {
        progress.setVisibility(View.GONE);
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("Tests attempted").orderByValue().equalTo(codetxt).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.getChildrenCount()>0)
                    Toast.makeText(taketest.this,"You have already attempted this test once",Toast.LENGTH_SHORT).show();
                else{
                    Intent intent = new Intent(taketest.this,test.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(taketest.this,"Try again - "+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showcode(View view) {
        details.setVisibility(View.GONE);
        code.setVisibility(View.VISIBLE);
        start.setEnabled(false);
        show.setEnabled(false);
//        inst.setText("");
    }
}