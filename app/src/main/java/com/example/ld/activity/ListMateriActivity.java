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
import com.example.ld.adapter.ListMateriAdapter;
import com.example.ld.adapter.RecyclerTouchListener;
import com.example.ld.helper.BaseApiService;
import com.example.ld.helper.ClickListener;
import com.example.ld.helper.Session;
import com.example.ld.helper.UrlApi;
import com.example.ld.model.Materi;
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

public class ListMateriActivity extends AppCompatActivity {

    KAlertDialog pDialog;
    Session session;
    BaseApiService apiService;

    ArrayList<Materi> materis = new ArrayList<>();

    RecyclerView rvListMateri;
    ImageView btnMenu;
    TextView txtMenu;
    GridLayoutManager layoutmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_materi);

        session = new Session(getApplicationContext());
        ButterKnife.bind(this);
        apiService = UrlApi.getAPIService();

        pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Sedang Mengambil Data ...");
        pDialog.setCancelable(false);

        rvListMateri = findViewById(R.id.rvListMateri);
        btnMenu = findViewById(R.id.btnMenu);
        txtMenu = findViewById(R.id.txtMenu);


        layoutmanager = new GridLayoutManager(this, 2);
        rvListMateri.setLayoutManager(layoutmanager);
        rvListMateri.setHasFixedSize(true);
        rvListMateri.setItemAnimator(new DefaultItemAnimator());

        btnMenu.setImageResource(R.drawable.ic_arrow_back);
        txtMenu.setText("Daftar Materi");

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
            pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Sedang Mengambil Data ...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        apiService.getAllMateri("Bearer " + session.getString("token")).enqueue(new Callback<ResponseBody>() {
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
                                new KAlertDialog(ListMateriActivity.this, KAlertDialog.ERROR_TYPE)
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
                                    new KAlertDialog(ListMateriActivity.this, KAlertDialog.WARNING_TYPE)
                                            .setTitleText("Oops...")
                                            .setContentText("Data Kosong").setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
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

                                        materis.add(new Materi(materiObject.getInt("id")
                                                , materiObject.getString("namaMateri"),
                                                materiObject.getString("file")));
                                        pDialog.dismissWithAnimation();
                                        setData();
                                    }

                                }
                            }

                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                            pDialog.dismissWithAnimation();

                            new KAlertDialog(ListMateriActivity.this, KAlertDialog.ERROR_TYPE)
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
                        new KAlertDialog(ListMateriActivity.this, KAlertDialog.ERROR_TYPE)
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
                    new KAlertDialog(ListMateriActivity.this, KAlertDialog.ERROR_TYPE)
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
                    new KAlertDialog(ListMateriActivity.this, KAlertDialog.ERROR_TYPE)
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
                new KAlertDialog(ListMateriActivity.this, KAlertDialog.ERROR_TYPE)
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

    void setData() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }

        rvListMateri.setAdapter(new ListMateriAdapter(getApplicationContext(), materis, this));

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
