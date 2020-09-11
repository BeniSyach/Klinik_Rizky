package com.example.klinikrizky;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class cetak_laporan extends AppCompatActivity {

    private WebView webView;
    private FirebaseFirestore databaseReference;
    private String name,cnic,status,class1,class2;
    String TAG="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cetak_laporan);

        webView=findViewById(R.id.webview);
        databaseReference=FirebaseFirestore.getInstance();
        databaseReference.collection("antri")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String html="<!DOCTYPE html>\n" +
                                        "<html>\n" +
                                        "<head>\n" +
                                        "\t<title></title>\n" +
                                        "\t<link rel=\"stylesheet\" type=\"text/css\" href=\"file:android_asset/CreatePdf.css\">\n" +
                                        "</head>\n" +
                                        "<body>\n" +
                                        "\t<div>\n" +
                                        "\t\t<h3>Data Laporan Antrian Pasien</h3>\n" +
                                        "\t\t<table border=\"1\">\n" +
                                        "\t\t\t<tr>\n" +
                                        "\t\t\t\t<td>Nik</td>\n" +
                                        "\t\t\t\t<td>Nama</td>\n" +
                                        "\t\t\t\t<td>Nomor</td>\n" +
                                        "\t\t\t\t<td>Poli</td>\n" +
                                        "\t\t\t\t<td>Waktu</td>\n" +
                                        "\t\t\t</tr>\n" +
                                        "\t\t\t<tr>\n" +
                                        "\t\t\t\t<td id=\"nik\">"+document.getData().get("nik")+"</td>\n" +
                                        "\t\t\t\t<td id=\"nama\">"+document.getData().get("nama")+"</td>\n" +
                                        "\t\t\t\t<td id=\"nomor\">"+document.getData().get("nomor")+"</td>\n" +
                                        "\t\t\t\t<td id=\"poli\">"+document.getData().get("poli")+"</td>\n" +
                                        "\t\t\t\t<td id=\"waktu\">"+document.getData().get("waktu")+"</td>\n" +
                                        "\t\t\t</tr>\n" +
                                        "\t\t</table>\n" +
                                        "\t</div>\n" +
                                        "\n" +
                                        "</body>\n" +
                                        "</html>";
                                webView.loadDataWithBaseURL(null,html,"text/html","utf-8",null);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    public void CreatePdf(View view){
        Context context=cetak_laporan.this;
        PrintManager printManager=(PrintManager)cetak_laporan.this.getSystemService(context.PRINT_SERVICE);
        PrintDocumentAdapter adapter=null;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.P){
            adapter=webView.createPrintDocumentAdapter();
        }
        String JobName=getString(R.string.app_name) +"Document";
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.P){
            PrintJob printJob=printManager.print(JobName,adapter,new PrintAttributes.Builder().build());
        }

    }
}
