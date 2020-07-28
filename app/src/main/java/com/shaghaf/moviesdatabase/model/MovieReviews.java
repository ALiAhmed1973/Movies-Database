package com.shaghaf.moviesdatabase.model;

public class MovieReviews {
    private String mContent;
    private String mAuthor;

    public MovieReviews(String mContent, String mAuthor) {
        this.mContent = mContent;
        this.mAuthor = mAuthor;
    }



    public String getmContent() {
        return mContent;
    }

    public String getmAuthor() {
        return mAuthor;
    }
}
