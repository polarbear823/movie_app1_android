package com.example.movieapp.utils;

import com.example.movieapp.model.MovieInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParseUtils {

    private static final String ORIGINAL_TITLE = "original_title";
    private static final String VOTE_AVERAGE= "vote_average";
    private static final String POSTER_PATH = "poster_path";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String RESULTS = "results";

    public static List<MovieInfo> parseJsonStringToMovieList(String movieListJsonString)
    throws JSONException {
        JSONObject movieInfoJson = new JSONObject(movieListJsonString);
        JSONArray movieResults = movieInfoJson.optJSONArray(RESULTS);
        List<MovieInfo> movieList  = new ArrayList<>();
        if (movieResults != null && movieResults.length() != 0) {
            for (int i = 0; i < movieResults.length(); i++) {
                JSONObject movieJson = movieResults.optJSONObject(i);
                if (movieJson != null) {
                    MovieInfo movieInfo = new MovieInfo(
                            movieJson.optString(ORIGINAL_TITLE),
                            movieJson.optString(POSTER_PATH),
                            movieJson.optString(OVERVIEW),
                            movieJson.optDouble(VOTE_AVERAGE),
                            movieJson.optString(RELEASE_DATE));
                    movieList.add(movieInfo);
                }
            }
        }
        return movieList;
    }
}
