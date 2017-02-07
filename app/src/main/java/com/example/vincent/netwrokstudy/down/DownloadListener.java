package com.example.vincent.netwrokstudy.down;

/**
 * description ：
 * project name：NetwrokStudy
 * author : Vincent
 * creation date: 2017/2/7 23:40
 *
 * @version 1.0
 */
public interface DownloadListener {

    void onProgress(int progress);//通知当前下载进度

    void onSuccess();//下载成功

    void onFailed();//下载失败

    void onPaused();//下载暂停

    void onCanceled();//下载取消
}
