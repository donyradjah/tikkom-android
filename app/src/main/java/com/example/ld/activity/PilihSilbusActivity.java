package com.example.ld.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ld.R;
import com.example.ld.helper.BaseApiService;
import com.example.ld.helper.Session;
import com.example.ld.model.Score;
import com.kinda.alert.KAlertDialog;

import java.util.ArrayList;

public class PilihSilbusActivity extends AppCompatActivity {

    KAlertDialog pDialog;
    Session session;
    BaseApiService apiService;

    ArrayList<Score> scores = new ArrayList<>();

    RecyclerView rvListScore;
    ImageView btnMenu;
    TextView txtMenu;
    GridLayoutManager layoutmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_silbus);

        btnMenu = findViewById(R.id.btnMenu);
        txtMenu = findViewById(R.id.txtMenu);

        btnMenu.setImageResource(R.drawable.ic_arrow_back);
        txtMenu.setText("Info");

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void openSilabus(View view) {
        Intent ListVideo = new Intent(getApplicationContext(), SilabusActivity.class);
        startActivity(ListVideo);
    }

    public void openTujuan(View view) {
        Intent ListVideo = new Intent(getApplicationContext(), TujuanActivity.class);
        startActivity(ListVideo);
    }
}
