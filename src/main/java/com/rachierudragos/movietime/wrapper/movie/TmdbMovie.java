package com.rachierudragos.movietime.wrapper.movie;

import java.util.List;

public class TmdbMovie {
	public boolean adult;
	public String homepage;
	public String poster_path;
	public String backdrop_path;
	public long id;
	public int runtime;
	public String original_title;
	public String title;
	public String overview;
	public String status;
	public String release_date;
	public double popularity;
	public float vote_average;
	public int vote_count;
	
	public List<Genre> genres;
	public List<Company> production_companies;
	public List<Country> production_countries;
	
	public List<Language> spoken_languages;
	public String original_language;
	
	public Colection belongs_to_colection;
	
	
	public class Colection {
		public long id;
		public String name;
		public String poster_path;
	}
	public class Genre {
		public long id;
		public String name;
	}
	public class Company {
		public String name;
		public long id;
	}
	public class Country {
		public String iso_3166_1;
	    public String name;
	}
	public class Language {
		public String iso_639_1;
		public String name;
	}
}
