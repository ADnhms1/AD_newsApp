package com.example.ad_newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    GestureDetector gestureDetector;
    gestureListener gestureListener;
    static ArrayList<String> newsFetcher = new ArrayList<>();
    static ArrayList<String> newsContent = new ArrayList<>();
    static String singleData [];
    newsImgDdownload imgDdownload;
    static TextView singleNewsContent;
    static TextView singleNewsTitle;
    static TextView sourceView;
    static int counter;
    int flag;
    int MIN_DISTANCE = 150;
    float x1,x2,y1,y2;
    public static String url = "http://newsapi.org/v2/top-headlines?country=in&apiKey=3c8d73133f934ffaa623cb10daefb8a5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emtyData();

        counter = 0;

        gestureListener = new gestureListener();
        gestureDetector = new GestureDetector(this,gestureListener);

        singleNewsContent = (TextView) findViewById(R.id.textView);
        singleNewsTitle = (TextView) findViewById(R.id.textView2);
        sourceView = (TextView) findViewById(R.id.source);

//        url = "http://newsapi.org/v2/top-headlines?country=in&apiKey=3c8d73133f934ffaa623cb10daefb8a5";

        newsDownload newsDownload = new newsDownload();
        newsDownload.execute(url);
        Log.d("ADurl", "onCreate: " + url);
    }

    class gestureListener implements GestureDetector.OnGestureListener
    {

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d("ADcheckScroll", "onScroll: Scroll");
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);

        switch(event.getAction())
        {
            // get x and y when pressed!
            case MotionEvent.ACTION_DOWN :
                x1 = event.getX();
                y1 = event.getY();
                break;

            // get x and y when released
            case MotionEvent.ACTION_UP :
                x2 = event.getX();
                y2 = event.getY();

                // getting value for horizontal swipe
                float valueX = x2-x1;

                // getting value for vertical swipe
                float valueY = y2-y1;


                if(Math.abs(valueX) > MIN_DISTANCE)
                {
                    // detect left to right swipe
                    if(x2>x1)
                    {
                        Toast.makeText(this, "right swipe", Toast.LENGTH_SHORT).show();

                        // pass the user to news types
                        startActivity(new Intent(MainActivity.this,AD_pref.class));
                    }
                    // detect right to left swipe
                    else
                    {
                        Toast.makeText(this, "left swipe", Toast.LENGTH_SHORT).show();

                        // pass the user to see webpage
                        Intent intent = new Intent(MainActivity.this,AD_newsSite.class);
                        startActivity(intent);
                    }
                }
                else if(Math.abs(valueY) > MIN_DISTANCE)
                {
                    // detect top to bottom swipe
                    if(y2 > y1)
                    {
                        Toast.makeText(this, "down swipe", Toast.LENGTH_SHORT).show();

                        // prev
                        if(counter >= 2)
                        {
                            counter = counter - 2;
                            nextNews();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Feeds Already Updated!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    // detect bottom to top swipe
                    else
                    {
                        Toast.makeText(this, "up swipe", Toast.LENGTH_SHORT).show();

                        // next
                        if(counter<newsContent.size())
                        {
                            nextNews();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "End!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        }
        return super.onTouchEvent(event);
    }

    public void nextNews() {
       singleData = newsContent.get(counter).split("AD");
       newsImgDdownload imgDdownload = new newsImgDdownload();
       imgDdownload.execute(singleData[3]);
       singleNewsTitle.setText(singleData[0]);
       singleNewsContent.setText(singleData[1]);
       sourceView.setText(singleData[4]);
        Log.d("ADcheckname", "nextNews: " + singleData[4]);
       counter++;
   }

    class newsImgDdownload extends AsyncTask<String,Void,Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                Bitmap img = BitmapFactory.decodeStream(inputStream);
                return img;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);
            super.onPostExecute(bitmap);
        }
    }


    class newsDownload extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            try {
                String newsData = "";
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStreamReader.read();
                while(data!=-1)
                {
                    newsData += (char) data;
                    data = inputStreamReader.read();
                }
                return newsData;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = new JSONArray(jsonObject.getString("articles"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    String source = getSource(jsonArray,i);
                    newsFetcher.add(jsonArray.getJSONObject(i).getString("title") + "AD" + jsonArray.getJSONObject(i).getString("description") + "AD" + jsonArray.getJSONObject(i).getString("url") + "AD" + jsonArray.getJSONObject(i).getString("urlToImage") + "AD" + source);
                }
                newsContent.addAll(newsFetcher);
                nextNews();
                newsDownloadTopNews newsDownloadTopNews = new newsDownloadTopNews();
                newsDownloadTopNews.execute("https://newsapi.org/v2/top-headlines?sources=google-news&apiKey=3c8d73133f934ffaa623cb10daefb8a5");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    static class newsDownloadTopNews extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            try {
                newsFetcher.clear();
                String topNewsString = "";
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStreamReader.read();
                while(data!=-1)
                {
                    topNewsString += (char) data;
                    data = inputStreamReader.read();
                }
                return topNewsString;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = new JSONArray(jsonObject.getString("articles"));
                for(int i=0; i<jsonArray.length(); i++)
                {
                    String source = getSource(jsonArray,i);
                    newsFetcher.add(jsonArray.getJSONObject(i).getString("title") + "AD" + jsonArray.getJSONObject(i).getString("description") + "AD" + jsonArray.getJSONObject(i).getString("url") + "AD" + jsonArray.getJSONObject(i).getString("urlToImage") + "AD" + source);
                }
                Log.d("AD", "onPostExecute: " + newsFetcher);
                newsContent.addAll(newsFetcher);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }

    static String getSource(JSONArray jsonArray, int i)
    {
        String source = null;
        try {
            source = jsonArray.getJSONObject(i).getString("source");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String eachSource [] = source.split(":");
        String name = eachSource[2].substring(1,eachSource[2].length()-2);
        return name;
    }

    void emtyData()
    {
        newsContent.clear();
        newsFetcher.clear();
    }
}