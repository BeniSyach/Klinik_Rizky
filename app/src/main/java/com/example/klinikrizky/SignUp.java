package com.example.klinikrizky;

import android.content.Intent;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.klinikrizky.model.Akun;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "signup";
    private TextView tvLoginActivity;
    private Button mTombolSignUp;
    private EditText editTextEmail;
    private EditText editPassword1;
    private EditText editPassword2;
    private EditText editNoKtp;
    private EditText editNoBpjs;
    private EditText editNamaPasien;
    private EditText editTglLahir;
    private EditText editAlamat;
    private EditText editNoTlp;

    private RadioGroup rdgKategori;
    private RadioButton rdbBPJS;
    private RadioButton rdbUmum;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    ProgressBar progressBar1;

    RelativeLayout relativeLayout;
    AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        relativeLayout = findViewById(R.id.relativeLayout);
        animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(2000);

        tvLoginActivity = findViewById(R.id.tv_sudah_punya_akun);
        mTombolSignUp = findViewById(R.id.tombol_signup);

        editNoKtp = findViewById(R.id.no_ktp);
        editNoBpjs = findViewById(R.id.no_bpjs);
        rdgKategori = findViewById(R.id.rdg_kategori);
        rdbBPJS = findViewById(R.id.rdb_bpjs);
        rdbUmum = findViewById(R.id.rdb_umum);

        editNamaPasien = findViewById(R.id.nama_pasien);
        editTglLahir = findViewById(R.id.tgl_lahir);
        editAlamat = findViewById(R.id.alamat_pasien);
        editNoTlp = findViewById(R.id.tlp_pasien);
        editTextEmail = findViewById(R.id.email_signup);
        editPassword1 = findViewById(R.id.password1);
        editPassword2 = findViewById(R.id.password2);
        progressBar1 = findViewById(R.id.progressbar_signup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editNoBpjs.setVisibility(View.GONE);

        rdgKategori.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rdb_bpjs) {
                    rdbUmum.setChecked(false);
                    editNoBpjs.setVisibility(View.VISIBLE);
                    editNoKtp.setVisibility(View.GONE);
                } else {
                    rdbBPJS.setChecked(false);
                    editNoBpjs.setVisibility(View.GONE);
                    editNoKtp.setVisibility(View.VISIBLE);
                }
            }
        });

        tvLoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        mTombolSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    private void registerUser() {
        final String no_ktp = editNoKtp.getText().toString().trim();
        final String no_bpjs = editNoBpjs.getText().toString().trim();
        final String nama_pasien = editNamaPasien.getText().toString().trim();
        final String tgl_lahir = editTglLahir.getText().toString().trim();
        final String alamat_pasien = editAlamat.getText().toString().trim();
        final String no_telpon = editNoTlp.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editPassword1.getText().toString().trim();

        if (rdbBPJS.isChecked()) {
            if (no_bpjs.isEmpty()) {
                editNoBpjs.setError("Wajib diisi!");
                editNoBpjs.requestFocus();
                return;
            }
        }else{
            if (no_ktp.isEmpty()) {
                editNoKtp.setError("Wajib diisi!");
                editNoKtp.requestFocus();
                return;
            }
        }
        if (nama_pasien.isEmpty()) {
            editNamaPasien.setError("Wajib diisi!");
            editNamaPasien.requestFocus();
            return;
        }

        if (tgl_lahir.isEmpty()) {
            editTglLahir.setError("Wajib diisi");
            editTglLahir.requestFocus();
            return;
        }

        if (alamat_pasien.isEmpty()) {
            editAlamat.setError("Wajib diisi!");
            editAlamat.requestFocus();
            return;
        }

        if (no_telpon.isEmpty()) {
            editNamaPasien.setError("Wajib diisi!");
            editNamaPasien.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Wajib diisi!");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Wajib membuat email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editPassword1.setError("Wajib diisi!");
            editPassword1.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editPassword1.setError("Minimum 6 digit password!");
            editPassword1.requestFocus();
            return;
        }

        if (!password.equals(editPassword2.getText().toString())){
            editPassword2.setError("Password tidak sama!");
            editPassword2.requestFocus();
            return;
        }

        progressBar1.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar1.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    CollectionReference dbAkun = db.collection("akun");

                    Akun akun = new Akun(
                            no_ktp,
                            nama_pasien,
                            tgl_lahir,
                            alamat_pasien,
                            email,
                            password, "1",
                            no_telpon, "",
                            no_bpjs
                    );

                    Log.d("", akun.toString());

                    dbAkun.add(akun)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(SignUp.this, "Akun Ditambahkan", Toast.LENGTH_LONG).show();
                                    finish();
                                    startActivity(new Intent(SignUp.this, Login.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                } else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }
    @Override
    protected void onPause() {
        super.onPause();

        if (animationDrawable != null && animationDrawable.isRunning()){
            animationDrawable.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (animationDrawable != null && !animationDrawable.isRunning()){
            animationDrawable.start();
        }
    }
}
