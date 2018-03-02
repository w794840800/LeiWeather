package com.example.raise.leiweather;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raise.leiweather.adapter.ChooseRecyclerAdapter;
import com.example.raise.leiweather.db.City;
import com.example.raise.leiweather.db.Country;
import com.example.raise.leiweather.db.Province;
import com.example.raise.leiweather.utils.HttpUtils;
import com.example.raise.leiweather.utils.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by raise on 18-3-1.
 */

public class ChooseFragment extends Fragment{
    int currentLevel;
    ArrayList<Province> mProvinceList;
    ArrayList<City> mCityList;
    ArrayList<Country>mCountryArrayList;
    Province selectProvince;
    City selectCity;
    Country selectCountry;
    ChooseRecyclerAdapter mChooseRecyclerAdapter;
    ArrayList<String> mDateList;
    RecyclerView mRecyclerView;
    Button back_bt;
    TextView select_tv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false);
        mDateList = new ArrayList<>();
        mChooseRecyclerAdapter = new ChooseRecyclerAdapter(getActivity(),mDateList);
        select_tv = (TextView) view.findViewById(R.id.choose_text);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
            back_bt = (Button) view.findViewById(R.id.back_bt);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mChooseRecyclerAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mChooseRecyclerAdapter.setOnItemClickListener(new ChooseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                //Toast.makeText(getActivity()," "+position,Toast.LENGTH_SHORT).show();


              if (currentLevel==0){
                  selectProvince = mProvinceList.get(position);
                queryCity();
              }else if (currentLevel==1){

                  selectCity = mCityList.get(position);
                  Log.d("wanglei1", " selectCity= "+selectCity.getCityName()+" "+selectCity.getId());

                  queryCountry();

              }else if (currentLevel==2){

                  selectCountry = mCountryArrayList.get(position);
                  Intent intent = new Intent(getActivity(),WeatherActivity.class);
                  intent.putExtra("weatherId",selectCountry.getWeatherId());
                  intent.putExtra("cityName",selectCountry.getCountryName());
                  getActivity().startActivity(intent);
                  getActivity().finish();
              }
            }
        });


        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLevel ==1){

                    queryProvince();
                }else if (currentLevel==2){
                    queryCity();
                }

            }
        });

        queryProvince();
    }

    public void queryProvince(){

        select_tv.setText("中国");
        back_bt.setVisibility(View.INVISIBLE);
        //DataSupport.findAll(Province.class);
        mProvinceList = (ArrayList<Province>) DataSupport.findAll(Province.class);
        if (mProvinceList.size()>0){
            mDateList.clear();
            for (Province province:mProvinceList){

                mDateList.add(province.getProviceName());
            }
            currentLevel = 0;
            mChooseRecyclerAdapter.notifyDataSetChanged();
        }else{
            String address = "http://guolin.tech/api/china";
             queryFromServer(address,"province");
            //HttpUtils.sendOkHttpRequest(address);
        }
    }

    private void queryFromServer(String address, final String type) {

        HttpUtils.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (type.equals("province")){

                    Utility.handleProvinceJson(response.body().string());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            queryProvince();

                        }
                    });

                }else if (type.equals("city")){
                    String content = response.body().string();
                    Log.d("wanglei1", " content= "+content);
                    Utility.handleCityJson(content,selectProvince.getId());
                   getActivity().runOnUiThread(new Runnable() {
                       @Override
                       public void run() {

                           queryCity();

                       }
                   });
                }else{

                    Utility.handleCountryJson(response.body().string(),selectCity.getId());
getActivity().runOnUiThread(new Runnable() {
    @Override
    public void run() {
        queryCountry();

    }
});

                }
            }
        });

    }

    private void queryCountry() {

        back_bt.setVisibility(View.VISIBLE);
        select_tv.setText(selectCity.getCityName());
        mCountryArrayList = (ArrayList<Country>)
                DataSupport.where("cityId=?",String.valueOf(selectCity.getId())).find(Country.class);
        Log.d("as", "queryCountry: mCountryArrayList = "+mCountryArrayList.size());
        if (mCountryArrayList.size()>0){
            mDateList.clear();

            for (Country country:mCountryArrayList){

                mDateList.add(country.getCountryName());
            }
            currentLevel = 2;
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {

                    mChooseRecyclerAdapter.notifyDataSetChanged();
                }
            });
        }else{

            String address = "http://guolin.tech/api/china/"+selectProvince.getProviceCode()+"/"+selectCity.getCityCode();

            Log.d("wanglei "," country address= "+address+" getProviceCode= "+selectProvince.getProviceCode());
                    queryFromServer(address,"country");
        }

    }

    private void queryCity() {
        back_bt.setVisibility(View.VISIBLE);
        select_tv.setText(selectProvince.getProviceName());
        mCityList = (ArrayList<City>) DataSupport.where("provinceId=?",String.valueOf(selectProvince.getId())).find(City.class);
        Log.d("wanglei ", "queryCity: size = "+mCityList.size());

        if (mCityList.size()>0){
            mDateList.clear();
            for (City city:mCityList){

                mDateList.add(city.getCityName());
            }
           currentLevel = 1;
            for (String s:mDateList){
                Log.d("wanglei123", "queryCity: s= "+s);

            }
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {

                    mChooseRecyclerAdapter.notifyDataSetChanged();
                }
            });

        }else {
            String address = "http://guolin.tech/api/china/"+selectProvince.getProviceCode();
            queryFromServer(address,"city");
        }



    }

}
