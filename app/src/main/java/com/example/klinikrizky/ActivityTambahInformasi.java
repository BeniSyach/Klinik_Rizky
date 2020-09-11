package com.example.klinikrizky;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ActivityTambahInformasi extends AppCompatActivity {
    private EditText Informasi;
    private Button SaveDataInformasi;
    private Button KembaliInformasi;
    private FirebaseFirestore db;
    boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_informasi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Informasi = findViewById(R.id.edit_informasi);
        SaveDataInformasi = findViewById(R.id.btn_savedatainformasi);

        KembaliInformasi = findViewById(R.id.btn_kembaliinformasi);

        KembaliInformasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        db = FirebaseFirestore.getInstance();
        SaveDataInformasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Informasi.getText().toString().isEmpty()) {
                    Toast.makeText(ActivityTambahInformasi.this, "Tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                final CollectionReference dbAkun = db.collection("informasi");

                final Map<String, Object> informasi = new HashMap<>();
                informasi.put("konten", Informasi.getText().toString());

                //update
                dbAkun.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                isUpdate = true;
                                Toast.makeText(ActivityTambahInformasi.this, "Informasi telah di perbaharui",
                                        Toast.LENGTH_LONG).show();
                                dbAkun.document(document.getId()).set(informasi, SetOptions.merge());
                                finish();
                            }

                            if (!isUpdate) {//insertdata
                                //ngesafe ke firebase nya
                                dbAkun.add(informasi)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(ActivityTambahInformasi.this, "Informasi telah di tambahkan",
                                                        Toast.LENGTH_LONG).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ActivityTambahInformasi.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }

                        }
                    }
                });


            }
        });

    }
}
