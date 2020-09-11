package com.example.klinikrizky;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class nomor_antrian extends AppCompatActivity {
    private TextView rngPeriksa;
    private TextView nmrAntrian;
    private TextView noKtp;
    private TextView namaPasien;
    private TextView waktu;

    String poli;
    String nomor;
    String id;
    String nmPasien;
    String wkt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nomor_antrian);
        poli = getIntent().getStringExtra("poli");
        wkt = getIntent().getStringExtra("waktu");
        id= getIntent().getStringExtra("id");
        nmPasien = getIntent().getStringExtra("nama");

        rngPeriksa = findViewById(R.id.tv_ruangperiksa);
        nmrAntrian = findViewById(R.id.tv_nomorAntrian);
        noKtp = findViewById(R.id.tv_noKtp);
        namaPasien = findViewById(R.id.tv_namaPasien);
        waktu = findViewById(R.id.tv_waktu);

        rngPeriksa.setText(poli);
        waktu.setText(wkt);
        namaPasien.setText(nmPasien);
        noKtp.setText(id);
        nmrAntrian.setText(getIntent().getStringExtra("nomor"));

    }
}
