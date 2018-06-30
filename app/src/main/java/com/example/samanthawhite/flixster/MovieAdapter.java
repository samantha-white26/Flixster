package com.example.samanthawhite.flixster;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samanthawhite.flixster.models.Config;
import com.example.samanthawhite.flixster.models.Movie;
import com.example.samanthawhite.flixster.models.MovieDetailsActivity;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {


    //list of movies
    ArrayList<Movie> movies;
    //config needed for image urls
    Config config;
    //context for rendering
    Context context;

    //Intialize with list
    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    //Creates and inflates a new view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //get the context and create the inflate

        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //create the view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);


        //return view wrapped in a viewholder
        return new ViewHolder(movieView);
    }

    //binds an infalted view to a new item
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //get the movie data at the specified position
        Movie movie = movies.get(position);
        // populate the view with the movie data
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        //determine the current orientation
        boolean isPortrait =  context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;


        //build url for poster image
        String imageUrl = null;

        //If in portrait mode, load the poster image
        if (isPortrait) {
            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        } else {
            //landscape
            imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
        }

        //get the correct placeholder and imageview for the current orientation
        int placeholderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? holder.ivPosterImage : holder.ivBackdropImage;

        //load image using glide
        GlideApp.with(context)
                .load(imageUrl)
                .transform(new RoundedCornersTransformation(25, 10))
                .placeholder(placeholderId)
                .error(placeholderId)
                .into(imageView);

    }



    // return the toal number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    //create the viewholder as a static inner class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Track view objects
        @Nullable @BindView(R.id.ivBackdropImage) ImageView ivBackdropImage;
        @Nullable @BindView(R.id.ivPosterImage) ImageView ivPosterImage;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvOverview) TextView tvOverview;

//        ImageView ivPosterImage;
//        ImageView ivBackdropImage;
//        TextView tvTitle;
//        TextView tvOverview;

        @Override
        public void onClick(View view) {
            //get item position
            int position = getAdapterPosition();
            //make sure position is valid (is in the view)
            if (position != RecyclerView.NO_POSITION){
                //get the movie at that position
                Movie movie = movies.get(position);
                //create an intent for an activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                //set up so the information we want to pass will be passed to the new activtiy
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                //wrapping the image to pass to movie details activity
                intent.putExtra("image_poster", Parcels.wrap(config));
                //show the activity
                context.startActivity(intent);
            }
        }

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

//            //lookup view objects by id
//            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage); //remember @Nullable
//            ivBackdropImage = (ImageView) itemView.findViewById(R.id.ivBackdropImage); //remember @Nullable
//            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
//            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
//
//            //be sure to import butterknife
//            //@Nullable @BindView
//            //@Nullable @BindView
//            //@BindView
//            //@BindView

            itemView.setOnClickListener(this);
        }
    }
}
