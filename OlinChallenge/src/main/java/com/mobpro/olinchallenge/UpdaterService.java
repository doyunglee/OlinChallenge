package com.mobpro.olinchallenge;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class UpdaterService extends Service {
    private Timer autoUpdate;
    ArrayList<Person> people = new ArrayList<Person>();

    double lat;
    double lon;

    GPS gps;


    public UpdaterBinder onBind(Intent intent) {
        return new UpdaterBinder();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        gps = new GPS(this);
        lat = gps.getLatitude();
        lon = gps.getLongitude();
        autoUpdate = new Timer();
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        updateLocation();
                        postLocation();
                        getPeople();
                        makeNotifications();
                    }
                }).start();

            }
        }, 0, 5000);

        return super.onStartCommand(intent, flags, startId);
    }

    public boolean canGetLocation() {
        return gps.canGetLocation();
    }

    public void updateLocation(){
        if(gps.canGetLocation()){
            lat = gps.getLatitude();
            lon = gps.getLongitude();
        }
    }

    public void postLocation(){
        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://creepy-oasis-4040.herokuapp.com/people/rachel");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("lat", ""+lat));
            nameValuePairs.add(new BasicNameValuePair("lon", ""+lon));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httppost);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getPeople(){
        String responseString ="";
        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet("http://creepy-oasis-4040.herokuapp.com/people");

            HttpResponse response = httpClient.execute(httpget);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();
            responseString = out.toString();
            List<List<String>> parsedResponse = JSONParse(responseString);
            people = new ArrayList<Person>();
            for (List<String> resp:parsedResponse){
                if (!resp.get(0).equals("rachel")){
                    Person person = new Person(resp.get(0), Double.valueOf(resp.get(1)), Double.valueOf(resp.get(2)));
                    Log.d("person", person.toString());
                    if (Math.abs(person.lat - lat) <= .005 && Math.abs(person.lon - lon) <=.005){
                        people.add(person);
                        Log.d("person", "added");
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void makeNotifications(){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent resultIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (people.size() == 0){
            notificationManager.cancelAll();
            return;
        }

        String title;
        String text;

        if (people.size() == 1){
            title = "You are near 1 Oliner";
            text = people.get(0)._id + " is near you";
        } else if (people.size() == 2){
            title = "You are near 2 Oliners";
            text = people.get(0)._id + " and " + people.get(1) + " are near you";
        } else {
            title = "You are near " + people.size() + " Oliners";
            text = people.get(0)._id + ", " + people.get(1) + ", and " + (people.size()-2) + " other Oliners are near you";
        }


        Notification n = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(resultIntent)
                .getNotification();

        notificationManager.notify(0, n);
    }


    public List<List<String>> JSONParse(String responseString) throws JSONException {
        JSONObject obj = new JSONObject(responseString);
        List<List<String>> res = new ArrayList<List<String>>();
        JSONArray array;
        array = obj.getJSONArray("people");

        for (int j = 0; j<array.length(); j++){
            List<String> inner = new ArrayList<String>();
            inner.add(array.getJSONObject(j).getString("_id"));
            inner.add(array.getJSONObject(j).getString("lat"));
            inner.add(array.getJSONObject(j).getString("lon"));
            res.add(inner);
        }
        return res;
    }


    public class UpdaterBinder extends Binder {
        UpdaterService getService() {
            return UpdaterService.this;
        }
    }

}
