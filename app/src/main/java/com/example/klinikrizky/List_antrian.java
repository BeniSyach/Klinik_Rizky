package com.example.klinikrizky;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class List_antrian extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<HashMap<String, Object>> myDataset = new ArrayList<>();
    private Map<String, Object> mydataset1 = new HashMap<>();
    private FirebaseFirestore lvfirestore;
    private ImageView cetak;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan__pasien);
        webView=findViewById(R.id.webview);

        cetak=findViewById(R.id.cetak_laporan);
        cetak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent datapasien = new Intent(List_antrian.this, cetak_laporan.class);
                startActivity(datapasien);
            }
        });

        lvfirestore = FirebaseFirestore.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        lvfirestore.collection("antri").orderBy("nama", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<Map<String, Object>> list = new ArrayList<>();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    list.add(document.getData());
                    Log.e("abc", document.getData().get("nama").toString());
                }
                mAdapter = new MyAdapter(list);
                recyclerView.setAdapter(mAdapter);
            }
        });


    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView nik;
            private TextView nama;
            private TextView nomor;
            private TextView waktu;
            private TextView poli;
            private TextView status;

            public ViewHolder(View itemView) {
                super(itemView);
                nik = itemView.findViewById(R.id.tv_noKtp);
                nama = itemView.findViewById(R.id.tv_namaPasien);
                nomor = itemView.findViewById(R.id.tv_nomorAntrian);
                waktu = itemView.findViewById(R.id.tv_waktu);
                poli = itemView.findViewById(R.id.tv_ruangperiksa);
            }
        }

        private ArrayList<Map<String, Object>> ulist;

        public MyAdapter(ArrayList<Map<String, Object>> userlist) {
            ulist = userlist;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View contactView = inflater.inflate(R.layout.data_pasien, parent, false);
            ViewHolder viewHolder = new ViewHolder(contactView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder viewHolder, int position) {
            TextView Nik = viewHolder.nik;
            TextView Nama = viewHolder.nama;
            TextView Nomor = viewHolder.nomor;
            TextView Poli = viewHolder.poli;
            TextView Waktu = viewHolder.waktu;
            TextView Status = viewHolder.status;
            Nik.setText("NIK           : " + ulist.get(position).get("nik").toString());
            Nama.setText("NAMA      : " + ulist.get(position).get("nama").toString());
            Nomor.setText("NOMOR    : " + ulist.get(position).get("nomor").toString());
            Poli.setText("POLI         : " + ulist.get(position).get("poli").toString());
            Waktu.setText("WAKTU    : " + ulist.get(position).get("waktu").toString());

        }

        @Override
        public int getItemCount() {
            return ulist.size();
        }
    }

}



