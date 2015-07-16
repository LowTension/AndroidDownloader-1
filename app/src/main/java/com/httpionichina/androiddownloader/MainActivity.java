package com.httpionichina.androiddownloader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.httpionichina.androiddownloader.entity.FileInfo;
import com.httpionichina.androiddownloader.services.DownloadServices;

public class MainActivity extends AppCompatActivity {


    private TextView mTVDownloadProgress;

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
        mTVDownloadProgress = (TextView) findViewById(R.id.tv_download_progress);
        mPBDownloadProgress = (ProgressBar) findViewById(R.id.pb_download_progress);
        mBTNStop = (Button) findViewById(R.id.btn_stop);
        mBTNStart = (Button) findViewById(R.id.btn_start);

        final FileInfo fileInfo = new FileInfo(0, "http://img.huxiu.com/portal/201408/26/22232930hzhnzywn9hy3k4.jpg",
                "22232930hzhnzywn9hy3k4.jpg", 0, 0);
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

    }
}
