package com.example.klinikrizky;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class Nav_Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LinearLayout mDataPasien;
    private LinearLayout mPendaftaran;
    private LinearLayout mPoliklinik;
    private LinearLayout mPanggil;
    private TextView mTvLevel;
    private ImageView mImgProfil;
    private TextView mNameProfil;
    private TextView mnik;
    private LinearLayout mInformasi;
    private LinearLayout notifikasi;

    private MenuItem mSetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav__home);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);
        mImgProfil = headerview.findViewById(R.id.img_profil);
        loadImage(tools.getSharedPreferenceString(this, "foto", ""));

        mNameProfil = headerview.findViewById(R.id.name_profil);
        mNameProfil.setText(tools.getSharedPreferenceString(this, "nama", ""));

        mImgProfil = findViewById(R.id.img_profil);
        mNameProfil = findViewById(R.id.name_profil);
        mPendaftaran = findViewById(R.id.home_pendaftaran);
        mDataPasien = findViewById(R.id.home_data_pasien);
        mPoliklinik = findViewById(R.id.home_poliklinik);
        mTvLevel = findViewById(R.id.judul_level);
        mPanggil = findViewById(R.id.panggil_nomor);
        mInformasi = findViewById(R.id.home_informasi);
        notifikasi = findViewById(R.id.notif);


        if (tools.getSharedPreferenceString(this, "level", "").equals("1")) {
            navigationView.getMenu().findItem(R.id.nav_data_pasien).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_panggil).setVisible(false);

            mDataPasien.setVisibility(View.GONE);
            mPanggil.setVisibility(View.GONE);
            notifikasi.setVisibility(View.GONE);
            mTvLevel.setText("PASIEN");
        }else {
            mTvLevel.setText("ADMIN");
        }

        mDataPasien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent datapasien = new Intent(Nav_Home.this, LihatDataPasien.class);
                startActivity(datapasien);
            }
        });

        mPendaftaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pendaftaran = new Intent(Nav_Home.this, jadwal.class);
                startActivity(pendaftaran);
            }
        });

        mPoliklinik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent poliklinik = new Intent(Nav_Home.this, layout_poli.class);
                startActivity(poliklinik);
            }
        });
        mImgProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imgProfil = new Intent(Nav_Home.this, profile.class);
                startActivity(imgProfil);
            }
        });
        mNameProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nameProfil = new Intent(Nav_Home.this, profile.class);
                startActivity(nameProfil);
            }
        });
        mPanggil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent panggilan = new Intent(Nav_Home.this, panggil_nomor.class);
                startActivity(panggilan);
            }
        });

        mInformasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent panggilan = new Intent(Nav_Home.this, activity_informasi.class);
                startActivity(panggilan);
            }

        });

        notifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent panggilan = new Intent(Nav_Home.this, List_antrian.class);
                startActivity(panggilan);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_panggil) {
            Intent intent = new Intent(Nav_Home.this, panggil_nomor.class);
            startActivity(intent);
        } else if (id == R.id.nav_data_pasien) {
            Intent intent = new Intent(Nav_Home.this, LihatDataPasien.class);
            startActivity(intent);
        } else if (id == R.id.nav_jadwal) {
            Intent intent = new Intent(Nav_Home.this, jadwal.class);
            startActivity(intent);
        } else if (id == R.id.nav_poli) {
            Intent intent = new Intent(Nav_Home.this, layout_poli.class);
            startActivity(intent);
        } else if (id == R.id.nav_info) {
            Intent intent = new Intent(Nav_Home.this, activity_informasi.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            tools.setSharedPreference(Nav_Home.this,"isLogin", "0");
            Intent logout = new Intent(Nav_Home.this, Login.class);
            startActivity(logout);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        loadImage(tools.getSharedPreferenceString(this, "foto", ""));
        mNameProfil.setText(tools.getSharedPreferenceString(this, "nama", ""));
        super.onResume();
    }

    private void loadImage(String url){
        if (!url.isEmpty()) {
            Picasso.get()
                    .load(url)
                    .fit()
                    .centerCrop()
                    .into(mImgProfil);
        }
    }
}
