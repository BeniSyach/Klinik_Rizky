package com.example.klinikrizky;


import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.klinikrizky.model.Akun;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class tambah_data_pasien extends AppCompatActivity {
    private Button mSave;
    private Button mKembali;
    private EditText editTextEmail;
    private EditText editPassword1;
    private EditText editPassword2;
    private EditText editNoKtp;
    private EditText editNoBpjs;
    private EditText editNamaPasien;
    private EditText editTglLahir;
    private EditText editAlamat;
    private EditText editNoTlp;

    private LinearLayout ly_bpjs;
    private LinearLayout ly_nik;

    private RadioGroup rdgKategori;
    private RadioButton rdbBPJS;
    private RadioButton rdbUmum;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data_pasien);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSave = findViewById(R.id.btn_savedata);
        mKembali = findViewById(R.id.btnkembali);

        editNoBpjs = findViewById(R.id.edt_bpjsPas);
        editNoKtp = findViewById(R.id.edt_nikPas);
        rdgKategori = findViewById(R.id.rdg_kategori);
        rdbBPJS = findViewById(R.id.rdb_bpjs);
        rdbUmum = findViewById(R.id.rdb_umum);

        ly_bpjs = findViewById(R.id.bpjs_layout);
        ly_nik = findViewById(R.id.nik_layout);

        editNamaPasien = findViewById(R.id.edt_namaPas);
        editTglLahir = findViewById(R.id.edt_tglLahirPas);
        editAlamat = findViewById(R.id.edt_alamatPas);
        editNoTlp = findViewById(R.id.edit_teleponPas);
        editTextEmail = findViewById(R.id.edit_emailPas);
        editPassword1 = findViewById(R.id.edit_passwordPas);
        editPassword2 = findViewById(R.id.edit_konfPasswordPas);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ly_bpjs.setVisibility(View.GONE);

        rdgKategori.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rdb_bpjs) {
                    rdbUmum.setChecked(false);
                    ly_bpjs.setVisibility(View.VISIBLE);
                    ly_nik.setVisibility(View.GONE);
                } else {
                    rdbBPJS.setChecked(false);
                    ly_bpjs.setVisibility(View.GONE);
                    ly_nik.setVisibility(View.VISIBLE);
                }
            }
        });

        mKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
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

        if (!password.equals(editPassword2.getText().toString())) {
            editPassword2.setError("Password tidak sama!");
            editPassword2.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    CollectionReference dbAkun = db.collection("akun");

                    Akun akun = new Akun(
                            no_ktp,
                            nama_pasien,
                            tgl_lahir,
                            alamat_pasien,
                            email,
                            password, "1",
                            no_telpon, "", no_bpjs
                    );

                    Log.d("", akun.toString());

                    dbAkun.add(akun)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(tambah_data_pasien.this, "Akun Ditambahkan", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(tambah_data_pasien.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
}
