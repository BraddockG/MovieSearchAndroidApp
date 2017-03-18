package com.gmbprods.gerome.moviesearch;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Gerome on 11/02/2017.
 */

public class MovieItemStorage {
    private static MovieItemStorage sMovieItemStorage;
    private List<Movie> mMovies;

    public static MovieItemStorage get(Context context) {
        if(sMovieItemStorage == null) {
            sMovieItemStorage = new MovieItemStorage(context);
        }
        return sMovieItemStorage;
    }

    private MovieItemStorage(Context context){
        mMovies = new ArrayList<>();
    }

    public void addMovie(Movie m) {
        for(Movie movie: mMovies) {
            if(movie.getTitle().equals(m.getTitle())) {
                return;
            }
        }
        mMovies.add(0, m);
    }

    public List<Movie> getMovies() {
        return mMovies;
    }

    public Movie getMovie(String id) {
        for(Movie movie: mMovies) {
            if(movie.getID().equals(id)) {
                return movie;
            }
        }
        return null;
    }
}
