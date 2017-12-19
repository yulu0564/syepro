package com.syepro.app.api.upload;

import android.content.Context;
import android.os.Message;

import com.syepro.app.api.upload.service.UploadLogService;

import java.io.File;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.Socket;

/**
 * 断点上传
 */

public class UploadUtils {
    private UploadLogService logService;
    private boolean start = true;

    /**
     * 上传文件
     *
     * @param uploadFile
     */
    private void uploadFile(final File uploadFile, Context context) {
        ConnectionThread connectionThread = new ConnectionThread(uploadFile, context);
        connectionThread.start();
    }
}
