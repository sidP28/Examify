package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.api.fallback.service.FirebaseAuthFallbackService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.regex.Pattern;

import static java.lang.System.load;
import static java.lang.System.out;

public class MainActivity extends AppCompatActivity {

    ChipGroup stud_fac;
    Chip stud;
    Chip fac;
    LinearLayout reg_log;
    ConstraintLayout reglay, loglay;
    TextView des, register, login, name, user, mail, pass, userin, passin, usertaken,respass;
    int count1 = 0, count2 = 0, count = 0, e = 0, x = 0, x1 = 0,studfac;
    FirebaseAuth firebase;
    ProgressBar loadreg, loadlog;
    Snackbar snackbar;
    String studorfac,emailtxt;
    ImageButton passvisibility, passvisibilitylog;
    Intent intent;
    EditText maildialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        firebase = FirebaseAuth.getInstance();
        setTitle("Home");
        if (firebase.getCurrentUser() != null){
            Toast.makeText(MainActivity.this,"Logging in",Toast.LENGTH_SHORT).show();
            FirebaseDatabase.getInstance().getReference().child("Users").child(firebase.getCurrentUser().getUid()).child("usertype").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        if (String.valueOf(task.getResult().getValue()).equals("Student")) {
                            intent = new Intent(MainActivity.this, Student.class);
                            startActivity(intent);
                        } else if (String.valueOf(task.getResult().getValue()).equals("Faculty")) {
                            intent = new Intent(MainActivity.this, Faculty.class);
                            startActivity(intent);
                        }
                        finish();
                    }
                }
            });
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        user = findViewById(R.id.user);
        mail = findViewById(R.id.mail);
        pass = findViewById(R.id.pass);
        userin = findViewById(R.id.userin);
        passin = findViewById(R.id.passin);
        stud_fac = findViewById(R.id.stud_fac);
        stud = findViewById(R.id.stud);
        fac = findViewById(R.id.fac);
        reg_log = findViewById(R.id.reg_log);
        des = findViewById(R.id.des);
        reglay = findViewById(R.id.reglay);
        loglay = findViewById(R.id.loglay);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        loadreg = findViewById(R.id.loadreg);
        loadlog = findViewById(R.id.loadlog);
        usertaken = findViewById(R.id.usertaken);
        passvisibility = findViewById(R.id.passvisibility);
        passvisibilitylog = findViewById(R.id.passvisibilitylog);
        respass = findViewById(R.id.respass);

        ChipGroup.OnCheckedChangeListener chipclick = new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                name.setText("");
                user.setText("");
                mail.setText("");
                pass.setText("");
                userin.setText("");
                passin.setText("");
                name.setError(null);
                user.setError(null);
                mail.setError(null);
                pass.setError(null);
                x = 0;
                pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                passvisibility.setImageResource(R.drawable.not_visible);
                x1 = 0;
                passvisibilitylog.setImageResource(R.drawable.not_visible);
                passin.setTransformationMethod(PasswordTransformationMethod.getInstance());
                studfac = stud_fac.getCheckedChipId();
                switch (studfac == stud.getId() ? 0 : 1) {
                    case 0:
                        studorfac = "Student";
                        break;
                    case 1:
                        studorfac = "Faculty";
                        break;
                    default:
                        break;
                }
                if (stud.isChecked() || fac.isChecked()) {
                    reg_log.setVisibility(View.VISIBLE);
                    des.setVisibility(View.GONE);
                } else {
                    reg_log.setVisibility(View.GONE);
                    register.setBackground(null);
                    login.setBackground(null);
                    x = 0;
                    passvisibility.setImageResource(R.drawable.not_visible);
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    x1 = 0;
                    passvisibilitylog.setImageResource(R.drawable.not_visible);
                    passin.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    des.setVisibility(View.VISIBLE);
                    reglay.setVisibility(View.GONE);
                    loglay.setVisibility(View.GONE);
                }
            }
        };
        stud_fac.setOnCheckedChangeListener(chipclick);
        user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                Query uniqueuser = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("username").equalTo(user.getText().toString());
                uniqueuser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount() > 0) {
                            usertaken.setVisibility(View.VISIBLE);
                            e = 1;
                        } else {
                            usertaken.setVisibility(View.GONE);
                            e = 0;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }

    public void register(View view){
        register.setBackgroundColor(Color.LTGRAY);
        login.setBackground(null);
        reglay.setVisibility(View.VISIBLE);
        loglay.setVisibility(View.GONE);
        if (count1 != 1 || count == 2)
            count2++;
    }

    public void login(View view) {
        login.setBackgroundColor(Color.LTGRAY);
        register.setBackground(null);
        loglay.setVisibility(View.VISIBLE);
        reglay.setVisibility(View.GONE);
        if (count2 != 1 || count == 2)
            count1++;
    }

    public void passvisibility(View view) {
        if (x % 2 == 0) {
            passvisibility.setImageResource(R.drawable.visible);
            pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            passvisibility.setImageResource(R.drawable.not_visible);
            pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        x++;
    }

    public void passvisibilitylog(View view) {
        if (x1 % 2 == 0) {
            passvisibilitylog.setImageResource(R.drawable.visible);
            passin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            passvisibilitylog.setImageResource(R.drawable.not_visible);
            passin.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        x1++;
    }

    public void registerbtn(View view) {
        String getname = name.getText().toString();
        String getuser = user.getText().toString();
        String getmail = mail.getText().toString();
        String getpass = pass.getText().toString();
        x = 0;
        passvisibility.setImageResource(R.drawable.not_visible);
        pass.setTransformationMethod(PasswordTransformationMethod.getInstance());

        if (getname.isEmpty()) {
            name.setError("Required");
            e = 1;
        }
        if (getuser.isEmpty()) {
            user.setError("Required");
            e = 1;
        }
        if (getmail.isEmpty()) {
            mail.setError("Required");
            e = 1;
        }
        if (getpass.isEmpty()) {
            pass.setError("Required");
            e = 1;
        }
        if (getpass.length() < 6 || !Pattern.compile("[0-9]").matcher(getpass).find()) {
            pass.setError("Password Requirements not met");
            e = 1;
        }
        if (getuser.contains(" ")){
            user.setError("Spaces not allowed");
            e = 1;
        }
        if (getname.trim().isEmpty()){
            name.setError("Enter some characters");
            e = 1;
        }
        if (e != 1) {
            loadreg.setVisibility(View.VISIBLE);
            firebase.createUserWithEmailAndPassword(getmail, getpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        user newuser = new user(getname, getuser, studorfac);
                        FirebaseDatabase.getInstance().getReference().child("Users").child(firebase.getCurrentUser().getUid()).setValue(newuser);
                        loadreg.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                        name.setText("");
                        user.setText("");
                        mail.setText("");
                        pass.setText("");
                        firebase.signOut();
                    } else {
                        snackbar = Snackbar.make(reglay, "Error : " + task.getException().getMessage(), Snackbar.LENGTH_INDEFINITE);
                        loadreg.setVisibility(View.GONE);
                        snackbar.setAction("Understood", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                            }
                        }).show();
                    }
                }
            });
        } else {
            Toast.makeText(MainActivity.this,"Error!",Toast.LENGTH_SHORT).show();
        }
    }

    public void loginbtn(View view) throws FirebaseAuthException, IOException {
        String getuserin = userin.getText().toString();
        String getpassin = passin.getText().toString();
        x1 = 0;
        passvisibilitylog.setImageResource(R.drawable.not_visible);
        passin.setTransformationMethod(PasswordTransformationMethod.getInstance());
        if (getuserin.isEmpty()) {
            userin.setError("Required");
            e = 1;
        }
        if (getpassin.isEmpty()) {
            passin.setError("Required");
            e = 1;
        }
        if (e != 1) {
            loadlog.setVisibility(View.VISIBLE);
            firebase.signInWithEmailAndPassword(getuserin, getpassin).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseDatabase.getInstance().getReference().child("Users").child(firebase.getCurrentUser().getUid()).child("usertype").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    snackbar = Snackbar.make(loglay, "Error : " + task.getException().getMessage(), Snackbar.LENGTH_INDEFINITE);
                                    loadlog.setVisibility(View.GONE);
                                    snackbar.setAction("Understood", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            snackbar.dismiss();
                                        }
                                    }).show();
                                }
                                else {
                                    if(String.valueOf(task.getResult().getValue()).equals(studorfac)) {
                                        loadlog.setVisibility(View.GONE);
                                        userin.setText("");
                                        passin.setText("");
                                        finish();
                                        if (String.valueOf(task.getResult().getValue()).equals("Student")) {
                                            intent = new Intent(MainActivity.this, Student.class);
                                        } else {
                                            intent = new Intent(MainActivity.this, Faculty.class);
                                        }
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        loadlog.setVisibility(View.GONE);
                                        snackbar = Snackbar.make(loglay, "Student/Faculty mismatch!", Snackbar.LENGTH_INDEFINITE);
                                        loadlog.setVisibility(View.GONE);
                                        snackbar.setAction("Understood", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                snackbar.dismiss();
                                            }
                                        }).show();
                                    }
                                }
                            }
                        });
                        } else {
                        snackbar = Snackbar.make(reglay, "Error : " + task.getException().getMessage(), Snackbar.LENGTH_INDEFINITE);
                        loadlog.setVisibility(View.GONE);
                        snackbar.setAction("Understood", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                            }
                        }).show();
                    }
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void respass(View view) {
        respass.setTextAppearance(R.style.textclick);
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("RESET PASSWORD");
        dialog.setMessage("Is the email entered on the login sceen correct?");
        String Email = userin.getText().toString();
        if(!Email.isEmpty()){
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    respass.setTextAppearance(R.style.normal);
                    firebase.sendPasswordResetEmail(Email).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(MainActivity.this, "Password reset link sent", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Error - " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setMessage("Enter Email Id");
                maildialog = new EditText(alert.getContext());
                alert.setView(maildialog);
                alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        respass.setTextAppearance(R.style.normal);
                        try {
                            firebase.sendPasswordResetEmail(emailtxt).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(MainActivity.this, "Password reset link sent", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Error - " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        catch (Exception e){
                            Toast.makeText(MainActivity.this,"Error - "+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alert.show();
            }
        });
        dialog.create().show();
    }
}
