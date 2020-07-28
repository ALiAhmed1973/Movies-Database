package com.shaghaf.moviesdatabase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.shaghaf.moviesdatabase.database.AppDatabase;
import com.shaghaf.moviesdatabase.model.Movie;

class MovieDetailsViewModel extends ViewModel {


    private LiveData<Movie> movieLiveData;
    public MovieDetailsViewModel(AppDatabase database, int mTaskId) {
        movieLiveData = database.movieDao().loadMovieById(mTaskId);
    }

    public LiveData<Movie> getMovie() {
        return movieLiveData;
    }
}
