package com.example;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.rss.RssFeed;
import com.example.rss.RssItem;
import com.example.rss.RssReader;

import java.net.URL;

public class MainActivity extends Activity {

    public static final String RSS_URL = "http://habrahabr.ru/rss/hubs/";
    private ArrayAdapter<RssItem> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRssWork();
            }
        });

        ListView listView = (ListView) findViewById(R.id.list);
        adapter = new ArrayAdapter<RssItem>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

    }

    private void doRssWork() {
        new AsyncTask<Void, Void, RssFeed>() {

            @Override
            protected void onPreExecute() {
                adapter.clear();
            }

            @Override
            protected RssFeed doInBackground(Void... params) {

                try {
                    URL url = new URL(RSS_URL);
                    RssFeed feed = RssReader.read(url);
                    return feed;
                } catch (Exception e) {
                    Log.e("RSS Reader", "Can't parse RSS", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(RssFeed rssFeed) {
                if (rssFeed != null) {
                    adapter.addAll(rssFeed.getRssItems());
                }
            }

        }.execute();
    }

}
