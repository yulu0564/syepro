package com.syepro.app.api.upload;

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
    private void uploadFile(final File uploadFile) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String souceid = logService.getBindId(uploadFile);
                    String head = "Content-Length=" + uploadFile.length() + ";filename=" + uploadFile.getName() + ";sourceid=" +
                            (souceid == null ? "" : souceid) + "\r\n";
                    Socket socket = new Socket("192.168.26.72", 7878);
                    OutputStream outStream = socket.getOutputStream();
                    outStream.write(head.getBytes());

                    PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());
                    String response = StreamTool.readLine(inStream);
                    String[] items = response.split(";");
                    String responseid = items[0].substring(items[0].indexOf("=") + 1);
                    String position = items[1].substring(items[1].indexOf("=") + 1);
                    if (souceid == null) {//代表原来没有上传过此文件，往数据库添加一条绑定记录
                        logService.save(responseid, uploadFile);
                    }
                    RandomAccessFile fileOutStream = new RandomAccessFile(uploadFile, "r");
                    fileOutStream.seek(Integer.valueOf(position));
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    int length = Integer.valueOf(position);
                    while (start && (len = fileOutStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                        length += len;
                        Message msg = new Message();
                        msg.getData().putInt("size", length);
//                        handler.sendMessage(msg);
                    }
                    fileOutStream.close();
                    outStream.close();
                    inStream.close();
                    socket.close();
                    if (length == uploadFile.length()) logService.delete(uploadFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
