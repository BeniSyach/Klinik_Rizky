package com.example.klinikrizky;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.klinikrizky.model.Akun;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.io.IOException;

public class profile extends AppCompatActivity {
    private TextView nik;
    private TextView nama;
    private TextView email;
    private TextView tglLahir;
    private TextView alamat;
    private TextView notlp;
    private TextView password;
    private ImageView avatar;
    private ImageView avatarMini;

    private FirebaseFirestore db;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;

    private int PICK_IMAGE_REQUEST = 123;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {

                uploadImage();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        nik = findViewById(R.id.nik_profil);
        nama = findViewById(R.id.username_profil);
        email = findViewById(R.id.email_profil);
        tglLahir = findViewById(R.id.tgl_lahir_profil);
        alamat = findViewById(R.id.alamat_profil);
        notlp = findViewById(R.id.notlp_profil);
        password = findViewById(R.id.pass_profil);

        avatar = findViewById(R.id.avatar_profil);
        avatarMini = findViewById(R.id.avatar_mini);

        avatar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                return false;
            }
        });
        avatarMini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                return;
            }
        });

        Log.d("email lagi", tools.getSharedPreferenceString
                (profile.this, "email", ""));
        db.collection("akun").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("email", document.getData().get("email").toString());

                                if (document.getData().get("email").toString().equals(tools.getSharedPreferenceString
                                        (profile.this, "email", ""))){

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
                                    loadImage(document.getData().get("foto").toString());

                                }
                            }
                        } else {
                            Toast.makeText(profile.this, "gagal menerima data", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void uploadImage() {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/"+ (nik.getText().toString()));

            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final Uri downloadUrl = uri;

                            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                            final CollectionReference complaintsRef = rootRef.collection("akun");
                            complaintsRef.whereEqualTo("nik", nik.getText().toString())
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String no_nik = "";
                                            String no_bpjs ="";

                                            if((document.getData().get("nik").toString().isEmpty())){
                                                no_bpjs = (document.getData().get("bpjs").toString());
                                            } else {
                                                no_nik = (document.getData().get("nik").toString());
                                            }

                                            Akun akun = new Akun(
                                                    no_nik,
                                                    nama.getText().toString(),
                                                    tglLahir.getText().toString(),
                                                    alamat.getText().toString(),
                                                    email.getText().toString(),
                                                    password.getText().toString(), document.getData().get("level").toString(),
                                                    notlp.getText().toString(), downloadUrl.toString(), no_bpjs
                                            );

                                            complaintsRef.document(document.getId()).set(akun, SetOptions.merge());

                                            progressDialog.dismiss();
                                            tools.setSharedPreference(profile.this,"foto",downloadUrl.toString());
                                            Toast.makeText(profile.this, "Uploaded" +downloadUrl, Toast.LENGTH_SHORT).show();
                                            loadImage(downloadUrl.toString());
                                        }
                                    }
                                }
                            });
                        }
                    });



                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(profile.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    private void loadImage(String url){
        if (!url.isEmpty()) {
            Picasso.get()
                    .load(url)
                    .fit()
                    .centerCrop()
                    .into(avatar);
            }
        }
    }
