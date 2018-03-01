package com.example.raise.leiweather.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.raise.leiweather.db.Province;

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


}
