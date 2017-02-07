package com.example.vincent.netwrokstudy.asynctask;

import android.os.AsyncTask;

/**
 * description ：
 * project name：NetwrokStudy
 * author : Vincent
 * creation date: 2017/2/7 23:17
 *
 * @version 1.0
 */
public class DownLoadTast extends AsyncTask<Void,Integer,Boolean> {

    /**
     * 后台任务开始之前调用 进行界面上的初始化操作
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    /**
     * 当后台任务调用了publishProgress（Proress..）方法后，会调用此方法 这个方法中的参数是后台任务中传递过来的 此方法中可以对UI进行操作
     * @param values
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    /**
     * 后台任务执行完毕 调用此方法 后台返回的结果作为参数传递到此方法中 可根据结果进行一些操作
     * @param aBoolean
     */
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }

    /**
     * 后台任务 在子线程中执行 耗时操作
     * @param params
     * @return
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        return null;
    }
}
