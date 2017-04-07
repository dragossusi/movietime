package com.rachierudragos.movietime;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rachierudragos.movietime.fragments.AchievmentsFragment;
import com.rachierudragos.movietime.fragments.MoviesSeenFragment;
import com.rachierudragos.movietime.fragments.TvShowsSeenFragment;
import com.rachierudragos.movietime.utils.Ranks;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_SEARCH = 1;

    private SharedPreferences prefMovieTime;
    private SharedPreferences.Editor editorMoviesSeen;


    private static FragmentManager fm;
    private TextView txt_personaname;
    private TextView txt_rank;
    private TextView txt_minutes;

    public int minutes = 0;
    private String personaname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        txt_personaname = (TextView) header.findViewById(R.id.personaname);
        txt_rank = (TextView) header.findViewById(R.id.rank);
        txt_minutes = (TextView) header.findViewById(R.id.minutes);

        prefMovieTime = getSharedPreferences("movies_seen", Context.MODE_PRIVATE);
        editorMoviesSeen = prefMovieTime.edit();

        minutes = prefMovieTime.getInt("minutes_spent", 0);
        txt_minutes.setText(timeTransform(minutes));
        txt_rank.setText(Ranks.getRank(minutes));

        personaname = prefMovieTime.getString("personaname", "Titi Sportivu");
        txt_personaname.setText(personaname);

        fm = getFragmentManager();
    }
    private String timeTransform(int minutes) {

        int hh = minutes / 60; //since both are ints, you get an int
        int mm = minutes % 60;
        String text = "";
        if (hh > 0) {
            text += hh + ":";
            text += (mm < 10 ? "0" : "") + mm;
        } else
            text += mm;
        return text;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            saveTimeSpent();
            super.onBackPressed();
        }
    }

    private void saveTimeSpent() {
        editorMoviesSeen.putInt("minutes_spent", minutes);
        editorMoviesSeen.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            dialogNume();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void dialogNume() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText = new EditText(this);
        builder.setView(editText)
                .setTitle("Cum vrei sa te cheme?")
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!editText.getText().toString().equals("")) {
                            personaname = editText.getText().toString();
                            editorMoviesSeen.putString("personaname", personaname).commit();
                            txt_personaname.setText(personaname);
                        } else {
                            Toast.makeText(MainActivity.this, "Zi cum te cheama", Toast.LENGTH_SHORT).show();
                            dialogNume();
                        }
                    }
                });
        builder.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_seen_movies) {
            // Handle the camera action
            fm.beginTransaction().replace(R.id.content_main, new MoviesSeenFragment()).commit();
        } else if (id == R.id.nav_seen_tv_shows) {
            fm.beginTransaction().replace(R.id.content_main, new TvShowsSeenFragment()).commit();
        } else if (id == R.id.nav_slideshow) {
        } else if (id == R.id.nav_achievements) {
            fm.beginTransaction().replace(R.id.content_main, new AchievmentsFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void updateMinutes(int mins) {
        minutes += mins;
        txt_minutes.setText(timeTransform(minutes));
        txt_rank.setText(Ranks.getRank(minutes));
        saveTimeSpent();
    }

    public int getMinutes() {
        return minutes;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SEARCH) {
            if (resultCode == Activity.RESULT_OK) {

            }
        }
    }
}
