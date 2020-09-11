package com.example.klinikrizky.model;

public class Pelayanan {
    private String iddokter;
    private String namadokter;
    private String senkam;
    private String jumat;
    private String sabtu;
    private String poli;

    public String getIddokter() {
        return iddokter;
    }

    public void setIddokter(String iddokter) {
        this.iddokter = iddokter;
    }

    public String getNamadokter() {
        return namadokter;
    }

    public void setNamadokter(String namadokter) {
        this.namadokter = namadokter;
    }

    public String getSenkam() {
        return senkam;
    }

    public void setSenkam(String senkam) {
        this.senkam = senkam;
    }

    public String getJumat() {
        return jumat;
    }

    public void setJumat(String jumat) {
        this.jumat = jumat;
    }

    public String getSabtu() {
        return sabtu;
    }

    public void setSabtu(String sabtu) {
        this.sabtu = sabtu;
    }

    public String getPoli() {
        return poli;
    }

    public void setPoli(String poli) {
        this.poli = poli;
    }


    public Pelayanan(String iddokter, String namadokter, String senkam, String jumat,
                     String sabtu, String poli) {
        this.iddokter = iddokter;
        this.namadokter = namadokter;
        this.senkam = senkam;
        this.jumat = jumat;
        this.sabtu = sabtu;
        this.poli = poli;
    }
}
