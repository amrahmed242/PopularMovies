package com.nanodegree.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.List;


//////////////////////////////***********normal Recycler view adapter filling the grid with the movie data*******///////////////////////////////////


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private static List<MovieItem> Movies_Array_list;
    private static Context context;
    public MovieAdapter(List<MovieItem> Movies_Array_list) {
        this.Movies_Array_list=Movies_Array_list;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView Poster;


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            Poster=itemView.findViewById(R.id.movie_poster);
        }

        //overriding onclick to send every clicked item data to the detail activity
        @Override
        public void onClick(View v) {

            Intent intent=new Intent(context,DetailActivity.class);
            intent.putExtra("movie_title",Movies_Array_list.get(getAdapterPosition()).getMovie_title());
            intent.putExtra("movie_picture",Movies_Array_list.get(getAdapterPosition()).getMovie_picture());
            intent.putExtra("movie_rating",Movies_Array_list.get(getAdapterPosition()).getMovie_rating());
            intent.putExtra("movie_overview",Movies_Array_list.get(getAdapterPosition()).getMovie_overview());
            intent.putExtra("movie_release_date",Movies_Array_list.get(getAdapterPosition()).getMovie_release_date());
            context.startActivity(intent);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context=parent.getContext();
        int item_layout=R.layout.movie_item_view;
        View Movie_item = LayoutInflater.from(context).inflate(item_layout, parent, false);
        return new ViewHolder(Movie_item);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        try {

          Picasso.with(holder.Poster.getContext())
                    .load(String.valueOf("http://image.tmdb.org/t/p/w500/"+Movies_Array_list.get(position).getMovie_picture()))
                    .error(R.drawable.error)
                    .into(holder.Poster);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    @Override
    public int getItemCount() {
        if (Movies_Array_list==null) return 0;
        return Movies_Array_list.size();
    }



}
