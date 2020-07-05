package com.example.ld.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ld.R;

public class TentangActivity extends AppCompatActivity {

    ImageView btnMenu;
    TextView txtMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang);

        btnMenu = findViewById(R.id.btnMenu);
        txtMenu = findViewById(R.id.txtMenu);
        btnMenu.setImageResource(R.drawable.ic_arrow_back);
        txtMenu.setText("TENTANG");
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
