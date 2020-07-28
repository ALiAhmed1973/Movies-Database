package com.shaghaf.moviesdatabase;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shaghaf.moviesdatabase.database.AppDatabase;
import com.shaghaf.moviesdatabase.model.Movie;
import com.shaghaf.moviesdatabase.utils.MovieJsonUtilis;
import com.shaghaf.moviesdatabase.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdpter.MovieAdapterOnClick,
        LoaderManager.LoaderCallbacks<List<Movie>> {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String MOVIE_EXTRA_KEY="movie";
    private static final int LOADER_ID =10;
    private static final String URL_BUNDLE_EXTRA = "movie_url";
    private static final String FAVORITE_LIST_CHECK_BUNDLE_EXTRA = "favorite";
    private static final String RECYCLERVIEW_STATE_BUNDLE_EXTRA = "recyclerview_state";
    private MovieAdpter movieAdpter;
    private RecyclerView recyclerViewMain;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private TextView noFavoritesMoviesTextView;

    private String savedUrl;
    private AppDatabase database;
    private  List<Movie> currentMovies;
    private boolean isFavoritesListClicked;
    private Parcelable layouManagerState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewMain = findViewById(R.id.recyclerView_main);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2,
                RecyclerView.VERTICAL, false);
        recyclerViewMain.setLayoutManager(gridLayoutManager);
        recyclerViewMain.hasFixedSize();
        movieAdpter = new MovieAdpter(this);
        recyclerViewMain.setAdapter(movieAdpter);
        progressBar = findViewById(R.id.loading_progress_bar);
        errorTextView = findViewById(R.id.tv_error);
        noFavoritesMoviesTextView = findViewById(R.id.tv_no_favorites_saved);

        database = AppDatabase.getInstance(getApplicationContext());
        setupViewModel();

        if (savedInstanceState != null) {
            savedUrl = savedInstanceState.getString(URL_BUNDLE_EXTRA);
            isFavoritesListClicked = savedInstanceState.getBoolean(FAVORITE_LIST_CHECK_BUNDLE_EXTRA);

            if(isFavoritesListClicked)
            {
                SetFavoritesData(currentMovies);
            }else
            {
                makeUrlBundleAndLoader(savedUrl);
            }

        } else {
                makeUrlBundleAndLoader(NetworkUtils.DISCOVER_MOVIES_URL);

        }


    }

    private void showData()
    {
        errorTextView.setVisibility(View.INVISIBLE);
        noFavoritesMoviesTextView.setVisibility(View.INVISIBLE);
        recyclerViewMain.setVisibility(View.VISIBLE);
    }
    private void showError()
    {
        errorTextView.setVisibility(View.VISIBLE);
        noFavoritesMoviesTextView.setVisibility(View.INVISIBLE);
        recyclerViewMain.setVisibility(View.INVISIBLE);
    }
    private void showNoMoviesMessage()
    {
        errorTextView.setVisibility(View.INVISIBLE);
        noFavoritesMoviesTextView.setVisibility(View.VISIBLE);
        recyclerViewMain.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemClick(Movie movie) {
        Intent intent = new Intent(this,MovieDetails.class);
        intent.putExtra(MOVIE_EXTRA_KEY,movie);
        startActivity(intent);

    }
    private void makeUrlBundleAndLoader(String urlString)
    {

        Bundle bundle= new Bundle();
        bundle.putString(URL_BUNDLE_EXTRA,urlString);
        savedUrl =urlString;
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<Movie>> listLoader = loaderManager.getLoader(LOADER_ID);
        isFavoritesListClicked = false;
        if(listLoader==null)
        {
            loaderManager.initLoader(LOADER_ID,bundle,this);
        }else
        {
            loaderManager.restartLoader(LOADER_ID,bundle,this);
        }
    }

    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int i, @Nullable final Bundle bundle) {
        return new AsyncTaskLoader<List<Movie>>(this) {
            List<Movie> movies;


            @Override
            protected void onStartLoading() {
                if(movies!=null&&!movies.isEmpty())
                {
                    deliverResult(movies);
                }else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(@Nullable List<Movie> data) {
                movies =data;
                super.deliverResult(data);
            }

            @Nullable
            @Override
            public List<Movie> loadInBackground() {
                String urlString = bundle.getString(URL_BUNDLE_EXTRA);
                if(TextUtils.isEmpty( urlString))
                {
                    return null;
                }

                try {
                    URL url= NetworkUtils.buildUrl(urlString);
                    String s =NetworkUtils.getResponseFromHttpUrl(url);
                    List<Movie> movies= MovieJsonUtilis.getMovieFromJson(s);

                    return movies;

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> movies) {
        progressBar.setVisibility(View.INVISIBLE);
        if(movies != null && !movies.isEmpty())
        {
            showData();
            movieAdpter.setMoviesList(movies);
            restoreRecyclerviewScrollPosition();

        }else
        {
            showError();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {
      movieAdpter.setMoviesList(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.most_popular_action:
                makeUrlBundleAndLoader(NetworkUtils.POPULAR_MOVIES_URL);
                resetRecyclerviewScrollPosition();
                return true;
            case R.id.top_rated_action:
                makeUrlBundleAndLoader(NetworkUtils.TOP_RATED_MOVIES_URL);
                resetRecyclerviewScrollPosition();
                return true;
            case R.id.favorites_action:
                showData();
                SetFavoritesData(currentMovies);
                resetRecyclerviewScrollPosition();
                isFavoritesListClicked = true;
                return true;
                default:
                    return super.onOptionsItemSelected(item);

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(URL_BUNDLE_EXTRA,savedUrl);
        outState.putBoolean(FAVORITE_LIST_CHECK_BUNDLE_EXTRA,isFavoritesListClicked);
        outState.putParcelable(RECYCLERVIEW_STATE_BUNDLE_EXTRA,recyclerViewMain.getLayoutManager().onSaveInstanceState());
        Log.i(TAG,"onSaveInstanceState :"+isFavoritesListClicked);
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
            layouManagerState = savedInstanceState.getParcelable(RECYCLERVIEW_STATE_BUNDLE_EXTRA);
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isFavoritesListClicked) {
                showData();
                SetFavoritesData(currentMovies);

        }

    }

    private void setupViewModel()
    {

        MainViewModel model = ViewModelProviders.of(this).get(MainViewModel.class);
        model.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {

                if(movies!=null&&!movies.isEmpty()) {
                    currentMovies = movies;
                    if(isFavoritesListClicked)
                    {
                        movieAdpter.setMoviesList(movies);
                    }
                }else
                {
                    showNoMoviesMessage();
                }

            }
        });
    }

    private void restoreRecyclerviewScrollPosition()
    {
        if(layouManagerState!=null) {
            recyclerViewMain.getLayoutManager().onRestoreInstanceState(layouManagerState);
            layouManagerState=null;
        }
    }
    private void resetRecyclerviewScrollPosition()
    {


            recyclerViewMain.getLayoutManager().scrollToPosition(0);

    }

    private void SetFavoritesData(List<Movie> movies)
    {
        if(movies!=null&&!movies.isEmpty()) {
            movieAdpter.setMoviesList(movies);
        }else
        {
            showNoMoviesMessage();
        }
    }


}
