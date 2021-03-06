package com.syepro.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.syepro.app.R;
import com.syepro.app.api.upload.BrokenPointUploadFile;
import com.syepro.app.base.activity.BaseActivity;
import com.syepro.app.utils.FileUtils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yu.lu on 2017/12/18.
 */

public class VideoActivity extends BaseActivity {

    private static final int VIDEO_CAPTURE = 0;
    String filePath = "";

    @BindView(R.id.img_video)
    ImageView imgVideo;
    @BindView(R.id.uploadbar)
    ProgressBar uploadbar;

    @Override
    public int getContentView() {
        return R.layout.activity_video;
    }

    @Override
    protected void initViews() {
        uploadbar.setMax(100);
    }


    @OnClick({R.id.img_video})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_video:
//                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);//限制录制时间10秒
//                startActivityForResult(intent, 0);
                aa();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == VIDEO_CAPTURE) {
            Uri uri = data.getData();
            Cursor cursor = this.getContentResolver().query(uri, null, null,
                    null, null);
            if (cursor != null && cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID));
                filePath = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Video.VideoColumns.DATA));
                imgVideo.setImageBitmap(getVideoThumbnail(filePath));
                cursor.close();
                aa();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void aa(){
        String filename = "bbb";
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File uploadFile = new File(Environment.getExternalStorageDirectory(), filename);
            if (uploadFile.exists()) {

                List<File> files = FileUtils.getFileList(uploadFile);
                for (File mFile:files){
                    BrokenPointUploadFile mBrokenPointUploadFile = new BrokenPointUploadFile(this) {
                        @Override
                        public void onProgress(float value) {
                            uploadbar.setProgress((int) (value*100));
                        }
                    };
                    mBrokenPointUploadFile.setUploadFile(mFile);
                    mBrokenPointUploadFile.start();
                }
            }
        }

    }

    /**
     * 获得视频的缩略图
     */

    public static Bitmap getVideoThumbnail(String videoPath) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(videoPath);
        Bitmap bitmap = media.getFrameAtTime();
        return bitmap;
    }
}
