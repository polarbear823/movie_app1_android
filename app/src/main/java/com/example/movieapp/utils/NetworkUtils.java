package com.example.movieapp.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String BASE_MOVIE_URL =
            "http://api.themoviedb.org/3/movie";
    private static final String SORT_BY_POPULAR = "popular";
    private static final String SORT_BY_RATING = "top_rated";
    private static final String API_KEY_PARAM = "api_key";
    //TODO add your api key value below.
    private static final String API_KEY = "placeholder";
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE_DEFAULT = "w185";

    public static URL buildUrl(int sortType) {
        String sortPath;
        switch (sortType) {
            case 0:
                sortPath = SORT_BY_POPULAR;
                break;
            case 1:
            default:
                sortPath = SORT_BY_RATING;
                break;
        }
        Uri builtUri = Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendPath(sortPath)
                .appendQueryParameter(API_KEY_PARAM, API_KEY).build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.d(TAG, e.toString());
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String buildImageUrlString(String path) {
        return IMAGE_BASE_URL + IMAGE_SIZE_DEFAULT + path;
    }

}
