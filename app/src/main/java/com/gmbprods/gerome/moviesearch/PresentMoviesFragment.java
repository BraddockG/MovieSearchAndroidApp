package com.gmbprods.gerome.moviesearch;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Gerome on 09/02/2017.
 */

public class PresentMoviesFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "PresentMoviesFragment";

    private RecyclerView mMovieRecyclerView;
    private List<Movie> mItems = new ArrayList<>();
    private MovieAdapter mAdapter;


    public static PresentMoviesFragment newInstance() {
        return new PresentMoviesFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //setupHardcodeList();
        setRetainInstance(false);


        Log.i(TAG, "Background thread started");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_presenter, container, false);
        mMovieRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_movies_recycler_view);
        mMovieRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        MovieItemStorage movieItemStorage = MovieItemStorage.get(getActivity());
        List<Movie> movies = movieItemStorage.getMovies();

        if(mAdapter == null){
            mAdapter = new MovieAdapter(movies);
            mMovieRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setMovies(movies);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_movie_presenter, menu);

        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s ) {
                Log.d(TAG, "QueryTextSubmit: " + s);
                QueryPreferences.setStoredQuery(getActivity(), s.trim());

                searchView.clearFocus();
                searchItem.collapseActionView();

                updateItems();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(TAG, "QueryTextChange: " + s);
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = QueryPreferences.getStoredQuery(getActivity());
                searchView.setQuery(query, false);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*
            case R.id.menu_item_clear:
                QueryPreferences.setStoredQuery(getActivity(), null);
                updateItems();
                return true;
                */
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateItems() {
        String query = QueryPreferences.getStoredQuery(getActivity());
        new FetchMoviesTask(query).execute();
    }

    private void setupAdapter() {
        if(isAdded()) {
            mMovieRecyclerView.setAdapter(new MovieAdapter(MovieItemStorage.get(getActivity()).getMovies()));
        }
    }

    private void setupHardcodeList() {
        Log.i(TAG, "Added new hardcoded movies");
        new FetchMoviesTask("jumper")      .execute();
        new FetchMoviesTask("looper")      .execute();
        new FetchMoviesTask("watchmen")    .execute();
        new FetchMoviesTask("pulp fiction").execute();
    }

    private class MovieHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener {
        private Movie mMovieItem;
        private ImageView mImageView;
        private TextView mTitleTextView;
        private TextView mPlotTextView;

        public MovieHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mImageView = (ImageView) itemView.findViewById(R.id.presenter_item_movie_image_view);
            mTitleTextView = (TextView) itemView.findViewById(R.id.presenter_item_movie_title_view);
            mPlotTextView = (TextView) itemView.findViewById(R.id.presenter_item_movie_plot_view);
        }

        public void bindMovieItem(Movie item) {
            mMovieItem = item;

            mImageView.setImageBitmap(mMovieItem.getPosterBitmap());
            mTitleTextView.setText(mMovieItem.getTitle());
            mPlotTextView.setText(mMovieItem.getPlot());
        }

        @Override
        public void onClick(View v) {
            Intent intent = MovieActivity.newIntent(getActivity(), mMovieItem.getID());
            startActivity(intent);
        }
    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {
        private List<Movie> mMovieItems;

        public MovieAdapter(List<Movie> movieItems) {
            mMovieItems = movieItems;
        }

        @Override
        public MovieHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.presenter_item_movie, viewGroup, false);
            return new MovieHolder(view);
        }

        @Override
        public void onBindViewHolder(MovieHolder movieHolder, int position) {
            Movie movieItem = mMovieItems.get(position);
            movieHolder.bindMovieItem(movieItem);

        }

        @Override
        public int getItemCount() {
            return mMovieItems.size();
        }

        public void setMovies(List<Movie> movies) { mMovieItems = movies; }

    }


    private class FetchMoviesTask extends AsyncTask<Void, Void, Movie> {
        private String mQuery;

        public FetchMoviesTask(String query){
            mQuery = query;
        }

        @Override
        protected Movie doInBackground(Void... params) {

               return new FetchMovies().fetchMovie(mQuery);

        }
        @Override
        protected void onPostExecute(Movie movieItem) {
            try {
                MovieItemStorage.get(getActivity()).addMovie(movieItem);
                Intent intent = MovieActivity.newIntent(getActivity(), movieItem.getID());
                startActivity(intent);
                setupAdapter();
                Log.i(TAG, "Added the movie: " + movieItem);
            } catch (NullPointerException npe)   {
                Toast.makeText(getActivity(), "Failed to load", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
