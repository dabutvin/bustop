package com.dabutvin.bustime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 1/12/2016.
 */
public class Route {
    private String description;
    private String id;
    private String shortName;
    private String url;
    private Departure departure;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Departure getDeparture() {
        return departure;
    }

    public void setDeparture(Departure departure) {
        this.departure = departure;
    }
}
