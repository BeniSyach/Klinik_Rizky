package com.example.klinikrizky;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.klinikrizky.model.Pelayanan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Objects;

public class DetailPoli extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button mSave;
    private Button mKembali;
    private EditText editIdDokter;
    private EditText editNamaDokter;
    private EditText editSenkam;
    private EditText editJumat;
    private EditText editSabtu;
    boolean isUpdate = false;

    Spinner spinner;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_poli);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSave = findViewById(R.id.btn_savedata);
        mKembali = findViewById(R.id.btnkembali);
        editIdDokter = findViewById(R.id.edt_idDok);
        editNamaDokter = findViewById(R.id.edt_namaDok);
        editSenkam = findViewById(R.id.edt_senkam);
        editJumat = findViewById(R.id.edt_jumat);
        editSabtu = findViewById(R.id.edt_sabtu);
        spinner = findViewById(R.id.spinner1);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        mKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerDokter();
            }
        });

        db = FirebaseFirestore.getInstance();
        db.collection("poli")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> data = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(String.valueOf("bbb"), document.getId() + " => " + document.getData());
                                data.add(document.getData().get("nama").toString());
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(DetailPoli.this,
                                        android.R.layout.simple_list_item_1, android.R.id.text1, data);
//                                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(jadwal.this, data, android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(adapter);
                                spinner.setOnItemSelectedListener(DetailPoli.this);
                            }
                        } else {
                            Log.w(String.valueOf("aaa"), "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    private void registerDokter() {
        final String iddokter = editIdDokter.getText().toString().trim();
        final String namadokter = editNamaDokter.getText().toString().trim();
        final String senkam = editSenkam.getText().toString().trim();
        final String jumat = editJumat.getText().toString().trim();
        final String sabtu = editSabtu.getText().toString().trim();


        if (iddokter.isEmpty()) {
            editIdDokter.setError("Wajib diisi!");
            editIdDokter.requestFocus();
            return;
        }

        if (namadokter.isEmpty()) {
            editNamaDokter.setError("Wajib diisi!");
            editNamaDokter.requestFocus();
            return;
        }

        if (senkam.isEmpty()) {
            editSenkam.setError("Wajib diisi!");
            editSenkam.requestFocus();
            return;
        }

        if (jumat.isEmpty()) {
            editJumat.setError("Wajib diisi!");
            editJumat.requestFocus();
            return;
        }

        if (sabtu.isEmpty()) {
            editSabtu.setError("Wajib diisi");
            editSabtu.requestFocus();
            return;
        }

        final CollectionReference dbPelayanan = db.collection("pelayanan");

        final Pelayanan pelayanan = new Pelayanan(
                iddokter,
                namadokter,
                senkam,
                jumat,
                sabtu,
                spinner.getSelectedItem().toString()
        );

        Log.d("", pelayanan.toString());

        dbPelayanan.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.contains("poli") && document.get("poli").toString().equals(spinner.getSelectedItem().toString())){
                                isUpdate = true;
                                Toast.makeText(DetailPoli.this, "Informasi telah di perbaharui",
                                        Toast.LENGTH_LONG).show();
                                dbPelayanan.document(document.getId()).set(pelayanan, SetOptions.merge());
                                finish();
                        }
                    }

                    if (!isUpdate) {//databaru
                        //ngesafe ke firebase nya
                        dbPelayanan.add(pelayanan)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(DetailPoli.this, "Informasi telah di tambahkan",
                                                Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(DetailPoli.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }

                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
