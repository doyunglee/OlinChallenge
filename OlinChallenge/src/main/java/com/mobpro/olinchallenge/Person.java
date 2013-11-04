package com.mobpro.olinchallenge;

/**
 * Created by rachel on 11/3/13.
 */
public class Person {
    String _id;
    double lat;
    double lon;

    public Person(String id, double lat, double lon) {
        this._id = id;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String toString() {
        return _id;
    }
}
