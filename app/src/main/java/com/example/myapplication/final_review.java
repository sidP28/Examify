package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class final_review extends AppCompatActivity {
    TextView ques;
    int right, na, wrong;
    ProgressBar progress;

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Press finish", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_review);
        ques = findViewById(R.id.textView13);
        progress = findViewById(R.id.progressBar4);
        FirebaseDatabase.getInstance().getReference().child("Questions").child(taketest.codetxt).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {
                String questions = "", correct = "", question = "", a = "", b = "", c = "", d = "", a1 = "", b1 = "", c1 = "", d1 = "";
                int count2 = 0;
                if (task.isSuccessful()) {
                    if (task.getResult().getChildrenCount() != 0) {
                        for (DataSnapshot ds : task.getResult().getChildren()) {
                            int count3 = 0;
                            String chosen = "";
                            try {
                                chosen = recycler1.seloptions[count2].split(". ")[1];
                            } catch (Exception e) {
                            }
                            questions += "\n" + ds.getKey() + " :\n\n";
                            for (DataSnapshot x : ds.getChildren()) {
                                count3++;
                                switch (count3) {
                                    case 1:
                                        correct = x.getValue().toString();
                                        break;
                                    case 2:
                                        a = x.getValue().toString();
                                        if (correct.equals(a)) {
                                            a1 = "  A. " + x.getValue() + " (Correct)\n";
                                            if (chosen.equals(a))
                                                a1 = "  A. " + x.getValue() + " (Correct) (Selected)\n";
                                        } else {
                                            a1 = "  A. " + x.getValue() + "\n";
                                            if (chosen.equals(a))
                                                a1 = "  A. " + x.getValue() + " (Selected)\n";
                                        }
                                        break;
                                    case 3:
                                        b = x.getValue().toString();
                                        if (correct.equals(b)) {
                                            b1 = "  B. " + x.getValue() + " (Correct)\n";
                                            if (chosen.equals(b))
                                                b1 = "  B. " + x.getValue() + " (Correct) (Selected)\n";
                                        } else {
                                            b1 = "  B. " + x.getValue() + "\n";
                                            if (chosen.equals(b))
                                                b1 = "  B. " + x.getValue() + " (Selected)\n";
                                        }
                                        break;
                                    case 4:
                                        c = x.getValue().toString();
                                        if (correct.equals(c)) {
                                            c1 = "  C. " + x.getValue() + " (Correct)\n";
                                            if (chosen.equals(c))
                                                c1 = "  C. " + x.getValue() + " (Correct) (Selected)\n";
                                        } else {
                                            c1 = "  C. " + x.getValue() + "\n";
                                            if (chosen.equals(c))
                                                c1 = "  A. " + x.getValue() + " (Selected)\n";
                                        }
                                        break;
                                    case 5:
                                        d = x.getValue().toString();
                                        if (correct.equals(d)) {
                                            d1 = "  D. " + x.getValue() + " (Correct)\n";
                                            if (chosen.equals(d))
                                                d1 = "  D. " + x.getValue() + " (Correct) (Selected)\n";
                                        } else {
                                            d1 = "  D. " + x.getValue() + "\n";
                                            if (chosen.equals(d))
                                                d1 = "  D. " + x.getValue() + " (Selected)\n";
                                        }
                                        break;
                                    case 6:
                                        question = " Q. " + x.getValue() + "\n";
                                }
                            }
                            count2++;
                            questions += question + a1 + b1 + c1 + d1;
                        }
                        try {
                            right = test.correct.size();
                        } catch (Exception e) {
                            right += 0;
                        }
                        try {
                            na = test.na.size();
                        } catch (Exception e) {
                            na += 1;
                        }
                        try {
                            wrong = test.incorrect.size();
                        } catch (Exception e) {
                            wrong += 0;
                        }
                        questions += "\n\nCorrect - " + right + "\nIncorrect - " + wrong + "\nNot attempted - " + na;
                        ques.setText(questions);
                    } else {
                        Toast.makeText(final_review.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(final_review.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    public void finish(View view) {
        Intent intent = new Intent(final_review.this,Student.class);
        startActivity(intent);
        finish();
    }
}
