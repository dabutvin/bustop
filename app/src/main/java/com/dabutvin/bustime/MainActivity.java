package com.dabutvin.bustime;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {

    TextView prefetchTextView;
    ListView stopsListView;
    double latitude;
    double longitude;
    private SwipeRefreshLayout refreshContainer;

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

        prefetchTextView = (TextView) findViewById(R.id.prefetch);

        refreshContainer = (SwipeRefreshLayout)findViewById(R.id.swipecontainer);
        refreshContainer.setOnRefreshListener(this);
        refreshContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refreshContainer.setEnabled(true);

        stopsListView = (ListView) findViewById(R.id.stopsList);
        stopsListView.setOnScrollListener(this);

        updateLocation();
        fetchCurrentBusData(this);
    }

    private void fetchCurrentBusData(final Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            this.prefetchTextView.setText("fetching...");

            refreshContainer.setRefreshing(true);

            new DownloadJsonTask(new StringCallbackInterface() {
                @Override
                public void onTaskFinished(String result) {
                    new DeserializeStopsForLocationTask(new StopsForLocationCallbackInterface() {
                        @Override
                        public void onTaskFinished(List<Stop> stops) {

                            Collections.sort(stops, new StopDistanceComparer(latitude, longitude));

                            prefetchTextView.setVisibility(View.GONE);

                            StopAdapter stopAdapter = new StopAdapter(context, stops);
                            stopsListView.setAdapter(stopAdapter);
                            refreshContainer.setRefreshing(false);
                        }
                    }).execute(result);
                }
            }).execute(UrlBuilder.getStopsForLocation(latitude, longitude));


        } else {
            this.prefetchTextView.setText("No network :(");
        }
    }

    private void updateLocation() {
        try {
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
            }

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {
                this.latitude = location.getLatitude();
                this.longitude = location.getLongitude();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("LOC", "Location Not Found");
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (PackageManager.PERMISSION_GRANTED == checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    updateLocation();
                } else {
                    Log.e("HERE", "NNNNN");
                }
                break;
        }
    }

    String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST = 1337;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;

    @Override
    public void onRefresh() {
        this.stopsListView.setBackgroundColor(Color.parseColor("#CCCCCC"));
        fetchCurrentBusData(this);
        this.stopsListView.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int topRowVerticalPosition = (stopsListView == null || stopsListView.getChildCount() == 0) ? 0 :
                stopsListView.getChildAt(0).getTop();

        refreshContainer.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
    }
}
