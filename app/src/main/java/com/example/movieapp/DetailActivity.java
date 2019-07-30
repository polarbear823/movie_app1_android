package com.example.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movieapp.model.MovieInfo;
import com.example.movieapp.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private TextView mTitleTextView;
    private ImageView mPosterThumbnail;
    private TextView mReleaseDateTextView;
    private TextView mRatingTextView;
    private TextView mOverviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mTitleTextView = findViewById(R.id.tv_title);
        mPosterThumbnail = findViewById(R.id.iv_poster_thumbnail);
        mReleaseDateTextView = findViewById(R.id.tv_release_date);
        mRatingTextView = findViewById(R.id.tv_rating);
        mOverviewTextView = findViewById(R.id.tv_overview);
        Intent intent = getIntent();
        if (intent.hasExtra(MainActivity.MOVIE)) {
            MovieInfo movieInfo = (MovieInfo) intent.getSerializableExtra(MainActivity.MOVIE);
            populateMovieInfo(movieInfo);
        }
    }

    private void populateMovieInfo(MovieInfo movieInfo) {
        mTitleTextView.setText(movieInfo.getOriginalTitle());
        mReleaseDateTextView.setText(movieInfo.getReleaseDate());
        mRatingTextView.setText(String.valueOf(movieInfo.getVoteAverage()));
        mOverviewTextView.setText(movieInfo.getOverview());
        String posterUrl = NetworkUtils.buildImageUrlString(movieInfo.getPosterPath());
        Picasso.get().load(posterUrl).into(mPosterThumbnail);
    }
}
