package com.example.ld.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class DetailMateriActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_detail_materi);
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
        txtMenu.setText("Materi");

        dirPath = Utils.getRootDirPath(getApplicationContext());

        Intent intent = getIntent();

        id = intent.getIntExtra("id", 0);
        namaMateri = intent.getStringExtra("namaMateri");
        file = intent.getStringExtra("file");

        tvNamaMateri.setText(namaMateri);

        url = UrlApi.BASE_URL_API + "public/upload/pdf/" + file;

        File tempFile = new File(dirPath + "/" + file);
        boolean exists = tempFile.exists();
        if (!exists) {
            DownloadPdf.setVisibility(View.VISIBLE);
            pdfView.setVisibility(View.GONE);
            download();
        } else {
            DownloadPdf.setVisibility(View.GONE);
            pdfView.setVisibility(View.VISIBLE);
            openPdf();

        }

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void download() {
        int downloadId = PRDownloader.download(url, dirPath, file)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        tvProgress.setText(bytesToMeg(progress.currentBytes) + " Mb / " + bytesToMeg(progress.totalBytes) + " Mb");
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        tvProgress.setText("SELESAI");
                        Log.wtf("SIMPAN DOWNLOAD", dirPath);
                        DownloadPdf.setVisibility(View.GONE);
                        pdfView.setVisibility(View.VISIBLE);
                        openPdf();
                    }

                    @Override
                    public void onError(Error error) {

                    }

                });
    }


    public static long bytesToMeg(long bytes) {
        return bytes / MEGABYTE;
    }

    void openPdf() {
        File pdf = new File(dirPath + "/" + file);
        Log.wtf("PDF", pdf.getAbsolutePath());
        pdfView.fromFile(pdf)
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
                        new KAlertDialog(DetailMateriActivity.this, KAlertDialog.ERROR_TYPE)
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
}
