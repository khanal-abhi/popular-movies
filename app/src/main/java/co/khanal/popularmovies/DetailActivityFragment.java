package co.khanal.popularmovies;

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

    TextView movieTitle, movieYear, movieLength, movieRating, synapsis;
    ImageView poster;

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

        Movie movie = (Movie) getActivity().getIntent().getExtras().getParcelable(Movie.MOVIE_KEY);
        Log.v("TAG", movie.toString());

        DecimalFormat df = new DecimalFormat("0.0");
//        Picasso.with(getContext()).load(movie.getImageUri()).into(poster);
        movieTitle.setText(movie.getOriginalTitle());
        String releaseYear = movie.getReleaseDate();
        releaseYear = releaseYear.substring(0,4);
        movieYear.setText(releaseYear);
        movieRating.setText(String.valueOf(df.format(movie.getUserRating())) +"/10");
        synapsis.setText(movie.getSynopsis());

        new PosterLoader().execute(movie);

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
}
