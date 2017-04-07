package com.rachierudragos.movietime.wrapper;

/**
 * Created by Dragos on 21.01.2017.
 */

public class TmdbShortDesc {
    public long id;
    public String title;
    public String overview;
    public float vote_average;
    public int vote_count;
    public String realease_date;
    public String poster_path;

    public TmdbShortDesc(long id, String title, String overview, float vote_average, int vote_number, String date, String poster_path) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.vote_average = vote_average;
        this.vote_count = vote_number;
        this.realease_date = date;
        this.poster_path = poster_path;
    }
}
