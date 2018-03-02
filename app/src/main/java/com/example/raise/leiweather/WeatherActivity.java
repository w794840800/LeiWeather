package com.example.raise.leiweather;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.raise.leiweather.bean.Weather;
import com.example.raise.leiweather.utils.HttpUtils;
import com.example.raise.leiweather.utils.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
TextView toobar_city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        toobar_city = (TextView) findViewById(R.id.toobar_city);
        //String cityName = getIntent().getStringExtra("cityName");
        //toobar_city.setText(cityName);
        String weatherId = getIntent().getStringExtra("weatherId");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getString("weather",null)!=null){
            Weather weather = Utility.handleWeatherResponse(sharedPreferences.getString("weather",null));

            showWeatherInfo(weather);

        }else{

            requestWeather(weatherId);
        }




    }

    private void requestWeather(final String weatherId) {

        HttpUtils.sendOkHttpRequest("https://free-api.heweather.com/s6/weather/forecast?location=" +
                weatherId+"&key=cb57347356fb435a891c952e5166a9ed", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //
                String content =  response.body().string();
                Log.d("wanglei1234", "onCreate: id= "+content);
               Weather weather = Utility.handleWeatherResponse(content);
                if (weather!=null&&weather.getHeWeather6().get(0).getStatus().equals("ok")){

                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this)
                            .edit();
                    editor.putString("weather",content);
                    editor.commit();
                    showWeatherInfo(weather);
                }
            }
        });    }

    private void showWeatherInfo(Weather weather) {
        Log.d("wanglei ", "showWeatherInfo: size= "+weather.getHeWeather6().size());
    toobar_city.setText(weather.getHeWeather6().get(0).getUpdate().getUtc());

    }
}
