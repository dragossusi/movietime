package com.rachierudragos.movietime.wrapper.tvshow;

import com.rachierudragos.movietime.wrapper.movie.TmdbMovie;

import java.util.List;

/**
 * Created by Dragos on 17.01.2017.
 */
public class TmdbTvShow {
    public List<TmdbMovie.Company> created_by;
    public List<Integer> episode_run_time;
    public String first_air_date;
    public List<TmdbMovie.Genre> genres;
    public String homepage;
    public long id;
    public boolean in_production;
    public String last_air_time;
    public String name;
    public List<TmdbMovie.Company> networks;
    public int number_of_episodes;
    public int number_of_seasons;
    public List<String> origin_country;
    public String original_name;
    public String original_language;
    public String overview;
    public String poster_path;
    public double popularity;
    public List<TmdbMovie.Company> production_companies;
    public List<Season> seasons;
    public String status;
    public String type;
    public float vote_average;
    public int vote_count;


    public List<TmdbMovie.Language> spoken_languages;

    public class Season {
        public String air_date;
        public int episode_count;
        public long id;
        public String poster_path;
        public int season_number;
    }
}
