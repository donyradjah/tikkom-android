package com.example.ld.model;

import java.util.ArrayList;

public class Materi {

    int id;
    String namaMateri, file;

    ArrayList<DaftarMateri> daftarMateris;

    public ArrayList<DaftarMateri> getDaftarMateris() {
        return daftarMateris;
    }

    public void setDaftarMateris(ArrayList<DaftarMateri> daftarMateris) {
        this.daftarMateris = daftarMateris;
    }

    public Materi() {
    }

    public Materi(int id, String namaMateri, String file) {
        this.id = id;
        this.namaMateri = namaMateri;
        this.file = file;
    }


    public Materi(int id, String namaMateri, String file, ArrayList<DaftarMateri> daftarMateris) {
        this.id = id;
        this.namaMateri = namaMateri;
        this.file = file;
        this.daftarMateris = daftarMateris;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaMateri() {
        return namaMateri;
    }

    public void setNamaMateri(String namaMateri) {
        this.namaMateri = namaMateri;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
