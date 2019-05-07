package edu.hzuapps.androidlabs.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import edu.hzuapps.androidlabs.model.Task;
import edu.hzuapps.androidlabs.presenter.TaskService;
import edu.hzuapps.androidlabs.soft1714080902223.NotificationUtils;

public class NofyService extends Service {
    private final String TAG = "NofyService";
    private Intent in;
    private int flags;
    private int startId;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {        return null;    }

    @Override
    public void onCreate() {
        super.onCreate();

        Toast.makeText(getApplication(), "service", Toast.LENGTH_SHORT).show();
        Log.v(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.in = intent;
        this.flags = flags;
        this.startId = startId;

        new Thread(checkTime).start();
        Toast.makeText(getApplication(), "service",
                Toast.LENGTH_SHORT).show();
        Log.v(TAG, "onStart");
        return START_STICKY;
    }

    //Service 的功能，创建通知栏通知
    Runnable checkTime = new Runnable() {
        @Override
        public void run() {
            String title = in.getStringExtra("title");
            String content = in.getStringExtra("content");
            Log.v(TAG, title + content);
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.sendNotification(title, content);

            //利用 Notification 类设置通知的属性
//            Notification notification = new Notification.Builder(getApplicationContext())
//                    .setContentTitle("事件提醒")
//                    .setContentText("有事件")
//                    // 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
//                    .setDefaults( Notification.DEFAULT_VIBRATE | Notification.DEFAULT_ALL | Notification.DEFAULT_SOUND )
//                    .build();
//            notification.flags = Notification.FLAG_INSISTENT;
//            //利用 NotificationManager 类发出通知栏通知
//            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            nm.notify(1, notification);

        }
    };

    @Override
    public void onDestroy() {
        this.onStartCommand(in, flags, startId);
    }
}