package com.example.vincent.netwrokstudy;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.vincent.netwrokstudy.down.DownloadService;
import com.example.vincent.netwrokstudy.okhttp.HttpCallbackListener;
import com.example.vincent.netwrokstudy.okhttp.HttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * description ：
 * project name：NetwrokStudy
 * author : Vincent
 * creation date: 2017/2/7 21:43
 *
 * @version 1.0
 */
public class OkHttpActivity extends AppCompatActivity {

    private Button btnOkhttp;
    private Button btnDownStart;
    private Button btnDownPause;
    private Button btnDownCancel;
    private String downUrl = "http://sw.bos.baidu.com/sw-search-sp/software/4b8362acdfc7e/QQ_8.9.19990.0_setup.exe";
    private static String TAG = OkHttpActivity.class.getSimpleName();


    private DownloadService.DownloadBinder downLoadBinder;


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downLoadBinder = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);
        btnOkhttp = (Button) findViewById(R.id.btn_okhttp);
        btnOkhttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        requestNetwork();
                    }
                }).start();*/

                /*OkhttpUtils.sendOkHttpRequest("http://www.baidu.com", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: error");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d(TAG, "onResponse: success"+response.body().string());
                    }
                });*/
                HttpUtils.sendHttpRequest("https://www.baidu.com", new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        Log.d(TAG, "onFinish: success" + response);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d(TAG, "onError: error");
                    }
                });

            }
        });

        Intent intent = new Intent(this,DownloadService.class);
        startService(intent);
        bindService(intent,connection,BIND_AUTO_CREATE);

        btnDownStart = (Button) findViewById(R.id.btn_down_start);
        btnDownStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downLoadBinder.startdownload(downUrl);
            }
        });
        btnDownPause = (Button) findViewById(R.id.btn_down_pause);
        btnDownPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downLoadBinder.pauseDownload();
            }
        });
        btnDownCancel = (Button) findViewById(R.id.btn_down_cancel);
        btnDownCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downLoadBinder.cancelDownload();
            }
        });
    }

    private void requestNetwork() {
        /**
         * OkHttp对象
         */
        OkHttpClient httpClient = new OkHttpClient();
        /**
         * 请求对象
         */
        Request request = new Request
                .Builder()
                .url("http://www.baidu.com")
                .build();
        try {
            //获取服务器响应对象
            Response response = httpClient.newCall(request).execute();
            //拿到服务器相应数据
            String responseData = response.body().string();
            Message message = Message.obtain();
            message.obj = responseData;
            message.arg1 = 1;
            handler.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 1) {
                String data = (String) msg.obj;
                Log.d(TAG, "handleMessage: " + data);
            }
        }
    };


    public static void actionStart(Context context) {
        Intent intent = new Intent(context, OkHttpActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
