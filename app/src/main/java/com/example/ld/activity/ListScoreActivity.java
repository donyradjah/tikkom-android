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
import com.example.ld.adapter.ListScoreAdapter;
import com.example.ld.helper.BaseApiService;
import com.example.ld.helper.Session;
import com.example.ld.helper.UrlApi;
import com.example.ld.model.Score;
import com.example.ld.model.Score;
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

public class ListScoreActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_list_score);

        session = new Session(getApplicationContext());
        ButterKnife.bind(this);
        apiService = UrlApi.getAPIService();

        pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Sedang Mengambil Data ...");
        pDialog.setCancelable(false);

        rvListScore = findViewById(R.id.rvListScore);
        btnMenu = findViewById(R.id.btnMenu);
        txtMenu = findViewById(R.id.txtMenu);


        layoutmanager = new GridLayoutManager(this, 1);
        rvListScore.setLayoutManager(layoutmanager);
        rvListScore.setHasFixedSize(true);
        rvListScore.setItemAnimator(new DefaultItemAnimator());

        btnMenu.setImageResource(R.drawable.ic_arrow_back);
        txtMenu.setText("History Score");

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ambilDataScore();
    }

    void ambilDataScore() {
        if (!pDialog.isShowing()) {
            pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Sedang Mengambil Data ...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        apiService.getAllScore("Bearer " + session.getString("token")).enqueue(new Callback<ResponseBody>() {
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
                                new KAlertDialog(ListScoreActivity.this, KAlertDialog.ERROR_TYPE)
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
                                    new KAlertDialog(ListScoreActivity.this, KAlertDialog.WARNING_TYPE)
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
                                    JSONArray scoreArray = result.getJSONArray("data");

                                    for (int i = 0; i < scoreArray.length(); i++) {
                                        JSONObject scoreObject = scoreArray.getJSONObject(i);

                                        scores.add(new Score(scoreObject.getInt("id")
                                                , scoreObject.getString("waktu")
                                                , scoreObject.getString("score")
                                                , scoreObject.getString("benar")
                                                , scoreObject.getString("salah"),
                                                scoreObject.getString("user_id")));
                                        pDialog.dismissWithAnimation();
                                        setData();
                                    }

                                }
                            }

                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                            pDialog.dismissWithAnimation();

                            new KAlertDialog(ListScoreActivity.this, KAlertDialog.ERROR_TYPE)
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
                        new KAlertDialog(ListScoreActivity.this, KAlertDialog.ERROR_TYPE)
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
                    new KAlertDialog(ListScoreActivity.this, KAlertDialog.ERROR_TYPE)
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
                    new KAlertDialog(ListScoreActivity.this, KAlertDialog.ERROR_TYPE)
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
                new KAlertDialog(ListScoreActivity.this, KAlertDialog.ERROR_TYPE)
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

        rvListScore.setAdapter(new ListScoreAdapter(getApplicationContext(), scores, this));

//        rvListScore.addOnItemTouchListener(new RecyclerTouchListener(this, rvListScore, new ClickListener() {
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
