package com.example.vincent.netwrokstudy.okhttp;

/**
 * description ：
 * project name：NetwrokStudy
 * author : Vincent
 * creation date: 2017/2/7 22:34
 *
 * @version 1.0
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
