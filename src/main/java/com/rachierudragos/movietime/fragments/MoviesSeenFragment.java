package com.rachierudragos.movietime.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.rachierudragos.movietime.MainActivity;
import com.rachierudragos.movietime.R;
import com.rachierudragos.movietime.SearchMovieActivity;
import com.rachierudragos.movietime.utils.DBHelper;
import com.rachierudragos.movietime.utils.ShortDescAdapter;
import com.rachierudragos.movietime.wrapper.TmdbAPI;
import com.rachierudragos.movietime.wrapper.TmdbShortDesc;
import com.rachierudragos.movietime.wrapper.movie.TmdbMovie;

import java.util.ArrayList;

/**
 * Created by Dragos on 16.01.2017.
 */

public class MoviesSeenFragment extends Fragment {
    protected ArrayList<TmdbShortDesc> movies;
    protected TmdbAPI api;
    protected ListView list_movies;
    private static final int REQUEST_SEARCH = 1;
    protected DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies_seen, container, false);
        dbHelper = new DBHelper(getActivity());

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchMovieActivity.class);
                startActivityForResult(intent, REQUEST_SEARCH);
            }
        });
        fab.show();

        api = new TmdbAPI();

        list_movies = (ListView) rootView.findViewById(R.id.list_seen_movie);

        movies = dbHelper.getSeenMovies();
        list_movies.setAdapter(new ShortDescAdapter(getActivity(), movies));
        list_movies.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final TmdbShortDesc desc = (TmdbShortDesc)adapterView.getItemAtPosition(i);
                builder.setTitle("Vrei sa stergi filmul "+desc.title+"?")
                        .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new GetMovieDuration().execute(desc.id,null,null);
                            }
                        });
                builder.show();
                return false;
            }
        });
        return rootView;
    }
    //Back to Main
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SEARCH) {
            if (resultCode == Activity.RESULT_OK)
                notifyModification(data.getIntExtra("minutes_added", 0));
        }
    }

    private void notifyModification(int mins) {
        movies = dbHelper.getSeenMovies();
        ((MainActivity) getActivity()).updateMinutes(mins);
        ((ArrayAdapter) list_movies.getAdapter()).clear();
        ((ArrayAdapter) list_movies.getAdapter()).addAll(movies);
    }

    //AsyncTask
    protected class GetMovieDuration extends AsyncTask<Long, Void, Integer> {
        private TmdbMovie movie;
        @Override
        protected Integer doInBackground(Long... id) {
            try {
                movie = api.getMovie(id[0]);
                return movie.runtime;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer != -1) {
                dbHelper.deleteSeenMovie(movie.id);
                notifyModification(-integer);
            } else
                Toast.makeText(getActivity(), "cica nu merge", Toast.LENGTH_LONG).show();
        }
    }
}
