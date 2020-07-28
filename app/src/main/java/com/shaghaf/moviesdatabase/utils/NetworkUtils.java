package com.shaghaf.moviesdatabase.utils;

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

    public static final String DISCOVER_MOVIES_URL= "https://api.themoviedb.org/3/discover/movie";
    public static final String POPULAR_MOVIES_URL = "https://api.themoviedb.org/3/movie/popular";
    public static final String TOP_RATED_MOVIES_URL ="https://api.themoviedb.org/3/movie/top_rated";

    private static final String MOVIE_TRAILER ="https://api.themoviedb.org/3/movie/";

    private static final String IMAGE_MOVIE_BASE ="http://image.tmdb.org/t/p/";



    private static final String API_KEY_VALUE ="";


    private final static String API_KEY_PARAM = "api_key";

    private final static String IMAGE_SIZE ="w185";

    public static URL buildImageUrl(String imagePath)
    {
        Uri builtUri = Uri.parse(IMAGE_MOVIE_BASE).buildUpon().
                appendPath(IMAGE_SIZE).
                appendEncodedPath(imagePath).build();
        URL url=null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;


    }
    public static URL buildUrl(String baseUrl)
    {
        Uri builtUri = Uri.parse(baseUrl).buildUpon().
                appendQueryParameter(API_KEY_PARAM,API_KEY_VALUE).build();
        URL url=null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;

    }

    public static URL buildMovieTrailersUrl(String movieId)
    {
        Uri builtUri = Uri.parse(MOVIE_TRAILER).buildUpon().
                appendPath(movieId).appendPath("videos").
                appendQueryParameter(API_KEY_PARAM,API_KEY_VALUE).build();
        URL url=null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;

    }
    public static URL buildMovieReviewsUrl(String movieId)
    {
        Uri builtUri = Uri.parse(MOVIE_TRAILER).buildUpon().
                appendPath(movieId).appendPath("reviews").
                appendQueryParameter(API_KEY_PARAM,API_KEY_VALUE).build();
        URL url=null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection Connection = null ;
        String jsonResponse = "";

        try {
            Connection=(HttpURLConnection) url.openConnection();

            if (Connection.getResponseCode() == 200) {
                InputStream in = Connection.getInputStream();
                jsonResponse = readFromStream(in);
            }
            else if(Connection.getResponseCode() == 401)
            {
                Log.e(TAG, "Invalid API key: You must be granted a valid key.");

            }
            else
            {
                Log.e(TAG, "Error response code: " + Connection.getResponseCode());
            }

        }catch (IOException e)
        {
            Log.e(TAG, "Error can't get jsonResponse " + e.getMessage());
        }
        finally {
            Connection.disconnect();
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream in) {

        Scanner scanner = new Scanner(in);
        scanner.useDelimiter("\\A");

        boolean hasInput = scanner.hasNext();
        if (hasInput) {
            return scanner.next();
        } else {
            return null;
        }
    }

}
