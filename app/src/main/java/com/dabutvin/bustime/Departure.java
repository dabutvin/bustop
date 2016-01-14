    package com.dabutvin.bustime;

/**
 * Created by Dan on 1/12/2016.
 */
public class Departure {
    private boolean departureEnabled;
    private double distanceFromStop;
    private long lastUpdateTime;
    private int numberOfStopsAway;
    private boolean predicted;
    private long predictedDepartureTime;
    private String routeId;
    private String routeShortName;
    private long scheduledDepartureTime;
    private String tripHeadsign;

    public boolean isDepartureEnabled() {
        return departureEnabled;
    }

    public void setDepartureEnabled(boolean departureEnabled) {
        this.departureEnabled = departureEnabled;
    }

    public double getDistanceFromStop() {
        return distanceFromStop;
    }

    public void setDistanceFromStop(double distanceFromStop) {
        this.distanceFromStop = distanceFromStop;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getNumberOfStopsAway() {
        return numberOfStopsAway;
    }

    public void setNumberOfStopsAway(int numberOfStopsAway) {
        this.numberOfStopsAway = numberOfStopsAway;
    }

    public boolean isPredicted() {
        return predicted;
    }

    public void setPredicted(boolean predicted) {
        this.predicted = predicted;
    }

    public long getPredictedDepartureTime() {
        return predictedDepartureTime;
    }

    public void setPredictedDepartureTime(long predictedDepartureTime) {
        this.predictedDepartureTime = predictedDepartureTime;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteShortName() {
        return routeShortName;
    }

    public void setRouteShortName(String routeShortName) {
        this.routeShortName = routeShortName;
    }

    public long getScheduledDepartureTime() {
        return scheduledDepartureTime;
    }

    public void setScheduledDepartureTime(long scheduledDepartureTime) {
        this.scheduledDepartureTime = scheduledDepartureTime;
    }

    public String getTripHeadsign() {
        return tripHeadsign;
    }

    public void setTripHeadsign(String tripHeadsign) {
        this.tripHeadsign = tripHeadsign;
    }
}
