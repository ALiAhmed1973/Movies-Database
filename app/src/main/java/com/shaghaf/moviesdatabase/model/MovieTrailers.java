package com.shaghaf.moviesdatabase.model;

public  class MovieTrailers
{
    private String mName;

    private String mKey;

    public MovieTrailers(String mName, String mKey) {
        this.mName = mName;
        this.mKey = mKey;
    }

    public String getmName() {
        return mName;
    }

    public String getmKey() {
        return mKey;
    }

}
