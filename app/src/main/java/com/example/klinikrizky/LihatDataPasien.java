package com.example.klinikrizky;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.klinikrizky.model.Akun;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class LihatDataPasien extends AppCompatActivity {
    private ImageView btnEditPas;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter mAdapter;
    RelativeLayout relativeLihatDataPasien;

    private FirebaseFirestore db;
    List<Akun> akunwes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_data_pasien);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = findViewById(R.id.recycleview1);
        btnEditPas = findViewById(R.id.prefences);
        relativeLihatDataPasien = findViewById(R.id.relativeLihatDataPasien);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);

        btnEditPas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent poliklinik = new Intent(LihatDataPasien.this, tambah_data_pasien.class);
                startActivity(poliklinik);
            }
        });

        db = FirebaseFirestore.getInstance();
        db.collection("akun")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            final ArrayList<String> data = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getData().get("level").toString().equals("1")) {
                                    data.add(document.getData().get("nama").toString());
                                }
                                db.collection("akun")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    akunwes = new ArrayList<>();
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Akun akun = new Akun(
                                                                document.getData().get("nik").toString(),
                                                                document.getData().get("nama").toString(),
                                                                document.getData().get("tgl_lahir").toString(),
                                                                document.getData().get("alamat").toString(),
                                                                document.getData().get("email").toString(),
                                                                document.getData().get("password").toString(),
                                                                document.getData().get("level").toString(),
                                                                document.getData().get("notlp").toString(),
                                                                document.getData().get("foto").toString(),
                                                                document.getData().get("bpjs").toString()
                                                        );
                                                        akunwes.add(akun);
                                                    }

                                                    mAdapter = new LihatDataPasien2(data, LihatDataPasien.this, relativeLihatDataPasien, akunwes);
                                                    mRecyclerView.setAdapter(mAdapter);

                                                } else {

                                                }
                                            }
                                        });

                            }
                        } else {
                            Log.w(String.valueOf("aaa"), "Error getting documents.", task.getException());
                        }
                    }
                });

    }

};


