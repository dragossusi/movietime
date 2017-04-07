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
import com.rachierudragos.movietime.SearchTvShowActivity;
import com.rachierudragos.movietime.utils.DBHelper;
import com.rachierudragos.movietime.utils.ShortDescAdapter;
import com.rachierudragos.movietime.wrapper.TmdbAPI;
import com.rachierudragos.movietime.wrapper.TmdbShortDesc;
import com.rachierudragos.movietime.wrapper.tvshow.TmdbTvShow;

import java.util.ArrayList;

/**
 * Created by Dragos on 17.01.2017.
 */

public class TvShowsSeenFragment extends Fragment {
    protected ArrayList<TmdbShortDesc> tvshows;
    protected TmdbAPI api;
    protected ListView list_tvshows;
    private static final int REQUEST_SEARCH = 1;
    protected DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies_seen, container, false);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchTvShowActivity.class);
                startActivityForResult(intent, REQUEST_SEARCH);
            }
        });
        fab.show();

        api = new TmdbAPI();

        list_tvshows = (ListView) rootView.findViewById(R.id.list_seen_movie);

        dbHelper = new DBHelper(getActivity());
        tvshows = dbHelper.getSeenTvShows();

        list_tvshows.setAdapter(new ShortDescAdapter(getActivity(), tvshows));
        list_tvshows.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final TmdbShortDesc desc = (TmdbShortDesc) adapterView.getItemAtPosition(i);
                builder.setTitle("Vrei sa stergi serialul " + desc.title + "?")
                        .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new GetTvShowDuration().execute(desc.id, null, null);
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
    //DONE
    private void notifyModification(int mins) {
        tvshows = dbHelper.getSeenMovies();
        ((MainActivity) getActivity()).updateMinutes(mins);
        ((ArrayAdapter) list_tvshows.getAdapter()).clear();
        ((ArrayAdapter) list_tvshows.getAdapter()).addAll(tvshows);
    }

    //DONE
    protected class GetTvShowDuration extends AsyncTask<Long, Void, Integer> {
        private TmdbTvShow tvshow;

        @Override
        protected Integer doInBackground(Long... id) {
            try {
                tvshow = api.getTvShow(id[0]);
                return tvshow.episode_run_time.get(0);
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer != -1) {
                int episodes = 0;
                for (TmdbTvShow.Season i : tvshow.seasons) {
                    episodes += i.episode_count;
                }
                dbHelper.deleteSeenMovie(tvshow.id);
                notifyModification(-integer * episodes);
            } else
                Toast.makeText(getActivity(), "cica nu merge", Toast.LENGTH_LONG).show();
        }
    }
}
