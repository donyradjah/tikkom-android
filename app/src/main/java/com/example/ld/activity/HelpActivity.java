package com.example.ld.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.example.ld.R;
import com.example.ld.helper.BaseApiService;
import com.example.ld.helper.Session;
import com.example.ld.helper.UrlApi;
import com.example.ld.helper.Utils;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.kinda.alert.KAlertDialog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import butterknife.ButterKnife;

public class HelpActivity extends AppCompatActivity {

    String dirPath, url;

    KAlertDialog pDialog;
    Session session;
    BaseApiService apiService;

    TextView tvProgress, tvNamaMateri;
    ImageView btnMenu;
    TextView txtMenu, totalHalaman;
    RelativeLayout DownloadPdf;

    PDFView pdfView;
    int id;
    String namaMateri, file;

    Button btnPrev, btnNext;
    EditText spinnerPage;
    int currentPage = 0, maxPage = 0;

    private static final long MEGABYTE = 1024L * 1024L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_pdf);

        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setReadTimeout(30_000)
                .setConnectTimeout(30_000)
                .build();

        PRDownloader.initialize(getApplicationContext(), config);
        session = new Session(getApplicationContext());
        ButterKnife.bind(this);
        apiService = UrlApi.getAPIService();

        btnMenu = findViewById(R.id.btnMenu);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        spinnerPage = findViewById(R.id.spinnerPage);
        DownloadPdf = findViewById(R.id.DownloadPdf);
        txtMenu = findViewById(R.id.txtMenu);
        totalHalaman = findViewById(R.id.totalHalaman);
        tvNamaMateri = findViewById(R.id.tvNamaMateri);
        pdfView = findViewById(R.id.pdfView);
        tvProgress = findViewById(R.id.tvProgress);
        btnMenu.setImageResource(R.drawable.ic_arrow_back);

        txtMenu.setText("Help");
        dirPath = Utils.getRootDirPath(getApplicationContext());

        Intent intent = getIntent();

        id = intent.getIntExtra("id", 0);
        namaMateri = intent.getStringExtra("namaMateri");
        file = intent.getStringExtra("file");
        tvNamaMateri.setVisibility(View.GONE);
        tvNamaMateri.setText(namaMateri);


        DownloadPdf.setVisibility(View.GONE);
        pdfView.setVisibility(View.VISIBLE);
        openPdf();


        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public static long bytesToMeg(long bytes) {
        return bytes / MEGABYTE;
    }

    void openPdf() {
        File pdf = new File(dirPath + "/" + file);
        Log.wtf("PDF", pdf.getAbsolutePath());

        pdfView.fromAsset("help.pdf")
                .defaultPage(0)
                .enableSwipe(false)
                .swipeHorizontal(false)
                .pageSnap(false)
                .pageFling(true)
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                        totalHalaman.setText("Total Halaman : " + nbPages);
                        maxPage = nbPages - 1;
                    }
                })
                .onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        new KAlertDialog(HelpActivity.this, KAlertDialog.ERROR_TYPE)
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
                }).onPageChange(new OnPageChangeListener() {
            @Override
            public void onPageChanged(int page, int pageCount) {
                spinnerPage.setText((page + 1) + "");
                currentPage = page;
            }
        }).pageFitPolicy(FitPolicy.BOTH)
                .load();

        spinnerPage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    int page = Integer.parseInt(v.getText().toString()) - 1;

                    if (page >= maxPage) {
                        pdfView.jumpTo(maxPage);
                    } else {
                        pdfView.jumpTo(page);
                    }

                    handled = true;
                }
                return handled;
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int page = currentPage - 1;

                if (page < 0) {
                    pdfView.jumpTo(0);
                } else {
                    pdfView.jumpTo(page);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int page = currentPage + 1;

                if (page >= maxPage) {
                    pdfView.jumpTo(maxPage);
                } else {
                    pdfView.jumpTo(page);
                }
            }
        });
    }

    public String LoadData(String inFile) {
        String tContents = "";

        try {
            InputStream stream = getAssets().open(inFile);

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
        } catch (IOException e) {
            // Handle exceptions here

            e.printStackTrace();
        }

        return tContents;

    }
}
