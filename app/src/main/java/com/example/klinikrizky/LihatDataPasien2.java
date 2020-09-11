package com.example.klinikrizky;

import android.content.Context;
import android.content.Intent;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.klinikrizky.model.Akun;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


import static android.content.Context.LAYOUT_INFLATER_SERVICE;

class LihatDataPasien2 extends RecyclerView.Adapter<LihatDataPasien2.ViewHolder> {
    ArrayList<String> dataGlobal;
    Context context;
    View relativeTambahDataPasien;
    private TextView editNikPop;
    private TextView editNamaPop;
    private TextView editTglPop;
    private TextView editAlamatPop;
    private TextView editTeleponPop;
    private TextView editEmailPop;
    private TextView editPasswordPop;
    private TextView editBpjsPop;

    FirebaseFirestore db;
    PopupWindow popupWindow2;
    List<Akun> akunwes = new ArrayList<>();

    public LihatDataPasien2(ArrayList<String> data, Context context, View relativeTambahDataPasien, List<Akun> akunwes) {
        dataGlobal = data;
        this.context = context;
        this.relativeTambahDataPasien = relativeTambahDataPasien;
        this.akunwes = akunwes;
    }

    @NonNull
    @Override
    public LihatDataPasien2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mview = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.activity_lihat_data_pasien2, parent, false);
        return new ViewHolder(mview);
        //untuk mengeset layout yang digunakan
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //mengeset masing views
        holder.textView.setText(dataGlobal.get(position));
    }

    @Override
    public int getItemCount() {
        return dataGlobal.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            //untuk logika didalam adapter
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageJudul);
            textView = itemView.findViewById(R.id.tvJudul);

            // on item click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get position
                    int pos = getAdapterPosition();

                    // check if item still exists
                    if (pos != RecyclerView.NO_POSITION) {
                        String clickedDataItem = dataGlobal.get(pos);

                        boolean visible = false;

                        for (int i = 0; i < akunwes.size(); i++) {
                            if (akunwes.get(i).getNama().equals(clickedDataItem)) {
                                Intent lihatData = new Intent(context, tampil_akun.class).
                                        putExtra("nik", akunwes.get(i).getNik());
                                context.startActivity(lihatData);

//                                visible = true;
//                                Toast.makeText(v.getContext(), "You clicked " + clickedDataItem, Toast.LENGTH_SHORT).show();
//                                onButtonShowPopupWindowClick(relativeTambahDataPasien, clickedDataItem, akunwes.get(i));
                                break;
                            }
                        }
                        if (!visible) {

                        }

                    }
                }
            });
        }


        public void onButtonShowPopupWindowClick(final View view, String poli, Akun akunwes) {

            // inflate the layout of the popup window
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_window_data_pasien, null);
            LinearLayout Linier_pasienBpjs = popupView.findViewById(R.id.Linier_pasienBpjs);
            LinearLayout Linier_pasienNik = popupView.findViewById(R.id.Linier_pasienNik);

            editNikPop = popupView.findViewById(R.id.edit_nikPop);
            editNamaPop = popupView.findViewById(R.id.edit_namaPop);
            editTglPop = popupView.findViewById(R.id.edit_tglPop);
            editAlamatPop = popupView.findViewById(R.id.edit_alamatPop);
            editTeleponPop = popupView.findViewById(R.id.edit_teleponPop);
            editEmailPop = popupView.findViewById(R.id.edit_emailPop);
            editPasswordPop = popupView.findViewById(R.id.edit_passwordPop);
            editBpjsPop = popupView.findViewById(R.id.edit_bpjsPop);

            // create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it
            popupWindow2 = new PopupWindow(popupView, width, height, focusable);

            //menghilangkan kalok Bpjs/Nik tidak di isi
            if (akunwes.getBpjs().isEmpty()) {
                Linier_pasienBpjs.setVisibility(View.GONE);
            } else {
                Linier_pasienNik.setVisibility(View.GONE);
            }

            editNikPop.setText(akunwes.getNik());
            editNamaPop.setText(akunwes.getNama());
            editTglPop.setText(akunwes.getTgl_lahir());
            editAlamatPop.setText(akunwes.getAlamat());
            editTeleponPop.setText(akunwes.getNotlp());
            editEmailPop.setText(akunwes.getEmail());
            editPasswordPop.setText(akunwes.getPassword());
            editBpjsPop.setText(akunwes.getBpjs());

            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window tolken
            popupWindow2.showAtLocation(view, Gravity.CENTER, 0, 0);
            // dismiss the popup window when touched
            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popupWindow2.dismiss();
                    return true;
                }
            });
        }
    }
}

