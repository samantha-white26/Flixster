package com.example.samanthawhite.flixster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.samanthawhite.flixster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {

    public final static String API_BASE_URL = "https://api.themoviedb.org/3";

    public final static String API_KEY_PARAM = "api_key";

    public final static String TAG = "MovieListActivity";


    //instance fields
    AsyncHttpClient client;

    String imageBaseUrl;

    String posterSize;

    //the list of currently playing movies
    ArrayList<Movie> movies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        //intialize client
        client = new AsyncHttpClient();
        //initialize the list of movies
        movies = new ArrayList<>();


        //get the configuration on app creation
        getConfiguration();





    }

    //get the list of currently playing movies from the API
    //now playing is the endpoint
    private void getNowPlaying() {
        //create url
        String url = API_BASE_URL + "/movie/now_playing";

        //set the request parameters...end point... request param object exists already
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API key always required

        //executre a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //load the results into movie list
                try {
                    JSONArray results = response.getJSONArray("results");
                    //iterate through result set and create Movie object
                    for (int i = 0; i < results.length(); i++) {
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));

                } catch (JSONException e) {
                    logError("failed to parse now playing movies", e , true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now playing endpoint", throwable, true);
            }
        });

    }



    //access config endpoint, get secure http and poster size
    private void getConfiguration() {
        //create url
        String url = API_BASE_URL + "/configuration";

        //set the request parameters...end point... request param object exists already
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API key always required

        //executre a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //get image base url
                try {
                    JSONObject images = response.getJSONObject("images");

                    imageBaseUrl = images.getString("secure_base_url");

                    //get poster size
                    JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
                    //use the option at index 3 or w342 as a fallback
                    posterSize = posterSizeOptions.optString(3, "w342");
                    Log.i(TAG, String.format("Loaded configuration with imageBaseUrl %s and posterSize %s", imageBaseUrl, posterSize));

                    getNowPlaying();

                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configuration", throwable, true);

            }
        });



    }


    private void logError (String message, Throwable error, boolean alertUser) {
        Log.e(TAG, message, error);

        //alert user to avoid silent errors
        if (alertUser) {
            //display toast error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

}