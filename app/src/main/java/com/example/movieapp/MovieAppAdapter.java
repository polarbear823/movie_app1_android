package com.example.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.model.MovieInfo;
import com.example.movieapp.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAppAdapter extends RecyclerView.Adapter<MovieAppAdapter.MovieAdapterViewHolder> {

    private List<MovieInfo> movieData;

    private final MovieAdapterOnClickHandler mClickHandler;

    interface MovieAdapterOnClickHandler {
        void onClickItem(MovieInfo item);
    }

    public MovieAppAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView mMovieImageView;

        private MovieAdapterViewHolder(View view) {
            super(view);
            mMovieImageView = view.findViewById(R.id.iv_movie_picture);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            MovieInfo movieInfo = movieData.get(pos);
            mClickHandler.onClickItem(movieInfo);
        }
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recyclerview_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder holder, int position) {
        MovieInfo movieInfo = movieData.get(position);
        if (movieInfo.getPosterPath() != null) {
            Picasso.get().load(NetworkUtils.buildImageUrlString(movieInfo.getPosterPath()))
                    .into(holder.mMovieImageView);
        }
    }

    @Override
    public int getItemCount() {
        if (movieData == null) {
            return 0;
        }
        return movieData.size();
    }

    public void setMovieData(List<MovieInfo> movieData) {
        this.movieData = movieData;
        notifyDataSetChanged();
    }
}
