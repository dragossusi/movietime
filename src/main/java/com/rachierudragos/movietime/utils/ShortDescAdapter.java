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
import com.rachierudragos.movietime.wrapper.TmdbShortDesc;

import java.util.List;

/**
 * Created by Dragos on 21.01.2017.
 */

public class ShortDescAdapter extends ArrayAdapter<TmdbShortDesc> {
    public ShortDescAdapter(Context context, List<TmdbShortDesc> results) {
        super(context,0,results);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TmdbShortDesc detail = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie, parent, false);
        }
        Ion.with((ImageView)convertView.findViewById(R.id.image_movie))
                .placeholder(R.drawable.blank)
                .error(R.drawable.blank)
                .load("https://image.tmdb.org/t/p/w92"+detail.poster_path);
        ((TextView)convertView.findViewById(R.id.text_name)).setText(detail.title);
        ((TextView)convertView.findViewById(R.id.text_description)).setText(detail.overview);
        ((TextView)convertView.findViewById(R.id.text_rating)).setText(detail.vote_average+"("+detail.vote_count+")");
        ((TextView)convertView.findViewById(R.id.text_date)).setText(detail.realease_date);
        return convertView;
    }
}
