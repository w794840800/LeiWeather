package com.example.raise.leiweather.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.raise.leiweather.bean.Weather;
import com.example.raise.leiweather.db.City;
import com.example.raise.leiweather.db.Country;
import com.example.raise.leiweather.db.Province;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Created by raise on 18-3-1.
 */

public class Utility {

  public static boolean handleProvinceJson(String response){

      if (!TextUtils.isEmpty(response)){

          try {
              JSONArray jsonArray = new JSONArray(response);
              for (int i = 0; i < jsonArray.length(); i++) {
                  JSONObject jsonObject = jsonArray.getJSONObject(i);
                  Province province = new Province();
                  province.setProviceCode(jsonObject.getInt("id"));
                  province.setProviceName(jsonObject.getString("name"));
                  Log.d("wanglei", "handleProvinceJson: code= "+province.getProviceCode()+" name= "
                  +province.getProviceName());
                  province.save();
              }
          } catch (JSONException e) {
              e.printStackTrace();
          }
          return true;

      }

    return false;
  }

  public static Weather handleWeatherResponse(String response){

      Weather weather = new Gson().fromJson(response,Weather.class);
      Log.d(TAG, "handleWeatherResponse: weather = "+weather.getHeWeather6().get(0).getBasic().getCnty());
      return weather;
  }

    public static boolean handleCityJson(String response,int provinceId){

        if (!TextUtils.isEmpty(response)){

            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    City city= new City();
                    city.setCityCode(jsonObject.getInt("id"));
                    city.setCityName(jsonObject.getString("name"));
                    city.setProvinceId(provinceId);
                    Log.d("wanglei", "handleCityJson: code= "+city.getCityName()+" name= "
                            +city.getCityCode());
                    city.save();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;

        }

        return false;
    }
    public static boolean handleCountryJson(String response,int CityId){

        if (!TextUtils.isEmpty(response)){

            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Country country= new Country();
                    country.setCountyCode(jsonObject.getInt("id"));
                    country.setCountryName(jsonObject.getString("name"));
                    country.setWeatherId(jsonObject.getString("weather_id"));
                    country.setCityId(CityId);
                    Log.d("wanglei", "handleCountryJson: code= "+country.getCountryName()+" name= "
                            +country.getWeatherId()+" "+country.getCountyCode());
                    country.save();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;

        }

        return false;
    }
}
