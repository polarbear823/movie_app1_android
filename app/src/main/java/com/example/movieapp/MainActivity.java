package com.example.movieapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.model.MovieInfo;
import com.example.movieapp.utils.JsonParseUtils;
import com.example.movieapp.utils.NetworkUtils;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAppAdapter.MovieAdapterOnClickHandler, AdapterView.OnItemSelectedListener {

    private static final int COLUMN_NUMBER = 2;
    public static final String MOVIE = "movie";
    private static final int DEFAULT_SORT_BY_MOST_POPULAR = 0;
    private MovieAppAdapter mAdapter;
    private RecyclerView mMoviesList;
    private TextView mErrorMessageTextView;
    private ProgressBar mProgressBar;
    private int selectedSortType = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMoviesList = findViewById(R.id.recyclerview_movie);
        mErrorMessageTextView = findViewById(R.id.tv_error_message_display);
        mProgressBar = findViewById(R.id.pb_loading_indicator);
        GridLayoutManager layoutManager = new GridLayoutManager(this, COLUMN_NUMBER);
        mMoviesList.setLayoutManager(layoutManager);
        mMoviesList.setHasFixedSize(true);
        mAdapter = new MovieAppAdapter(this);
        mMoviesList.setAdapter(mAdapter);
        loadMovieData(selectedSortType == -1 ? DEFAULT_SORT_BY_MOST_POPULAR : selectedSortType);
    }

    @Override
    public void onClickItem(MovieInfo item) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(MOVIE, item);
        startActivity(intent);
    }

    private int getTitleBasedOnSortType(int selectedSortType) {
        if (selectedSortType == 1) {
            return R.string.highest_rated_movie;
        } else {
            return R.string.pop_movie;
        }
    }

    private void loadMovieData(int sortType) {
        showMovieDataView();
        setTitle(getTitleBasedOnSortType(selectedSortType));
        new FetchMoviesTask().execute(sortType);
    }

    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mMoviesList.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mMoviesList.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_spinner);
        Spinner spinner = (Spinner) item.getActionView();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sortby_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        return true;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedSortType = position;
        loadMovieData(selectedSortType);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

    private class FetchMoviesTask extends AsyncTask<Integer, Void, List<MovieInfo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<MovieInfo> doInBackground(Integer... params) {
            int sortType = params[0];
            URL moviesRequestUrl = NetworkUtils.buildUrl(sortType);
            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);
                return JsonParseUtils.parseJsonStringToMovieList(jsonMovieResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<MovieInfo> movieInfos) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (movieInfos != null && movieInfos.size() != 0) {
                showMovieDataView();
                mAdapter.setMovieData(movieInfos);
            } else {
                showErrorMessage();
            }
        }
    }
}
