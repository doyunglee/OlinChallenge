package com.mobpro.olinchallenge;

import android.util.Log;

public class FeedItem {

    public String text;
    public String userName;
    public String condition;

    public FeedItem(String userName, String text){
        this.userName = userName;
        this.text = text;
        Log.d("I made it here", "yipee");
    }
}
