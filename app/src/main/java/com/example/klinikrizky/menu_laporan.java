package com.example.klinikrizky;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class menu_laporan extends AppCompatActivity {

    Button filter;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<HashMap<String, Object>> myDataset = new ArrayList<>();
    private Map<String, Object> mydataset1 = new HashMap<>();
    private FirebaseFirestore lvfirestore;

    EditText search_text;
    Button search_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_laporan) ;

        filter=findViewById(R.id.btfilter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent datapasien = new Intent(menu_laporan.this, List_antrian.class);
                startActivity(datapasien);
            }
        });

        search_text = findViewById(R.id.search_field) ;
        search_button =  findViewById(R.id.search_button) ;

        lvfirestore = FirebaseFirestore.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDataset.clear();

                String searchText = search_text.getText().toString();

                Query searchQuery;

                searchQuery = lvfirestore.collection("antri").orderBy("waktu").startAt(searchText).endAt(searchText+ "\uf8ff");
                {
                    if (searchQuery != null) {

                        searchQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    ArrayList<Map<String, Object>> list = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        list.add(document.getData());
                                        Log.e("abc", document.getData().get("nama").toString());
                                    }
                                    mAdapter = new menu_laporan.MyAdapter(list);
                                    recyclerView.setAdapter(mAdapter);
                                }
                                else{
                                    Toast.makeText(menu_laporan.this, "data tidak ada.", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }
                }
            }
        });


    }

    public class MyAdapter extends RecyclerView.Adapter<menu_laporan.MyAdapter.ViewHolder> {

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
        public menu_laporan.MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View contactView = inflater.inflate(R.layout.data_pasien, parent, false);
           menu_laporan.MyAdapter.ViewHolder viewHolder = new menu_laporan.MyAdapter.ViewHolder(contactView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(menu_laporan.MyAdapter.ViewHolder viewHolder, int position) {
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
