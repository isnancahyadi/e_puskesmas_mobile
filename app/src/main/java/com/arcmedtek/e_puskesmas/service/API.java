package com.arcmedtek.e_puskesmas.service;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface API {
    //@FormUrlEncoded
    @Multipart
    @POST("pasien")
    Call<ResponseBody> createDataPatient(
            @Part("nik") String nik,
            @Part("nama_depan") String frontName,
            @Part("nama_tengah") String middleName,
            @Part("nama_belakang") String lastName,
            @Part("jenis_kelamin") String gender,
            @Part("tempat_lahir") String birthPlace,
            @Part("tanggal_lahir") String birthDate,
            @Part("alamat") String address,
            @Part MultipartBody.Part photoKTP
    );
}
