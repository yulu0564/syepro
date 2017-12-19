package com.syepro.app.utils;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件操作工具类
 */
public class FileUtils {
    /**
     * 判断SD卡是否可用
     *
     * @return
     */
    public static boolean isSDCardMounted() {
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取文件的大小
     */
    public static long getFileSize(File paramFile) throws Exception {
        File[] arrayOfFile;
        arrayOfFile = paramFile.listFiles();
        long t = 0;
        if (null == arrayOfFile) {
            return t;
        }
        int length = arrayOfFile.length;
        for (int index = 0; index < length; index++) {
            if (arrayOfFile[index].isDirectory()) {
                t = t + getFileSize(arrayOfFile[index]);
            } else {
                t = t + arrayOfFile[index].length();
            }
        }
        return t;
    }

    /**
     * 截取url中最后一个"/"后字符串为文件名
     *
     * @param url
     * @return
     */
    public static String getOfflinefileNameFromUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        String imgdata[] = url.split("\\|");
        return (imgdata[0].substring(imgdata[0].lastIndexOf("/") + 1)).replace(
                "|", "");
    }

    /**
     * 删除SD下指定文件
     *
     * @param file
     * @return
     */
    public static boolean delSDFile(File file) {
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {
                if (file.exists()) {
                    File[] arrayOfFile;
                    arrayOfFile = file.listFiles();
                    int length = arrayOfFile.length;
                    for (int index = 0; index < length; index++) {
                        if (arrayOfFile[index].isDirectory()) {
                            delSDFile(arrayOfFile[index]);
                        } else {
                            arrayOfFile[index].delete();
                        }
                    }
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取文件路径
     *
     * @return
     */
    public static String getFilePath(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return context.getExternalCacheDir().getPath();
        }else{
            return context.getCacheDir().getPath();
        }
    }

    // ##################### 存取文本文件 #####################//
    // 从SD卡读取文本文件
    public static void readTextFileToSD(final Context activity, final String filePath, final String fileName, final OnReadListener onReadListener) {
        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                if (onReadListener != null) {
                    onReadListener.onResponse((String) message.obj);
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                FileInputStream fileInputStream = null;
                // 缓冲区的流，和磁盘无关，不需要关闭
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                File file = new File(new File(getFilePath(activity) + filePath),
                        fileName);
                try {
                    fileInputStream = new FileInputStream(file);
                    int len = 0;
                    byte[] b = new byte[1024];
                    while ((len = fileInputStream.read(b)) != -1) {
                        byteArrayOutputStream.write(b, 0, len);
                    }
                    Message msg = handler.obtainMessage();
                    msg.obj = new String(byteArrayOutputStream.toByteArray()).toString();
                    handler.sendMessage(msg);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
//        }
//        return new String(byteArrayOutputStream.toByteArray());
    }

    // 向SD卡存入文本文件
    public static boolean saveTextFileToSD(Context activity, final String filePath, String fileName, String content) {
        boolean flag = false;
        FileOutputStream fileOutputStream = null;
        File destDir = new File(getFilePath(activity) + filePath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        // SD路径
        File file = new File(new File(getFilePath(activity) + filePath),
                fileName);
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
            flag = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        }
        return flag;
    }

    public interface OnReadListener {
        void onResponse(String json);
    }
}