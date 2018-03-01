package com.example.raise.leiweather.utils;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by raise on 18-3-1.
 */

public class HttpUtils {

    public static void sendOkHttpRequest(String address, Callback callback){

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request =new Request.Builder().url(address).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

}
