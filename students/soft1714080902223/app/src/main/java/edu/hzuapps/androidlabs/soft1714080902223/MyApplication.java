package edu.hzuapps.androidlabs.soft1714080902223;


import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.hzuapps.androidlabs.model.Task;
import edu.hzuapps.androidlabs.presenter.TaskService;
import edu.hzuapps.androidlabs.service.NofyService;

public class MyApplication extends Application {

    private static Context context;
    private List<Long> times = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.v("aaa", "进来了");
                List<Task> tasks = TaskService.INSTANCE.getAllList();
                //遍历所有任务，获取时间
                for (Task task : tasks) {
                    //如果任务已完成，就跳过
                    if(task.getFinish() == 1)
                        continue;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Log.v("aaa", task.getLastTime());
                    long timeTemp = 0;
                    try {
                        timeTemp = simpleDateFormat.parse(task.getLastTime()).getTime();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (timeTemp != 0) {
                        Log.v("aaa", "ggg");
                        times.add(timeTemp);
                        NotificationUtils notificationUtils = new NotificationUtils(MyApplication.getContext());
                        notificationUtils.notifyy(task.getTitle(), task.getContent(), timeTemp, task.getId());
                    }
                }
                Collections.sort(times);
                //启动通知的服务
//                Intent service = new Intent(context, NofyService.class);
//
//                context.startService(service);
            }
        }).start();
//        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//
//        Intent intent = new Intent(this, MyBroadcastReceiver.class);
//        intent.setAction("action_alarm");
//        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        if(Build.VERSION.SDK_INT < 19){
//            am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3000, pendingIntent);
//        }else{
//            am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3000, pendingIntent);
//        }
        //NotificationUtils notificationUtils = new NotificationUtils(this);
        //notificationUtils.notifyy();

    }

    public static Context getContext() {
        return context;
    }
}
