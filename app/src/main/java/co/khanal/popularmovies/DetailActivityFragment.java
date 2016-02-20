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

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private final String PERSON_KEY = "person";

    private TextView movieYear;
    private TextView movieLength;
    private TextView movieRating;
    private TextView synapsis;
    private TextView movieTitle;
    private ImageView poster;
    private Movie movie;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        Activity activity = getActivity();
        movie = ((Movie.CanGetMovie)getActivity()).getMovie();
        loadMovie(movie);

        super.onActivityCreated(savedInstanceState);
    }

    public void loadMovie(Movie movie){
        if(movie != null) {
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

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        movieTitle = (TextView) rootView.findViewById(R.id.movie_title);
        movieYear = (TextView) rootView.findViewById(R.id.movie_year);
        movieRating = (TextView) rootView.findViewById(R.id.movie_rating);
        synapsis = (TextView) rootView.findViewById(R.id.synapsis);

        poster = (ImageView) rootView.findViewById(R.id.poster);

        return rootView;
    }

    public class PosterLoader extends AsyncTask<Movie, Void, Uri>{

        @Override
        protected Uri doInBackground(Movie... params) {
            return params[0].getImageUri();
        }

        @Override
        protected void onPostExecute(Uri uri) {
            Picasso.with(getContext()).load(uri).into(poster);
        }
    }

    public interface DetailActivityListener{
        public void OnMessageFromDetailActivityFragment(Bundle bundle);
    }
}
