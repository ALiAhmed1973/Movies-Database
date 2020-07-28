package com.shaghaf.moviesdatabase;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.shaghaf.moviesdatabase.database.AppDatabase;

public class MovieDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final int mTaskId;

    public MovieDetailsViewModelFactory(AppDatabase mDb, int mTaskId) {
        this.mDb = mDb;
        this.mTaskId = mTaskId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new MovieDetailsViewModel(mDb, mTaskId);
    }
}
