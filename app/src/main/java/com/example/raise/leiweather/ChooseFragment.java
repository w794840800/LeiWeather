package com.example.raise.leiweather;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
        mChooseRecyclerAdapter = new ChooseRecyclerAdapter(getContext(),mDateList);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
            back_bt = (Button) view.findViewById(R.id.back_bt);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mChooseRecyclerAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        queryProvince();
    }

    public void queryProvince(){

        select_tv.setText("中国");
        back_bt.setVisibility(View.VISIBLE);
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
                    queryProvince();

                }else if (type.equals("city")){

                    Utility.handleCityJson(response.body().string(),selectProvince.getId());
                    queryCity();
                }else{
                    Utility.handleCountryJson(response.body().string(),selectCity.getId());
                    queryCountry();

                }
            }
        });

    }

    private void queryCountry() {

        back_bt.setVisibility(View.VISIBLE);
        select_tv.setText(selectCity.getCityName());
        mCountryArrayList = (ArrayList<Country>) DataSupport.where("id",String.valueOf(selectCountry.getId())).find(Country.class);
        if (mCountryArrayList.size()>0){
            mDateList.clear();

            for (Country country:mCountryArrayList){

                mDateList.add(country.getCountryName());
            }
            currentLevel = 2;
        }else{

            String address = "http://guolin.tech/api/china/"+selectProvince.getProviceCode()+selectCity.getCityCode();
            queryFromServer(address,"country");
        }

    }

    private void queryCity() {

        back_bt.setVisibility(View.VISIBLE);
        select_tv.setText(selectProvince.getProviceName());
        mCityList = (ArrayList<City>) DataSupport.where("provinceId",String.valueOf(selectCity.getId())).find(City.class);
        if (mCityList.size()>0){
            mDateList.clear();
            for (City city:mCityList){

                mDateList.add(city.getCityName());
            }
           currentLevel = 1;

        }else {
            String address = "http://guolin.tech/api/china/"+selectProvince.getProviceCode();
            queryFromServer(address,"city");
        }



    }

}
