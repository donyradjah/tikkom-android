package com.example.ld.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
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
import com.example.ld.model.KuisPengetahuan;
import com.example.ld.model.KuisPengetahuanJawaban;
import com.example.ld.model.PostKuisPengetahuanJawaban;
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

public class KuisPengetahuanActivity extends AppCompatActivity {

    TextView tvPertanyaaan, tvPaging;

    Button btnPrev, btnNext, btnjawab1, btnjawab2, btnjawab3, btnjawab4, btnjawab5;
    ImageView btnMenu;
    TextView txtMenu;
    int page = 0;
    KAlertDialog pDialog;
    Session session;
    BaseApiService apiService;

    boolean start = false;

    ArrayList<KuisPengetahuan> kuisPengetahuans = new ArrayList<>();
    ArrayList<KuisPengetahuanJawaban> pils = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuis_pengetahuan);

        session = new Session(getApplicationContext());
        ButterKnife.bind(this);
        apiService = UrlApi.getAPIService();

        new KAlertDialog(this, KAlertDialog.WARNING_TYPE)
                .setTitleText("Mulai Kuis")
                .setConfirmText("Mulai")
                .setCancelText("Batal")
                .setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(KAlertDialog kAlertDialog) {
                        kAlertDialog.dismissWithAnimation();
                        ambilDataKuisPengetahuan();
                        start = true;
                    }
                })
                .setCancelClickListener(new KAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(KAlertDialog kAlertDialog) {
                        finish();
                    }
                })
                .show();

        KAlertDialog dialogConfirm = new KAlertDialog(this, KAlertDialog.WARNING_TYPE);
        dialogConfirm.setCancelable(false);
        dialogConfirm.setTitleText("Mulai Kuis ?");
        dialogConfirm.setConfirmText("Mulai");
        dialogConfirm.setCancelText("Batal");
        dialogConfirm.setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(KAlertDialog kAlertDialog) {
                dialogConfirm.dismissWithAnimation();
                ambilDataKuisPengetahuan();
                start = true;
            }
        });
        dialogConfirm.setCancelClickListener(new KAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(KAlertDialog kAlertDialog) {
                finish();
            }
        });
        dialogConfirm.setCanceledOnTouchOutside(false);
        dialogConfirm.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        dialogConfirm.setCancelable(false);

//        if (!dialogConfirm.isShowing()) {
//            dialogConfirm.show();
//        }

                pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Sedang Mengambil Data ...");
        pDialog.setCancelable(false);


        tvPertanyaaan = findViewById(R.id.tvPertanyaaan);
        tvPaging = findViewById(R.id.tvPaging);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnjawab1 = findViewById(R.id.btnjawab1);
        btnjawab2 = findViewById(R.id.btnjawab2);
        btnjawab3 = findViewById(R.id.btnjawab3);
        btnjawab4 = findViewById(R.id.btnjawab4);
        btnjawab5 = findViewById(R.id.btnjawab5);

        btnMenu = findViewById(R.id.btnMenu);
        txtMenu = findViewById(R.id.txtMenu);
        btnMenu.setImageResource(R.drawable.ic_arrow_back);
        txtMenu.setText("Kuis Pengetahuan");

        btnMenu.setVisibility(View.GONE);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    void ambilDataKuisPengetahuan() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }

        apiService.getAllKuisPengetahuan("Bearer " + session.getString("token")).enqueue(new Callback<ResponseBody>() {
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
                                new KAlertDialog(KuisPengetahuanActivity.this, KAlertDialog.ERROR_TYPE)
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
                                    new KAlertDialog(KuisPengetahuanActivity.this, KAlertDialog.WARNING_TYPE)
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
                                        kuisPengetahuans.add(new KuisPengetahuan(
                                                materiObject.getInt("id"),
                                                materiObject.getString("pertanyaan"),
                                                materiObject.getString("jawab1"),
                                                materiObject.getString("jawab2"),
                                                materiObject.getString("jawab3"),
                                                materiObject.getString("jawab4"),
                                                materiObject.getString("jawab5"),
                                                materiObject.getString("kunci")
                                        ));

                                        pils.add(new KuisPengetahuanJawaban(
                                                materiObject.getInt("id"),
                                                materiObject.getString("pertanyaan"),
                                                materiObject.getString("jawab1"),
                                                materiObject.getString("jawab2"),
                                                materiObject.getString("jawab3"),
                                                materiObject.getString("jawab4"),
                                                materiObject.getString("jawab5"),
                                                materiObject.getString("kunci"),
                                                "0",
                                                "salah"
                                        ));

                                    }

                                    setSoal(0);

                                    pDialog.dismissWithAnimation();
                                }
                            }

                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                            pDialog.dismissWithAnimation();

                            new KAlertDialog(KuisPengetahuanActivity.this, KAlertDialog.ERROR_TYPE)
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
                        new KAlertDialog(KuisPengetahuanActivity.this, KAlertDialog.ERROR_TYPE)
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
                    new KAlertDialog(KuisPengetahuanActivity.this, KAlertDialog.ERROR_TYPE)
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
                    new KAlertDialog(KuisPengetahuanActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Gagal Mengambil Data")
                            .setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(KAlertDialog kAlertDialog) {
                                    finish();
                                }
                            })
                            .show();

                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.dismissWithAnimation();
                new KAlertDialog(KuisPengetahuanActivity.this, KAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Gagal Mengambil Data")
                        .setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(KAlertDialog kAlertDialog) {
                                finish();
                            }
                        })
                        .show();

                finish();
            }
        });
    }


    void setSoal(int i) {


        page = i;
        KuisPengetahuan kuisPengetahuan = kuisPengetahuans.get(i);

        tvPertanyaaan.setText(kuisPengetahuan.getPertanyaan());
        btnjawab1.setText(kuisPengetahuan.getJawab1());
        btnjawab2.setText(kuisPengetahuan.getJawab2());
        btnjawab3.setText(kuisPengetahuan.getJawab3());
        btnjawab4.setText(kuisPengetahuan.getJawab4());
        btnjawab5.setText(kuisPengetahuan.getJawab5());
        tvPaging.setText((i + 1) + " / " + kuisPengetahuans.size());

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = (page + 1) < kuisPengetahuans.size() ? (page + 1) : (kuisPengetahuans.size() - 1);
                setSoal(page);
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = (page - 1) <= 0 ? 0 : (page - 1);
                setSoal(page);
            }
        });

        btnjawab1.setOnClickListener(view -> pilihSoal(i, "1"));

        btnjawab2.setOnClickListener(view -> pilihSoal(i, "2"));

        btnjawab3.setOnClickListener(view -> pilihSoal(i, "3"));

        btnjawab4.setOnClickListener(view -> pilihSoal(i, "4"));

        btnjawab5.setOnClickListener(view -> pilihSoal(i, "5"));

        if (page == (kuisPengetahuans.size() - 1)) {
            btnNext.setText("Selesai");
            btnNext.setOnClickListener(view -> hitungScore());
        } else {
            btnNext.setText("next");
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    page = (page + 1) < kuisPengetahuans.size() ? (page + 1) : (kuisPengetahuans.size() - 1);
                    setSoal(page);
                }
            });
        }

        KuisPengetahuanJawaban pil = pils.get(i);

        btnjawab1.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
        btnjawab2.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
        btnjawab3.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
        btnjawab4.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
        btnjawab5.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

        if (pil.getJawab().equals("1")) {
            btnjawab1.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorPrimaryDark));
        } else if (pil.getJawab().equals("2")) {
            btnjawab2.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorPrimaryDark));
        } else if (pil.getJawab().equals("3")) {
            btnjawab3.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorPrimaryDark));
        } else if (pil.getJawab().equals("4")) {
            btnjawab4.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorPrimaryDark));
        } else if (pil.getJawab().equals("5")) {
            btnjawab5.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorPrimaryDark));
        }
    }

    void pilihSoal(int i, String pil) {

        KuisPengetahuan kuisPengetahuan = kuisPengetahuans.get(i);

        String status = "";

        if (kuisPengetahuan.getKunci().equals(pil)) {
            status = "benar";
        } else {
            status = "salah";
        }

        KuisPengetahuanJawaban pilJawaban = new KuisPengetahuanJawaban(
                kuisPengetahuan.getId(),
                kuisPengetahuan.getPertanyaan(),
                kuisPengetahuan.getJawab1(),
                kuisPengetahuan.getJawab2(),
                kuisPengetahuan.getJawab3(),
                kuisPengetahuan.getJawab4(),
                kuisPengetahuan.getJawab5(),
                kuisPengetahuan.getKunci(),
                pil,
                status
        );

        pils.set(i, pilJawaban);

        btnjawab1.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
        btnjawab2.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
        btnjawab3.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
        btnjawab4.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
        btnjawab5.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

        if (pil.equals("1")) {
            btnjawab1.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorPrimaryDark));
        } else if (pil.equals("2")) {
            btnjawab2.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorPrimaryDark));
        } else if (pil.equals("3")) {
            btnjawab3.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorPrimaryDark));
        } else if (pil.equals("4")) {
            btnjawab4.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorPrimaryDark));
        } else if (pil.equals("5")) {
            btnjawab5.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorPrimaryDark));
        }
    }

    void hitungScore() {

        if (!pDialog.isShowing()) {
            pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Sedang Mengambil Data ...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        PostKuisPengetahuanJawaban postKuisPengetahuanJawaban = new PostKuisPengetahuanJawaban(pils);
        apiService.simpanScore("Bearer " + session.getString("token"), postKuisPengetahuanJawaban).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        pDialog.dismissWithAnimation();
                        new KAlertDialog(KuisPengetahuanActivity.this, KAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success")
                                .setContentText("Data Berhasil Di kirim")
                                .setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog kAlertDialog) {
                                        Intent intent = new Intent(getApplicationContext(), SkorActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .show();
                    } else {
                        pDialog.dismissWithAnimation();
                        new KAlertDialog(KuisPengetahuanActivity.this, KAlertDialog.ERROR_TYPE)
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
                    pDialog.dismiss();
                    new KAlertDialog(KuisPengetahuanActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Gagal Mengirim Data")
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
                new KAlertDialog(KuisPengetahuanActivity.this, KAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Gagal Mengirim Data")
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

    @Override
    public void onBackPressed() {

        if (start) {
            new KAlertDialog(KuisPengetahuanActivity.this, KAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Selesaikan Kuis")
                    .show();
        } else {
            finish();
        }
        return;
    }
}
