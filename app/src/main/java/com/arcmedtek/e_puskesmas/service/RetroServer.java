package com.arcmedtek.e_puskesmas.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroServer {
    //private static final String IPCONF = "192.168.1.105";
    private static final String BASE_URL = "http://192.168.1.105/e-puskesmas/public/restapi/";
    private static RetroServer mInstance;
    private static Retrofit retrofit;

    private RetroServer() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetroServer getInstance() {
        if (mInstance == null) {
            mInstance = new RetroServer();
        }
        return mInstance;
    }

    public API getAPI() {
        return retrofit.create(API.class);
    }
}
