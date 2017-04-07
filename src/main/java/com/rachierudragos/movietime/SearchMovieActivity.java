package com.rachierudragos.movietime;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.rachierudragos.movietime.utils.DBHelper;
import com.rachierudragos.movietime.utils.MovieSearchAdapter;
import com.rachierudragos.movietime.wrapper.TmdbAPI;
import com.rachierudragos.movietime.wrapper.movie.TmdbMovie;
import com.rachierudragos.movietime.wrapper.movie.TmdbSearchMovies;

import java.util.ArrayList;

public class SearchMovieActivity extends AppCompatActivity {
    private TmdbAPI api;
    private String name;
    protected int minutesAdded = 0;
    protected TmdbSearchMovies search;
    protected Button searchButton;
    protected long movieId;
    protected ArrayList<Long> movies_ids;
    protected DBHelper dbHelper;
    protected ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) SearchMovieActivity.this.findViewById(R.id.list_search);

        api = new TmdbAPI();
        //TODO movies_ids from database
        dbHelper = new DBHelper(this);
        movies_ids = dbHelper.getMoviesIds();
        //
        searchButton = (Button)findViewById(R.id.button_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = ((EditText)findViewById(R.id.edit_nume)).getText().toString();
                if(name.equals(""))
                    Toast.makeText(SearchMovieActivity.this, "Scrie ceva inainte bo$$", Toast.LENGTH_SHORT).show();
                else {
                    searchButton.setEnabled(false);
                    new SearchMovie().execute(null, null, null);
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    //DONE
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("minutes_added",minutesAdded);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //DONE
    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("minutes_added",minutesAdded);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    protected class SearchMovie extends AsyncTask<Void,Void,Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                search = api.searchMovie(name);
                return true;
            } catch(Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean res) {
            searchButton.setEnabled(true);
            if(res) {
                listView.setAdapter(new MovieSearchAdapter(SearchMovieActivity.this, search.results, movies_ids));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        View alertSeen = view.findViewById(R.id.alert_seen);
                        movieId = ((TmdbSearchMovies.MovieDetail)adapterView.getItemAtPosition(position)).id;
                        if(alertSeen.getVisibility()==View.GONE)
                            new GetMovieDuration().execute(false,null,null);
                        else
                            new GetMovieDuration().execute(true,null,null);
                    }
                });
            } else
                Toast.makeText(SearchMovieActivity.this,"Probabil ca nu ai net, RiP.", Toast.LENGTH_SHORT).show();
        }
    }
    protected class GetMovieDuration extends AsyncTask<Boolean, Void, Integer> {
        private boolean adaugat;
        private TmdbMovie movie;
        @Override
        protected Integer doInBackground(Boolean... alert) {
            try {
                movie = api.getMovie(movieId);
                adaugat = alert[0];
                return movie.runtime;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer != -1) {
                if(adaugat==false) {
                    minutesAdded += integer;
                    movies_ids.add(movieId);
                    dbHelper.insertSeenMovie(movie.id,movie.title,movie.overview,movie.vote_average,movie.vote_count,movie.release_date,movie.poster_path);
                } else {
                    minutesAdded -= integer;
                    movies_ids.remove(movieId);
                    dbHelper.deleteSeenMovie(movieId);
                }
                ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
                System.out.println(minutesAdded);
            } else
                Toast.makeText(SearchMovieActivity.this, "cica nu merge", Toast.LENGTH_LONG).show();
        }
    }
}
