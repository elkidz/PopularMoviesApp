package com.example.popularmovies.utils;

import com.example.popularmovies.data.database.Movie;
import com.example.popularmovies.data.Review;
import com.example.popularmovies.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class MovieJsonUtils {

    public static String[] getListFromJson(String jsonStr) throws JSONException {

        final String RESULT_LIST = "results";
        final String STATUS_CODE = "status_code";
        final String STATUS_MESSAGE = "status_message";

        String[] parsedData;

        JSONObject movieJson = new JSONObject(jsonStr);

        /* Is there an error? */
        if (movieJson.has(STATUS_CODE) && movieJson.has(STATUS_MESSAGE)) {
            String message = movieJson.getString(STATUS_MESSAGE);

            throw new JSONException(message);
        }

        JSONArray resultArray = movieJson.getJSONArray(RESULT_LIST);

        parsedData = new String[resultArray.length()];

        for (int i = 0; i < resultArray.length(); i++) {
            parsedData[i] = resultArray.getJSONObject(i).toString();
        }

        return parsedData;
    }

    public static List<Movie> getMoviesFromJson(String moviesJsonStr) throws JSONException {

        String[] resultArray = getListFromJson(moviesJsonStr);

        List<Movie> movies = new ArrayList<>();

        for (String s : resultArray) {
            movies.add(getMovieFromJson(s));
        }

        return movies;
    }

    public static List<Trailer> getTrailersFromJson(String trailersJsonStr) throws JSONException {

        String[] resultArray = getListFromJson(trailersJsonStr);

        List<Trailer> trailers = new ArrayList<>();

        for (String s : resultArray) {
            trailers.add(getTrailerFromJson(s));
        }

        return trailers;
    }

    public static List<Review> getReviewsFromJson(String reviewsJsonStr) throws JSONException {

        String[] resultArray = getListFromJson(reviewsJsonStr);

        List<Review> reviews = new ArrayList<>();

        for (String s : resultArray) {
            reviews.add(getReviewFromJson(s));
        }

        return reviews;
    }

    public static Movie getMovieFromJson(String movieJsonStr) throws JSONException {

        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_ID = "id";
        final String MOVIE_TITLE = "title";
        final String MOVIE_VOTE_AVERAGE = "vote_average";
        final String MOVIE_POSTER = "poster_path";
        final String MOVIE_OVERVIEW = "overview";

        JSONObject movieJson = new JSONObject(movieJsonStr);

        /* Is there an error? */
        if (!movieJson.has(MOVIE_TITLE) && !movieJson.has(MOVIE_POSTER)) {
            String message = "Invalid JSON Object.";

            throw new JSONException(message);
        }

        Movie movie = new Movie();
        movie.setId(movieJson.getInt(MOVIE_ID));
        movie.setTitle(movieJson.getString(MOVIE_TITLE));
        movie.setVoteAverage(movieJson.getDouble(MOVIE_VOTE_AVERAGE));
        movie.setPoster(NetworkUtils.buildImageUrl(movieJson.getString(MOVIE_POSTER).replace("/", "")).toString());
        movie.setReleaseDate(movieJson.getString(MOVIE_RELEASE_DATE));
        movie.setOverview(movieJson.getString(MOVIE_OVERVIEW));

        return movie;
    }

    public static Trailer getTrailerFromJson(String trailerJsonStr) throws JSONException {

        final String TRAILER_ID = "id";
        final String TRAILER_KEY = "key";
        final String TRAILER_NAME = "name";
        final String TRAILER_TYPE = "type";

        JSONObject trailerJson = new JSONObject(trailerJsonStr);

        /* Is there an error? */
        if (!trailerJson.has(TRAILER_NAME) && !trailerJson.has(TRAILER_KEY)) {
            String message = "Invalid JSON Object.";

            throw new JSONException(message);
        }

        Trailer trailer = new Trailer();
        trailer.setId(trailerJson.getString(TRAILER_ID));
        trailer.setKey(trailerJson.getString(TRAILER_KEY));
        trailer.setName(trailerJson.getString(TRAILER_NAME));
        trailer.setType(trailerJson.getString(TRAILER_TYPE));

        return trailer;
    }


    public static Review getReviewFromJson(String reviewJsonStr) throws JSONException {
        final String REVIEW_ID = "id";
        final String REVIEW_AUTHOR = "author";
        final String REVIEW_CONTENT = "content";

        JSONObject reviewJson = new JSONObject(reviewJsonStr);

        /* Is there an error? */
        if (!reviewJson.has(REVIEW_AUTHOR) && !reviewJson.has(REVIEW_CONTENT)) {
            String message = "Invalid JSON Object.";

            throw new JSONException(message);
        }

        Review review = new Review();
        review.setId(reviewJson.getString(REVIEW_ID));
        review.setAuthor(reviewJson.getString(REVIEW_AUTHOR));
        review.setContent(reviewJson.getString(REVIEW_CONTENT));

        return review;
    }
}