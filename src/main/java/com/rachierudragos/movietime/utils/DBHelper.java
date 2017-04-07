package com.rachierudragos.movietime.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rachierudragos.movietime.wrapper.TmdbShortDesc;

import java.util.ArrayList;

/**
 * Created by Dragos on 19.01.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movietime.db";
    private static final String MOVIES_TABLE = "table_movies";
    private static final String TV_SHOWS_TABLE = "table_tv_shows";

    public static final String COL_1 = "_id";
    public static final String COL_2 = "title";
    public static final String COL_3 = "overview";
    public static final String COL_4 = "vote_average";
    public static final String COL_5 = "vote_number";
    public static final String COL_6 = "release_date";
    public static final String COL_7 = "poster_path";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    //Database Creation
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_Stringm = "CREATE TABLE " + MOVIES_TABLE + "("
                + COL_1 + " INTEGER PRIMARY KEY, "
                + COL_2 + " TEXT, "
                + COL_3 + " TEXT, "
                + COL_4 + " REAL, "
                + COL_5 + " INTEGER, "
                + COL_6 + " TEXT, "
                + COL_7 + " TEXT)";
        String SQL_Stringt = "CREATE TABLE " + TV_SHOWS_TABLE + "("
                + COL_1 + " INTEGER PRIMARY KEY, "
                + COL_2 + " TEXT, "
                + COL_3 + " TEXT, "
                + COL_4 + " REAL, "
                + COL_5 + " INTEGER, "
                + COL_6 + " TEXT, "
                + COL_7 + " TEXT)";
        db.execSQL(SQL_Stringm);
        db.execSQL(SQL_Stringt);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + MOVIES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TV_SHOWS_TABLE);
        onCreate(db);
    }

    //////specific actions

    //Movies
    public ArrayList<TmdbShortDesc> getSeenMovies() {
        ArrayList<TmdbShortDesc> movies = new ArrayList<>();
        Cursor c = getSeen(MOVIES_TABLE);
        while (!c.isAfterLast()) {
            movies.add(new TmdbShortDesc(c.getLong(c.getColumnIndex(COL_1)),
                    c.getString(c.getColumnIndex(COL_2)),
                    c.getString(c.getColumnIndex(COL_3)),
                    c.getFloat(c.getColumnIndex(COL_4)),
                    c.getInt(c.getColumnIndex(COL_5)),
                    c.getString(c.getColumnIndex(COL_6)),
                    c.getString(c.getColumnIndex(COL_7))));
            c.moveToNext();
        }
        return movies;
    }
    public ArrayList<Long> getMoviesIds() {
        ArrayList<Long> movies_id = new ArrayList<>();
        Cursor cursor = getIds(MOVIES_TABLE);
        while (!cursor.isAfterLast()) {
            movies_id.add(cursor.getLong(0));
            cursor.moveToNext();
        }
        return movies_id;
    }

    public void deleteSeenMovie(long id) {
        deleteSeen(MOVIES_TABLE, id);
    }

    public boolean insertSeenMovie(long id, String title, String overview, float voteAvg, int voteNumber, String date, String poster_path) {
        return insertSeen(MOVIES_TABLE, id, title, overview, voteAvg, voteNumber, date, poster_path);
    }

    //TvShows
    public ArrayList<TmdbShortDesc> getSeenTvShows() {
        ArrayList<TmdbShortDesc> tvShows = new ArrayList<>();
        Cursor c = getSeen(TV_SHOWS_TABLE);
        while (!c.isAfterLast()) {
            tvShows.add(new TmdbShortDesc(c.getLong(c.getColumnIndex(COL_1)),
                    c.getString(c.getColumnIndex(COL_2)),
                    c.getString(c.getColumnIndex(COL_3)),
                    c.getFloat(c.getColumnIndex(COL_4)),
                    c.getInt(c.getColumnIndex(COL_5)),
                    c.getString(c.getColumnIndex(COL_6)),
                    c.getString(c.getColumnIndex(COL_7))));
            c.moveToNext();
        }
        return tvShows;
    }

    public ArrayList<Long> getTvShowsIds() {
        ArrayList<Long> movies_id = new ArrayList<>();
        Cursor cursor = getIds(TV_SHOWS_TABLE);
        while (!cursor.isAfterLast()) {
            movies_id.add(cursor.getLong(0));
            cursor.moveToNext();
        }
        return movies_id;
    }

    public boolean insertSeenTvShow(long id, String title, String overview, float voteAvg, int voteNumber, String date, String poster_path) {
        return insertSeen(TV_SHOWS_TABLE, id, title, overview, voteAvg, voteNumber, date, poster_path);
    }


    public void deleteSeenTvShow(long id) {
        deleteSeen(TV_SHOWS_TABLE, id);
    }

    //////non specific actions
    //DONE
    private boolean insertSeen(String table, long id, String title, String overview, float voteAvg, int voteNumber, String date, String poster_path) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_1, id);
        values.put(COL_2, title);
        values.put(COL_3, overview);
        values.put(COL_4, voteAvg);
        values.put(COL_5, voteNumber);
        values.put(COL_6, date);
        values.put(COL_7, poster_path);
        long result = db.insert(table, null, values);
        if (result == -1)
            return false;
        else
            return true;
    }
    //DONE
    private void deleteSeen(String table, long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + table + " WHERE " + COL_1 + "=" + id);
        db.close();
    }
    //DONE
    private Cursor getSeen(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCursor = db.query(table, new String[]{"*"},
                null, null, null, null, COL_2 + " ASC");

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    //DONE
    private Cursor getIds(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCursor = db.query(table,new String[]{COL_1},
                null, null, null, null, null);
        if(mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
}
