package com.mobpro.olinchallenge;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    private Timer autoUpdate;

    public void setService(UpdaterService service) {
        this.service = service;
    }

    private UpdaterService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        Intent i = new Intent(getApplicationContext(), UpdaterService.class);

        startService(i);

        ServiceConnection conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, final IBinder service) {
                setService(((UpdaterService.UpdaterBinder) service).getService());
                startDisplayUpdates();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                if (autoUpdate != null){
                    autoUpdate.cancel();
                    autoUpdate = null;
                }
            }
        };

        bindService(new Intent(this, UpdaterService.class), conn, BIND_AUTO_CREATE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }


    public void startDisplayUpdates(){
        autoUpdate = new Timer();
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if(service.canGetLocation()){

                            ListView listView = (ListView) MainActivity.this.findViewById(R.id.listView);
                            listView.setAdapter(new ArrayAdapter<Person>(MainActivity.this, android.R.layout.simple_list_item_1, service.people));

                        }
                    }
                });
            }
        }, 0, 5000);

    }

}
