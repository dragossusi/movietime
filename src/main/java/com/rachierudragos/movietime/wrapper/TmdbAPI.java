package com.rachierudragos.movietime.wrapper;

import com.google.gson.Gson;
import com.rachierudragos.movietime.wrapper.movie.TmdbMovie;
import com.rachierudragos.movietime.wrapper.movie.TmdbSearchMovies;
import com.rachierudragos.movietime.wrapper.tvshow.TmdbSearchTvShow;
import com.rachierudragos.movietime.wrapper.tvshow.TmdbTvShow;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class TmdbAPI {
	private static final String API_HOST = "https://api.themoviedb.org/3/";
	private static final String API_KEY = "8b31c5d8d91a62874e11f040f10c97e5";
	Gson gson;
	
	public TmdbAPI() {
		gson = new Gson();
	}
	
	public TmdbSearchMovies searchMovie(String name) throws Exception {
		return gson.fromJson(readUrl(API_HOST + "search/movie?api_key=" + API_KEY + "&query=" + name), TmdbSearchMovies.class);
	}
	public TmdbSearchTvShow searchTvShow(String name) throws Exception {
		return gson.fromJson(readUrl(API_HOST + "search/tv?api_key=" + API_KEY + "&query=" + name), TmdbSearchTvShow.class);
	}
	public TmdbMovie getMovie(long id) throws Exception {
		return gson.fromJson(readUrl(API_HOST + "movie/" + id + "?api_key=" + API_KEY), TmdbMovie.class);
	}
	public TmdbTvShow getTvShow(long id) throws Exception {
		return gson.fromJson(readUrl(API_HOST + "tv/" + id + "?api_key=" + API_KEY),TmdbTvShow.class);
	}
	///util
	private String readUrl(String urlString) throws Exception {
		System.out.println("Requesting " + urlString);
		BufferedReader reader = null;
		try {
			URL url = new URL(urlString.replace(" ", "%20"));
		    reader = new BufferedReader(new InputStreamReader(url.openStream()));
		    StringBuffer buffer = new StringBuffer();
		    int read;
		    char[] chars = new char[1024];
		    while ((read = reader.read(chars)) != -1)
		    	buffer.append(chars, 0, read); 

		    return buffer.toString();
		} finally {
		    if (reader != null)
		    reader.close();
		}
	}
}
