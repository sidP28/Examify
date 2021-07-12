package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;

public class test extends AppCompatActivity {
    RecyclerView recycler;
    ArrayList questions;
    String question="",a="",b="",c="",d="";
    TextView timer;
    ProgressBar progress;
    int fin = 0;
    CountDownTimer time;
    static ArrayList<String> correct;
    static ArrayList<String> incorrect;
    static ArrayList<String> na;
    long dur;

    @Override
    public void onBackPressed() {
        Toast.makeText(this,"Not allowed",Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        questions = new ArrayList<testdisp>();
        timer = findViewById(R.id.textView17);
        progress = findViewById(R.id.progressBar3);
        Intent intent = new Intent(this,taketest.class);
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("Tests attempted").push().setValue(taketest.codetxt).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(test.this, "Try again\nError - " + e.getMessage(), Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("username").get().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(test.this, "Try again\nError - " + e.getMessage(), Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    }
                }).addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        FirebaseDatabase.getInstance().getReference().child("Tests").child(taketest.codetxt).child("Attempted by:").push().setValue(dataSnapshot.getValue().toString()).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(test.this,"Try again\nError - "+e.getMessage(),Toast.LENGTH_LONG).show();
                                startActivity(intent);
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                FirebaseDatabase.getInstance().getReference().child("Questions").child(taketest.codetxt).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(Task<DataSnapshot> task) {
                                        if(task.isSuccessful()) {
                                            if (task.getResult().getChildrenCount() != 0) {
                                                for (DataSnapshot ds : task.getResult().getChildren()) {
                                                    int count2 = 0;
                                                    int i = 0;
                                                    int[] num = {2,3,4,5};
                                                    ArrayList<Integer> rand = new ArrayList<Integer>();
                                                    while(rand.size()!=4){
                                                        Random r = new Random();
                                                        int a = r.nextInt(4);
                                                        if(!rand.contains(num[a])) {
                                                            rand.add(num[a]);
                                                            continue;
                                                        }else
                                                            continue;
                                                    }
                                                    for(DataSnapshot x : ds.getChildren()){
                                                        count2++;
                                                        if(count2==rand.get(0))
                                                            a = "A. " + x.getValue().toString();
                                                        else if(count2== rand.get(1))
                                                            b = "B. " + x.getValue().toString();
                                                        else if(count2==rand.get(2))
                                                            c = "C. " + x.getValue().toString();
                                                        else if(count2==rand.get(3))
                                                            d = "D. " + x.getValue().toString();
                                                        else
                                                            question = x.getValue().toString();
                                                    }
                                                    testdisp obj = new testdisp(a,b,c,d,question);
                                                    questions.add(obj);
                                                }
                                            }
                                            recycler.setAdapter(new recycler1(questions));
                                        }
                                        else {
                                            Toast.makeText(test.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                });
                                FirebaseDatabase.getInstance().getReference().child("Tests").child(taketest.codetxt).child("Duration").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(Task<DataSnapshot> task) {
                                        dur = Long.parseLong(task.getResult().getValue().toString());
                                        time = new CountDownTimer(dur,1000) {
                                            @Override
                                            public void onTick(long millisUntilFinished) {
                                                NumberFormat f = new DecimalFormat("00");
                                                long hour = (millisUntilFinished / 3600000) % 24;
                                                long min = (millisUntilFinished / 60000) % 60;
                                                long sec = (millisUntilFinished / 1000) % 60;
                                                timer.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                                            }
                                            @Override
                                            public void onFinish() {
                                                View v = findViewById(R.id.button);
                                                fin = 1;
                                                submit(v);
                                            }
                                        }.start();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    public void submit(View view) {
        progress.setVisibility(View.VISIBLE);
        if(fin!=1){
            AlertDialog.Builder alert = new AlertDialog.Builder(test.this,android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK);
            alert.setMessage("Are you sure?");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    time.cancel();
                    correct = new ArrayList<String>();
                    incorrect = new ArrayList<String>();
                    na = new ArrayList<String>();
                    FirebaseDatabase.getInstance().getReference().child("Questions").child(taketest.codetxt).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(Task<DataSnapshot> task) {
                            if(task.isSuccessful()) {
                                int count3 = 0;
                                for (DataSnapshot d : task.getResult().getChildren()) {
                                    if (recycler1.seloptions[count3] == null)
                                        na.add(d.getKey());
                                    else if (recycler1.seloptions[count3].split(". ")[1].equals(d.child("Correct option").getValue().toString()))
                                        correct.add(d.getKey());
                                    else
                                        incorrect.add(d.getKey());
                                    count3++;
                                }
                                Intent intent = new Intent(test.this, final_review.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(test.this,task.getException().getMessage().toString(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            });
            alert.show();
        }
        else{
            correct = new ArrayList<String>();
            incorrect = new ArrayList<String>();
            na = new ArrayList<String>();
            FirebaseDatabase.getInstance().getReference().child("Questions").child(taketest.codetxt).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(Task<DataSnapshot> task) {
                    int count3 = 0;
                    if(task.isSuccessful()) {
                        for (DataSnapshot d : task.getResult().getChildren()) {
                            if (recycler1.seloptions[count3] == null)
                                na.add(d.getKey());
                            else if (recycler1.seloptions[count3].split(". ")[1].equals(d.child("Correct option").getValue().toString()))
                                correct.add(d.getKey());
                            else
                                incorrect.add(d.getKey());
                            count3++;
                        }
                    }else {
                        Toast.makeText(test.this, task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                    Intent intent = new Intent(test.this, final_review.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        progress.setVisibility(View.GONE);
    }
}