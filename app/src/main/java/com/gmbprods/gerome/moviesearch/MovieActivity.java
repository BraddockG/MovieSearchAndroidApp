package com.gmbprods.gerome.moviesearch;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by Gerome on 10/02/2017.
 */

public class MovieActivity extends SingleFragmentActivity {

    public static final String EXTRA_MOVIE_ID = "com.gmbprods.gerome.moviesearch.movie_id";

    @Override
    public Fragment createFragment() {
        return MovieFragment.newInstance();
    }

    public static Intent newIntent(Context packageContext, String movieId) {
        Intent intent = new Intent(packageContext, MovieActivity.class);
        intent.putExtra(EXTRA_MOVIE_ID, movieId);
        return intent;
    }

}
