package com.example.ld.activity;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
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
import com.example.ld.adapter.ListDaftarMateriAdapter;
import com.example.ld.adapter.ListMateriAdapter;
import com.example.ld.adapter.RecyclerTouchListener;
import com.example.ld.helper.BaseApiService;
import com.example.ld.helper.ClickListener;
import com.example.ld.helper.Session;
import com.example.ld.helper.UrlApi;
import com.example.ld.helper.Utils;
import com.example.ld.model.DaftarMateri;
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
    View overlay;

    ArrayList<DaftarMateri> daftarMateris = new ArrayList<>();

    ConstraintLayout listContainer;

    PDFView pdfView;
    int id;
    String namaMateri, file;

    Button btnPrev, btnNext, btndaftarIsi;
    EditText spinnerPage;
    boolean openOverlay = false;
    int currentPage = 0, maxPage = 0;

    private static final long MEGABYTE = 1024L * 1024L;

    RecyclerView daftarIsi;
    GridLayoutManager layoutmanager;

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
        btndaftarIsi = findViewById(R.id.btndaftarIsi);
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
        overlay = findViewById(R.id.overlay);
        listContainer = findViewById(R.id.listContainer);


        overlay.setVisibility(View.GONE);
        listContainer.setVisibility(View.GONE);

        btndaftarIsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOverlay = true;
                toggle();
            }
        });

        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOverlay = false;
                toggle();
            }
        });

        dirPath = Utils.getRootDirPath(getApplicationContext());

        Intent intent = getIntent();

        id = intent.getIntExtra("id", 0);
        namaMateri = intent.getStringExtra("namaMateri");
        file = intent.getStringExtra("file");
        daftarMateris = (ArrayList<DaftarMateri>) intent.getSerializableExtra("daftarIsi");
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

        for (DaftarMateri daftarMateri : daftarMateris) {
            Log.d("Daftar Isi ", daftarMateri.getJudul());
        }

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        daftarIsi = findViewById(R.id.daftarIsi);

        layoutmanager = new GridLayoutManager(this, 1);
        daftarIsi.setLayoutManager(layoutmanager);
        daftarIsi.setHasFixedSize(true);
        daftarIsi.setItemAnimator(new DefaultItemAnimator());

        daftarIsi.setAdapter(new ListDaftarMateriAdapter(getApplicationContext(), daftarMateris, this));

        daftarIsi.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), daftarIsi, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.d("Halaman", daftarMateris.get(position).getHalaman() + "");
                int page = daftarMateris.get(position).getHalaman() - 1;

                if (page >= maxPage) {
                    pdfView.jumpTo(maxPage);
                } else {
                    pdfView.jumpTo(page);
                }

                openOverlay = false;
                toggle();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public void onBackPressed() {
        if (openOverlay) {
            openOverlay = false;
            toggle();
        } else {
            super.onBackPressed();
        }
    }

    private void toggle() {
        Transition transition = new Slide(Gravity.LEFT);
        transition.setDuration(600);
        transition.addTarget(R.id.listContainer);

        TransitionManager.beginDelayedTransition(getWindow().getDecorView().findViewById(android.R.id.content), transition);
        overlay.setVisibility(openOverlay ? View.VISIBLE : View.GONE);
        listContainer.setVisibility(openOverlay ? View.VISIBLE : View.GONE);
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
