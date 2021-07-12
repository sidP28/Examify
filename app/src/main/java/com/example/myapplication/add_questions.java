package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class add_questions extends AppCompatActivity {

    TextView addnew;
    static String a,b,c,d;
    Button save;
    LinearLayout addqeus;
    int questions,questionid,option1,option2,option3,option4,enterquestxt,enteranstxt,correctans,line,choosecorrectans,layoutid;
    ArrayList<Integer> nonemptyquestionids;
    ProgressBar progressBar;
    View horizontalline;
    static RecyclerView recycler;
    ArrayList<questions> arrayList;
    recycler adapter;
    static EditText question;

    @Override
    public void onBackPressed() {
        Toast.makeText(this,"Save questions to go back",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_questions);

        addnew = findViewById(R.id.addnew);
        save = findViewById(R.id.saveforlater);
        addqeus = findViewById(R.id.addques);
        questionid = 0;
        nonemptyquestionids = new ArrayList<Integer>();
        progressBar = findViewById(R.id.progressBar);
        addnew.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                addnew.setTextAppearance(R.style.textclick);
                return false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addinst(View view) {
        questionid+=11;
        option1=questionid+1;
        option2=questionid+2;
        option3=questionid+3;
        option4=questionid+4;
        enterquestxt=questionid+5;
        enteranstxt=questionid+6;
        correctans=questionid+7;
        choosecorrectans=questionid+8;
        line=questionid+9;
        layoutid=questionid+10;
        save.setEnabled(true);
        addnew.setTextColor(Color.WHITE);
        addnew.setTypeface(null, Typeface.NORMAL);
        TextView enterques = new TextView(this);
        TextView enterans = new TextView(this);
        TextView selcorrectans = new TextView(this);
        question = new EditText(this);
        EditText setoption1 = new EditText(this);
        EditText setoption2 = new EditText(this);
        EditText setoption3 = new EditText(this);
        EditText setoption4 = new EditText(this);
        RadioGroup _correctans = new RadioGroup(this);
        RadioButton a = new RadioButton(this);
        RadioButton b = new RadioButton(this);
        RadioButton c = new RadioButton(this);
        RadioButton d = new RadioButton(this);
        horizontalline = new View(this);
        ViewGroup.LayoutParams layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,5);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        horizontalline.setLayoutParams(layout);
        horizontalline.setBackgroundColor(Color.RED);
        enterques.setText("Enter the question:");
        enterans.setText("Enter 4 options:");
        selcorrectans.setText("Set correct option:");
        a.setText("Option 1");
        b.setText("Option 2");
        c.setText("Option 3");
        d.setText("Option 4");
        _correctans.setOrientation(LinearLayout.HORIZONTAL);
        _correctans.addView(a);
        _correctans.addView(b);
        _correctans.addView(c);
        _correctans.addView(d);
        selcorrectans.setId(choosecorrectans);
        _correctans.setId(correctans);
        question.setId(questionid);
        setoption1.setId(option1);
        setoption2.setId(option2);
        setoption3.setId(option3);
        setoption4.setId(option4);
        enterques.setId(enterquestxt);
        enterans.setId(enteranstxt);
        horizontalline.setId(line);
        ll.setId(layoutid);
        nonemptyquestionids.add(questionid);
        ll.addView(enterques);
        ll.addView(question);
        ll.addView(enterans);
        ll.addView(setoption1);
        ll.addView(setoption2);
        ll.addView(setoption3);
        ll.addView(setoption4);
        ll.addView(selcorrectans);
        ll.addView(_correctans);
        ll.addView(horizontalline);
        addqeus.addView(ll,addqeus.getChildCount()-1);
    }

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void save(View view) {
        questions = 0;
        closeKeyboard();
        progressBar.setVisibility(View.VISIBLE);
        int x = 0;
        int j = 0;
        for (int i : nonemptyquestionids) {
            j++;
            EditText question = findViewById(i);
            EditText option1 = findViewById(i + 1);
            EditText option2 = findViewById(i + 2);
            EditText option3 = findViewById(i + 3);
            EditText option4 = findViewById(i + 4);
            RadioGroup _correctans = findViewById(i + 7);
            try {
                if (!question.getText().toString().trim().isEmpty() || !option1.getText().toString().trim().isEmpty() || !option2.getText().toString().trim().isEmpty() || !option3.getText().toString().trim().isEmpty() || !option4.getText().toString().trim().isEmpty() || _correctans.getCheckedRadioButtonId() != -1) {
                    x = 1;
                    break;
                } else {
                    if (j == nonemptyquestionids.size()) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(add_questions.this);
                        alert.setMessage("No questions added, add some!");
                        alert.setNeutralButton("Edit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alert.show();
                        progressBar.setVisibility(View.GONE);
                    }
                    continue;
                }
            }catch (Exception e){}
        }
        if (x == 1) {
            recycler = new RecyclerView(this);
            recycler.setLayoutManager(new LinearLayoutManager(this));
            recycler.setHasFixedSize(true);
            arrayList = new ArrayList<questions>();
            adapter = new recycler(this, arrayList);
            recycler.setAdapter(adapter);
            LayoutInflater layout = getLayoutInflater();
            View view1 = layout.inflate(R.layout.questions_dialog, null);
            j = 0;
            for (int i : nonemptyquestionids) {
                j++;
                EditText question = findViewById(i);
                EditText option1 = findViewById(i + 1);
                EditText option2 = findViewById(i + 2);
                EditText option3 = findViewById(i + 3);
                EditText option4 = findViewById(i + 4);
                TextView enterques = findViewById(i + 5);
                TextView enterans = findViewById(i + 6);
                RadioGroup _correctans = findViewById(i + 7);
                TextView selcorrectans = findViewById(i + 8);
                View horizontalline = findViewById(i + 9);
                LinearLayout ll = findViewById(i + 10);
                try {
                    if ((question.getText().toString().trim().isEmpty() && option1.getText().toString().trim().isEmpty() && option2.getText().toString().trim().isEmpty() && option3.getText().toString().trim().isEmpty()) && option4.getText().toString().trim().isEmpty() && _correctans.getCheckedRadioButtonId() == -1) {
                        progressBar.setVisibility(View.GONE);
                        addqeus.removeView(ll);
                        nonemptyquestionids.remove(j - 1);
                    } else if (question.getText().toString().trim().isEmpty() || option1.getText().toString().trim().isEmpty() || option2.getText().toString().trim().isEmpty() || option3.getText().toString().trim().isEmpty() || option4.getText().toString().trim().isEmpty() || _correctans.getCheckedRadioButtonId() == -1) {
                        progressBar.setVisibility(View.GONE);
                        if (question.getText().toString().trim().isEmpty())
                            question.setError("Required");
                        if (option1.getText().toString().trim().isEmpty())
                            option1.setError("Required");
                        if (option2.getText().toString().trim().isEmpty())
                            option2.setError("Required");
                        if (option3.getText().toString().trim().isEmpty())
                            option3.setError("Required");
                        if (option4.getText().toString().trim().isEmpty())
                            option4.setError("Required");
                        if (_correctans.getCheckedRadioButtonId() == -1)
                            Toast.makeText(this, "Please set the correct answer for question " + j, Toast.LENGTH_LONG).show();
                    } else {
                        questions++;
                        a = "A. " + option1.getText().toString().trim();
                        b = "B. " + option2.getText().toString().trim();
                        c = "C. " + option3.getText().toString().trim();
                        d = "D. " + option4.getText().toString().trim();
                        String correctanswer = "";
                        RadioButton option = findViewById(_correctans.getCheckedRadioButtonId());
                        switch (option.getText().toString()) {
                            case "Option 1":
                                correctanswer = option1.getText().toString();
                                a += " (Correct)";
                                break;
                            case "Option 2":
                                correctanswer = option2.getText().toString();
                                b += " (Correct)";
                                break;
                            case "Option 3":
                                correctanswer = option3.getText().toString();
                                c += " (Correct)";
                                break;
                            case "Option 4":
                                correctanswer = option4.getText().toString();
                                d += " (Correct)";
                                break;
                            default:
                                break;
                        }
                        questions obj = new questions("Q. " + question.getText().toString().trim(), a, b, c, d);
                        arrayList.add(obj);
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Questions").child(String.valueOf(createtest.code));
                        ref.child("Question " + questions).child("Question").setValue(question.getText().toString().trim()).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(add_questions.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                ref.removeValue();
                                return;
                            }
                        });
                        ref.child("Question " + questions).child("Option 1").setValue(option1.getText().toString().trim()).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(add_questions.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                ref.removeValue();
                                return;
                            }
                        });
                        ref.child("Question " + questions).child("Option 2").setValue(option2.getText().toString().trim()).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(add_questions.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                ref.removeValue();
                                return;
                            }
                        });
                        ref.child("Question " + questions).child("Option 3").setValue(option3.getText().toString().trim()).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(add_questions.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                ref.removeValue();
                                return;
                            }
                        });
                        ref.child("Question " + questions).child("Option 4").setValue(option4.getText().toString().trim()).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(add_questions.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                ref.removeValue();
                                return;
                            }
                        });
                        int k = j;
                        ref.child("Question " + questions).child("Correct option").setValue(correctanswer).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(add_questions.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                ref.removeValue();
                                return;
                            }
                        })
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(Void unused) {
                                        if (k == nonemptyquestionids.size()) {
                                            progressBar.setVisibility(View.GONE);
                                            TextView dialogtitle = new TextView(add_questions.this);
                                            dialogtitle.setText("Test name - " + createtest.title.getText() + " - " + String.valueOf(createtest.code));
                                            dialogtitle.setBackgroundColor(Color.parseColor("#CCEF2D"));
                                            dialogtitle.setGravity(Gravity.CENTER);
                                            dialogtitle.setTextSize(25);
                                            dialogtitle.setTypeface(Typeface.DEFAULT_BOLD);
                                            dialogtitle.setTextColor(Color.BLACK);
                                            AlertDialog.Builder dialog = new AlertDialog.Builder(add_questions.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                                            dialog.setCustomTitle(dialogtitle);
                                            dialog.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    ref.removeValue();
                                                }
                                            })
                                                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent pasttests = new Intent(add_questions.this, past_tests.class);
                                                            startActivity(pasttests);
                                                            Toast.makeText(add_questions.this, "Unique code of the test you created - " + String.valueOf(createtest.code), Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                            dialog.setView(recycler).show();
                                        }
                                    }
                                });
                    }
                } catch (Exception e) {
                }
            }
        }
    }
    private void closeKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

