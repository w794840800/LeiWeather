package com.example.raise.leiweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.raise.leiweather.utils.HttpUtils;
import com.example.raise.leiweather.utils.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* HttpUtils.sendOkHttpRequest("http://guolin.tech/api/china/", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.d("wanglei", "onResponse: "+(response.body().string()));

                Utility.handleProvinceJson(response.body().string());

            }
        });


        HttpUtils.sendOkHttpRequest("http://guolin.tech/api/china/13", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.d("wanglei", "onResponse: "+(response.body().string()));

                Utility.handleCityJson(response.body().string(),13);

            }
        });
        HttpUtils.sendOkHttpRequest("http://guolin.tech/api/china/13/67", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.d("wanglei", "onResponse: "+(response.body().string()));

                Utility.handleCountryJson(response.body().string(),67);

            }
        });
   */ }
}
