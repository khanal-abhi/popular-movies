package co.khanal.popularmovies;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

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
        movieLength = (TextView) rootView.findViewById(R.id.movie_length);
        movieRating = (TextView) rootView.findViewById(R.id.movie_rating);
        synapsis = (TextView) rootView.findViewById(R.id.synapsis);

        poster = (ImageView) rootView.findViewById(R.id.poster);

        //Movie moview = getActivity().getIntent().getExtras().get


        return rootView;
    }
}
