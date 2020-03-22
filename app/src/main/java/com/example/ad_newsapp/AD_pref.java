package com.example.ad_newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import java.util.ArrayList;

public class AD_pref extends AppCompatActivity {

    ArrayList<String> news_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_pref);
        news_type = new ArrayList<>();
        initData();
    }

    private void initData() {
        news_type.add("BUSINESS");
        news_type.add("ENTERTAINMENT");
        news_type.add("HEALTH");
        news_type.add("SCIENCE");
        news_type.add("SPORTS");
        news_type.add("TECHNOLOGY");
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.news_type_recyclerView);
        ADprefAdapter adapter = new ADprefAdapter(this,news_type);
        recyclerView.setAdapter(adapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
    }
}
