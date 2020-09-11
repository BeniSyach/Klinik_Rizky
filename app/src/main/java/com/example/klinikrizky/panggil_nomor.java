package com.example.klinikrizky;

import android.speech.tts.TextToSpeech;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.klinikrizky.model.NomorAntrianModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class panggil_nomor extends AppCompatActivity {
    private static final String TAG ="panggil_nomor" ;
    private TextView mNomorAntrian;
    private LinearLayout mplayBtn;
    private LinearLayout mnextBtn;
    private ImageView btn_balek;
    private TextToSpeech textToSpeech;

    String date = "";
    NomorAntrianModel model;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ValueEventListener listener;

    @Override
    public void onBackPressed() {
        textToSpeech.stop();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panggil_nomor);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNomorAntrian = findViewById(R.id.nomor_antrian);
        mplayBtn = findViewById(R.id.btn_play);
        mnextBtn = findViewById(R.id.btn_next);
        database = FirebaseDatabase.getInstance();
        model = new NomorAntrianModel();


        btn_balek = findViewById(R.id.btnbalek);
        btn_balek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        SimpleDateFormat format2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            format2 = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.forLanguageTag("in"));
        }
        date = format2.format(new Date());

        getNomorAntrian(false);

        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        int ttsLang = 0;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            ttsLang = textToSpeech.setLanguage(Locale.forLanguageTag("in-ID"));
                        }
                        if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                                || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Log.e("TTS", "The Language is not supported!");
                        } else {
                            Log.i("TTS", "Language Supported.");
                        }
                        Log.i("TTS", "Initialization success.");
                    } else {
                        Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        mplayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!model.getNik().isEmpty()) {
                    String data = "panggilan nomor antrian " + mNomorAntrian.getText().toString();
                    Log.i("TTS", "button clicked: " + data);
                    int speechStatus = textToSpeech.speak(data, TextToSpeech.QUEUE_ADD, null);
                    if (speechStatus == TextToSpeech.ERROR) {
                        Log.e("TTS", "Error in converting Text to Speech!");
                    }
                } else {
                    Toast.makeText(panggil_nomor.this, "Tidak ada antrian hari ini", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mnextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!model.getNik().isEmpty()) {
                    DatabaseReference myRef = database.getReference(date + "/" +
                            model.getNomor() + "-" + model.getNik());

                    NomorAntrianModel nomorAntrianModel = new NomorAntrianModel(
                            model.getNik(),
                            String.valueOf(model.getNomor()),
                            model.getNama(),
                            model.getPoli(),
                            model.getWaktu(), true
                    );
                    myRef.setValue(nomorAntrianModel, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError,
                                                       @NonNull DatabaseReference databaseReference) {
                                    getNomorAntrian(true);
                                }
                            }
                    );
                } else {
                    Toast.makeText(panggil_nomor.this, "Tidak ada antrian hari ini", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disConnectReailtimeDB();
    }

    private void disConnectReailtimeDB() {

        if (myRef != null && listener != null) {
            myRef.removeEventListener(listener);
        }
    }

    boolean isfound = false;

    private void getNomorAntrian(final boolean useSound) {
        isfound = false;
        myRef = database.getReference(date);

        listener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    if (userSnapshot.hasChild("status")) {
                        if (userSnapshot.getChildrenCount() > 5) {
                            if (userSnapshot.child("status").getValue().toString().equals("false")) {
                                isfound = true;
                                model = new NomorAntrianModel(
                                        userSnapshot.child("nik").getValue().toString(),
                                        userSnapshot.child("nomor").getValue().toString(),
                                        userSnapshot.child("nama").getValue().toString(),
                                        userSnapshot.child("poli").getValue().toString(),
                                        userSnapshot.child("waktu").getValue().toString(),
                                        Boolean.getBoolean(userSnapshot.child("status").getValue().toString())
                                );
                                mNomorAntrian.setText(model.getNomor());

                                if (useSound) {
                                    mplayBtn.performClick();
                                }
                                break;
                            }
                        }
                    }
                }

                if (!isfound) {
                    Toast.makeText(panggil_nomor.this, "Tidak ada antrian lagi", Toast.LENGTH_SHORT).show();
                }
                disConnectReailtimeDB();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                disConnectReailtimeDB();
            }
        });
    }
}