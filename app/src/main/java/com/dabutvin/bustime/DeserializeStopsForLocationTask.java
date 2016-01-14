package com.dabutvin.bustime;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dan on 1/12/2016.
 */
public class DeserializeStopsForLocationTask extends AsyncTask<String, Void, List<Stop>> {
    private StopsForLocationCallbackInterface callback;

    DeserializeStopsForLocationTask(StopsForLocationCallbackInterface callback){

        this.callback = callback;
    }

    @Override
    protected List<Stop> doInBackground(String... params) {
        String json = params[0];

        JSONObject jsonObject = null;
        List<Stop> stops = new ArrayList<>();

        try{
            jsonObject = new JSONObject(json);

            JSONObject dataJsonObject = jsonObject.getJSONObject("data");
            JSONObject referencesJsonObject = dataJsonObject.getJSONObject("references");

            JSONArray routesJsonArray = referencesJsonObject.getJSONArray("routes");
            Map<String, Route> routeDictionary = new HashMap<>();

            for(int i=0; i<routesJsonArray.length(); i++) {
                JSONObject routeJsonObject = routesJsonArray.getJSONObject(i);
                Route route = new Route();

                route.setDescription(routeJsonObject.getString("description"));
                route.setId(routeJsonObject.getString("id"));
                route.setShortName(routeJsonObject.getString("shortName"));
                route.setUrl(routeJsonObject.getString("url"));

                routeDictionary.put(route.getId(), route);
            }

            JSONArray stopsJsonArray = dataJsonObject.getJSONArray("list");
            for(int i=0; i < stopsJsonArray.length(); i++) {
                JSONObject stopJsonObject = stopsJsonArray.getJSONObject(i);
                Stop stop = new Stop();

                stop.setCode(stopJsonObject.getString("code"));
                stop.setDirection(stopJsonObject.getString("direction"));
                stop.setId(stopJsonObject.getString("id"));
                stop.setLat(stopJsonObject.getDouble("lat"));
                stop.setLon(stopJsonObject.getDouble("lon"));
                stop.setName(stopJsonObject.getString("name"));

                String departuresJson = new DownloadJsonTask(null).doInBackground(UrlBuilder.getArrivalsForStop(stop.getId()));
                JSONObject departuresJsonObject = new JSONObject(departuresJson);
                JSONObject departuresDataJsonObject = departuresJsonObject.getJSONObject("data");
                JSONObject departuresEntryJsonObject = departuresDataJsonObject.getJSONObject("entry");
                JSONArray departuresJsonArray = departuresEntryJsonObject.getJSONArray("arrivalsAndDepartures");

                List<Departure> departures = new ArrayList<>();
                for(int j = 0; j < departuresJsonArray.length(); j++) {
                    Departure departure = new Departure();
                    JSONObject departureJsonObject = departuresJsonArray.getJSONObject(j);

                    departure.setDepartureEnabled(departureJsonObject.getBoolean("departureEnabled"));
                    departure.setDistanceFromStop(departureJsonObject.getDouble("distanceFromStop"));
                    departure.setLastUpdateTime(departureJsonObject.getLong("lastUpdateTime"));
                    departure.setNumberOfStopsAway(departureJsonObject.getInt("lastUpdateTime"));
                    departure.setPredicted(departureJsonObject.getBoolean("predicted"));
                    departure.setPredictedDepartureTime(departureJsonObject.getLong("predictedDepartureTime"));
                    departure.setRouteId(departureJsonObject.getString("routeId"));
                    departure.setRouteShortName(departureJsonObject.getString("routeShortName"));
                    departure.setScheduledDepartureTime(departureJsonObject.getLong("scheduledDepartureTime"));
                    departure.setTripHeadsign(departureJsonObject.getString("tripHeadsign"));

                    departures.add(departure);
                }

                List<Route> routes = new ArrayList<>();
                JSONArray routeIdJsonArray = stopJsonObject.getJSONArray("routeIds");
                for (int j=0; j< routeIdJsonArray.length(); j++) {
                    Map<String, Route> localRouteDictionary = new HashMap<>(routeDictionary);
                    String routeId = routeIdJsonArray.getString(j);
                    Route route = localRouteDictionary.get(routeId);

                    for (int k=0; k<departures.size(); k++) {
                        if (departures.get(k).getRouteId().equals(routeId)) {
                            route.setDeparture(departures.get(k));
                        }
                    }

                    routes.add(route);
                }

                stop.setRoutes(routes);

                stops.add(stop);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return stops;
    }

    @Override
    protected void onPostExecute(List<Stop> result) {
        callback.onTaskFinished(result);
    }
}
