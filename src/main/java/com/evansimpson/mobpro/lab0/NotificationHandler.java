package com.evansimpson.mobpro.lab0;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

/**
 * Created by rachel on 10/29/13.
 */
public class NotificationHandler {

    private Context context;
    private NotificationManager notificationManager;
    private static NotificationHandler instance;



    public static NotificationHandler getInstance(){
        if (instance == null){
            instance = new NotificationHandler();
        }
        return instance;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void makeNotifications(Context context){
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification n = new Notification.Builder(context)
                .setContentTitle("You are near 3 Oliners")
                .setContentText("Scott, Chris, and Greg are near you")
                .setSmallIcon(R.drawable.ic_launcher)
                .build();

        notificationManager.notify(0, n);
    }
}
