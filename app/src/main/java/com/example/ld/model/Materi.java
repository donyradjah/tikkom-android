package com.example.ld.model;

public class Materi {

    int id;
    String namaMateri, file;

    public Materi() {
    }

    public Materi(int id, String namaMateri, String file) {
        this.id = id;
        this.namaMateri = namaMateri;
        this.file = file;
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
