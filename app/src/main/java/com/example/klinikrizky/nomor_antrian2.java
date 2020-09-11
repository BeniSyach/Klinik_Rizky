package com.example.klinikrizky;


import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.klinikrizky.model.NomorAntrianModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class nomor_antrian2 extends AppCompatActivity {

    private TextView rngPeriksa;
    private TextView nmrAntrian;
    private TextView noKtp;
    private TextView namaPasien;
    private TextView waktu;

    String poli;
    String nomor;
    String nik;
    String nmPasien;
    String wkt;
    FirebaseDatabase database;
    String date="";
    NomorAntrianModel  model ;
    NomorAntrianModel  modelNow ;
    DatabaseReference myRefdbRealtimeNomorKita;
    DatabaseReference myRefdbRealtimeNomorLain;
    ValueEventListener listenerNomorKita;
    ValueEventListener listenerNomorLain;

    TextView tv_nomor_anda,tv_nomor_now, tv_ruangperiksa_anda, tv_waktu_anda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nomor_antrian2);
        poli = getIntent().getStringExtra("poli");
        wkt = getIntent().getStringExtra("waktu");


        tv_nomor_anda = findViewById(R.id.tv_nomor_anda);
        tv_nomor_now= findViewById(R.id.tv_nomor_now);
        tv_ruangperiksa_anda= findViewById(R.id.tv_ruangperiksa_anda);
        tv_waktu_anda= findViewById(R.id.tv_waktu_anda);


        database = FirebaseDatabase.getInstance();
        SimpleDateFormat format2= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            format2 =new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.forLanguageTag("in"));
        }
        date= format2.format(new Date());

        getNomorAntrianAnda();
        getNomorAntrianLain();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disConnectReailtimeDB();
    }

    boolean isfound=false;

    private void disConnectReailtimeDB(){

        if (myRefdbRealtimeNomorKita != null && listenerNomorKita != null) {
            myRefdbRealtimeNomorKita.removeEventListener(listenerNomorKita);
        }

        if (myRefdbRealtimeNomorLain != null && listenerNomorLain != null) {
            myRefdbRealtimeNomorLain.removeEventListener(listenerNomorLain);
        }


    }

    private void getNomorAntrianAnda(){
        isfound=false;
        myRefdbRealtimeNomorKita = database.getReference(date);


        listenerNomorKita = myRefdbRealtimeNomorKita. addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    Log.d("aaa", "onDataChange: ");
                    Log.d("aaa", "onDataChange: ");
                    if (userSnapshot.getChildrenCount()>0){
                        if (userSnapshot.child("status").getValue().toString().equals("false") &&
                                userSnapshot.child("nik").getValue().toString()
                                        .equals(tools.getSharedPreferenceString(nomor_antrian2
                                                .this, "nik", ""))){
                            isfound=true;
                            model = new NomorAntrianModel(
                                    userSnapshot.child("nik").getValue().toString(),
                                    userSnapshot.child("nomor").getValue().toString(),
                                    userSnapshot.child("nama").getValue().toString(),
                                    userSnapshot.child("poli").getValue().toString(),
                                    userSnapshot.child("waktu").getValue().toString(),
                                    Boolean.getBoolean(userSnapshot.child("status").getValue().toString())
                            );
                            tv_nomor_anda  .setText(model.getNomor());
                            tv_ruangperiksa_anda  .setText(model.getPoli());
                            tv_waktu_anda  .setText(model.getWaktu());

                            break;
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


    }

    private void getNomorAntrianLain(){
        isfound=false;
        myRefdbRealtimeNomorLain = database.getReference(date);


        listenerNomorLain = myRefdbRealtimeNomorLain. addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                isfound=false;

                int index=0;
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    index ++;
                    Log.d("aaa", "onDataChange: ");
                    Log.d("aaa", "onDataChange: ");
                    if (userSnapshot.getChildrenCount()>0){
                        if (userSnapshot.child("status").getValue().toString().equals("false")) {
                            isfound=true;

                        }else {
                            if (index ==dataSnapshot.getChildrenCount()){
                                isfound =true;
                            }
                        }



                        if (isfound){
                            modelNow = new NomorAntrianModel(
                                    userSnapshot.child("nik").getValue().toString(),
                                    userSnapshot.child("nomor").getValue().toString(),
                                    userSnapshot.child("nama").getValue().toString(),
                                    userSnapshot.child("poli").getValue().toString(),
                                    userSnapshot.child("waktu").getValue().toString(),
                                    Boolean.getBoolean(userSnapshot.child("status").getValue().toString())
                            );

                            break;
                        }
                    }
                }

                if (isfound){
                    tv_nomor_now.setText(modelNow.getNomor());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


    }

}
