package com.example.ld.model;

public class Score {

    int id;
    String waktu, score, benar, salah, user_id;

    public Score() {
    }

    public Score(int id, String waktu, String score, String benar, String salah, String user_id) {
        this.id = id;
        this.waktu = waktu;
        this.score = score;
        this.benar = benar;
        this.salah = salah;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getBenar() {
        return benar;
    }

    public void setBenar(String benar) {
        this.benar = benar;
    }

    public String getSalah() {
        return salah;
    }

    public void setSalah(String salah) {
        this.salah = salah;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
