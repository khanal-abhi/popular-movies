package co.khanal.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.List;

import co.khanal.popularmovies.DB.MovieModel;
import co.khanal.popularmovies.DB.ReviewModel;
import co.khanal.popularmovies.DB.TrailerModel;

/**
 * Created by abhi on 2/22/16.
 */
public class OfflineSaver extends AsyncTask<Void, Void, Void> {

    private Movie movie;
    private List<Trailer> trailers;
    private List<Review> reviews;
    private Context context;

    public OfflineSaver(Movie movie, List<Trailer> trailers, List<Review> reviews, Context context) {
        this.movie = movie;
        this.trailers = trailers;
        this.reviews = reviews;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        MovieModel movieModel = new MovieModel(context);
        TrailerModel trailerModel = new TrailerModel(context);
        ReviewModel reviewModel = new ReviewModel(context);

        try {
            movieModel.addMovie(movie);
            trailerModel.addTrailers(trailers);
            reviewModel.addReviews(reviews);
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Toast.makeText(context, "Movie Saved", Toast.LENGTH_SHORT).show();
    }
}
