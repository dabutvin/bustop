package com.dabutvin.bustime;

import java.math.BigInteger;

/**
 * Created by Dan on 1/12/2016.
 */
public class Departure {
    private boolean departureEnabled;
    private double distanceFromStop;
    private BigInteger lastUpdateTime;
    private int numberOfStopsAway;
    private boolean predicted;
    private BigInteger predictedDepartureTime;
    private String routeId;
    private String routeShortName;
    private BigInteger scheduledDepartureTime;
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

    public BigInteger getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(BigInteger lastUpdateTime) {
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

    public BigInteger getPredictedDepartureTime() {
        return predictedDepartureTime;
    }

    public void setPredictedDepartureTime(BigInteger predictedDepartureTime) {
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

    public BigInteger getScheduledDepartureTime() {
        return scheduledDepartureTime;
    }

    public void setScheduledDepartureTime(BigInteger scheduledDepartureTime) {
        this.scheduledDepartureTime = scheduledDepartureTime;
    }

    public String getTripHeadsign() {
        return tripHeadsign;
    }

    public void setTripHeadsign(String tripHeadsign) {
        this.tripHeadsign = tripHeadsign;
    }
}
