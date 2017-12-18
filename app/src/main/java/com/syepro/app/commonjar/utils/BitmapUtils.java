package com.syepro.app.commonjar.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Bitmap处理工具类
 */
public class BitmapUtils {

    /**
     * View生成Bitmap
     */
    public static Bitmap convertViewToBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        Bitmap bitmap = view.getDrawingCache(true);
        Bitmap bmp = duplicateBitmap(bitmap);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        view.setDrawingCacheEnabled(false);
        return bmp;
    }

    /**
     * Bitmap存储至SD卡
     * @param bitmap
     * @param realPath  路径
     * @param name  图片名
     * @param mOnSaveImgListener   子线程回调
     */
    public static void storeBitmapToSdcard(final Bitmap bitmap, final String realPath, final String name, final OnSaveImgListener mOnSaveImgListener) {

        if (bitmap == null)
            return;
        final File dir = new File(realPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                File myCaptureFile = (File) message.obj;
                if (mOnSaveImgListener != null) {
                    mOnSaveImgListener.onResponse(myCaptureFile);
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                Bitmap bitmaptemp = bitmap;
                BufferedOutputStream bos;// = null;
                try {
                    File myCaptureFile = new File(dir, name);
                    bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                    if (bitmaptemp.compress(Bitmap.CompressFormat.PNG, 100, bos)) {
                        bos.flush();
                        bos.close();
                    }
                    Message message = handler.obtainMessage(0, myCaptureFile);
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();

                } finally {
                    bitmaptemp = null;
                    bos = null;
                }
            }
        }.start();
    }

    public interface OnSaveImgListener {
        void onResponse(File file);
    }

    /**
     * View生成bigmap格式转换
     */
    public static Bitmap duplicateBitmap(Bitmap bmpSrc) {
        if (null == bmpSrc) {
            return null;
        }
        int bmpSrcWidth = bmpSrc.getWidth();
        int bmpSrcHeight = bmpSrc.getHeight();
        Bitmap bmpDest = Bitmap.createBitmap(bmpSrcWidth, bmpSrcHeight, Bitmap.Config.ARGB_8888);
        if (null != bmpDest) {
            Canvas canvas = new Canvas(bmpDest);
            final Rect rect = new Rect(0, 0, bmpSrcWidth, bmpSrcHeight);
            canvas.drawBitmap(bmpSrc, rect, rect, null);
        }

        return bmpDest;
    }

    /**
     * Bitmap横向拼接
     */
    public static Bitmap addHorizontalBitmap(Bitmap first, Bitmap second) {
        int width = first.getWidth() + second.getWidth();
        int height = Math.max(first.getHeight(), second.getHeight());
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);
        canvas.drawBitmap(second, first.getWidth(), 0, null);
        return result;
    }


    /**
     * Bitmap纵向拼接
     */
    public static Bitmap addVerticallyBitmap(Bitmap... mBitmaps) {
        if (mBitmaps.length > 0) {
            Bitmap first = mBitmaps[0];
            int width = first.getWidth();
            for (int i = 1; i < mBitmaps.length; i++) {
                mBitmaps[i] = bigAddSmall(mBitmaps[i], ((float) width) / mBitmaps[i].getWidth());
            }
            int height = 0;
            for (Bitmap mBitmap : mBitmaps) {
                height += mBitmap.getHeight();
            }

            Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            int top = 0;
            for (Bitmap mBitmap : mBitmaps) {
                canvas.drawBitmap(mBitmap, 0, top, null);
                top += mBitmap.getHeight();
            }
            return result;
        }
        return null;
    }

    public enum Alignment {
        LEFT_TOP, LEFT_BOTTOM, LEFT_CENTER, RIGHT_TOP, RIGHT_BOTTOM, RIGHT_CENTER, CENTER_TOP, CENTER_BOTTOM, CENTER
    }

    /**
     * Bitmap覆盖拼接
     */
    public static Bitmap addFrameBitmap(Bitmap first, Bitmap second, Alignment alignment) {
        int firstWidth = first.getWidth();
        second = bigAddSmall(second, ((float) firstWidth) / 1080);
        Canvas canvas = new Canvas(first);
        int secondWidth = second.getWidth();
        int firstHeight = first.getHeight();
        int secondHeight = second.getHeight();
        switch (alignment) {
            case LEFT_TOP:
                canvas.drawBitmap(second, 0, 0, null);
                break;
            case LEFT_BOTTOM:
                canvas.drawBitmap(second, 0, firstHeight - secondHeight, null);
                break;
            case LEFT_CENTER:
                canvas.drawBitmap(second, 0, (firstHeight - secondHeight) / 2, null);
                break;
            case RIGHT_TOP:
                canvas.drawBitmap(second, firstWidth - secondWidth, 0, null);
                break;
            case RIGHT_BOTTOM:
                canvas.drawBitmap(second, firstWidth - secondWidth, firstHeight - secondHeight, null);
                break;
            case RIGHT_CENTER:
                canvas.drawBitmap(second, firstWidth - secondWidth, (firstHeight - secondHeight) / 2, null);
                break;
            case CENTER_TOP:
                canvas.drawBitmap(second, (firstWidth - secondWidth) / 2, 0, null);
                break;
            case CENTER_BOTTOM:
                canvas.drawBitmap(second, (firstWidth - secondWidth) / 2, firstHeight - secondHeight, null);
                break;
            case CENTER:
                canvas.drawBitmap(second, (firstWidth - secondWidth) / 2, (firstHeight - secondHeight) / 2, null);
                break;
        }
        return first;
    }


    /**
     * Bitmap放大缩放图片
     *
     * @param bitmap
     * @param proportion 缩放放大的比例，>1放大，<1缩放
     */
    public static Bitmap bigAddSmall(Bitmap bitmap, float proportion) {
        Matrix matrix = new Matrix();
        matrix.postScale(proportion, proportion); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

}
