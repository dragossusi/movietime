package com.rachierudragos.movietime.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.rachierudragos.movietime.R;
import com.rachierudragos.movietime.wrapper.movie.TmdbSearchMovies;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dragos on 16.01.2017.
 */
public class MovieSearchAdapter extends ArrayAdapter<TmdbSearchMovies.MovieDetail> {
    ArrayList<Long>movies_seen;
    public MovieSearchAdapter(Context context, List<TmdbSearchMovies.MovieDetail> results, ArrayList<Long>moviesseen) {
        super(context,0,results);
        movies_seen = moviesseen;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TmdbSearchMovies.MovieDetail movieDetail = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie, parent, false);
        }
        Ion.with((ImageView)convertView.findViewById(R.id.image_movie))
                .placeholder(R.drawable.blank)
                .error(R.drawable.blank)
                .load("https://image.tmdb.org/t/p/w92"+movieDetail.poster_path);
        ((TextView)convertView.findViewById(R.id.text_name)).setText(movieDetail.title);
        ((TextView)convertView.findViewById(R.id.text_description)).setText(movieDetail.overview);
        ((TextView)convertView.findViewById(R.id.text_rating)).setText(movieDetail.vote_average+"("+movieDetail.vote_count+")");
        ((TextView)convertView.findViewById(R.id.text_date)).setText(movieDetail.release_date);
        if(movies_seen.contains(movieDetail.id))
            (convertView.findViewById(R.id.alert_seen)).setVisibility(View.VISIBLE);
        else
            (convertView.findViewById(R.id.alert_seen)).setVisibility(View.GONE);
        return convertView;
    }
}
