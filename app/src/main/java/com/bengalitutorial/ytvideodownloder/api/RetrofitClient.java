package com.bengalitutorial.ytvideodownloder.api;

import com.bengalitutorial.ytvideodownloder.constans.Constans;
import com.bengalitutorial.ytvideodownloder.service.ServiceInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit=null;
    private static RetrofitClient mInstance;

    public RetrofitClient() {
        if (retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constans.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }

    public static synchronized RetrofitClient getInstance(){
        if (mInstance==null){
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }


    public ServiceInterface getService(){

        return retrofit.create(ServiceInterface.class);
    }
}
