package com.example.ld.helper;

import com.example.ld.model.KuisPengetahuanJawaban;
import com.example.ld.model.PostKuisPengetahuanJawaban;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BaseApiService {

    @FormUrlEncoded
    @POST("api/v1/login")
    Call<ResponseBody> loginRequest(@Field("email") String email,
                                    @Field("password") String password);


    // Rest api for FRONT
    @Headers({
            "Accept: application/json",
            "Content-type: application/json",
    })
    @GET("api/v1/getUser")
    Call<ResponseBody> getUser(@Header("Authorization") String auth);

    // Rest api for FRONT
    @Headers({
            "Accept: application/json",
            "Content-type: application/json",
    })
    @GET("api/v1/getAllMateri")
    Call<ResponseBody> getAllMateri(@Header("Authorization") String auth);

    // Rest api for FRONT
    @Headers({
            "Accept: application/json",
            "Content-type: application/json",
    })
    @GET("api/v1/getAllScore")
    Call<ResponseBody> getAllScore(@Header("Authorization") String auth);

    // Rest api for FRONT
    @Headers({
            "Accept: application/json",
            "Content-type: application/json",
    })
    @GET("api/v1/getLastScore")
    Call<ResponseBody> getLastScore(@Header("Authorization") String auth);

    // Rest api for FRONT
    @Headers({
            "Accept: application/json",
            "Content-type: application/json",
    })
    @GET("api/v1/getAllVideo")
    Call<ResponseBody> getAllVideo(@Header("Authorization") String auth);

    // Rest api for FRONT
    @Headers({
            "Accept: application/json",
            "Content-type: application/json",
    })
    @GET("api/v1/getAllKuisPengetahuan")
    Call<ResponseBody> getAllKuisPengetahuan(@Header("Authorization") String auth);

    // Rest api for FRONT
    @Headers({
            "Accept: application/json",
            "Content-type: application/json",
    })
    @GET("api/v1/getAllKuisKeterampilan")
    Call<ResponseBody> getAllKuisKeterampilan(@Header("Authorization") String auth);

    // Rest api for FRONT
    @Headers({
            "Accept: application/json",
            "Content-type: application/json",
    })
    @GET("api/v1/detailKuisKeterampilan")
    Call<ResponseBody> detailKuisKeterampilan(@Header("Authorization") String auth, @Query("id") int id);

    // Rest api for FRONT
    @Headers({
            "Accept: application/json",
            "Content-type: application/json",
    })
    @POST("api/v1/simpanScore")
    Call<ResponseBody> simpanScore(@Header("Authorization") String auth, @Body PostKuisPengetahuanJawaban kuisPengetahuanJawabanArrayList);
}
