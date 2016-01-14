package com.dabutvin.bustime;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Dan on 1/12/2016.
 */
public class StopAdapter extends BaseAdapter {

    private Context context;
    private List<Stop> stops;

    public StopAdapter(Context context, List<Stop> stops){

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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater =  (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_stop, null);
        }

        TextView nameTextView = (TextView)convertView.findViewById(R.id.stop_name);
        TextView directionTextView = (TextView)convertView.findViewById(R.id.stop_direction);
        TextView route1NameTextView = (TextView)convertView.findViewById(R.id.stop_route1_name);
        TextView route2NameTextView = (TextView)convertView.findViewById(R.id.stop_route2_name);

        Stop stop = stops.get(position);

        nameTextView.setText(stop.getName());
        directionTextView.setText(stop.getDirection());

        List<Route> routes = stop.getRoutes();
        Route route1 = null;
        Route route2 = null;

        if (routes.size() > 0) {
            route1 = routes.get(0);
        }
        if(routes.size() > 1) {
            route2 = routes.get(1);
        }

        if(route1 != null) {
            Departure route1Departure = route1.getDeparture();
            if (route1Departure == null ){
                route1NameTextView.setText(route1.getShortName() + " - " + route1.getDescription());
            } else {
                route1NameTextView.setText(route1.getShortName() + " - " + route1Departure.getTripHeadsign() + " - ");
            }
        }

        if (route2 != null) {
            Departure route2Departure = route2.getDeparture();
            if (route2Departure == null) {
                route2NameTextView.setText(route2.getShortName() + " - " + route2.getDescription());
            } else {
                route2NameTextView.setText(route2.getShortName() + " - " + route2Departure.getTripHeadsign());
            }
        }

        return convertView;
    }
}
