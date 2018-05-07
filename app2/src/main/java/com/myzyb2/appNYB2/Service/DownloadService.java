package com.myzyb2.appNYB2.Service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.myzyb2.appNYB2.common.Constant;
import com.myzyb2.appNYB2.util.CommonUtil;

import java.io.File;

/**
 * 下载更新版本
 *
 * Created by cuiz on 2016/2/18.
 */

public class DownloadService extends Service {
    private DownloadManager dm;
    private long enqueue;
    private String url;
    private BroadcastReceiver receiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        url = intent.getStringExtra("url");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/NYB/" + Constant.UPDATE_SAVENAME)),
                        "application/vnd.android.package-archive");
                startActivity(intent);
                stopSelf();
            }
        };

        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        startDownload();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void startDownload() {
        File file = new File(Environment.getExternalStorageDirectory() + "/NYB/" + Constant.UPDATE_SAVENAME);
        if (file.exists()) {
            file.delete();
        }

        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setShowRunningNotification(true);
        //不显示下载界面
        request.setVisibleInDownloadsUi(true);
        // 设置为可被媒体扫描器找到
        request.allowScanningByMediaScanner();
        // 设置为可见和可管理
        request.setVisibleInDownloadsUi(true);
        request.setMimeType("application/vnd.android.package-archive");
        request.setDestinationInExternalPublicDir("NYB", Constant.UPDATE_SAVENAME);
        enqueue = dm.enqueue(request);
        CommonUtil.StartToast(getApplicationContext(), "正在后台下载!");
    }
}