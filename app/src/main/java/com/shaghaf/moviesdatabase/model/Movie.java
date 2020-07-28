package com.shaghaf.moviesdatabase.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie")
public class Movie implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "movie_id")
    private int mMovieId;
    @ColumnInfo(name = "original_title")
    private String mOriginalTitle;
    @ColumnInfo(name = "poster_path")
    private String mPosterPath;
    @ColumnInfo(name = "movie_overview")
    private String mMovieOverview;
    @ColumnInfo(name = "movie_rating")
    private double mMovieRating;
    @ColumnInfo(name = "release_date")
    private String mReleaseDate;



    @Ignore
    public Movie(int mMovieId, String mOriginalTitle, String mPosterPath, String mMovieOverview, double mMovieRating, String mReleaseDate) {
        this.mMovieId = mMovieId;
        this.mOriginalTitle = mOriginalTitle;
        this.mPosterPath = mPosterPath;
        this.mMovieOverview = mMovieOverview;
        this.mMovieRating = mMovieRating;
        this.mReleaseDate = mReleaseDate;
    }

    public Movie(int id, int mMovieId, String mOriginalTitle, String mPosterPath, String mMovieOverview, double mMovieRating, String mReleaseDate) {
        this.id = id;
        this.mMovieId = mMovieId;
        this.mOriginalTitle = mOriginalTitle;
        this.mPosterPath = mPosterPath;
        this.mMovieOverview = mMovieOverview;
        this.mMovieRating = mMovieRating;
        this.mReleaseDate = mReleaseDate;
    }
    public int getId() {
        return id;
    }

    public int getMovieId() {
        return mMovieId;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public String getMovieOverview() {
        return mMovieOverview;
    }

    public double getMovieRating() {
        return mMovieRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    protected Movie(Parcel in) {
        id=in.readInt();
        mMovieId=in.readInt();
        mOriginalTitle = in.readString();
        mPosterPath = in.readString();
        mMovieOverview = in.readString();
        mMovieRating = in.readDouble();
        mReleaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(mMovieId);
        dest.writeString(mOriginalTitle);
        dest.writeString(mPosterPath);
        dest.writeString(mMovieOverview);
        dest.writeDouble(mMovieRating);
        dest.writeString(mReleaseDate);
    }
}
