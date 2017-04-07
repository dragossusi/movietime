package com.rachierudragos.movietime.wrapper.tvshow;

import java.util.List;

/**
 * Created by Dragos on 17.01.2017.
 */
public class TmdbSearchTvShow {
    public int page;
    public int total_pages;
    public int total_results;
    public List<TvShowDetail> results;
    public class TvShowDetail {
        public String poster_path;
        public double popularity;
        public long id;
        public float vote_average;
        public String overview;
        public String first_air_date;
        public List<String> origin_country;
        public List<Integer> genre_ids;
        public String original_language;
        public int vote_count;
        public String name;
        public String original_name;
    }
}
