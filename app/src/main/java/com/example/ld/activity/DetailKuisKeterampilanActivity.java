package com.example.ld.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ld.R;
import com.example.ld.helper.BaseApiService;
import com.example.ld.helper.Session;
import com.example.ld.helper.UrlApi;
import com.google.gson.JsonObject;
import com.kinda.alert.KAlertDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.IOException;

import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailKuisKeterampilanActivity extends AppCompatActivity {

    TextView tvPertanyaaan, tvInput, tvOutput;
    Button btnUpload;
    ImageView btnMenu, imgPertanyaan;
    TextView txtMenu;

    KAlertDialog pDialog;
    Session session;
    BaseApiService apiService;
    String pertanyaan, input, output, upload, gambar;

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kuis_keterampilan);

        session = new Session(getApplicationContext());
        ButterKnife.bind(this);
        apiService = UrlApi.getAPIService();
        btnMenu = findViewById(R.id.btnMenu);
        txtMenu = findViewById(R.id.txtMenu);
        tvPertanyaaan = findViewById(R.id.tvPertanyaaan);
        tvInput = findViewById(R.id.tvInput);
        tvOutput = findViewById(R.id.tvOutput);
        btnUpload = findViewById(R.id.btnUpload);
        imgPertanyaan = findViewById(R.id.imgPertanyaan);

        btnMenu.setImageResource(R.drawable.ic_arrow_back);
        txtMenu.setText("Kuis Keterampilan");

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();

        pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Sedang Mengambil Data ...");
        pDialog.setCancelable(false);

        id = intent.getIntExtra("id", 0);
        ambilDataDetail();
    }

    void ambilDataDetail() {
        if (!pDialog.isShowing()) {
            pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Sedang Mengambil Data ...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        apiService.detailKuisKeterampilan("Bearer " + session.getString("token"), id).enqueue(new Callback<ResponseBody>() {
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
                                new KAlertDialog(DetailKuisKeterampilanActivity.this, KAlertDialog.ERROR_TYPE)
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
                                    new KAlertDialog(DetailKuisKeterampilanActivity.this, KAlertDialog.WARNING_TYPE)
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

                                    Log.d("KUIS", result.getString("data"));

                                    pertanyaan = jsonObject.getString("pertanyaan");
                                    input = jsonObject.getString("input");
                                    output = jsonObject.getString("output");
                                    upload = jsonObject.getString("upload");
                                    gambar = jsonObject.getString("gambar_pertanyaan");

                                    tampilData();
                                }
                            }

                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                            pDialog.dismissWithAnimation();

                            new KAlertDialog(DetailKuisKeterampilanActivity.this, KAlertDialog.ERROR_TYPE)
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
                        new KAlertDialog(DetailKuisKeterampilanActivity.this, KAlertDialog.ERROR_TYPE)
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
                    new KAlertDialog(DetailKuisKeterampilanActivity.this, KAlertDialog.ERROR_TYPE)
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
                    new KAlertDialog(DetailKuisKeterampilanActivity.this, KAlertDialog.ERROR_TYPE)
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
                new KAlertDialog(DetailKuisKeterampilanActivity.this, KAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Gagal Mengambil Data").setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(KAlertDialog kAlertDialog) {
                        finish();
                    }
                })
                        .show();
            }
        });
    }

    void tampilData() {

        tvPertanyaaan.setText(pertanyaan);
        tvInput.setText(input);
        tvPertanyaaan.setText(output);

        if (upload.equals("ya")) {
            btnUpload.setVisibility(View.VISIBLE);
        } else {
            btnUpload.setVisibility(View.GONE);
        }

        Log.d("image", gambar);

        if (gambar.equals("") || gambar.equals("null") || gambar == null) {
            imgPertanyaan.setVisibility(View.GONE);
        } else {
            Picasso.get().load(UrlApi.BASE_URL_API + "public/upload/gambar_pertanyaan/" + gambar).into(imgPertanyaan);
        }


        Log.d("image", UrlApi.BASE_URL_API + "public/upload/gambar_pertanyaan/" + gambar);
        if (pDialog.isShowing()) {
            pDialog.dismissWithAnimation();
        }
    }
}
