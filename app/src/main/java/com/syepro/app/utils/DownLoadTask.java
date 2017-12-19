package com.syepro.app.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 多线程下载文件s
 */
public class DownLoadTask extends Thread{
    private String downloadUrl;// 下载链接地址
    private int threadNum;// 开启的线程数
    private String filePath;// 保存文件路径地址
    private int blockSize;// 每一个线程的下载量
    private int total;
    private int downloadLength = 0;
    private static DownLoadTask instance = null;
    private DownLoadTask(){

    }
    public static DownLoadTask getInstance(){
        if(instance==null){
            instance = new  DownLoadTask();
        }
        return instance;
    }

    public DownLoadTask build(String downloadUrl, int threadNum, String filePath,OnDownloadListener onDownloadListener){
        this.downloadUrl = downloadUrl;
        this.threadNum = threadNum;
        this.filePath = filePath;
        this.onDownloadListener = onDownloadListener;
        return instance;
    }

    @Override
    public void run() {
        super.run();
        FileDownloadThread[] threads = new FileDownloadThread[threadNum];
        try {

            URL url = new URL(downloadUrl);
            URLConnection conn = url.openConnection();
            int fileSize = conn.getContentLength();
            if (fileSize <= 0) {
                return;
            }
            total = fileSize;
            // 计算每条线程下载的数据长度
            blockSize = (fileSize % threadNum) == 0 ? fileSize / threadNum : fileSize / threadNum + 1;
            File file = new File(filePath);
            if(file.exists()){
                file.delete();
            }
            for (int i = 0; i < threads.length; i++) {
                // 启动线程，分别下载每个线程需要下载的部分
                threads[i] = new FileDownloadThread(url, file, blockSize, (i + 1));
                threads[i].setName("Thread:" + i);
                threads[i].start();
            }
            boolean isfinished = false;
            while (!isfinished) {
                isfinished = true;
                // 当前所有线程下载总量
                for (int i = 0; i < threads.length; i++) {
                    if (!threads[i].isCompleted()) {
                        isfinished = false;
                    }
                    downloadLength += threads[i].getDownloadLength();
                }
                if(onDownloadListener!=null){
                    onDownloadListener.onDownloading(downloadLength);
                }
                downloadLength = 0;
                LogUtils.error("download======",downloadLength+"");
                Thread.sleep(1000);
            }
            if(onDownloadListener!=null){
                onDownloadListener.onDownloading(total);
                onDownloadListener.onDownloadFinish(filePath);
            }
        } catch (MalformedURLException e) {
            if(onDownloadListener!=null){
                onDownloadListener.onDownloadFailure(e.toString());
            }
        } catch (IOException e) {
            if(onDownloadListener!=null){
                onDownloadListener.onDownloadFailure(e.toString());
            }
        } catch (InterruptedException e) {
            if(onDownloadListener!=null){
                onDownloadListener.onDownloadFailure(e.toString());
            }
        }
    }


    class FileDownloadThread extends Thread {

        /** 当前下载是否完成 */
        private boolean isCompleted = false;
        /** 当前下载文件长度 */
        private int downloadLength = 0;
        /** 文件保存路径 */
        private File file;
        /** 文件下载路径 */
        private URL downloadUrl;
        /** 当前下载线程ID */
        private int threadId;
        /** 线程下载数据长度 */
        private int blockSize;

        /**
         *
         * @param downloadUrl:文件下载地址
         * @param file:文件保存路径
         * @param blocksize:下载数据长度
         * @param threadId:线程ID
         */
        public FileDownloadThread(URL downloadUrl, File file, int blocksize, int threadId) {
            this.downloadUrl = downloadUrl;
            this.file = file;
            this.threadId = threadId;
            this.blockSize = blocksize;
        }

        @Override
        public void run() {

            BufferedInputStream bis = null;
            RandomAccessFile raf = null;

            try {
                URLConnection conn = downloadUrl.openConnection();
                conn.setAllowUserInteraction(true);

                int startPos = blockSize * (threadId - 1);// 开始位置
                int endPos = blockSize * threadId - 1;// 结束位置

                // 设置当前线程下载的起点、终点
                conn.setRequestProperty("Range", "bytes=" + startPos + "-" + endPos);
                System.out.println(Thread.currentThread().getName() + "  bytes=" + startPos + "-" + endPos);

                byte[] buffer = new byte[2048];
                bis = new BufferedInputStream(conn.getInputStream());

                raf = new RandomAccessFile(file, "rwd");
                raf.seek(startPos);
                int len;
                while ((len = bis.read(buffer, 0, 2048)) != -1) {
                    raf.write(buffer, 0, len);
                    downloadLength += len;
                }
                isCompleted = true;
            } catch (IOException e) {
                if(onDownloadListener!=null){
                    onDownloadListener.onDownloadFailure(e.toString());
                }
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (raf != null) {
                    try {
                        raf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        /**
         * 线程文件是否下载完毕
         */
        public boolean isCompleted() {
            return isCompleted;
        }

        /**
         * 线程下载文件长度
         */
        public int getDownloadLength() {
            return downloadLength;
        }

    }

    private OnDownloadListener onDownloadListener;
    public interface OnDownloadListener{
        public void onDownloading(int downloadLength);
        public void onDownloadFinish(String filePath);
        public void onDownloadFailure(String error);
    }


}
