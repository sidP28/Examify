package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class createtest extends AppCompatActivity {

    TextView start,end,date,chosenstart,datetxt,chosenend,error,addnew;
    TimePicker time;
    ImageView tickstart,tickend,tickdur,tickdate,ticktitle;
    String starttime,endtime,chosendate,chosendateactual,instructions,name;
    DatePicker setdate;
    EditText hrs,mins;
    static EditText title;
    Button addinst,createbtn,saveinst;
    LinearLayout testpoints,scroll;
    int instructionids,x,e;
    public static int code;
    ArrayList<Integer> nonemptyinstids;
    ConstraintLayout detailsview;
    Date d2;
    ProgressBar prog;
    LocalTime d3,d4,d5;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createtest);
        start = findViewById(R.id.t1);
        end = findViewById(R.id.t2);
        time = findViewById(R.id.timepicker);
        tickstart = findViewById(R.id.tickstart);
        tickend = findViewById(R.id.tickend);
        tickdur = findViewById(R.id.tickdur);
        ticktitle = findViewById(R.id.ticktitle);
        setdate = findViewById(R.id.datepicker);
        date = findViewById(R.id.d1);
        tickdate = findViewById(R.id.tickdate);
        title = findViewById(R.id.gettitle);
        hrs = findViewById(R.id.hrs);
        mins = findViewById(R.id.mins);
        addinst = findViewById(R.id.addinst);
        createbtn = findViewById(R.id.createbtn);
        chosenstart = findViewById(R.id.chosenstart);
        chosenend = findViewById(R.id.chosenend);
        datetxt = findViewById(R.id.chosendate);
        error = findViewById(R.id.error);
        testpoints = findViewById(R.id.testpoints);
        detailsview = findViewById(R.id.detailsview);
        addnew = findViewById(R.id.addnew);
        scroll = findViewById(R.id.scroll);
        saveinst = findViewById(R.id.saveinst);
        instructionids = 0;
        nonemptyinstids = new ArrayList<Integer>();
        prog = findViewById(R.id.prog);
        hrs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(hrs.getText().toString().isEmpty() || mins.getText().toString().isEmpty())
                   tickdur.setVisibility(View.GONE);
                else
                    tickdur.setVisibility(View.VISIBLE);
                if(tickstart.getVisibility()==View.VISIBLE && tickend.getVisibility()==View.VISIBLE && tickdur.getVisibility()==View.VISIBLE && tickdate.getVisibility()==View.VISIBLE && ticktitle.getVisibility()==View.VISIBLE) {
                    addinst.setEnabled(true);
                    createbtn.setEnabled(true);
                }else{
                    addinst.setEnabled(false);
                    createbtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mins.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (hrs.getText().toString().isEmpty() || mins.getText().toString().isEmpty())
                        tickdur.setVisibility(View.GONE);
                    else
                        tickdur.setVisibility(View.VISIBLE);
                    if (Integer.parseInt(mins.getText().toString()) > 59) {
                        error.setVisibility(View.VISIBLE);
                        tickdur.setVisibility(View.GONE);
                    } else {
                        error.setVisibility(View.GONE);
                    }
                }catch(Exception e){}
                if(tickstart.getVisibility()==View.VISIBLE && tickend.getVisibility()==View.VISIBLE && tickdur.getVisibility()==View.VISIBLE && tickdate.getVisibility()==View.VISIBLE && ticktitle.getVisibility()==View.VISIBLE) {
                    addinst.setEnabled(true);
                    createbtn.setEnabled(true);
                }else{
                    addinst.setEnabled(false);
                    createbtn.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(title.getText().toString().isEmpty() || title.getText().toString().trim().isEmpty())
                    ticktitle.setVisibility(View.GONE);
                else
                    ticktitle.setVisibility(View.VISIBLE);
                if(tickstart.getVisibility()==View.VISIBLE && tickend.getVisibility()==View.VISIBLE && tickdur.getVisibility()==View.VISIBLE && tickdate.getVisibility()==View.VISIBLE && ticktitle.getVisibility()==View.VISIBLE) {
                    addinst.setEnabled(true);
                    createbtn.setEnabled(true);
                }else{
                    addinst.setEnabled(false);
                    createbtn.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        start.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                start.setTextAppearance(R.style.textclick);
                return false;
            }
        });
        end.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                end.setTextAppearance(R.style.textclick);
                return false;
            }
        });
        date.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                date.setTextAppearance(R.style.textclick);
                return false;
            }
        });
        addnew.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                addnew.setTextAppearance(R.style.textclick);
                return false;
            }
        });
        hrs.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hrs.setFocusable(true);
                hrs.setFocusableInTouchMode(true);
                if(time.getVisibility()==View.VISIBLE)
                    time.setVisibility(View.GONE);
                else if(setdate.getVisibility()==View.VISIBLE)
                    setdate.setVisibility(View.GONE);
                return false;
            }
        });
        mins.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mins.setFocusable(true);
                mins.setFocusableInTouchMode(true);
                if(time.getVisibility()==View.VISIBLE)
                    time.setVisibility(View.GONE);
                else if(setdate.getVisibility()==View.VISIBLE)
                    setdate.setVisibility(View.GONE);
                return false;
            }
        });
        title.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                title.setFocusable(true);
                title.setFocusableInTouchMode(true);
                if(time.getVisibility()==View.VISIBLE)
                    time.setVisibility(View.GONE);
                else if(setdate.getVisibility()==View.VISIBLE)
                    setdate.setVisibility(View.GONE);
                return false;
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addinst(View view) {
        instructionids++;
        saveinst.setEnabled(true);
        addnew.setTextColor(Color.WHITE);
        addnew.setTypeface(null, Typeface.NORMAL);
        EditText points = new EditText(this);
        points.setId(instructionids);
        nonemptyinstids.add(instructionids);
        scroll.addView(points,scroll.getChildCount()-1);
    }

    public void saveinst(View view) {
        closeKeyboard();
        time.setVisibility(View.GONE);
        setdate.setVisibility(View.GONE);
        if(instructionids==0) {
            AlertDialog.Builder alert = new AlertDialog.Builder(createtest.this);
            alert.setMessage("Are you sure? No instructions added.");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    nonemptyinstids.removeAll(nonemptyinstids);
                    detailsview.setVisibility(View.VISIBLE);
                    testpoints.setVisibility(View.GONE);
                    scroll.removeAllViews();
                    scroll.addView(addnew);
                    createbtn.setEnabled(true);
                }
            });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alert.show();
        }
        x = 0;
        for(int i : nonemptyinstids) {
            EditText instruction = findViewById(i);
            try {
                if (!instruction.getText().toString().trim().isEmpty()) {
                    x = 1;
                    break;
                } else {
                    if (i == nonemptyinstids.size()) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(createtest.this);
                        alert.setMessage("Are you sure? No instructions added.");
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                nonemptyinstids.removeAll(nonemptyinstids);
                                //instructionids = 0;
                                detailsview.setVisibility(View.VISIBLE);
                                testpoints.setVisibility(View.GONE);
                                x = 0;
                                scroll.removeAllViews();
                                instructionids = 0;
                                scroll.addView(addnew);
                                createbtn.setEnabled(true);
                            }
                        });
                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alert.show();
                    }
                    continue;
                }
            }catch (Exception e){}
        }
        if(x==1){
            for(int i : nonemptyinstids) {
                try {
                    EditText instruction = findViewById(i);
                    if (instruction.getText().toString().trim().isEmpty())
                        scroll.removeView(instruction);
                }catch (Exception e){}
            }
            detailsview.setVisibility(View.VISIBLE);
            testpoints.setVisibility(View.GONE);
            createbtn.setEnabled(true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createtest(View v) {
        instructions = "";
        e = 0;
        time.setVisibility(View.GONE);
        setdate.setVisibility(View.GONE);
        GregorianCalendar calendar = new GregorianCalendar();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date d1 = format.parse(calendar.get(Calendar.DATE) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.YEAR));
            d2 = format.parse(chosendateactual);
            DateTimeFormatter format1 = DateTimeFormatter.ofPattern("HH:mm");
            d3 = LocalTime.parse(chosenend.getText().toString(), format1);
            d4 = LocalTime.parse(chosenstart.getText().toString(), format1);
            d5 = LocalTime.parse(LocalTime.now().format(format1).toString());
            long duration = java.time.Duration.between(d4,d3).toMillis();
            if (d1.after(d2) || (d1.equals(d2) && d4.isBefore(d5))) {
                e = 1;
                Toast.makeText(createtest.this, "The chosen date/time has passed", Toast.LENGTH_LONG).show();
            }
            if(duration<0){
                duration = 24*3600000 - Math.abs(duration);
            }if(duration == 0)
                duration = 12*3600000;
            if (duration > 43200000) {
                e = 1;
                Toast.makeText(createtest.this, "The difference in your start and end time must be under 12 hours", Toast.LENGTH_LONG).show();
            } else {
                if (duration < Integer.parseInt(hrs.getText().toString()) * 3600000 + Integer.parseInt(mins.getText().toString()) * 60000) {
                    e = 1;
                    Toast.makeText(createtest.this, "The duration is greater than the difference of start and end", Toast.LENGTH_LONG).show();
                }
            }
        }catch (Exception e) {}

        if (e != 1) {
            prog.setVisibility(View.VISIBLE);
            AlertDialog.Builder confirm = new AlertDialog.Builder(createtest.this);
            confirm.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    prog.setVisibility(View.VISIBLE);
                    gencode();
                    int count = 0;
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Tests").child(String.valueOf(code));
                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if(task.isSuccessful()) {
                                name = task.getResult().getValue().toString();
                                ref.child("Faculty").setValue(name).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(createtest.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                });
                            }
                            else {
                                Toast.makeText(createtest.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
                    ref.child("Title").setValue(title.getText().toString()).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(createtest.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                    ref.child("Date").setValue(datetxt.getText()).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(createtest.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                    ref.child("Start").setValue(chosenstart.getText()).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(createtest.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                    ref.child("End").setValue(chosenend.getText()).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(createtest.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                    for (int i : nonemptyinstids) {
                        if (findViewById(i) != null) {
                            count++;
                            EditText inst = findViewById(i);
                            ref.child("Instructions").child("Instruction " + count).setValue(inst.getText().toString().trim()).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(createtest.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });
                        } else
                            continue;
                    }
                    ref.child("Duration").setValue(Integer.parseInt(hrs.getText().toString()) * 3600000 + Integer.parseInt(mins.getText().toString()) * 60000).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(createtest.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Intent intent = new Intent(createtest.this, add_questions.class);
                            startActivity(intent);
                            finish();
                            prog.setVisibility(View.GONE);
                        }
                    });
                }
            });
            View view = getLayoutInflater().inflate(R.layout.confirm_details,null);
            LinearLayout layout = view.findViewById(R.id.confirmdet);
            TextView titletxt = view.findViewById(R.id.titletxt);
            titletxt.setText("Title - " + title.getText().toString());
            TextView datetxt1 = view.findViewById(R.id.datetxt);
            datetxt1.setText("Date - " + datetxt.getText().toString());
            TextView starttxt = view.findViewById(R.id.starttxt);
            starttxt.setText("Start time - " + chosenstart.getText().toString());
            TextView endtxt = view.findViewById(R.id.endtxt);
            endtxt.setText("End time - " + chosenend.getText().toString());
            TextView durtxt = view.findViewById(R.id.durtxt);
            durtxt.setText("Duration - " + hrs.getText().toString() + " hours " + mins.getText().toString() + " minutes");
            for (int i : nonemptyinstids) {
                if (findViewById(i) != null) {
                    EditText instruction = findViewById(i);
                    instructions += "- " + instruction.getText().toString().trim()+"\n";
                } else
                    continue;
            }
            if(instructions != ""){
                TextView inst = view.findViewById(R.id.inst);
                TextView insttitle = view.findViewById(R.id.insttitle);
                inst.setText(instructions);
                insttitle.setVisibility(View.VISIBLE);
                inst.setVisibility(View.VISIBLE);
            }
            confirm.setView(view);
            confirm.show();

            prog.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void gencode() {
        Random random = new Random();
        code = random.ints(100000,999999).findFirst().getAsInt();
        FirebaseDatabase.getInstance().getReference().child("codes").equalTo(code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    gencode();
                } else {
                    FirebaseDatabase.getInstance().getReference().child("codes").push().setValue(code);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void addinstbtn(View view) {
        createbtn.setEnabled(false);
        testpoints.setVisibility(View.VISIBLE);
        detailsview.setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void end(View view) {
        closeKeyboard();
        mins.setFocusable(false);
        hrs.setFocusable(false);
        title.setFocusable(false);
        setdate.setVisibility(View.GONE);
        end.setTextAppearance(R.style.normal);
        time.setVisibility(View.VISIBLE);
        time.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                tickend.setVisibility(View.VISIBLE);
                if(hourOfDay<10 || minute<10) {
                    if (hourOfDay < 10 && minute<10)
                        endtime = "0" + hourOfDay + ":0" + minute;
                    else {
                        if (hourOfDay < 10)
                            endtime = "0" + hourOfDay + ":" + minute;
                        else
                            endtime = hourOfDay + ":0" + minute;
                    }
                }else
                    endtime = hourOfDay+":"+minute;
                chosenend.setText(endtime);
                if(tickstart.getVisibility()==View.VISIBLE && tickend.getVisibility()==View.VISIBLE && tickdur.getVisibility()==View.VISIBLE && tickdate.getVisibility()==View.VISIBLE && ticktitle.getVisibility()==View.VISIBLE) {
                    addinst.setEnabled(true);
                    createbtn.setEnabled(true);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void start(View view) {
        closeKeyboard();
        mins.setFocusable(false);
        hrs.setFocusable(false);
        title.setFocusable(false);
        start.setTextAppearance(R.style.normal);
        setdate.setVisibility(View.GONE);
        time.setVisibility(View.VISIBLE);
        time.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                tickstart.setVisibility(View.VISIBLE);
                if(hourOfDay<10 || minute<10) {
                    if (hourOfDay < 10 && minute<10)
                        starttime = "0" + hourOfDay + ":0" + minute;
                    else {
                        if (hourOfDay < 10)
                            starttime = "0" + hourOfDay + ":" + minute;
                        else
                            starttime = hourOfDay + ":0" + minute;
                    }
                }else
                    starttime = hourOfDay+":"+minute;
                chosenstart.setText(starttime);
                if(tickstart.getVisibility()==View.VISIBLE && tickend.getVisibility()==View.VISIBLE && tickdur.getVisibility()==View.VISIBLE && tickdate.getVisibility()==View.VISIBLE && ticktitle.getVisibility()==View.VISIBLE) {
                    addinst.setEnabled(true);
                    createbtn.setEnabled(true);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void date(View view) {
        closeKeyboard();
        mins.setFocusable(false);
        hrs.setFocusable(false);
        title.setFocusable(false);
        date.setTextAppearance(R.style.normal);
        time.setVisibility(View.GONE);
        setdate.setVisibility(View.VISIBLE);
        setdate.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tickdate.setVisibility(View.VISIBLE);
                chosendateactual = dayOfMonth+"-"+monthOfYear+"-"+year;
                monthOfYear = monthOfYear+1;
                chosendate = dayOfMonth+"-"+monthOfYear+"-"+year;
                datetxt.setText(chosendate);
                if(tickstart.getVisibility()==View.VISIBLE && tickend.getVisibility()==View.VISIBLE && tickdur.getVisibility()==View.VISIBLE && tickdate.getVisibility()==View.VISIBLE && ticktitle.getVisibility()==View.VISIBLE) {
                    addinst.setEnabled(true);
                    createbtn.setEnabled(true);
                }
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
}