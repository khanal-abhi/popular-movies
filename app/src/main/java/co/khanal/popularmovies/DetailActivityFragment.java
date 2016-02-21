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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements FetchVideosJson.FetchVideosJsonListener{

    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    private TextView movieYear;
    private TextView movieRating;
    private TextView synopsis;
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
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){
            movie = savedInstanceState.getParcelable(Movie.MOVIE_KEY);
            loadMovie(movie);
        } else {
            movie = ((Movie.MovieProvider) getActivity()).getMovie();
            if (movie != null) {
                loadMovie(movie);
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
            try{
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
                String userRating = String.valueOf(df.format(movie.getUserRating())) + "/10";
                movieRating.setText(userRating);
                synopsis.setText(movie.getSynopsis());

                final String url = String.format("http://api.themoviedb.org/3/movie/%s/videos?api_key=%s", movie.getId(), getString(R.string.api_key));
                Log.v(getClass().getSimpleName(), url);
                new FetchVideosJson(
                        (FetchVideosJson.FetchVideosJsonListener)getFragmentManager().findFragmentById(R.id.details_fragment)
                ).execute(url);

                new PosterLoader().execute(movie);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void loadMovie(Movie movie, boolean clicked){
        if(this.movie == null){
            loadMovie(movie);
        } else if (clicked){
            loadMovie(movie);
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
        synopsis = (TextView) rootView.findViewById(R.id.synopsis);

        poster = (ImageView) rootView.findViewById(R.id.poster);

        if(movie != null){
            loadMovie(movie);
        }

        return rootView;
    }

    @Override
    public void onVideosFetched(JSONObject videosJson) {

        try {
            JSONArray trailersArray = videosJson.getJSONArray("results");
            if(trailersArray.length() > 0){
                for (int i = 0; i < trailersArray.length(); i++){
                    String trailerName = trailersArray.getJSONObject(i).getString("name");
                    String trailerLink = YOUTUBE_BASE_URL + trailersArray.getJSONObject(i).getString("key");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
}
