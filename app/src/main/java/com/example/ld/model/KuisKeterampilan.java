package com.example.ld.model;

public class KuisKeterampilan {

    int id;
    String pertanyaan, input, output;
    String gambar;

    public KuisKeterampilan(int id, String pertanyaan, String input, String output, String gambar) {
        this.id = id;
        this.pertanyaan = pertanyaan;
        this.input = input;
        this.output = output;
        this.gambar = gambar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPertanyaan() {
        return pertanyaan;
    }

    public void setPertanyaan(String pertanyaan) {
        this.pertanyaan = pertanyaan;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
