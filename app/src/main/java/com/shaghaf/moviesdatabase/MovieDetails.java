package com.shaghaf.moviesdatabase;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shaghaf.moviesdatabase.database.AppDatabase;
import com.shaghaf.moviesdatabase.databinding.ActivityMovieDetailsBinding;
import com.shaghaf.moviesdatabase.model.Movie;
import com.shaghaf.moviesdatabase.model.MovieReviews;
import com.shaghaf.moviesdatabase.model.MovieTrailers;
import com.shaghaf.moviesdatabase.utils.MovieReviewsJsonUtils;
import com.shaghaf.moviesdatabase.utils.MovieTrailersJsonUtils;
import com.shaghaf.moviesdatabase.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MovieDetails extends AppCompatActivity implements MovieTrailersAdapter.MovieTrailersOnClickHandler {
    private static final String TAG = MainActivity.class.getSimpleName();
    ActivityMovieDetailsBinding mBinding;

    private String mMovieId;
    private List<MovieTrailers> movieTrailersList;
private static final int TRAILERS_LOADER_ID = 5;
    private static final int REVIEWS_LOADER_ID = 10;

    private LoaderManager.LoaderCallbacks<List<MovieTrailers>> fetchMovieTrailers;
    private LoaderManager.LoaderCallbacks<List<MovieReviews>> fetchMovieReviews;


    private AppDatabase database;



    private int movieId;

    private static boolean isSetFavorites=false;

   private Movie oldMovie ;

   private List<MovieReviews> movieReviewsList;
   private static int numOfReviewsListIndex;
    private static int number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        database = AppDatabase.getInstance(getApplicationContext());

        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_movie_details);
        final Context context = this;


        Intent intent = getIntent();
        if (intent.hasExtra(MainActivity.MOVIE_EXTRA_KEY)) {
            Movie movie = intent.getParcelableExtra(MainActivity.MOVIE_EXTRA_KEY);

            setViewsData(movie);


            fetchMovieTrailers = new LoaderManager.LoaderCallbacks<List<MovieTrailers>>() {
                @NonNull
                @Override
                public Loader<List<MovieTrailers>> onCreateLoader(int id, @Nullable Bundle args) {

                    return new AsyncTaskLoader<List<MovieTrailers>>(MovieDetails.this) {
                        List<MovieTrailers> movieTrailers;
                        @Override
                        protected void onStartLoading() {
                            if(movieTrailers!=null&&!movieTrailers.isEmpty())
                            {
                                deliverResult(movieTrailers);
                            }else
                            {
                                forceLoad();
                            }

                        }
                        @Override
                        public void deliverResult(@Nullable List<MovieTrailers> data) {
                            movieTrailers =data;
                            super.deliverResult(data);
                        }
                        @Nullable
                        @Override
                        public List<MovieTrailers> loadInBackground() {
                            if(mMovieId==null)
                            {
                                return null;
                            }
                            String id = mMovieId;
                            URL url = NetworkUtils.buildMovieTrailersUrl(id);
                            try {
                                String s = NetworkUtils.getResponseFromHttpUrl(url);
                                List<MovieTrailers> trailers = MovieTrailersJsonUtils.getMovieFromJson(s);
                                return trailers;
                            } catch (IOException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                    };
                }

                @Override
                public void onLoadFinished(@NonNull Loader<List<MovieTrailers>> loader, List<MovieTrailers> movieTrailers) {
                    if (movieTrailers != null && !movieTrailers.isEmpty()) {
//
                        movieTrailersList = movieTrailers;
                        RecyclerView recyclerView = findViewById(R.id.lv_trailers_buttons);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setHasFixedSize(true);
                        MovieTrailersAdapter movieTrailersAdapter = new MovieTrailersAdapter(movieTrailersList, MovieDetails.this);
                        recyclerView.setAdapter(movieTrailersAdapter);
//
                    }
                }

                @Override
                public void onLoaderReset(@NonNull Loader<List<MovieTrailers>> loader) {

                }
            };
            fetchMovieReviews = new LoaderManager.LoaderCallbacks<List<MovieReviews>>() {
                @NonNull
                @Override
                public Loader<List<MovieReviews>> onCreateLoader(int id, @Nullable Bundle args) {
                    return new AsyncTaskLoader<List<MovieReviews>>(MovieDetails.this) {
                        List<MovieReviews> movieReviews;
                        @Override
                        protected void onStartLoading() {
                            if (movieReviews != null && !movieReviews.isEmpty()) {
                                deliverResult(movieReviews);
                            } else {
                                forceLoad();
                            }
                        }
                        @Override
                        public void deliverResult(@Nullable List<MovieReviews> data) {
                            movieReviews = data;
                            super.deliverResult(data);
                        }

                            @Nullable
                        @Override
                        public List<MovieReviews> loadInBackground() {
                            if(mMovieId==null)
                            {
                                return null;
                            }
                            String id = mMovieId;
                            URL url = NetworkUtils.buildMovieReviewsUrl(id);
                            try {
                                String s = NetworkUtils.getResponseFromHttpUrl(url);
                                List<MovieReviews> reviews = MovieReviewsJsonUtils.getMovieFromJson(s);
                                return reviews;
                            } catch (IOException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                    };
                }

                @Override
                public void onLoadFinished(@NonNull Loader<List<MovieReviews>> loader, List<MovieReviews> movieReviews) {
                    if (movieReviews != null && !movieReviews.isEmpty()) {
                        movieReviewsList = movieReviews;
                        numOfReviewsListIndex=movieReviews.size();
                        changeReviewAndAuthorText(number);
                    } else if (movieReviews != null && movieReviews.isEmpty()) {
                        mBinding.tvReviewContent.setText("No Comment");
                        mBinding.btNext.setVisibility(View.INVISIBLE);
                        mBinding.btBack.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onLoaderReset(@NonNull Loader<List<MovieReviews>> loader) {

                }
            };
            initLoaders();



        }

        MovieDetailsViewModelFactory factory = new MovieDetailsViewModelFactory(database,movieId);
        final MovieDetailsViewModel viewModel = ViewModelProviders.of(this,factory).get(MovieDetailsViewModel.class);

        viewModel.getMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                if (movie!=null)
                {
                    oldMovie =movie;
                    mBinding.btSaveToFavorites.setText(getString(R.string.favorites_button_remove));
                    isSetFavorites=true;

                }else
                {
                    mBinding.btSaveToFavorites.setText(getString(R.string.favorites_button_add));
                    isSetFavorites=false;
                }
            }
        });



    }

    private void initLoaders()
    {
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<MovieTrailers>> TrailerslistLoader = loaderManager.getLoader(TRAILERS_LOADER_ID);
        Loader<List<MovieReviews>> ReviewslistLoader = loaderManager.getLoader(TRAILERS_LOADER_ID);
        if(TrailerslistLoader==null)
        {
            loaderManager.initLoader(TRAILERS_LOADER_ID,null,fetchMovieTrailers);
        }else
        {
            loaderManager.restartLoader(TRAILERS_LOADER_ID,null,fetchMovieTrailers);
        }
        if(ReviewslistLoader==null)
        {
            loaderManager.initLoader(REVIEWS_LOADER_ID,null,fetchMovieReviews);
        }else
        {
            loaderManager.restartLoader(REVIEWS_LOADER_ID,null,fetchMovieReviews);
        }
    }


    @Override
    public void onClick(String movieTrailerKey) {
        Uri uri = Uri.parse("https://www.youtube.com/watch?v=" + movieTrailerKey);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }



    private void setViewsData(final Movie movie)
    {


        URL url = NetworkUtils.buildImageUrl(movie.getPosterPath());
        Picasso.get().load(url.toString()).error(R.drawable.ic_launcher_background).into(mBinding.ivMovieDetailsPoster);
        setTitle(movie.getOriginalTitle());

        mBinding.tvMovieTitle.setText(movie.getOriginalTitle());

        mBinding.tvReleaseDate.setText(movie.getReleaseDate());
mBinding.tvRating.setText(String.valueOf(movie.getMovieRating()));
mBinding.tvMovieOverview.setText(movie.getMovieOverview());
        movieId = movie.getMovieId();
        mMovieId = String.valueOf(movieId);
        mBinding.btSaveToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(isSetFavorites)
                        {
                            database.movieDao().deleteMovie(oldMovie);
                            Log.i(TAG,"Movie deleted*****************");
                        }else {
                            database.movieDao().insertMovie(movie);
                            Log.i(TAG,"Movie Added*****************");
                        }

                    }
                });
            }
        });

        mBinding.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(number>0)
                {
                    number--;
                    changeReviewAndAuthorText(number);
                }

            }
        });
       mBinding.btNext.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(number<numOfReviewsListIndex-1)
               {
                   number++;
                   changeReviewAndAuthorText(number);
               }
           }
       });


    }

    private void changeReviewAndAuthorText(int index)
    {
        if(movieReviewsList!=null&&!movieReviewsList.isEmpty()) {
            mBinding.btNext.setVisibility(View.VISIBLE);
            mBinding.btBack.setVisibility(View.VISIBLE);
            String text = (index + 1) + "/" + numOfReviewsListIndex;
            mBinding.tvReviewPages.setText(text);
            mBinding.tvReviewContent.setText(movieReviewsList.get(index).getmContent());
            mBinding.tvReviewAuthor.setText(movieReviewsList.get(index).getmAuthor());
        }

    }


}
