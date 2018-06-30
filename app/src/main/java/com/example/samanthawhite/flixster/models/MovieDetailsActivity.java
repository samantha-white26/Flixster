package com.example.samanthawhite.flixster.models;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.samanthawhite.flixster.GlideApp;
import com.example.samanthawhite.flixster.MovieTrailerActivity;
import com.example.samanthawhite.flixster.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity {

    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvOverview) TextView tvOverview;
    @BindView(R.id.rbVoteAverage) RatingBar rbVoteAverage;
    @BindView(R.id.ivYoutube) ImageView ivYoutube;

    public final static String API_YOUTUBE_KEY_PARAM = "youtube_api_key";

    public final static String API_BASE_URL = "https://api.themoviedb.org/3";

    public final static String API_KEY_PARAM = "api_key";

    Movie movie;
    //instance client
    AsyncHttpClient client;
    Config config;


    //

//    TextView tvTitle;
//    TextView tvOverview;
//    RatingBar rbVoteAverage;

    @OnClick(R.id.ivYoutube)
    public void OnClick(View view) {
        getYoutubeKey();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ButterKnife.bind(this);
        //intialize client
        client = new AsyncHttpClient();

//        tvTitle = (TextView) findViewById(R.id.tvTitle);
//        tvOverview = (TextView) findViewById(R.id.tvOverview);
//        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);

        //unwrap the movie passed from the intent using its simple key name as the key and then use this specific movie
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        config = (Config) Parcels.unwrap(getIntent().getParcelableExtra("image_poster"));

        //log error with movie unwrapping
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        //set title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        //vote average is 1-10 so we need to divide by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);


      //  boolean isPortrait =  context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;


        //build url for poster image
        String imageUrl = null;

        //If in portrait mode, load the poster image
//        if (isPortrait) {
//            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
//        } else {
//            //landscape
//            imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
//        }
//
//        //get the correct placeholder and imageview for the current orientation
//        int placeholderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
//        ImageView imageView = isPortrait ? holder.ivPosterImage : holder.ivBackdropImage;


        //load image using glide
        GlideApp.with(this)
                .load(config.getImageUrl(config.getPosterSize(), movie.getPosterPath()))
                .transform(new RoundedCornersTransformation(25, 10))
                .placeholder(R.drawable.flicks_movie_placeholder)
                .error(R.drawable.flicks_movie_placeholder)
                .into(ivYoutube);




    }



    private void getYoutubeKey() {
        // create url
        String url = API_BASE_URL + "/movie/" + movie.getId() +"/videos";

        // set the request parameters...end point... request param object exists already
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); // API key always required

        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //load the results into movie list
                try {
                    JSONArray results = response.getJSONArray("results");
                    JSONObject object = results.getJSONObject(0);
                    String youtube_key = object.getString("key");

                    //create an intent for an activity
                    Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                    //set up so the information we want to pass will be passed to the new activtiy
                    intent.putExtra("youtube_key", Parcels.wrap(youtube_key));
                    startActivity(intent);

                    //iterate through result set and create Movie object
                    Log.i("MovieDetailsActivity", String.format("Loaded %s movies", results.length()));

                } catch (JSONException e) {
                    Log.e("MovieDetailsActivity", "failed to parse getYoutubeKey");
                    //LogError("failed to parse now playing movies", e , true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //logError("Failed to get data from now playing endpoint", throwable, true);
                Log.e("MovieDetailsActivity", "failed to parse getYoutubeKey second area ");
            }
        });


    }

}
