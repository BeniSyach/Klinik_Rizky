package com.example.klinikrizky;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.Manifest.permission.CALL_PHONE;

public class tampil_akun extends AppCompatActivity {
    private TextView nik;
    private TextView nama;
    private TextView email;
    private TextView tglLahir;
    private TextView alamat;
    private TextView notlp;
    private TextView password;

    private FirebaseFirestore db;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    private int REQUEST_READ_CONTACTS = 123;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_akun);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mayRequestContacts();

        nik = findViewById(R.id.nik_profil);
        nama = findViewById(R.id.username_profil);
        email = findViewById(R.id.email_profil);
        tglLahir = findViewById(R.id.tgl_lahir_profil);
        alamat = findViewById(R.id.alamat_profil);
        notlp = findViewById(R.id.notlp_profil);
        password = findViewById(R.id.pass_profil);

        notlp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", notlp.getText()
                        .toString(), null));
                startActivity(intent);
            }
        });

        final String nik2 = getIntent().getStringExtra("nik");
        db.collection("akun").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getData().get("nik").toString().equals(nik2)){

                                    email.setText(document.getData().get("email").toString());
                                    nama.setText(document.getData().get("nama").toString());

                                    if((document.getData().get("nik").toString().isEmpty())){
                                        nik.setText(document.getData().get("bpjs").toString());
                                    } else {
                                        nik.setText(document.getData().get("nik").toString());
                                    }

                                    notlp.setText(document.getData().get("notlp").toString());
                                    password.setText(document.getData().get("password").toString());
                                    tglLahir.setText(document.getData().get("tgl_lahir").toString());
                                    alamat.setText(document.getData().get("alamat").toString());
                                }
                            }
                        } else {
                            Toast.makeText(tampil_akun.this, "gagal menerima data", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(CALL_PHONE)) {
            Snackbar.make(notlp, "Nomor Telepon Untuk Menghubungi", Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{CALL_PHONE}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{CALL_PHONE}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }
}
