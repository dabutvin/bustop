package com.dabutvin.bustime;

/**
 * Created by Dan on 1/12/2016.
 */
public class UrlBuilder {
    private static String apiKey = "40d2e5ae-160f-4f89-a5dd-483e2ca8655e";
    private static String rootUrl = "http://api.pugetsound.onebusaway.org/api/where/";

    public static String getStopsForLocation(double latitude, double longitude) {
        return rootUrl + "stops-for-location.json?key=" + apiKey + "&lat=" + latitude + "&lon=" + longitude;
    }
}
