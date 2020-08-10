package com.example.ld.model;

public class KuisPengetahuan {

    int id;
    String pertanyaan, jawab1, jawab2, jawab3, jawab4, jawab5, kunci;

    public KuisPengetahuan(int id, String pertanyaan, String jawab1, String jawab2, String jawab3, String jawab4, String jawab5, String kunci) {
        this.id = id;
        this.pertanyaan = pertanyaan;
        this.jawab1 = jawab1;
        this.jawab2 = jawab2;
        this.jawab3 = jawab3;
        this.jawab4 = jawab4;
        this.jawab5 = jawab5;
        this.kunci = kunci;
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
}
