package com.dabutvin.bustime;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView prefetchTextView;
    ListView stopsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        prefetchTextView = (TextView)findViewById(R.id.prefetch);
        stopsListView = (ListView)findViewById(R.id.stopsList);
        fetchCurrentBusData(this);
    }

    private void fetchCurrentBusData(final Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            this.prefetchTextView.setText("fetching...");
            new DownloadJsonTask(new StringCallbackInterface() {
                @Override
                public void onTaskFinished(String result) {
                    new DeserializeStopsForLocationTask(new StopsForLocationCallbackInterface() {
                        @Override
                        public void onTaskFinished(List<Stop> stops) {
                            prefetchTextView.setVisibility(View.GONE);

                            StopAdapter stopAdapter = new StopAdapter(context, stops);
                            stopsListView.setAdapter(stopAdapter);
                        }
                    }).execute(result);
                }
            }).execute(UrlBuilder.getStopsForLocation(47.6206780, -122.3076390));
        } else{
            this.prefetchTextView.setText("No network :(");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
