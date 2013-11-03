package com.mobpro.olinchallenge;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    private Timer autoUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GPS gps = new GPS(this);

        autoUpdate = new Timer();
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if(gps.canGetLocation()){

                            TextView lat = (TextView) findViewById(R.id.lat);
                            TextView lon = (TextView) findViewById(R.id.lon);

                            lat.setText("Latitude: " + String.valueOf(gps.getLatitude()));
                            lon.setText("Longitude: " + String.valueOf(gps.getLongitude()));


                        }
                    }
                });
            }
        }, 0, 1000);


        NotificationHandler.getInstance().setContext(this).makeNotifications();
    }

    AsyncTask utahraptor = new AsyncTask<Void, Void, ArrayList<FeedItem>>(){
        protected ArrayList<FeedItem> doInBackground(Void... voids){

            ArrayList<FeedItem> twits = new ArrayList<FeedItem>();
            ArrayList<FeedItem> middle = new ArrayList<FeedItem>();
            String gatsby = "";

            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
//        HttpGet pull = new HttpGet("http://twitterproto.herokuapp.com/tweets");
            HttpGet pull = new HttpGet("http://api.wunderground.com/api/dd14b81695901028/hourly/q/MA/Needham.json");
            pull.setHeader("Content-type","application/json");

            try{
                HttpResponse httpResponse = httpClient.execute(pull);
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream is = httpEntity.getContent();

                BufferedReader read = new BufferedReader(new InputStreamReader(is,"UTF-8"),8);
                StringBuilder build = new StringBuilder();

                String line;
                String nl = System.getProperty("line.separator");
                while ((line = read.readLine()) != null) {
                    build.append(line + nl);
                }

                gatsby = build.toString();

            }



            catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject ntweet = new JSONObject(gatsby);
                JSONArray list = ntweet.getJSONArray("hourly_forecast");

//                            int x = 0;

                for (int i=0; i<list.length(); i++){
                    int cut = startHour.toString().indexOf(":");
                    int dHour = list.getJSONObject(i).getJSONObject("feelslike").getString("english").indexOf(":");
                    String time1;
                    String time2;

                    if (tb1.isChecked()){
                        time1 = "PM";
                    } else {
                        time1 = "AM";
                    }
                    if (tb2.isChecked()){
                        time2 = "PM";
                    } else {
                        time2 = "AM";
                    }
//                                String placeone = startHour.toString();
//                                int place1 = Integer.parseInt(placeone);
//
//                                String placetwo = endHour.toString();
//                                int place2 = Integer.parseInt(placetwo);

//                                int dif = place2 - place1;

                    FeedItem swag = new FeedItem(startHour.getText()+" " + time1 +" feels like " + list.getJSONObject(i).getJSONObject("feelslike").getString("english"), list.getJSONObject(i).getString("condition"));
                    Log.d(swag.toString(), "hello");

//                                ArrayList<Integer> nums = new ArrayList<Integer>();
//
//                                while (x < dif) {
//                                    nums.add(x);
//                                    x = x+1;
//                                }
//
//                                Log.d(nums.toString(), "hhhheeelllooo");

                    Log.isLoggable("What is it", cut);
                    Log.isLoggable("meeeeeeow", dHour);
                    if (list.getJSONObject(i).getJSONObject("FCTTIME").getString("pretty").contains(startHour.getText()+":00 " + time1)){

                        twits.add(swag);

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return twits;

        }

        protected void onPostExecute(ArrayList<FeedItem> twits) {
            Log.d("couldbehere", "yes");
            FeedListAdapter feedListAdapter = new FeedListAdapter(getApplicationContext(), twits);
            ListView wList = (ListView) findViewById(R.id.swag);
            wList.setAdapter(feedListAdapter);
            Log.d("itishere","yes");
        }

    }.execute();
}



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }
    
}
