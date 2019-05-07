package edu.hzuapps.androidlabs.soft1714080902223;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import edu.hzuapps.androidlabs.service.NofyService;


public class NotificationUtils extends ContextWrapper {

    private NotificationManager manager;
    public static final String id = "channel_1";
    public static final String name = "channel_name_1";
    static int iid = 0;

    public NotificationUtils(Context context){
        super(context);
    }

    public void createNotificationChannel(){
        // 级别为高时，会自动在当前页面中弹出
        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }
    private NotificationManager getManager(){
        if (manager == null){
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }
    public Notification.Builder getChannelNotification(String title, String content){
        return new Notification.Builder(getApplicationContext(), id)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(true);
    }
    public NotificationCompat.Builder getNotification_25(String title, String content){
        return new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(true);
    }
    public void sendNotification(String title, String content){
        //sdk26以上需要配置Notifycation
        if (Build.VERSION.SDK_INT>=26){
            createNotificationChannel();
            Notification notification = getChannelNotification
                    (title, content).build();
            getManager().notify(1,notification);
        }else{
            Notification notification = getNotification_25(title, content).build();
            getManager().notify(1,notification);
        }
    }

    public void notifyy(String title, String content, long time, long id) {
        // 如果时间小于现在的时间，直接返回
        if(time < System.currentTimeMillis())
            return;
        Log.v("ttt", Integer.toString(iid));
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, NofyService.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.setAction("action_alarm");
        PendingIntent pendingIntent = PendingIntent.getService(this, (int)id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(Build.VERSION.SDK_INT < 19){
            am.setRepeating(AlarmManager.RTC_WAKEUP, time,5000, pendingIntent);
        }else{
            am.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            //am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3000, pendingIntent);
        }
    }

    public void cancelNotify(long id){
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NofyService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, (int)id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(pendingIntent);
    }
}
