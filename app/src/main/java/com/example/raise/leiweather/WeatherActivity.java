package com.example.raise.leiweather;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.raise.leiweather.bean.Weather;
import com.example.raise.leiweather.utils.HttpUtils;
import com.example.raise.leiweather.utils.Utility;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    TextView toobar_city;
    @BindView(R.id.toolbar_update_time)
    TextView toolbar_update_time;
    @BindView(R.id.now_degree)
    TextView now_degree;
    @BindView(R.id.now_weather_status)
    TextView now_weather_status;
    @BindView(R.id.foreast_layout)
    LinearLayout foreast_layout;
    @BindView(R.id.aqi_text)
    TextView aqi_text;
    @BindView(R.id.pm25_text)
    TextView pm25_text;
    @BindView(R.id.comf_text)
    TextView comf_text;
    @BindView(R.id.wash_car_text)
    TextView wash_car_text;
    @BindView(R.id.sport_text)
    TextView sport_text;
    @BindView(R.id.weather_bg)
    ImageView weather_bg;
    @BindView(R.id.swipe_refresh)
    public SwipeRefreshLayout swipeRefreshLayout;
    TextView date_text;
    TextView info_text;
    TextView max_temp;
    TextView min_temp;
    @BindView(R.id.nav_button)
    Button nav_button;
    /*@BindView(R.id.foreast_item)
    LinearLayout foreast_item;*/
    String weatherId;
    //@BindView(R.id.drawer)
    public DrawerLayout drawerLayout;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=21){

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_weather);
        toobar_city = (TextView) findViewById(R.id.toobar_city);
        //String cityName = getIntent().getStringExtra("cityName");
        //toobar_city.setText(cityName);
        ButterKnife.bind(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        weatherId = getIntent().getStringExtra("weatherId");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        nav_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        if (sharedPreferences.getString("weather",null)!=null){
            Weather weather = Utility.handleWeatherResponse(sharedPreferences.getString("weather",""));
            weatherId = weather.getHeWeather5().get(0)
                    .getBasic().getId();

                    showWeatherInfo(weather);

        }else{

            requestWeather(weatherId);
        }
       if (sharedPreferences.getString("bing_pic",null)!=null){

           Glide.with(this).load(sharedPreferences.getString("bing_pic",null)).into(weather_bg);

       }else{
           loadWeatherbg();
       }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("wanglei ", "onRefresh: begin");

                requestWeather(weatherId);
            }
        });

          swipeRefreshLayout.post(new Runnable() {
              @Override
              public void run() {
                  if (!swipeRefreshLayout.isRefreshing())
                  {
                      swipeRefreshLayout.setRefreshing(true);
                      requestWeather(weatherId);
                  }
              }
          });
    }

    private void loadWeatherbg() {

        HttpUtils.sendOkHttpRequest("http://guolin.tech/api/bing_pic", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String picUrl = response.body().string();
                Log.d("wl123", "onResponse: picurl= "+picUrl);
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this)
                           .edit();
                editor.putString("bing_pic",picUrl);
                editor.apply();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(WeatherActivity.this)
                                    .load(picUrl)
                                    .into(weather_bg);
                        }
                    });
            }
        });
    }

    public void requestWeather(final String weatherId) {
//https://free-api.heweather.com/v5/weather?city
        HttpUtils.sendOkHttpRequest("https://free-api.heweather.com/v5/weather?city=" +
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
                if (weather!=null&&weather.getHeWeather5().get(0).getStatus().equals("ok")){

                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this)
                            .edit();
                    editor.putString("weather",content);
                    editor.commit();
                    showWeatherInfo(weather);
                    loadWeatherbg();

                }
            }
        });    }



   /* TextView toobar_city;
    @BindView(R.id.toolbar_update_time)
    TextView toolbar_update_time;
    @BindView(R.id.now_degree)
    TextView now_degree;
    @BindView(R.id.now_weather_status)
    TextView now_weather_status;
    @BindView(R.id.foreast_layout)
    LinearLayout foreast_layout;
    @BindView(R.id.aqi_text)
    TextView aqi_text;
    @BindView(R.id.pm25_text)
    TextView pm25_text;
   @BindView(R.id.date_text)
   TextView date_text;
    @BindView(R.id.info_text)
    TextView info_text;
    @BindView(R.id.max_temp)
    TextView max_temp;
    @BindView(R.id.min_temp)
    TextView min_temp;*/
    private void showWeatherInfo(final Weather weather) {
        Log.d("wanglei ", "showWeatherInfo: size= "+weather.getHeWeather5().size());
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               toobar_city.setText(weather.getHeWeather5().get(0).getBasic().getCity());
               toolbar_update_time.setText(weather.getHeWeather5().get(0)
               .getBasic().getUpdate().getLoc().split(" ")[1]);
               now_degree.setText(weather.getHeWeather5().get(0).getNow().getTmp());
               now_weather_status.setText(weather.getHeWeather5().get(0)
               .getNow().getCond().getTxt());
               int size = weather.getHeWeather5().get(0).getDaily_forecast().size();
               Log.d("wanglei ", "run: size= "+size);

               List<Weather.HeWeather5Bean.DailyForecastBean> dailyForecastBeanList = weather.getHeWeather5()
                       .get(0).getDaily_forecast();
               foreast_layout.removeAllViews();
               for (int i = 0; i < dailyForecastBeanList.size(); i++) {
                    Weather.HeWeather5Bean.DailyForecastBean dailyForecastBean = dailyForecastBeanList.get(i);
                   View view = LayoutInflater.from(WeatherActivity.this).inflate(R.layout.foreast_item,foreast_layout,false);
                    date_text = (TextView) view.findViewById(R.id.date_text);
                    date_text.setText(dailyForecastBean.getDate());
                    info_text = (TextView) view.findViewById(R.id.info_text);
                    info_text.setText(dailyForecastBean.getCond().getTxt_d());
                    max_temp = (TextView) view.findViewById(R.id.max_temp);
                    max_temp.setText(dailyForecastBean.getTmp().getMax());
                    min_temp = (TextView) view.findViewById(R.id.min_temp);
                    min_temp.setText(dailyForecastBean.getTmp().getMin());
                    foreast_layout.addView(view);
                    aqi_text.setText(weather.getHeWeather5().get(0)
                   .getAqi().getCity().getAqi());
                   pm25_text.setText(weather.getHeWeather5().get(0)
                           .getAqi().getCity().getPm25());

                   comf_text.setText(weather.getHeWeather5().get(0)
                   .getSuggestion().getComf().getTxt());
                   wash_car_text.setText(weather.getHeWeather5().get(0)
                   .getSuggestion().getCw().getTxt());
                   sport_text.setText(weather.getHeWeather5().get(0)
                   .getSuggestion().getUv().getTxt());
                  swipeRefreshLayout.setRefreshing(false);
               }

           }
       });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
            //swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }
}
