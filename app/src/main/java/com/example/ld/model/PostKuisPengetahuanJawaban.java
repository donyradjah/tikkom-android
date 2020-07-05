package com.example.ld.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PostKuisPengetahuanJawaban {

    @SerializedName("data")
    ArrayList<KuisPengetahuanJawaban> kuisPengetahuanJawabans;

    public PostKuisPengetahuanJawaban(ArrayList<KuisPengetahuanJawaban> kuisPengetahuanJawabans) {
        this.kuisPengetahuanJawabans = kuisPengetahuanJawabans;
    }

    public ArrayList<KuisPengetahuanJawaban> getKuisPengetahuanJawabans() {
        return kuisPengetahuanJawabans;
    }

    public void setKuisPengetahuanJawabans(ArrayList<KuisPengetahuanJawaban> kuisPengetahuanJawabans) {
        this.kuisPengetahuanJawabans = kuisPengetahuanJawabans;
    }
}
