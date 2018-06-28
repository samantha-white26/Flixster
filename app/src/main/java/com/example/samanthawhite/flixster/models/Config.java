package com.example.samanthawhite.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {
    //the base url for loading images
    String imageBaseUrl;
    //the poster size to view when fetching images, part of the url
    String posterSize;
    //the backdrop size to use when fetching backdrop images
    String backdropSize;

    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");

        imageBaseUrl = images.getString("secure_base_url");

        //get poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        //use the option at index 3 or w342 as a fallback
        posterSize = posterSizeOptions.optString(3, "w342");
        //parse the backdrop and use the option at index 1 or w780 as a fall back
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(1, "w780");
    }

    //helper method for creating url
    public String getImageUrl(String size, String path) {
        return String.format("%s%s%s", imageBaseUrl, size, path); //concatonate all three parts to get image url


    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }

    public String getBackdropSize() {
        return backdropSize;
    }
}
