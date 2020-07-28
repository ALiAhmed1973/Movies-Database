package com.shaghaf.moviesdatabase;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.shaghaf.moviesdatabase.model.MovieTrailers;

import java.util.List;

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.MovieTrailersViewHolder> {
    List<MovieTrailers> movieTrailers;
    private MovieTrailersOnClickHandler trailersOnClickHandler;


    public interface MovieTrailersOnClickHandler{
        void onClick(String movieTrailerKey);
    }
    public MovieTrailersAdapter(List<MovieTrailers> trailers,MovieTrailersOnClickHandler onClickHandler)
    {
        movieTrailers=trailers;
        trailersOnClickHandler = onClickHandler;
    }

    @NonNull
    @Override
    public MovieTrailersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutId = R.layout.movie_tariler_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId,viewGroup,false);
        return new MovieTrailersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTrailersViewHolder movieTrailersViewHolder, int i) {
        MovieTrailers trailer = movieTrailers.get(i);

        movieTrailersViewHolder.textView.setText(trailer.getmName());

    }

    @Override
    public int getItemCount() {
        if(movieTrailers==null)
        {return 0;}
        return movieTrailers.size();
    }

    public class MovieTrailersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        public MovieTrailersViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_movie_trailer_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           String key=  movieTrailers.get(getAdapterPosition()).getmKey();
           trailersOnClickHandler.onClick(key);
        }
    }
}
