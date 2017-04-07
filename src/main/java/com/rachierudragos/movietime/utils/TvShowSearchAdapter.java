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
import com.rachierudragos.movietime.wrapper.tvshow.TmdbSearchTvShow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dragos on 21.01.2017.
 */

public class TvShowSearchAdapter extends ArrayAdapter<TmdbSearchTvShow.TvShowDetail> {
    ArrayList<Long> tvshows_seen;
    public TvShowSearchAdapter(Context context, List<TmdbSearchTvShow.TvShowDetail> results, ArrayList<Long>tvshowsseen) {
        super(context,0,results);
        tvshows_seen = tvshowsseen;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TmdbSearchTvShow.TvShowDetail tvshowDetail = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie, parent, false);
        }
        Ion.with((ImageView)convertView.findViewById(R.id.image_movie))
                .placeholder(R.drawable.blank)
                .error(R.drawable.blank)
                .load("https://image.tmdb.org/t/p/w92"+tvshowDetail.poster_path);
        ((TextView)convertView.findViewById(R.id.text_name)).setText(tvshowDetail.name);
        ((TextView)convertView.findViewById(R.id.text_description)).setText(tvshowDetail.overview);
        ((TextView)convertView.findViewById(R.id.text_rating)).setText(tvshowDetail.vote_average+"("+tvshowDetail.vote_count+")");
        ((TextView)convertView.findViewById(R.id.text_date)).setText(tvshowDetail.first_air_date);
        if(tvshows_seen.contains(tvshowDetail.id))
            (convertView.findViewById(R.id.alert_seen)).setVisibility(View.VISIBLE);
        else
            (convertView.findViewById(R.id.alert_seen)).setVisibility(View.GONE);
        return convertView;
    }
}
