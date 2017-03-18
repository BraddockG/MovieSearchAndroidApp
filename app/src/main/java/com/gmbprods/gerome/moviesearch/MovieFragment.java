package com.gmbprods.gerome.moviesearch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.UUID;

/**
 * Created by Gerome on 10/02/2017.
 */

public class MovieFragment extends Fragment {
    private Movie mMovie;
    private ImageView mMoviePoster;

    public static MovieFragment newInstance() {
        return new MovieFragment();
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String movieId = (String) getActivity().getIntent().getSerializableExtra(MovieActivity.EXTRA_MOVIE_ID);

        mMovie = MovieItemStorage.get(getActivity()).getMovie(movieId);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_item, container, false);


        mMoviePoster = (ImageView) v.findViewById(R.id.fragment_movie_item_image_view);
        mMoviePoster.setImageBitmap(mMovie.getPosterBitmap());

        return v;
    }



}
