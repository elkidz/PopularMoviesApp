package com.example.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIE_DATABASE_URL =
            "https://api.themoviedb.org";

    private static final String API_KEY_PARAM = "api_key";
    private static final String API_KEY = "ab985f7eb8de45b5e0d7d35e611f2a8e";

    private static final String VERSION = "3";

    private static final String MOVIE_PATH = "movie";
    private static final String POPULAR_PATH = "popular";
    private static final String TOP_RATED_PATH = "top_rated";


    /**
     * Builds the URL used to return the list of movies
     *
     * @return The URL to use to query the The Movie DB.
     */
    public static URL buildUrl() {
        Uri builtUri = Uri.parse(MOVIE_DATABASE_URL).buildUpon()
                .appendPath(VERSION)
                .appendPath(MOVIE_PATH)
                .appendPath(POPULAR_PATH)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    // TODO buildImageUrl

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            int status = urlConnection.getResponseCode();
            Log.v(TAG, "Status code: " + status);

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
}