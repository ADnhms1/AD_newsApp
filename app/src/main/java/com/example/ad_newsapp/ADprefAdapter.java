package com.example.ad_newsapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_newsapp.MainActivity.newsDownload;

import java.util.ArrayList;

import static androidx.core.content.ContextCompat.startActivity;

public class ADprefAdapter extends RecyclerView.Adapter<ADprefAdapter.ADprefHolder> {

    ArrayList<String> each = new ArrayList<>();
    Context context;

    public ADprefAdapter(Context context,ArrayList<String> each) {
        this.each = each;
        this.context = context;
    }


    @Override
    public ADprefHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_pref_each_item,parent,false);
        ADprefHolder holder = new ADprefHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ADprefHolder holder, final int position) {
        holder.item.setText(each.get(position));
        holder.each_type_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+each.get(position), Toast.LENGTH_SHORT).show();
                if(each.get(position).equals("BUSINESS"))
                {
                    Log.d("ADcheck", "onClick: Business");
                    MainActivity.url = "http://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=3c8d73133f934ffaa623cb10daefb8a5";
                }
                else if(each.get(position).equals("ENTERTAINMENT"))
                {
                    Log.d("ADcheck", "onClick: Entertainment");
                    MainActivity.url = "http://newsapi.org/v2/top-headlines?country=in&category=entertainment&apiKey=3c8d73133f934ffaa623cb10daefb8a5";
                }
                else if(each.get(position).equals("HEALTH"))
                {
                    Log.d("ADcheck", "onClick: Health");
                    MainActivity.url = "http://newsapi.org/v2/top-headlines?country=in&category=health&apiKey=3c8d73133f934ffaa623cb10daefb8a5";
                }
                else if(each.get(position).equals("SCIENCE"))
                {
                    Log.d("ADcheck", "onClick: Science");
                    MainActivity.url = "http://newsapi.org/v2/top-headlines?country=in&category=science&apiKey=3c8d73133f934ffaa623cb10daefb8a5";
                }
                else if(each.get(position).equals("SPORTS"))
                {
                    Log.d("ADcheck", "onClick: Sports");
                    MainActivity.url = "http://newsapi.org/v2/top-headlines?country=in&category=sports&apiKey=3c8d73133f934ffaa623cb10daefb8a5";
                }
                else if(each.get(position).equals("TECHNOLOGY"))
                {
                    Log.d("ADcheck", "onClick: Technology");
                    MainActivity.url = "http://newsapi.org/v2/top-headlines?country=in&category=technology&apiKey=3c8d73133f934ffaa623cb10daefb8a5";
                }
                context.startActivity(new Intent(context,MainActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return each.size();
    }


    class ADprefHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout each_type_layout;
        TextView item;
        public ADprefHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.news_type);
            each_type_layout = itemView.findViewById(R.id.news_type_layout);
        }
    }
}
