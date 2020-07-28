package com.shaghaf.moviesdatabase.utils;

import android.text.TextUtils;

import com.shaghaf.moviesdatabase.model.MovieReviews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieReviewsJsonUtils {
    private static final String RESULT_JASON_ARRAY_KEY_NAME = "results" ;
    private static final String REVIEW_CONTENT_JSON_KEY_NAME = "content" ;
    private static final String REVIEW_AUTHOR_JSON_KEY_NAME = "author" ;


    public static List<MovieReviews> getMovieFromJson(String json){
        if(TextUtils.isEmpty(json))
        {
            return null;
        }

        List<MovieReviews> reviews = new ArrayList<>();

        try {
            JSONObject baseJson = new JSONObject(json);
            JSONArray moviesResultArray = baseJson.getJSONArray(RESULT_JASON_ARRAY_KEY_NAME);
            for (int i =0;i<moviesResultArray.length();i++)
            {
                JSONObject currentTrailer = moviesResultArray.getJSONObject(i);

                String content=currentTrailer.optString(REVIEW_CONTENT_JSON_KEY_NAME);
                String author = currentTrailer.optString(REVIEW_AUTHOR_JSON_KEY_NAME);
               MovieReviews movieReviews = new MovieReviews(content,author);
               reviews.add(movieReviews);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews;
    }
}
