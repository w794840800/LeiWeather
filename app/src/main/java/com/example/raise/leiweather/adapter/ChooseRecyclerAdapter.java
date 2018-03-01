package com.example.raise.leiweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.raise.leiweather.R;

import java.util.ArrayList;

/**
 * Created by raise on 18-3-1.
 */

public class ChooseRecyclerAdapter extends RecyclerView.Adapter {
    ArrayList<String>mStringArrayList;
    Context mContext;
    OnItemClickListener onItemClickListener;
    public ChooseRecyclerAdapter(Context context, ArrayList<String>list){
        mContext = context;
        mStringArrayList = list;

    }
    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

      View view =  LayoutInflater.from(mContext).inflate(R.layout.choose_recycle_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ((TextView)(holder.itemView)).setText(mStringArrayList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onItemClickListener.onItemClick(view,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStringArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
    public MyViewHolder(View itemView) {
        super(itemView);
    }
}
    public interface OnItemClickListener{
    void onItemClick(View v,int position);

}
}
