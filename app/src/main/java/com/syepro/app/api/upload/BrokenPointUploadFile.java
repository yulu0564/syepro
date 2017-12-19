package com.syepro.app.api.upload;

import android.content.Context;

import java.io.File;

/**
 * Created by Administrator on 2017/12/19 0019.
 */

public abstract class BrokenPointUploadFile {
    private File uploadFile;
    private Context context;
    private ConnectionThread connectionThread;

    public BrokenPointUploadFile( Context context) {
        this.context = context;
    }
    public void setUploadFile(File uploadFile){
        this.uploadFile = uploadFile;
    }

    //开始上传
    public void start() {
        connectionThread = new ConnectionThread(uploadFile, context);
        connectionThread.setOnReadListener(new ConnectionThread.OnReadListener() {
            @Override
            public void onProgress(float value) {
                BrokenPointUploadFile.this.onProgress(value);
            }

            @Override
            public void onFileSize(long fileSize, long progress) {
                BrokenPointUploadFile.this.onFileSize(fileSize,progress);
            }
        });
        connectionThread.start();
    }

    //结束上传
    public void stop() {
        connectionThread.setStart(false);
    }

    public void onError(Throwable e) {
        e.printStackTrace();
    }

    //已经结束 不调用了
    public void onComplete() {
        connectionThread.disConnection();
    }

    public abstract void onProgress(float value);
    public void onFileSize(long fileSize, long progress) {

    }

}
