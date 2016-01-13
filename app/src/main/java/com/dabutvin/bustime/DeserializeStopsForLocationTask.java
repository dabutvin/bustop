package com.dabutvin.bustime;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;
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

                List<Route> routes = new ArrayList<>();
                JSONArray routeIdJsonArray = stopJsonObject.getJSONArray("routeIds");
                for (int j=0; j< routeIdJsonArray.length(); j++) {
                    String routeId = routeIdJsonArray.getString(j);
                    routes.add(routeDictionary.get(routeId));
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
