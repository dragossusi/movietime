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
import com.rachierudragos.movietime.utils.TvShowSearchAdapter;
import com.rachierudragos.movietime.wrapper.TmdbAPI;
import com.rachierudragos.movietime.wrapper.tvshow.TmdbSearchTvShow;
import com.rachierudragos.movietime.wrapper.tvshow.TmdbTvShow;

import java.util.ArrayList;

public class SearchTvShowActivity extends AppCompatActivity {
    private TmdbAPI api;
    private String name;
    protected int minutesAdded = 0;
    protected TmdbSearchTvShow search;
    protected Button searchButton;
    protected long tvshowId;
    protected ArrayList<Long> tvshow_ids;
    protected DBHelper dbHelper;
    protected ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView)findViewById(R.id.list_search);

        api = new TmdbAPI();

        dbHelper = new DBHelper(this);
        tvshow_ids = dbHelper.getTvShowsIds();

        searchButton = (Button)findViewById(R.id.button_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = ((EditText)findViewById(R.id.edit_nume)).getText().toString();
                if(name.equals(""))
                    Toast.makeText(SearchTvShowActivity.this, "Scrie ceva inainte bo$$", Toast.LENGTH_SHORT).show();
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
                search = api.searchTvShow(name);
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
                listView.setAdapter(new TvShowSearchAdapter(SearchTvShowActivity.this, search.results, tvshow_ids));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        View alertSeen = view.findViewById(R.id.alert_seen);
                        tvshowId = ((TmdbSearchTvShow.TvShowDetail)adapterView.getItemAtPosition(position)).id;
                        if(alertSeen.getVisibility()==View.GONE)
                            new GetTvShowDuration().execute(false,null,null);
                        else
                            new GetTvShowDuration().execute(true,null,null);
                    }
                });
            } else
                Toast.makeText(SearchTvShowActivity.this,"Probabil ca nu ai net, RiP.", Toast.LENGTH_SHORT).show();
        }
    }

    protected class GetTvShowDuration extends AsyncTask<Boolean, Void, Integer> {
        private boolean adaugat;
        private TmdbTvShow tvshow;
        @Override
        protected Integer doInBackground(Boolean... alert) {
            try {
                tvshow = api.getTvShow(tvshowId);
                adaugat = alert[0];
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
                if(adaugat==false) {
                    minutesAdded += integer*episodes;
                    tvshow_ids.add(tvshowId);
                    dbHelper.insertSeenTvShow(tvshow.id,tvshow.name,tvshow.overview,tvshow.vote_average,tvshow.vote_count,tvshow.first_air_date,tvshow.poster_path);
                } else {
                    minutesAdded -= integer*episodes;
                    tvshow_ids.remove(tvshowId);
                    dbHelper.deleteSeenTvShow(tvshowId);
                }
                ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
                System.out.println(minutesAdded);
            } else
                Toast.makeText(SearchTvShowActivity.this, "cica nu merge", Toast.LENGTH_LONG).show();
        }
    }
}
