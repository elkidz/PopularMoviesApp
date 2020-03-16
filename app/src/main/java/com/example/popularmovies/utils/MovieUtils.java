package com.example.popularmovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class MovieUtils {

    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the details about a specific movie.
     *
     * @param movieJsonStr JSON response from server
     * @return Array of Strings describing weather data
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static String[] getListFromJson(String movieJsonStr)
            throws JSONException {

        final String RESULT_LIST = "results";
        final String MOVIE_ID = "id";
        final String MOVIE_TITLE = "title";
        final String MOVIE_RATING = "vote_average";
        final String MOVIE_POSTER = "poster_path";

        final String STATUS_CODE = "status_code";
        final String STATUS_MESSAGE = "status_message";

        String[] parsedData = null;

        JSONObject movieJson = new JSONObject(movieJsonStr);

        /* Is there an error? */
        if (movieJson.has(STATUS_CODE) && movieJson.has(STATUS_MESSAGE)) {
            String message = movieJson.getString(STATUS_MESSAGE);

            throw new JSONException(message);
        }

        JSONArray movieArray = movieJson.getJSONArray(RESULT_LIST);

        parsedData = new String[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {

            int id;
            String title;
            double rating;
            String poster;

            /* Get the JSON object representing the day */
            JSONObject movie = movieArray.getJSONObject(i);

            id = movie.getInt(MOVIE_ID);
            title = movie.getString(MOVIE_TITLE);
            rating = movie.getDouble(MOVIE_RATING);
            poster = movie.getString(MOVIE_POSTER);

            parsedData[i] = id + " - " + title + " - " + rating + " - " + poster;
            parsedData[i] = NetworkUtils.buildImageUrl(poster.replace("/", "")).toString();
        }

        return parsedData;
    }
}