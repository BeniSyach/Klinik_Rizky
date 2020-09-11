package com.example.klinikrizky.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.klinikrizky.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class CardviewAdapter extends FirestoreRecyclerAdapter<NomorAntrianModel,CardviewAdapter.CardHolder> {

    public CardviewAdapter(@NonNull FirestoreRecyclerOptions<NomorAntrianModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CardviewAdapter.CardHolder holder, int position, @NonNull NomorAntrianModel model) {
        holder.nik.setText(model.getNik());
        holder.nama.setText(model.getNama());
        holder.nomor.setText(model.getNomor());
        holder.waktu.setText(model.getWaktu());
        holder.poli.setText(model.getPoli());
        holder.status.setText(String.valueOf(model.getStatus()));
    }

    public void deleteNote(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    @NonNull
    @Override
    public CardviewAdapter.CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_pasien, parent, false);

        return new CardHolder(view);
    }

    public class CardHolder extends RecyclerView.ViewHolder {

        private TextView nik;
        private TextView nama;
        private TextView nomor;
        private TextView waktu;
        private TextView poli;
        private TextView status;

        public CardHolder(View itemView) {
            super(itemView);

            nik = itemView.findViewById(R.id.tv_noKtp);
            nama = itemView.findViewById(R.id.tv_namaPasien);
            nomor = itemView.findViewById(R.id.tv_nomorAntrian);
            waktu = itemView.findViewById(R.id.tv_waktu);
            poli = itemView.findViewById(R.id.tv_ruangperiksa);
        }
    }
}
