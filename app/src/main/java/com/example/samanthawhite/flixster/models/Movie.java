package com.example.samanthawhite.flixster.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel //indicates class is a parcelable
public class Movie {

    //Values from the API
    String title;
    String overview;
    String posterPath; //not full url
    String backdropPath;
    Double voteAverage;


    //no arg empty constructor necessary for parceler
    public Movie() {}

    //initialize from JSON data (getting info from JSON object
    //where do we get this JSON Object? from movie list activity line 94 from the response
    public Movie (JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
        voteAverage = object.getDouble("vote_average");

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

    public String getBackdropPath() {
        return backdropPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }
}
