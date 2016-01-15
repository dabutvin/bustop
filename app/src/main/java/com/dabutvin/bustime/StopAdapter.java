package com.dabutvin.bustime;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Dan on 1/12/2016.
 */
public class StopAdapter extends BaseAdapter {

    private Context context;
    private List<Stop> stops;

    public StopAdapter(Context context, List<Stop> stops) {

        this.context = context;
        this.stops = stops;
    }

    @Override
    public int getCount() {
        return stops.size();
    }

    @Override
    public Object getItem(int position) {
        return stops.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_stop, null);
        }

        final View row = convertView;
        final ListView listView = (ListView)parent;

        TextView nameTextView = (TextView) row.findViewById(R.id.stop_name);
        TextView directionTextView = (TextView) row.findViewById(R.id.stop_direction);
        final TextView route1NameTextView = (TextView) row.findViewById(R.id.stop_route1_name);
        final TextView route2NameTextView = (TextView) row.findViewById(R.id.stop_route2_name);

        Stop stop = stops.get(position);

        nameTextView.setText(stop.getName());
        directionTextView.setText(stop.getDirection());

        List<Route> routes = stop.getRoutes();
        Route route1 = null;
        Route route2 = null;

        if (routes.size() > 0) {
            route1 = routes.get(0);
        }
        if (routes.size() > 1) {
            route2 = routes.get(1);
        }

        if (route1 != null) {
            Departure route1Departure = route1.getDeparture();
            if (route1Departure == null) {
                route1NameTextView.setText(route1.getShortName() + " | " + route1.getDescription());
            } else {
                route1NameTextView.setText(route1.getShortName() + " | " + route1Departure.getTripHeadsign() + " | " + minutesAway(route1Departure.getPredictedDepartureTime()));
            }
        }

        if (route2 != null) {
            Departure route2Departure = route2.getDeparture();
            if (route2Departure == null) {
                route2NameTextView.setText(route2.getShortName() + " | " + route2.getDescription());
            } else {
                route2NameTextView.setText(route2.getShortName() + " | " + route2Departure.getTripHeadsign() + " | " + minutesAway(route2Departure.getPredictedDepartureTime()));
            }
        }

        row.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        Log.e("ACTION_DOWN", "upX=" + upX + " | downX=" + downX);
                        downX = event.getX();
                        return true;
                    }

                    case MotionEvent.ACTION_MOVE: {
//                        Log.e("ACTION_MOVE", "upX=" + upX + " | downX=" + downX);
                        upX = event.getX();
                        float deltaX = downX - upX;

                        if (Math.abs(deltaX) > 30 && listView != null && !motionInterceptDisallowed) {
                            listView.requestDisallowInterceptTouchEvent(true);
                            motionInterceptDisallowed = true;
                        }

                        swipe(row.findViewById(R.id.dismiss1), (int) deltaX);
                        swipe(route1NameTextView, (int) deltaX);
                        swipe(route2NameTextView, (int) deltaX);
                        return true;
                    }

                    case MotionEvent.ACTION_UP: {
                        upX = event.getX();
                        Log.e("ACTION_UP", "upX=" + upX + " | downX=" + downX);
                        float deltaX = upX - downX;

                        if (Math.abs(deltaX) > 300) {
                            swipe(row.findViewById(R.id.dismiss1), 1);
                            swipe(route1NameTextView, 1);
                            swipe(route2NameTextView, 1);
                            remove();
                        }

                        if(listView != null) {
                            listView.requestDisallowInterceptTouchEvent(false);
                            motionInterceptDisallowed = false;
                        }
                    }

                    case MotionEvent.ACTION_CANCEL:
                        return false;
                }

                return true;
            }

            private void swipe(View view, int distance) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
                params.rightMargin = distance;
                params.leftMargin = -distance;
                view.setLayoutParams(params);
            }

            private void remove() {
                stops.remove(position);
                notifyDataSetChanged();
            }
        });


        return row;
    }

    private float downX;
    private float upX;
    private boolean motionInterceptDisallowed = false;

    private String minutesAway(long predictedDepartureTime) {
        long difference = predictedDepartureTime - System.currentTimeMillis();
        return "" + difference / 60000 + " min away";
    }
}
