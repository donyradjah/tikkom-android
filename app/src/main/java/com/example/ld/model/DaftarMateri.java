package com.example.ld.model;

import java.io.Serializable;

public class DaftarMateri implements Serializable {

    private int id, halaman;
    private String judul;

    public DaftarMateri() {
    }

    public DaftarMateri(int id, String judul, int halaman) {
        this.id = id;
        this.judul = judul;
        this.halaman = halaman;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public int getHalaman() {
        return halaman;
    }

    public void setHalaman(int halaman) {
        this.halaman = halaman;
    }
}
