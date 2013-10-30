package com.evansimpson.mobpro.lab0;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by rachel on 10/29/13.
 */
public class NotificationHandler {

    private Context context;
    private NotificationManager notificationManager;
    private static NotificationHandler instance;
    private Handler handler;

    public NotificationHandler() {
        handler = new Handler();
    }


    public static NotificationHandler getInstance(){
        if (instance == null){
            instance = new NotificationHandler();
        }
        return instance;
    }

    public NotificationHandler setContext(Context context) {
        this.context = context;
        return this;
    }

    public void makeNotifications(){

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent resultIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification n = new Notification.Builder(context)
                .setContentTitle("You are near 3 Oliners")
                .setContentText("Scott, Chris, and Greg are near you")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(resultIntent)
                .getNotification();



        notificationManager.notify(0, n);
        Log.d("notification", "updated");

        setNextUpdate(10000);
    }

    private void setNextUpdate(int delay) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                makeNotifications();
            }
        }, delay);
    }
}
