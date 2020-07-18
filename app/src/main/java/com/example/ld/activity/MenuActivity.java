package com.example.ld.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.ld.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void openMateri(View view) {
        Intent ListMateri = new Intent(getApplicationContext(), ListMateriActivity.class);
        startActivity(ListMateri);
    }

    public void openVideo(View view) {
        Intent ListVideo = new Intent(getApplicationContext(), ListVideoActivity.class);
        startActivity(ListVideo);
    }

    public void openKuisKeterampilan(View view) {
        Intent ListVideo = new Intent(getApplicationContext(), KuisKeterampilanActivity.class);
        startActivity(ListVideo);
    }

    public void openKuisPengetahuan(View view) {
        Intent ListVideo = new Intent(getApplicationContext(), KuisPengetahuanActivity.class);
        startActivity(ListVideo);
    }

    public void openTentang(View view) {
        Intent ListVideo = new Intent(getApplicationContext(), TentangActivity.class);
        startActivity(ListVideo);
    }

    public void openSilabus(View view) {
        Intent ListVideo = new Intent(getApplicationContext(), PilihSilbusActivity.class);
        startActivity(ListVideo);
    }

    public void profile(View view) {
        Intent ListVideo = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(ListVideo);
    }


}
