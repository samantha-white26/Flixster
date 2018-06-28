package com.example.samanthawhite.flixster.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie {

    //Values from the API
    private String title;
    private String overview;
    private String posterPath; //not full url

    //initialize from JSON data (getting info from JSON object
    //but where are we going to get this info from??
    public Movie (JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");

    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
