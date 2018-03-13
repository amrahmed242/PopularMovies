package com.nanodegree.android.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class MovieParser {

    private final ArrayList<MovieItem> Movies=new ArrayList<>();

    //core code of the parsing process
    public List<MovieItem> Parse(String JSON_OBJECT_STRING) throws JSONException {

        JSONObject jsonObject=new JSONObject(JSON_OBJECT_STRING);
        JSONArray jsonArray=jsonObject.getJSONArray("results");

        for(int i=0;i<jsonArray.length();i++){
            JSONObject Movie=jsonArray.getJSONObject(i);

            String movie_title=Movie.getString("title");
            String movie_picture=Movie.getString("poster_path");
            String movie_rating=Movie.getString("vote_average");
            String movie_overview=Movie.getString("overview");
            String movie_release_date=Movie.getString("release_date");

            MovieItem movieItem=new MovieItem(movie_title,movie_picture,movie_rating,movie_overview,movie_release_date);
            Movies.add(movieItem);
        }

        return Movies;
    }
}
