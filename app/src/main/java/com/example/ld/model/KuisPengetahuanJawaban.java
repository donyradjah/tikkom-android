package com.example.ld.model;

import com.google.gson.annotations.SerializedName;

public class KuisPengetahuanJawaban {

    @SerializedName("id")
    int id;

    @SerializedName("pertanyaan")
    String pertanyaan;

    @SerializedName("jawab1")
    String jawab1;

    @SerializedName("jawab2")
    String jawab2;

    @SerializedName("jawab3")
    String jawab3;

    @SerializedName("jawab4")
    String jawab4;
    
    @SerializedName("jawab5")
    String jawab5;

    @SerializedName("kunci")
    String kunci;

    @SerializedName("jawab")
    String jawab;

    @SerializedName("status")
    String status;


    public KuisPengetahuanJawaban(int id, String pertanyaan, String jawab1, String jawab2, String jawab3, String jawab4, String jawab5, String kunci, String jawab, String status) {
        this.id = id;
        this.pertanyaan = pertanyaan;
        this.jawab1 = jawab1;
        this.jawab2 = jawab2;
        this.jawab3 = jawab3;
        this.jawab4 = jawab4;
        this.jawab5 = jawab5;
        this.kunci = kunci;
        this.jawab = jawab;
        this.status = status;
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

    public String getJawab1() {
        return jawab1;
    }

    public void setJawab1(String jawab1) {
        this.jawab1 = jawab1;
    }

    public String getJawab2() {
        return jawab2;
    }

    public void setJawab2(String jawab2) {
        this.jawab2 = jawab2;
    }

    public String getJawab3() {
        return jawab3;
    }

    public void setJawab3(String jawab3) {
        this.jawab3 = jawab3;
    }

    public String getJawab4() {
        return jawab4;
    }

    public void setJawab4(String jawab4) {
        this.jawab4 = jawab4;
    }
    public String getJawab5() {
        return jawab5;
    }

    public void setJawab5(String jawab5) {
        this.jawab5 = jawab5;
    }

    public String getKunci() {
        return kunci;
    }

    public void setKunci(String kunci) {
        this.kunci = kunci;
    }

    public String getJawab() {
        return jawab;
    }

    public void setJawab(String jawab) {
        this.jawab = jawab;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
