package com.shaghaf.moviesdatabase.utils;

import android.text.TextUtils;

import com.shaghaf.moviesdatabase.model.MovieTrailers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class MovieTrailersJsonUtils {

    private static final String RESULT_JASON_ARRAY_KEY_NAME = "results" ;
    private static final String TRAILER_NAME_JSON_KYE_NAME ="name";
    private static final String TRAILER_KEY_JSON_KYE_NAME ="key";


    public static List<MovieTrailers> getMovieFromJson(String json){
        if(TextUtils.isEmpty(json))
        {
            return null;
        }
        List<MovieTrailers> movieTrailers = new ArrayList<>();
        try {
            JSONObject baseJson = new JSONObject(json);
            JSONArray moviesResultArray = baseJson.getJSONArray(RESULT_JASON_ARRAY_KEY_NAME);
            for (int i =0;i<moviesResultArray.length();i++)
            {
                JSONObject currentTrailer = moviesResultArray.getJSONObject(i);

                String name=currentTrailer.optString(TRAILER_NAME_JSON_KYE_NAME);
                String key = currentTrailer.getString(TRAILER_KEY_JSON_KYE_NAME);
                MovieTrailers trailers = new MovieTrailers(name,key);
                movieTrailers.add(trailers);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieTrailers;

    }





}
