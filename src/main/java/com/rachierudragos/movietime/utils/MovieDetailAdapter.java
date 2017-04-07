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
import com.rachierudragos.movietime.wrapper.movie.TmdbMovie;

import java.util.List;

/**
 * Created by Dragos on 16.01.2017.
 */

public class MovieDetailAdapter extends ArrayAdapter<TmdbMovie> {
    public MovieDetailAdapter(Context context, List<TmdbMovie> results) {
        super(context,0,results);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TmdbMovie movieDetail = getItem(position);
        //TODO add more details, this will be adapter for longer details
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
        return convertView;
    }
}
