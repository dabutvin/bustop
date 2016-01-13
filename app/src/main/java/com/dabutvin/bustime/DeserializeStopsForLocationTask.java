package com.dabutvin.bustime;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
