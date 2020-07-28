package com.shaghaf.moviesdatabase.utils;

import android.text.TextUtils;
import android.util.Log;

import com.shaghaf.moviesdatabase.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class MovieJsonUtilis {
    private MovieJsonUtilis(){}

    private static final String TAG = MovieJsonUtilis.class.getSimpleName();

    private static final String RESULT_JASON_ARRAY_KEY_NAME = "results" ;
    private static final String ORIGINAL_TITLE_JSON_KEY_NAME = "original_title" ;
    private static final String POSTER_PATH_JSON_KEY_NAME = "poster_path" ;
    private static final String OVERVIEW_JSON_KEY_NAME = "overview" ;
    private static final String RATING_JSON_KEY_NAME = "vote_average" ;
    private static final String RELEASE_DATE_JSON_KEY_NAME = "release_date" ;
    private static final String MOVIE_ID_JSON_KEY_NAME = "id" ;




    public static List<Movie> getMovieFromJson(String json){

        if(TextUtils.isEmpty(json))
        {
            return null;
        }

        List<Movie> Movies = new ArrayList<>();

        try {
            JSONObject baseJson = new JSONObject(json);
            JSONArray moviesResultArray = baseJson.getJSONArray(RESULT_JASON_ARRAY_KEY_NAME);

            for (int i =0 ;i<moviesResultArray.length();i++)
            {
                JSONObject currentMovie = moviesResultArray.getJSONObject(i);

                String title = currentMovie.optString(ORIGINAL_TITLE_JSON_KEY_NAME);
                String posterPath = currentMovie.optString(POSTER_PATH_JSON_KEY_NAME);
                String overview = currentMovie.optString(OVERVIEW_JSON_KEY_NAME);
                double rating = currentMovie.optDouble(RATING_JSON_KEY_NAME);
                String releaseDate = currentMovie.getString(RELEASE_DATE_JSON_KEY_NAME);
                int  mMovieId = currentMovie.optInt(MOVIE_ID_JSON_KEY_NAME);

                Movie movie = new Movie(mMovieId, title,posterPath,overview,rating,releaseDate);
                Movies.add(movie);
            }

        } catch (JSONException e) {
            Log.e(TAG, "error",e);
        }
        return Movies;

    }
}
