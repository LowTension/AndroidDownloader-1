package com.httpionichina.androiddownloader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.httpionichina.androiddownloader.entity.FileInfo;
import com.httpionichina.androiddownloader.services.DownloadServices;
import com.httpionichina.androiddownloader.task.DownloadTask;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private TextView mTVDownloadFileName;

    private ProgressBar mPBDownloadProgress;


    private Button mBTNStop;

    private Button mBTNStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    /**
     * 初始化试图组件
     */
    private void initView() {
        mTVDownloadFileName = (TextView) findViewById(R.id.tv_download_file_name);
        mPBDownloadProgress = (ProgressBar) findViewById(R.id.pb_download_progress);
        mBTNStop = (Button) findViewById(R.id.btn_stop);
        mBTNStart = (Button) findViewById(R.id.btn_start);
        final String downloadUrl = "http://dlsw.baidu.com/sw-search-sp/soft/45/38616/tsfazsjsjhf2015.7.3.117.1436515748.exe";
        final FileInfo fileInfo = new FileInfo(0, downloadUrl,
                "tsfazsjsjhf2015.7.3.117.1436515748.exe", 0, 0);

        mTVDownloadFileName.setText(fileInfo.getFileName());

        mBTNStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intStartSver = new Intent(MainActivity.this, DownloadServices.class);
                intStartSver.setAction(DownloadServices.ACTION_START);
                intStartSver.putExtra(DownloadServices.FILE_INFO, fileInfo);
                startService(intStartSver);
            }
        });

        mBTNStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intStopSver = new Intent(MainActivity.this, DownloadServices.class);
                intStopSver.setAction(DownloadServices.ACTION_STOP);
                intStopSver.putExtra(DownloadServices.FILE_INFO, fileInfo);
                startService(intStopSver);
            }
        });

        //注册广播接受
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadServices.ACTION_UPDATE);
        registerReceiver(mReceiver, filter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除广播
        unregisterReceiver(mReceiver);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //判意图的动作
            if (DownloadServices.ACTION_UPDATE.equals(intent.getAction())) {
                int finishedSize = intent.getIntExtra(DownloadTask.FINISHED_SIZE, 0);
                Log.e(TAG, TAG + "完成进度。。。。" + finishedSize);
                //设置接收到的下载进度
                mPBDownloadProgress.setProgress(finishedSize);
            }
        }
    };
}
