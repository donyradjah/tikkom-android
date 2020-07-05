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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    KAlertDialog pDialog;
    Session session;
    BaseApiService apiService;

    String name, email, nisn;

    ImageView btnMenu;
    TextView txtMenu, tvNis, tvNama, tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        session = new Session(getApplicationContext());
        ButterKnife.bind(this);
        apiService = UrlApi.getAPIService();

        btnMenu = findViewById(R.id.btnMenu);
        txtMenu = findViewById(R.id.txtMenu);
        tvNis = findViewById(R.id.tvNis);
        tvNama = findViewById(R.id.tvNama);
        tvEmail = findViewById(R.id.tvEmail);

        btnMenu.setImageResource(R.drawable.ic_arrow_back);
        txtMenu.setText("Profil Anda");

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Sedang Mengambil Data ...");
        pDialog.setCancelable(false);
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

        apiService.getUser("Bearer " + session.getString("token")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        try {
                            String data = response.body().string();
                            Log.d("DATA", data);
                            JSONObject result = new JSONObject(data);


                            JSONObject jsonObject = result.getJSONObject("success");
//
                            Log.d("KUIS", result.getString("success"));
//
                            name = jsonObject.getString("name");
                            email = jsonObject.getString("email");
                            nisn = jsonObject.getString("nisn");

                            tampilData();

                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                            pDialog.dismissWithAnimation();

                            new KAlertDialog(ProfileActivity.this, KAlertDialog.ERROR_TYPE)
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
                        new KAlertDialog(ProfileActivity.this, KAlertDialog.ERROR_TYPE)
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
                    new KAlertDialog(ProfileActivity.this, KAlertDialog.ERROR_TYPE)
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
                    new KAlertDialog(ProfileActivity.this, KAlertDialog.ERROR_TYPE)
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
                new KAlertDialog(ProfileActivity.this, KAlertDialog.ERROR_TYPE)
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

        tvNis.setText(name);
        tvNama.setText(email);
        tvEmail.setText(nisn);

        if (pDialog.isShowing()) {
            pDialog.dismissWithAnimation();
        }
    }


    public void listScore(View view) {
        Intent ListVideo = new Intent(getApplicationContext(), ListScoreActivity.class);
        startActivity(ListVideo);
    }

    public void logout(View view) {
        session.destroy();
        Intent menu = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(menu);
        finish();
    }
}
