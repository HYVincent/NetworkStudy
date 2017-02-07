package com.example.vincent.netwrokstudy.okhttp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * description ：
 * project name：NetwrokStudy
 * author : Vincent
 * creation date: 2017/2/7 22:28
 *
 * @version 1.0
 */
public class HttpUtils {

    /**
     * 发起请求
     * @param address
     * @return
     */
    public static void sendHttpRequest(final String address,final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                StringBuffer response = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(8*1000);
                    connection.setConnectTimeout(8*1000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    response = new StringBuffer();
                    String line;
                    if((line = reader.readLine())!=null){
                        response.append(line);
                    }
                    if(listener!=null){
                        //回调onFinish方法
                        listener.onFinish(response.toString());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    //回调错误方法
                    listener.onError(e);
                }
            }
        }).start();
    }

}
