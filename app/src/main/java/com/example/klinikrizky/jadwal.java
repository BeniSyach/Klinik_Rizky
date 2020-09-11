package com.example.klinikrizky;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.klinikrizky.model.Akun;
import com.example.klinikrizky.model.NomorAntrianModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class jadwal extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button btnLihatNomor;
    Button btnlanjut;
    Spinner spinner;
    Spinner spinnerPasien;
    RelativeLayout relativeProgress;
    TextView date1;
    LinearLayout linearPasien;
    private int year, month, day;

    private FirebaseFirestore dbFirestore;
    DatabaseReference myRefdbRealtime;
    ValueEventListener listener;
    boolean cekSekali = false;

    String nik = "";
    String nama = "";
    String bpjs = "";
    ArrayList<String> dataAkun = new ArrayList<>();
    ArrayList<String> dataNIK = new ArrayList<>();
    ArrayList<String> dataBPJS = new ArrayList<>();
    String id;
    boolean isExecute = true;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disConnectReailtimeDB();
    }

    private void disConnectReailtimeDB() {
        if (myRefdbRealtime != null && listener != null) {
            myRefdbRealtime.removeEventListener(listener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal);
        btnlanjut = findViewById(R.id.btnjadi);
        spinner = findViewById(R.id.spinner1);
        date1 = findViewById(R.id.date1);
        btnLihatNomor = findViewById(R.id.btnLihatNomor);
        linearPasien = findViewById(R.id.linearPasien);
        spinnerPasien = findViewById(R.id.spinnerPasien);
        relativeProgress = findViewById(R.id.relativeProgress);

        nik = tools.getSharedPreferenceString(jadwal.this, "nik", "");

        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        if (tools.getSharedPreferenceString(this, "level", "").equals("0")) {
            btnLihatNomor.setVisibility(View.GONE);
        }

        if (tools.getSharedPreferenceString(this, "level", "").equals("1")) {
            linearPasien.setVisibility(View.GONE);
        } else {

            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
            final CollectionReference complaintsRef = rootRef.collection("akun");
            complaintsRef.whereEqualTo("level", "1")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        dataAkun = new ArrayList<>();
                        dataNIK = new ArrayList<>();
                        dataBPJS = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            dataAkun.add(document.getData().get("nama").toString());
                            dataNIK.add(document.getData().get("nik").toString());
                            dataBPJS.add(document.getData().get("bpjs").toString());
                        }

                        if (dataAkun.size() > 0) {
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(jadwal.this,
                                    android.R.layout.simple_list_item_1, android.R.id.text1, dataAkun);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerPasien.setAdapter(adapter);
                            spinnerPasien.setOnItemSelectedListener(jadwal.this);

                            spinnerPasien.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    nik = dataNIK.get(position);
                                    bpjs = dataBPJS.get(position);
                                    nama = dataAkun.get(position);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }
                    }
                }
            });
        }

        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });
        btnLihatNomor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent datapasien = new Intent(jadwal.this, nomor_antrian2.class);
                startActivity(datapasien);

            }
        });

        btnlanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (date1.getText().toString().isEmpty()) {
                    Toast.makeText(jadwal.this, "Tanggal belum ditentukan!", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                myRefdbRealtime = database.getReference(date1.getText().toString());


//                Read from the database
                listener = myRefdbRealtime.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        isExecute = true;
                        if (linearPasien.getVisibility() == View.VISIBLE) {
                            nama = jadwal.this.nama;
                            if (jadwal.this.bpjs.isEmpty()) {
                                id = jadwal.this.nik;
                            } else {
                                id = jadwal.this.bpjs;
                            }
                        } else {
                            if (tools.getSharedPreferenceString(jadwal.this, "bpjs", "").isEmpty()) {
                                id = tools.getSharedPreferenceString(jadwal.this, "nik", "");
                            } else {
                                id = tools.getSharedPreferenceString(jadwal.this, "bpjs", "");
                            }
                            nama = tools.getSharedPreferenceString(jadwal.this, "nama", "");
                        }

                        if (!cekSekali) {
                            cekSekali = true;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (snapshot.getChildrenCount() > 0) {
                                    if (snapshot.child("nik").getValue().toString().equals(id) &&
                                            snapshot.child("status").getValue().toString().equals("false")) {
                                        isExecute = false;
                                    }
                                }
                            }

                            if (isExecute) {
                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                final Intent lanjut = new Intent(jadwal.this, nomor_antrian.class);
                                int nomor = (int) (dataSnapshot.getChildrenCount());

                                if (nomor == 0) {
                                    nomor = 1;
                                } else {
                                    nomor++;
                                }

                                //////////////////////

                                dbFirestore = FirebaseFirestore.getInstance();
                                final int finalNomor = nomor;
                                dbFirestore.collection("limit")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {

                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Log.d(String.valueOf("bbb"), document.getId() + " => " + document.getData());
                                                        if (finalNomor <= (Integer.parseInt(document.getData().get("nomor").toString()))) {

                                                            DatabaseReference myRef = database.getReference(date1.getText().toString() + "/" +
                                                                    String.valueOf(finalNomor) + "-"
                                                                    + id);

                                                            NomorAntrianModel nomorAntrianModel = new NomorAntrianModel(
                                                                    id,
                                                                    String.valueOf(finalNomor),
                                                                    nama,
                                                                    spinner.getSelectedItem().toString(),
                                                                    date1.getText().toString(), false
                                                            );

                                                            lanjut.putExtra("poli", spinner.getSelectedItem().toString());
                                                            lanjut.putExtra("waktu", date1.getText().toString());
                                                            lanjut.putExtra("nomor", String.valueOf(finalNomor));
                                                            lanjut.putExtra("nama", nama);
                                                            lanjut.putExtra("id", id);

                                                            myRef.setValue(nomorAntrianModel);

                                                            CollectionReference dbAkun = FirebaseFirestore.getInstance().collection("antri");

                                                            NomorAntrianModel akun = new NomorAntrianModel(
                                                                    id,
                                                                    String.valueOf(finalNomor),
                                                                    nama,
                                                                    spinner.getSelectedItem().toString(),
                                                                    date1.getText().toString(), false
                                                            );

                                                            Log.d("", akun.toString());

                                                            dbAkun.add(akun)
                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentReference documentReference) {
                                                                            finish();
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(jadwal.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                                        }
                                                                    });

                                                            startActivity(lanjut);
                                                            finish();

                                                            break;
                                                        } else {
                                                            Toast.makeText(jadwal.this, "Kuota Sudah Penuh, Silahkan Tentukan Jadwal Lain", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }

                                                } else {
                                                    Log.w(String.valueOf("aaa"), "Error getting documents.", task.getException());
                                                }
                                            }
                                        });


                            } else {
                                cekSekali = false;
                                Toast.makeText(jadwal.this, "Nomor Antrian Anda Belum Terselesaikan", Toast.LENGTH_SHORT).show();
                                disConnectReailtimeDB();
                                return;
                            }
                        }
                        disConnectReailtimeDB();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        cekSekali = false;
                        disConnectReailtimeDB();
                    }
                });
            }
        });

        dbFirestore = FirebaseFirestore.getInstance();
        dbFirestore.collection("poli")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> data = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                data.add(document.getData().get("nama").toString());
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(jadwal.this,
                                        android.R.layout.simple_list_item_1, android.R.id.text1, data);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(adapter);
                                spinner.setOnItemSelectedListener(jadwal.this);
                            }
                        } else {
                            Log.w(String.valueOf("aaa"), "Error getting documents.", task.getException());
                        }
                    }
                });



        SimpleDateFormat format2= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            format2 =new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.forLanguageTag("in"));
        }
        String date= format2.format(new Date());


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefdbRealtime = database.getReference(date);

        listener = myRefdbRealtime.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isExecute = true;
                if (linearPasien.getVisibility() == View.VISIBLE) {
                    nama = jadwal.this.nama;
                    if (jadwal.this.bpjs.isEmpty()) {
                        id = jadwal.this.nik;
                    } else {
                        id = jadwal.this.bpjs;
                    }
                } else {
                    if (tools.getSharedPreferenceString(jadwal.this, "bpjs", "").isEmpty()) {
                        id = tools.getSharedPreferenceString(jadwal.this, "nik", "");
                    } else {
                        id = tools.getSharedPreferenceString(jadwal.this, "bpjs", "");
                    }
                    nama = tools.getSharedPreferenceString(jadwal.this, "nama", "");
                }

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getChildrenCount() > 0) {
                        Log.d("aaa", "onDataChange: ");

                        if (snapshot.child("nik").getValue().toString().equals(id) &&
                                snapshot.child("status").getValue().toString().equals("false")) {
                            isExecute = false;
                        }
                    }
                }

                if (isExecute) {
                    btnLihatNomor.setVisibility(View.GONE);
                    disConnectReailtimeDB();
                } else {
                    btnLihatNomor.setVisibility(View.VISIBLE);
                    disConnectReailtimeDB();
                    return;
                }
                disConnectReailtimeDB();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                disConnectReailtimeDB();
            }
        });
    }

    boolean hariiniusai = false;
    boolean isjumat = false;
    boolean isminggu = false;
    boolean issabtu = false;

    DatePickerDialog datePickerDialog;

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        datePickerDialog = new DatePickerDialog(this, datePickerListener, year, month, day);
        hariiniusai = false;
        isjumat = false;
        issabtu = false;
        isminggu = false;

        SimpleDateFormat format2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            format2 = new SimpleDateFormat("EEEE", Locale.forLanguageTag("in"));
        }
        if (format2.format(new Date()).toLowerCase().equals("jumat")) {
            isjumat = true;
        } else if (format2.format(new Date()).toLowerCase().equals("sabtu")) {
            issabtu = true;
        } else if (format2.format(new Date()).toLowerCase().equals("minggu")) {
            isminggu = true;
        }

        relativeProgress.setVisibility(View.VISIBLE);

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        final CollectionReference complaintsRef = rootRef.collection("pelayanan");
        complaintsRef.whereEqualTo("poli", spinner.getSelectedItem().toString())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String weekday = "";

                        if (isminggu) {
                            hariiniusai = true;
                            Toast.makeText(jadwal.this, "tidak ada pendaftaran dihari minggu", Toast.LENGTH_SHORT).show();
                            break;
                        }

                        if (!(document.getData().get("senkam").toString().isEmpty())
                                || !(document.getData().get("sabtu").toString().isEmpty())
                                || !(document.getData().get("jumat").toString().isEmpty())
                        ) {

                            if (isjumat) {
                                weekday = document.getData().get("jumat").toString();
                            } else if (issabtu) {
                                weekday = document.getData().get("sabtu").toString();
                            } else {
                                weekday = document.getData().get("senkam").toString();
                            }

                            if (weekday.contains("-")) {

                                String[] split = weekday.split("-");

                                if (split.length > 1 && !split[0].isEmpty() && !split[1].isEmpty()) {
                                    String strTimeStart = split[0].trim();
                                    String strTimeEnd = split[1].trim();
                                    DateFormat dateFormat = new SimpleDateFormat("HH.mm");
                                    Date dEnd = null;
                                    Date dStart = null;
                                    try {
                                        dStart = dateFormat.parse(strTimeStart);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        dEnd = dateFormat.parse(strTimeEnd);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if (dStart == null) {
                                        Toast.makeText(jadwal.this, "Format weekday mulai kurang sesuai", Toast.LENGTH_SHORT).show();
                                        break;
                                    } else if (dEnd == null) {
                                        Toast.makeText(jadwal.this, "Format weekday selesai kurang sesuai", Toast.LENGTH_SHORT).show();
                                        break;
                                    } else {

                                        DateFormat format = new SimpleDateFormat("HH.mm");
                                        Date date = new Date();

                                        Date dateCompare = null;
                                        try {
                                            dateCompare = dateFormat.parse(format.format(date));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        if (!dateCompare.before(dEnd)) {
                                            hariiniusai = true;
                                            Toast.makeText(jadwal.this, "Pendaftaran hari ini usai", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    }
                                } else {
                                    Toast.makeText(jadwal.this, "Format weekday kurang sesuai", Toast.LENGTH_SHORT).show();
                                    break;
                                }

                            } else {

                                Toast.makeText(jadwal.this, "Tidak ada weekday pelayanan pada poli ini", Toast.LENGTH_SHORT).show();
                                break;
                            }

                        } else {
                            Toast.makeText(jadwal.this, "Jam Kosong", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }

                if (!hariiniusai) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date());
                    c.add(Calendar.DATE, 1);
                    datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                } else {
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date());
                    c.add(Calendar.DATE, 1);
                    datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                    datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                }

                datePickerDialog.show();
                relativeProgress.setVisibility(View.GONE);
            }
        });
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            day = selectedDay;
            month = selectedMonth;
            year = selectedYear;
            String date = (selectedDay + " / " + (selectedMonth + 1) + " / "
                    + selectedYear);

            SimpleDateFormat format = new SimpleDateFormat("dd / MM / yyyy", Locale.ENGLISH);
            Date newDate = null;
            try {
                newDate = format.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //pengaturan output tanggal di layout jadwal
            SimpleDateFormat format2 = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                format2 = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.forLanguageTag("in"));
            }
            String s = format2.format(newDate);
            date1.setText(s);

        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}