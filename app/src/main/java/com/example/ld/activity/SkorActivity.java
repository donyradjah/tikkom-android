package com.example.ld.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ld.R;
import com.example.ld.helper.BaseApiService;
import com.example.ld.helper.Session;
import com.example.ld.helper.UrlApi;
import com.kinda.alert.KAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SkorActivity extends AppCompatActivity {


    ImageView btnMenu;
    TextView txtMenu;

    KAlertDialog pDialog;
    Session session;
    BaseApiService apiService;

    TextView tvNomor, tvBenar, tvSalah;

    String skor, benar, salah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skor);

        session = new Session(getApplicationContext());
        ButterKnife.bind(this);
        apiService = UrlApi.getAPIService();

        pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Sedang Mengambil Data ...");
        pDialog.setCancelable(false);
        btnMenu = findViewById(R.id.btnMenu);
        txtMenu = findViewById(R.id.txtMenu);
        tvNomor = findViewById(R.id.tvNomor);
        tvBenar = findViewById(R.id.tvBenar);
        tvSalah = findViewById(R.id.tvSalah);
        btnMenu.setImageResource(R.drawable.ic_arrow_back);
        txtMenu.setText("Skor");

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ambilLastScore();
    }

    void ambilLastScore() {
        if (!pDialog.isShowing()) {
            pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Sedang Mengambil Data ...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        apiService.getLastScore("Bearer " + session.getString("token")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        try {
                            String data = response.body().string();
                            Log.d("DATA", data);
                            JSONObject result = new JSONObject(data);
                            if (!result.getBoolean("success")) {
                                pDialog.dismissWithAnimation();
                                new KAlertDialog(SkorActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Terjadi Kesalahan Saat Mengambil Data").setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog kAlertDialog) {
                                        finish();
                                    }
                                })
                                        .show();
                            } else {
                                if (result.getInt("total") <= 0) {
                                    pDialog.dismissWithAnimation();
                                    new KAlertDialog(SkorActivity.this, KAlertDialog.WARNING_TYPE)
                                            .setTitleText("Oops...")
                                            .setContentText("Data Kosong").setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog kAlertDialog) {
                                            finish();
                                        }
                                    })
                                            .show();
                                } else {
                                    JSONObject jsonObject = result.getJSONObject("data");


                                    skor = jsonObject.getString("score");
                                    benar = jsonObject.getString("benar");
                                    salah = jsonObject.getString("salah");

                                    tampilData();
                                }
                            }

                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                            pDialog.dismissWithAnimation();

                            new KAlertDialog(SkorActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Terjadi Kesalahan Saat Mengambil Data").setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(KAlertDialog kAlertDialog) {
                                    finish();
                                }
                            })
                                    .show();

                        }
                    } else {
                        pDialog.dismissWithAnimation();
                        new KAlertDialog(SkorActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Terjadi Kesalahan Saat Mengambil Data").setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(KAlertDialog kAlertDialog) {
                                finish();
                            }
                        })
                                .show();

                    }
                } else if (response.code() == 401) {
                    pDialog.dismissWithAnimation();
                    new KAlertDialog(SkorActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Login Tidak Valid").setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(KAlertDialog kAlertDialog) {
                            session.destroy();
                            Intent menu = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(menu);
                            finish();
                        }
                    })
                            .show();
                } else {
                    pDialog.dismiss();
                    new KAlertDialog(SkorActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Gagal Mengambil Data").setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(KAlertDialog kAlertDialog) {
                            finish();
                        }
                    })
                            .show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.dismissWithAnimation();
                new KAlertDialog(SkorActivity.this, KAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Gagal Mengambil Data").setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(KAlertDialog kAlertDialog) {
                        finish();
                    }
                }).show();
            }
        });
    }

    void tampilData() {
        tvNomor.setText(skor + "");
        tvBenar.setText("Benar : " + benar + "");
        tvSalah.setText("Salah : " + salah + "");

        pDialog.dismissWithAnimation();
    }
}
