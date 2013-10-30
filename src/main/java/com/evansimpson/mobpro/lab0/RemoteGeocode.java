package com.evansimpson.mobpro.lab0;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.ByteArrayOutputStream;

/**
 * Created by evan on 9/19/13.
 */
public class RemoteGeocode extends AsyncTask<String, String, String> {
    static String generateUri(double lat,double lon){
        String Uri = String.format("http://ws.geonames.org/extendedFindNearby?lat=%s&lng=%s&username=demo", lat, lon);
        return Uri;
    }

    @Override
    protected String doInBackground(String... uri){
        String responseString ="";
        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(new HttpGet(uri[0]));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK){
                Log.v("URI", uri[0]);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else {
                response.getEntity().getContent().close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return responseString;
    }
}
