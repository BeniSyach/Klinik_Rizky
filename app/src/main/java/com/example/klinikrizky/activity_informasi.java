package com.example.klinikrizky;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class activity_informasi extends AppCompatActivity {
    private ImageView tomboltambah;
    FirebaseFirestore db;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = findViewById(R.id.recycleview1);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        db = FirebaseFirestore.getInstance();


        tomboltambah = findViewById(R.id.prefences);
        tomboltambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent datapasien = new Intent(activity_informasi.this, ActivityTambahInformasi.class);
                startActivity(datapasien);
            }
        });

        if (tools.getSharedPreferenceString(this, "level", "").equals("1")) {
            tomboltambah.setVisibility(View.GONE);
        } else {

        }

        getData();

    }


    @Override
    protected void onResume() {
        super.onResume();

        getData();
    }

    //membuat tulisan menjadi ganti secara otomatis
    private void getData() {
        final CollectionReference dbAkun = db.collection("informasi");
        dbAkun.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    final ArrayList<String> data = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        data.add(document.getData().get("konten").toString());
                    }

                    RecyclerView.Adapter mAdapter = new informasi_adapter(data);
                    mRecyclerView.setAdapter(mAdapter);

                } else {
                    Toast.makeText(activity_informasi.this, "Gagal Menerima Data", Toast.LENGTH_SHORT).show();

                }
            }

        });
    }
}



