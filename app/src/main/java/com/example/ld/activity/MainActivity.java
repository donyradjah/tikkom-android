package com.example.ld.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.ld.R;
import com.example.ld.helper.BaseApiService;
import com.example.ld.helper.Session;
import com.example.ld.helper.UrlApi;
import com.kinda.alert.KAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText etUser, etPass;

    KAlertDialog pDialog;
    Session session;
    BaseApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new Session(getApplicationContext());
        ButterKnife.bind(this);
        apiService = UrlApi.getAPIService();

        pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Sedang memproses login..");
        pDialog.setCancelable(false);

        etUser = findViewById(R.id.editText_Username);
        etPass = findViewById(R.id.editText_Password);

        if (session.getBoolean("login")) {
            Intent menu = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(menu);

            finish();
        }


    }

    boolean validate() {

        boolean valid = true;

        if (etUser.getText().toString() == "" || etUser.getText().toString().isEmpty()) {
            new KAlertDialog(this, KAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Masukkan username/email dan password dengan benar!")
                    .show();
            return false;
        } else if (etPass.getText().toString() == "" || etPass.getText().toString().isEmpty()) {
            new KAlertDialog(this, KAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Masukkan username/email dan password dengan benar!")
                    .show();
            return false;
        } else {
            return true;
        }
    }

    public void login(View view) {
        if (validate()) {

            pDialog.show();

            apiService.loginRequest(etUser.getText().toString(), etPass.getText().toString()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        if (response.isSuccessful()) {
                            try {
                                String data = response.body().string();
                                Log.d("LOGIN", data);
                                JSONObject result = new JSONObject(data);

                                Log.d("LOGIN", result.getString("success"));
                                if (!result.getBoolean("success")) {
                                    pDialog.dismiss();
                                    new KAlertDialog(MainActivity.this, KAlertDialog.ERROR_TYPE)
                                            .setTitleText("Oops...")
                                            .setContentText("Data Login Tidak Valid")
                                            .show();
                                } else {
                                    pDialog.dismiss();
                                    session.add("token", result.getString("token"));
                                    session.add("login", result.getBoolean("success"));

                                    Intent menu = new Intent(getApplicationContext(), MenuActivity.class);
                                    startActivity(menu);

                                    finish();
                                }

                            } catch (JSONException | IOException | NullPointerException e) {
                                e.printStackTrace();
                                pDialog.dismiss();
                            }
                        } else {
                            pDialog.dismiss();
                            new KAlertDialog(MainActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Data Login Tidak Valid")
                                    .show();
                        }
                    } else {
                        pDialog.dismiss();
                        new KAlertDialog(MainActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Data Login Tidak Valid")
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pDialog.dismiss();
                }
            });
        } else {
            new KAlertDialog(this, KAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Masukkan username/email dan password dengan benar!")
                    .show();
        }
    }
}
