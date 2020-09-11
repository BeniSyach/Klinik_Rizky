package com.example.klinikrizky.model;

public class NomorAntrianModel  {

    private String nik="";
    private String nomor="";
    private String nama="";
    private String poli="";
    private String waktu="";
    private Boolean status=false;

    public NomorAntrianModel(){

    }

    public NomorAntrianModel(String nik, String nomor, String nama, String poli, String waktu, Boolean status){
        this. nik = nik;
        this.nomor = nomor;
        this.nama = nama;
        this.poli = poli;
        this.waktu = waktu;
        this.status =status;
    }


    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPoli() {
        return poli;
    }

    public void setPoli(String poli) {
        this.poli = poli;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

}
