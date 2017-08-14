package com.daivp.api_worker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = new OkHttpClient.Builder().addNetworkInterceptor(new StethoInterceptor()).readTimeout(30000, TimeUnit.MILLISECONDS).build();
        findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            test("http://www.baidu.com");
                            try {
                                Thread.sleep(2000L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test1(1000);
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test2(1000);
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test2(2000);
            }
        });
    }

    ArrayList<Ball> objects = new ArrayList<>();

    public void test1(int x) {
        for (int i = x; i > 0; i--) {
            objects.add(new Ball(String.valueOf(i)));
        }
    }

    public void test2(int x) {
        ArrayList<Object> temp = new ArrayList<>();
        for (int i = x; i > 0; i--) {
            temp.add(new Ball(String.valueOf(i)));
        }
    }


    public void test(String url) {

        Request request = new Request.Builder()
                .url(url).addHeader("Connection", "close")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("DDAI", "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseString = response.body().string();
                    Log.d("DDAI", "onResponse=" + responseString.length());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
