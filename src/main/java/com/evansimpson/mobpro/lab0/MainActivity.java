package com.evansimpson.mobpro.lab0;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    private Timer autoUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NotificationHandler.getInstance().makeNotifications(this);

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
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }
    
}
