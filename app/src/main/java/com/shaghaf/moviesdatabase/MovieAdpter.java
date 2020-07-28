package com.shaghaf.moviesdatabase;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shaghaf.moviesdatabase.model.Movie;
import com.shaghaf.moviesdatabase.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class MovieAdpter extends RecyclerView.Adapter<MovieAdpter.MovieAdapterViewHolder> {
    private static final String TAG = MovieAdpter.class.getSimpleName();
    private List<Movie> movies ;
    private MovieAdapterOnClick movieAdapterOnClick;


    public interface MovieAdapterOnClick
    {
        void onItemClick(Movie movie);
    }

    public MovieAdpter(MovieAdapterOnClick adapterOnClick)
    {
        movieAdapterOnClick = adapterOnClick;
    }


    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView moviePosterImageView;
        public MovieAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            moviePosterImageView = itemView.findViewById(R.id.im_movie_poster);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = movies.get(adapterPosition);
            movieAdapterOnClick.onItemClick(movie);

        }
    }
    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutId = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId,viewGroup,false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder movieAdapterViewHolder, int i) {
        Movie movie = movies.get(i);

        if(movie.getPosterPath()!=null && !movie.getPosterPath().isEmpty())
        {

            URL url= NetworkUtils.buildImageUrl(movie.getPosterPath());
            Picasso.get().load(url.toString()).error(R.drawable.ic_launcher_background).into(movieAdapterViewHolder.moviePosterImageView);


        }

    }

    @Override
    public int getItemCount() {
        if(movies==null)
        {return 0;}

        return movies.size();
    }

    public void setMoviesList(List<Movie> moviesList)
    {
        movies=moviesList;
        notifyDataSetChanged();
    }


}
