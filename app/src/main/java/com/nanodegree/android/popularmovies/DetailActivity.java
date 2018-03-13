package com.nanodegree.android.popularmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.nanodegree.android.popularmovies.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private String movie_title;
    private String movie_picture;
    private String movie_rating;
    private String movie_overview;
    private String movie_release_date;
    private ActivityDetailBinding Binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);


        //receive the extras from the intent and fill the UI elements

        Intent intent = getIntent();

        if (intent.hasExtra("movie_title")) {

            movie_title= intent.getStringExtra("movie_title");
            movie_overview= intent.getStringExtra("movie_overview");
            movie_picture= intent.getStringExtra("movie_picture");
            movie_rating= intent.getStringExtra("movie_rating");
            movie_release_date= intent.getStringExtra("movie_release_date");

        }

        try {

            Picasso.with(this)
                    .load("http://image.tmdb.org/t/p/w780/"+movie_picture)
                    .error(R.drawable.error)
                    .into(Binding.DetailPoster);

        } catch (Exception e) {e.printStackTrace();}

        Binding.DetailReleaseDate.setText(getString(R.string.ReleaseDate)+" "+movie_release_date);
        Binding.DetailTitle.setText(movie_title);
        Binding.DetailDescription.setText(movie_overview);
        int i=(int) Float.parseFloat(movie_rating);
        Binding.DetailRating.setText(String.valueOf(i));


    }


}
