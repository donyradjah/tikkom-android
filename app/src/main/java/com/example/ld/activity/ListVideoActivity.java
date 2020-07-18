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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appunite.appunitevideoplayer.PlayerActivity;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.example.ld.R;
import com.example.ld.adapter.ListMateriAdapter;
import com.example.ld.adapter.ListVideoAdapter;
import com.example.ld.adapter.RecyclerTouchListener;
import com.example.ld.helper.BaseApiService;
import com.example.ld.helper.ClickListener;
import com.example.ld.helper.Session;
import com.example.ld.helper.UrlApi;
import com.example.ld.helper.Utils;
import com.example.ld.model.Video;
import com.kinda.alert.KAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListVideoActivity extends AppCompatActivity {

    String dirPath, url;

    KAlertDialog pDialog;
    Session session;
    BaseApiService apiService;

    ArrayList<Video> videos = new ArrayList<>();
    int i;

    RecyclerView rvListVideo;
    RelativeLayout DownloadPdf;
    ImageView btnMenu;
    TextView txtMenu, tvProgress;
    GridLayoutManager layoutmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_video);

        session = new Session(getApplicationContext());
        ButterKnife.bind(this);
        apiService = UrlApi.getAPIService();
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setReadTimeout(30_000)
                .setConnectTimeout(30_000)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);

        pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Sedang Mengambil Data ...");
        pDialog.setCancelable(false);

        rvListVideo = findViewById(R.id.rvListVideo);
        btnMenu = findViewById(R.id.btnMenu);
        txtMenu = findViewById(R.id.txtMenu);
        tvProgress = findViewById(R.id.tvProgress);
        DownloadPdf = findViewById(R.id.DownloadPdf);


        layoutmanager = new GridLayoutManager(this, 1);
        rvListVideo.setLayoutManager(layoutmanager);
        rvListVideo.setHasFixedSize(true);
        rvListVideo.setItemAnimator(new DefaultItemAnimator());

        btnMenu.setImageResource(R.drawable.ic_arrow_back);
        txtMenu.setText("Daftar Video");

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rvListVideo.setVisibility(View.VISIBLE);
        DownloadPdf.setVisibility(View.GONE);
        ambilDataVideo();
    }

    void ambilDataVideo() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }

        apiService.getAllVideo("Bearer " + session.getString("token")).enqueue(new Callback<ResponseBody>() {
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
                                new KAlertDialog(ListVideoActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Terjadi Kesalahan Saat Mengambil Data")
                                        .show();
                            } else {
                                if (result.getInt("total") <= 0) {
                                    pDialog.dismissWithAnimation();
                                    new KAlertDialog(ListVideoActivity.this, KAlertDialog.WARNING_TYPE)
                                            .setTitleText("Oops...")
                                            .setContentText("Data Kosong")
                                            .show();
                                } else {
                                    JSONArray materiArray = result.getJSONArray("data");

                                    for (int i = 0; i < materiArray.length(); i++) {
                                        JSONObject materiObject = materiArray.getJSONObject(i);

                                        videos.add(new Video(materiObject.getInt("id")
                                                , materiObject.getString("namaVideo"),
                                                materiObject.getString("thumbnail"), materiObject.getString("file")));
                                        pDialog.dismissWithAnimation();
                                        setData();
                                    }

                                }
                            }

                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                            pDialog.dismissWithAnimation();

                            new KAlertDialog(ListVideoActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Terjadi Kesalahan Saat Mengambil Data")
                                    .show();

                            finish();
                        }
                    } else {
                        pDialog.dismissWithAnimation();
                        new KAlertDialog(ListVideoActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Terjadi Kesalahan Saat Mengambil Data")
                                .show();

                        finish();
                    }
                } else if (response.code() == 401) {
                    pDialog.dismissWithAnimation();
                    new KAlertDialog(ListVideoActivity.this, KAlertDialog.ERROR_TYPE)
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
                    new KAlertDialog(ListVideoActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Gagal Mengambil Data")
                            .show();

                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.dismissWithAnimation();
                new KAlertDialog(ListVideoActivity.this, KAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Gagal Mengambil Data")
                        .show();

                finish();
            }
        });
    }


    void setData() {
        rvListVideo.setVisibility(View.VISIBLE);
        DownloadPdf.setVisibility(View.GONE);
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
        rvListVideo.setAdapter(new ListVideoAdapter(this, getApplicationContext(), videos));

        rvListVideo.addOnItemTouchListener(new RecyclerTouchListener(this, rvListVideo, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        pDialog.dismissWithAnimation();
    }
}
