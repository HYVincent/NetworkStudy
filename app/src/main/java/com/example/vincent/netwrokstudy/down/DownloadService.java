package com.example.vincent.netwrokstudy.down;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.example.vincent.netwrokstudy.MainActivity;
import com.example.vincent.netwrokstudy.R;

import java.io.File;

/**
 * description ：
 * project name：NetwrokStudy
 * author : Vincent
 * creation date: 2017/2/8 0:11
 *
 * @version 1.0
 */
public class DownloadService extends Service {

    private DownloadTask downloadTask;

    private String downloadUrl;

    private DownloadListener listener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1,getNotification("Downing",progress));
        }

        @Override
        public void onSuccess() {
            downloadTask = null;
            //下载成功通知前台关闭通知，并创建一个下载成功的通知
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("Download Success",-1));
            Toast.makeText(DownloadService.this,"Download Success",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailed() {
            downloadTask = null;
            //下载失败的时候通知前台关闭通知，并产生一个下载失败的通知
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("Download Failed",-1));
            Toast.makeText(DownloadService.this,"Download Failed",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onPaused() {
            downloadTask = null;
            //下载暂停
            stopForeground(true);
            Toast.makeText(DownloadService.this,"Download Paused",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCanceled() {
            //取消下载
            downloadTask = null;
            stopForeground(true);
            Toast.makeText(DownloadService.this,"Download Canceld",Toast.LENGTH_LONG).show();
        }
    };

    private DownloadBinder mBinder = new DownloadBinder();

    public class DownloadBinder extends Binder{

        public void startdownload (String url){
            if(downloadTask == null ){
                downloadUrl = url;
                downloadTask = new DownloadTask(listener);
                downloadTask.execute(downloadUrl);
                startForeground(1,getNotification("Downloading...",0));
                Toast.makeText(DownloadService.this,"Download...",Toast.LENGTH_LONG).show();
            }
        }
        public void pauseDownload(){
            if(downloadTask!=null){
                downloadTask.pauseDownload();
            }
        }
        public void cancelDownload(){
            if(downloadTask!=null){
                downloadTask.cancelDownload();
            }else {
                if(downloadTask!=null){
                    //取消下载需要将文件删除并且通知关闭
                    String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    File file = new File(directory+fileName);
                    if(file.exists()){
                        file.delete();
                    }
                    getNotificationManager().cancel(1);
                    stopForeground(true);
                    Toast.makeText(DownloadService.this,"Download Canceled",Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private NotificationManager getNotificationManager(){
        return (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    }
    private Notification getNotification(String title,int progress){
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        if(progress>0){
            //当progress>0或者等于0的时候才需要显示进度
            builder.setContentText(progress+"%");
            builder.setProgress(100,progress,false);
        }
        return builder.build();
    }

}
