package com.syepro.app.api.upload;
import java.io.File;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.syepro.app.api.upload.service.UploadLogService;
import com.syepro.app.R;


public class UploadActivity extends Activity {
    private EditText filenameText;
    private TextView resulView;
    private ProgressBar uploadbar;
    private UploadLogService logService;
    private boolean start=true;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int length = msg.getData().getInt("size");
            uploadbar.setProgress(length);
            float num = (float)uploadbar.getProgress()/(float)uploadbar.getMax();
            int result = (int)(num * 100);
            resulView.setText(result+ "%");
            if(uploadbar.getProgress()==uploadbar.getMax()){
                Toast.makeText(UploadActivity.this, R.string.success, 1).show();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        logService = new UploadLogService(this);
        filenameText = (EditText)this.findViewById(R.id.filename);
        uploadbar = (ProgressBar) this.findViewById(R.id.uploadbar);
        resulView = (TextView)this.findViewById(R.id.result);
        Button button =(Button)this.findViewById(R.id.button);
        Button button1 =(Button)this.findViewById(R.id.stop);
        button1 .setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                start=false;

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start=true;
                String filename = filenameText.getText().toString();
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    File uploadFile = new File(Environment.getExternalStorageDirectory(), filename);
                    if(uploadFile.exists()){
                        uploadFile(uploadFile);
                    }else{
                        Toast.makeText(UploadActivity.this, R.string.filenotexsit, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(UploadActivity.this, R.string.sdcarderror, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /**
     * 上传文件
     * @param uploadFile
     */
    private void uploadFile(final File uploadFile) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    uploadbar.setMax((int)uploadFile.length());
                    String souceid = logService.getBindId(uploadFile);
                    String head = "Content-Length="+ uploadFile.length() + ";filename="+ uploadFile.getName() + ";sourceid="+
                            (souceid==null? "" : souceid)+"\r\n";
                    Socket socket = new Socket("192.168.26.72",7878);
                    OutputStream outStream = socket.getOutputStream();
                    outStream.write(head.getBytes());

                    PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());
                    String response = StreamTool.readLine(inStream);
                    String[] items = response.split(";");
                    String responseid = items[0].substring(items[0].indexOf("=")+1);
                    String position = items[1].substring(items[1].indexOf("=")+1);
                    if(souceid==null){//代表原来没有上传过此文件，往数据库添加一条绑定记录
                        logService.save(responseid, uploadFile);
                    }
                    RandomAccessFile fileOutStream = new RandomAccessFile(uploadFile, "r");
                    fileOutStream.seek(Integer.valueOf(position));
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    int length = Integer.valueOf(position);
                    while(start&&(len = fileOutStream.read(buffer)) != -1){
                        outStream.write(buffer, 0, len);
                        length += len;
                        Message msg = new Message();
                        msg.getData().putInt("size", length);
                        handler.sendMessage(msg);
                    }
                    fileOutStream.close();
                    outStream.close();
                    inStream.close();
                    socket.close();
                    if(length==uploadFile.length()) logService.delete(uploadFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}  