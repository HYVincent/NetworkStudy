package com.example.vincent.netwrokstudy.okhttp;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * description ：
 * project name：NetwrokStudy
 * author : Vincent
 * creation date: 2017/2/7 22:10
 *
 * @version 1.0
 */
public class OkhttpUtils {

    private static OkHttpClient okHttpClient;

    public static OkHttpClient getOkHttpClient() {
        synchronized (OkHttpClient.class) {
            if (okHttpClient == null) {
                okHttpClient = new OkHttpClient();
            }
        }
        return okHttpClient;
    }

    public static void sendOkHttpRequest(final String address, final Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (okHttpClient == null) getOkHttpClient();
                Request request = new Request.Builder()
                        .url(address)
                        .build();
                okHttpClient.newCall(request).enqueue(callback);
            }
        }).start();

    }

}
