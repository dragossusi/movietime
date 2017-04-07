package com.rachierudragos.movietime.wrapper.movie;

import java.util.List;

public class TmdbSearchMovies {
	public int page;
	public int total_pages;
	public int total_results;
	public List<MovieDetail> results;
	public class MovieDetail {
		public String poster_path;
	    public boolean adult;
	    public String overview;
	    public String release_date;
	    public List<Integer> genre_ids;
	    public long id;
	    public String original_title;
	    public String original_language;
	    public String title;
	    public String backdrop_path;
	    public double popularity;
	    public int vote_count;
	    public float vote_average;
	}
}
