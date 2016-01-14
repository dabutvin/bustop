package com.dabutvin.bustime;

import android.location.Location;

import java.util.Comparator;

/**
 * Created by Dan on 1/13/2016.
 */
public class StopDistanceComparer implements Comparator<Stop> {

    private double latitude;
    private double longitude;

    public StopDistanceComparer(double latitude, double longitude) {

        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public int compare(Stop x, Stop y) {
        double stop1Lat = x.getLat();
        double stop1Lon = x.getLon();

        double stop2Lat = y.getLat();
        double stop2Lon = y.getLon();

        float[] distanceToStop1 = new float[1];
        Location.distanceBetween(stop1Lat, stop1Lon, this.latitude, this.longitude, distanceToStop1);

        float[] distanceToStop2 = new float[1];
        Location.distanceBetween(stop2Lat, stop2Lon, this.latitude, this.longitude, distanceToStop2);

        return distanceToStop1[0] < distanceToStop2[0] ? -1 :
               distanceToStop1[0] > distanceToStop2[0] ?  1 : 0;
    }
}
