package com.mobpro.olinchallenge;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;


public class FeedListAdapter extends ArrayAdapter<FeedItem> {

    private final Context context;
    private final List<FeedItem> data;

    public FeedListAdapter(Context context,  List<FeedItem> data){
        super(context, R.layout.feed_item, data);
        this.context = context;
        this.data = data;
        Log.d("hey", "hello");
    }


    private class FeedItemHolder{

        TextView userName;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        FeedItemHolder holder;
        View feedRow = convertView;

        if(feedRow == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            feedRow = inflater.inflate(R.layout.feed_item, parent, false);
            holder = new FeedItemHolder();
            Log.d("Maybe", "Maybe");
            holder.userName = (TextView) feedRow.findViewById(R.id.feedItemUser);
//            holder.text = (TextView) feedRow.findViewById(R.id.feedText);
            Log.d("yes", "YES!");

            feedRow.setTag(holder);
        } else {
            holder = (FeedItemHolder) feedRow.getTag();
        }

        FeedItem item = data.get(position);

        holder.userName.setText(item.userName);
//        holder.text.setText(item.text);

        return feedRow;
    }

}
