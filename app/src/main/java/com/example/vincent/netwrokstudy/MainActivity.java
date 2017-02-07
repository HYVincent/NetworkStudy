package com.example.vincent.netwrokstudy;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.vincent.netwrokstudy.okhttp.OkhttpUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button btnNetwork;
    private HttpURLConnection connection = null;
    private BufferedReader bufferedReader= null;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.arg1==1){
                String response = (String) msg.obj;
                Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
            }
        }
    };
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnNetwork = (Button) findViewById(R.id.btn_network);
        btnNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestNetwork();
            }
        });
        findViewById(R.id.tv_okhttp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpActivity.actionStart(MainActivity.this);
            }
        });

    }

    private void requestNetwork() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://www.baidu.com");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8*1000);
                    connection.setReadTimeout(8*1000);

                    InputStream is = connection.getInputStream();//获取服务器返回的流
                    bufferedReader = new BufferedReader(new InputStreamReader(is));//读取获取到的输入流
                    StringBuffer response = new StringBuffer();
                    String line ;
                    while ((line = bufferedReader.readLine())!=null){
                        response.append(line);
                    }
                    Log.d(TAG, "run: "+response);
                    Message message = Message.obtain();
                    message.obj=response.toString();
                    message.arg1=1;
                    handler.sendMessage(message);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,"发生错误了，请检查代码",Toast.LENGTH_LONG).show();
                }finally {
                    if(bufferedReader!=null){
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(connection!=null){
                        connection.disconnect();//关闭连接
                    }
                }
            }
        }).start();
    }
}
