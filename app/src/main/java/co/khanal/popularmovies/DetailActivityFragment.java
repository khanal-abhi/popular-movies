package co.khanal.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {



    private TextView movieYear;
    private TextView movieRating;
    private TextView synapsis;
    private TextView movieTitle;
    private ImageView poster;
    private Movie movie;


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(movie == null){
            throw new NullPointerException("Movie cannot be null, EVER!");
        }
        outState.putParcelable(Movie.MOVIE_KEY, movie);
//        Toast.makeText(getContext(), "Saving movie", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Toast.makeText(getContext(), "Activity created", Toast.LENGTH_SHORT).show();

        if(savedInstanceState != null){
//            Toast.makeText(getContext(), "Loading previous movie", Toast.LENGTH_SHORT).show();
            movie = savedInstanceState.getParcelable(Movie.MOVIE_KEY);
            loadMovie(movie);
        } else {
//            Toast.makeText(getContext(), "No previous movies? wth??", Toast.LENGTH_SHORT).show();
            movie = ((Movie.CanGetMovie) getActivity()).getMovie();
            if (movie != null) {
                loadMovie(movie.cleanMovie());
//                Toast.makeText(getContext(), "Loading movie", Toast.LENGTH_SHORT).show();
            }
        }



    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle != null) {
            this.movie = bundle.getParcelable(Movie.MOVIE_KEY);
        }
        super.onCreate(savedInstanceState);
    }

    public void loadMovie(Movie movie){
        if(movie != null) {
            this.movie = movie;
            DecimalFormat df = new DecimalFormat("0.0");
            movieTitle.setText(movie.getOriginalTitle());
            String releaseYear = movie.getReleaseDate();
            try {
                releaseYear = releaseYear.substring(0, 4);
            } catch (IndexOutOfBoundsException e) {
                releaseYear = "Unknown";
            }
            movieYear.setText(releaseYear);
            movieRating.setText(String.valueOf(df.format(movie.getUserRating())) + "/10");
            synapsis.setText(movie.getSynopsis());

            new PosterLoader().execute(movie);
        }
    }

    public void loadMovie(Movie movie, boolean clicked){
        if(this.movie == null){
            loadMovie(movie);
        } else if (this.movie != null && clicked){
            loadMovie(movie);
        }
    }

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

//        Toast.makeText(getContext(), "Creating view", Toast.LENGTH_SHORT).show();
        movieTitle = (TextView) rootView.findViewById(R.id.movie_title);
        movieYear = (TextView) rootView.findViewById(R.id.movie_year);
        movieRating = (TextView) rootView.findViewById(R.id.movie_rating);
        synapsis = (TextView) rootView.findViewById(R.id.synapsis);

        poster = (ImageView) rootView.findViewById(R.id.poster);


        if(movie != null){
            loadMovie(movie);
        }

        return rootView;
    }


    public class PosterLoader extends AsyncTask<Movie, Void, Uri>{

        @Override
        protected Uri doInBackground(Movie... params) {
            return params[0].getImageUri();
        }

        @Override
        protected void onPostExecute(Uri uri) {
            Picasso.with(getContext())
                    .load(uri)
                    .placeholder(R.drawable.ic_thumb_up_white_48dp)
                    .error(R.drawable.ic_trending_up_white_48dp)
                    .into(poster);
        }
    }

    public interface DetailActivityListener{
        public void OnMessageFromDetailActivityFragment(Bundle bundle);
    }
}
