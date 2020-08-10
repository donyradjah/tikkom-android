package com.example.ld.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ld.R;
import com.example.ld.adapter.ListKuisKeterampilanAdapter;
import com.example.ld.helper.BaseApiService;
import com.example.ld.helper.Session;
import com.example.ld.helper.UrlApi;
import com.example.ld.model.KuisKeterampilan;
import com.kinda.alert.KAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KuisKeterampilanActivity extends AppCompatActivity {
    KAlertDialog pDialog;
    Session session;
    BaseApiService apiService;

    ArrayList<KuisKeterampilan> kuisKeterampilans = new ArrayList<>();

    RecyclerView rvListKuisKeterampilan;
    ImageView btnMenu;
    TextView txtMenu;
    GridLayoutManager layoutmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuis_keterampilan);

        session = new Session(getApplicationContext());
        ButterKnife.bind(this);
        apiService = UrlApi.getAPIService();

        rvListKuisKeterampilan = findViewById(R.id.rvListKuisKeterampilan);
        btnMenu = findViewById(R.id.btnMenu);
        txtMenu = findViewById(R.id.txtMenu);

        pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Sedang Mengambil Data ...");
        pDialog.setCancelable(false);

        layoutmanager = new GridLayoutManager(this, 1);
        rvListKuisKeterampilan.setLayoutManager(layoutmanager);
        rvListKuisKeterampilan.setHasFixedSize(true);
        rvListKuisKeterampilan.setItemAnimator(new DefaultItemAnimator());

        btnMenu.setImageResource(R.drawable.ic_arrow_back);
        txtMenu.setText("KUIS KETERAMPILAN");

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ambilDataMateri();
    }

    void ambilDataMateri() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }

        apiService.getAllKuisKeterampilan("Bearer " + session.getString("token")).enqueue(new Callback<ResponseBody>() {
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
                                new KAlertDialog(KuisKeterampilanActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Terjadi Kesalahan Saat Mengambil Data")
                                        .setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog kAlertDialog) {
                                                finish();
                                            }
                                        })
                                        .show();
                            } else {
                                if (result.getInt("total") <= 0) {
                                    pDialog.dismissWithAnimation();
                                    new KAlertDialog(KuisKeterampilanActivity.this, KAlertDialog.WARNING_TYPE)
                                            .setTitleText("Oops...")
                                            .setContentText("Data Kosong")
                                            .setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(KAlertDialog kAlertDialog) {
                                                    finish();
                                                }
                                            })
                                            .show();
                                } else {
                                    JSONArray materiArray = result.getJSONArray("data");

                                    for (int i = 0; i < materiArray.length(); i++) {
                                        JSONObject materiObject = materiArray.getJSONObject(i);

                                        kuisKeterampilans.add(new KuisKeterampilan(materiObject.getInt("id")
                                                , materiObject.getString("pertanyaan"),
                                                materiObject.getString("input"),
                                                materiObject.getString("output"),
                                                materiObject.getString("gambar_pertanyaan")));
                                        pDialog.dismissWithAnimation();
                                        setData();
                                    }

                                }
                            }

                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                            pDialog.dismissWithAnimation();

                            new KAlertDialog(KuisKeterampilanActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Terjadi Kesalahan Saat Mengambil Data")
                                    .setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog kAlertDialog) {
                                            finish();
                                        }
                                    })
                                    .show();

                        }
                    } else {
                        pDialog.dismissWithAnimation();
                        new KAlertDialog(KuisKeterampilanActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Terjadi Kesalahan Saat Mengambil Data")
                                .setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog kAlertDialog) {
                                        finish();
                                    }
                                })
                                .show();

                    }
                } else if (response.code() == 401) {
                    pDialog.dismissWithAnimation();
                    new KAlertDialog(KuisKeterampilanActivity.this, KAlertDialog.ERROR_TYPE)
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
                    new KAlertDialog(KuisKeterampilanActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Gagal Mengambil Data")
                            .setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
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
                new KAlertDialog(KuisKeterampilanActivity.this, KAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Gagal Mengambil Data")
                        .setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(KAlertDialog kAlertDialog) {
                                finish();
                            }
                        })
                        .show();

            }
        });
    }

    void setData() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }

        rvListKuisKeterampilan.setAdapter(new ListKuisKeterampilanAdapter(getApplicationContext(), kuisKeterampilans, this));

//        rvListMateri.addOnItemTouchListener(new RecyclerTouchListener(this, rvListMateri, new ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//
//
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));

        pDialog.dismissWithAnimation();
    }
}
