package com.shaghaf.moviesdatabase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.shaghaf.moviesdatabase.database.AppDatabase;
import com.shaghaf.moviesdatabase.model.Movie;

import java.util.List;

public class MainViewModel extends AndroidViewModel {


    private LiveData<List<Movie>> movies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(this.getApplication());
      movies=  appDatabase.movieDao().loadAllMovies();
    }
    public LiveData<List<Movie>> getMovies() {
        return movies;
    }
}
