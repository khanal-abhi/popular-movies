package co.khanal.popularmovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import co.khanal.popularmovies.DB.MovieModel;
import co.khanal.popularmovies.DB.ReviewModel;
import co.khanal.popularmovies.DB.TrailerModel;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements FetchJsonTrailers.FetchJsonTrailersListener, FetchJsonReviews.FetchJsonReviewsListener {

    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    private TextView movieYear;
    private TextView movieRating;
    private TextView synopsis;
    private TextView movieTitle;
    private ImageView poster;
    private Button addToFavorite;

    private LinearLayout trailersLayout;
    private LinearLayout reviewsLayout;
    private ScrollView scrollView;

    private Movie movie;
    private List<Trailer> trailers;
    private List<Review> reviews;


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(movie == null){
            loadMovie(movie);
            return;
        }
        outState.putParcelable(Movie.MOVIE_KEY, movie);
        outState.putParcelableArray(Trailer.TRAILER_KEY, getTrailersArray());
        outState.putParcelableArray(Review.REVIEW_KEY, getReviewsArray());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){
            movie = savedInstanceState.getParcelable(Movie.MOVIE_KEY);
            addToTrailers((Trailer[])savedInstanceState.getParcelableArray(Trailer.TRAILER_KEY));
            addToReviews((Review[])savedInstanceState.getParcelableArray(Review.REVIEW_KEY));
            loadMovie(movie);
        } else {
            movie = ((Movie.MovieProvider) getActivity()).getMovie();
            loadMovie(movie);
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle != null) {
            this.movie = bundle.getParcelable(Movie.MOVIE_KEY);
        }
        trailers = new ArrayList<>();
        reviews = new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

    public void loadMovie(Movie movie){
        if(movie != null) {
            try{
                scrollView.setVisibility(View.VISIBLE);
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

                final String trailersUrl = String.format("http://api.themoviedb.org/3/movie/%s/videos?api_key=%s", movie.getId(), getString(R.string.api_key));
                final String reviewsUrl = String.format("http://api.themoviedb.org/3/movie/%s/reviews?api_key=%s", movie.getId(), getString(R.string.api_key));

                new FetchJsonTrailers(getFragmentManager().findFragmentById(R.id.details_fragment), trailersUrl).execute();
                new FetchJsonReviews(getFragmentManager().findFragmentById(R.id.details_fragment), reviewsUrl).execute();

                new PosterLoader().execute(movie);
            } catch (Exception e){
                e.printStackTrace();
            }
        } else {
            scrollView.setVisibility(View.GONE);
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

    public void loadMovieFromDB(Movie movie){

        addToFavorite.setVisibility(View.GONE);

        MovieModel movieModel = new MovieModel(getContext());
        TrailerModel trailerModel = new TrailerModel(getContext());
        ReviewModel reviewModel = new ReviewModel(getContext());

        this.movie = movieModel.getMovie(movie.getId());
        this.trailers = trailerModel.getTrailersForMovie(movie.getId());
        this.reviews = reviewModel.getReviewsForMovie(movie.getId());

        trailersLayout.removeAllViews();
        loadTrailers();

        reviewsLayout.removeAllViews();
        loadReviews();

        loadBitmapToImageView();


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
        addToFavorite = (Button) rootView.findViewById(R.id.add_to_favorite);

        trailersLayout = (LinearLayout) rootView.findViewById(R.id.main_layout_for_trailers);
        reviewsLayout = (LinearLayout) rootView.findViewById(R.id.main_layout_for_reviews);

        scrollView = (ScrollView) rootView.findViewById(R.id.details_scroll_view);

        addToFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable) poster.getDrawable()).getBitmap();
                movie.setBytesArray(MovieModel.getBytes(bitmap));
                new OfflineSaver(movie, trailers, reviews, getContext()).execute();
            }
        });

        if(movie != null){
            loadMovie(movie);
        }

        return rootView;
    }

    @Override
    public void onFetchedJsonReviews(JSONObject fetchedJsonReviews) {

        try {
            reviews = Review.parseReviewsJson(fetchedJsonReviews);

            loadReviews();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFetchedJsonTrailers(JSONObject fetchedJsonTrailers) {

        try {
            trailers = Trailer.parseTrailersJson(fetchedJsonTrailers);
            loadTrailers();

            for (Trailer trailer : trailers){
                trailer.setMovie_id(movie.getId());
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadBitmapToImageView(){
        Bitmap bitmap = BitmapFactory.decodeByteArray(movie.getBytesArray(), 0, movie.getBytesArray().length);
        poster.setImageBitmap(bitmap);
    }

    public void loadTrailers(){
        for (final Trailer trailer : trailers){
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View trailerLayout = layoutInflater.inflate(R.layout.single_trailer, null);
            ((TextView) trailerLayout.findViewById(R.id.trailer_title)).setText(trailer.getName());
            (trailerLayout.findViewById(R.id.trailer_play)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(trailer.getKey()));
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            });
            trailersLayout.addView(trailerLayout);
        }
    }

    public void loadReviews(){
        for(Review review : reviews){

            review.setMovie_id(movie.getId());

            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View reviewLayout = layoutInflater.inflate(R.layout.single_review, null);

            ((TextView) reviewLayout.findViewById(R.id.review_author)).setText(review.getAuthor());
            ((TextView) reviewLayout.findViewById(R.id.review_content)).setText(review.getContent());
            ((TextView) reviewLayout.findViewById(R.id.review_url)).setText(review.getUrl());

            reviewsLayout.addView(reviewLayout);

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

    private Trailer[] getTrailersArray(){
        if(trailers != null){
            int count = trailers.size();
            Trailer[] trailersArray = new Trailer[count];
            for(int i = 0; i< count; i++){
                trailersArray[i] = trailers.get(i);
            }
            return trailersArray;
        }
        return new Trailer[0];
    }

    private Review[] getReviewsArray(){
        if(reviews != null){
            int count = reviews.size();
            Review[] reviewsArray = new Review[count];
            for(int i = 0; i< count; i++){
                reviewsArray[i] = reviews.get(i);
            }
            return reviewsArray;
        }
        return new Review[0];
    }

    private void addToTrailers(Trailer[] trailersArray){
        int count = trailersArray.length;
        for(int i = 0; i < count; i++){
            trailers.add(trailersArray[i]);
        }
    }

    private void addToReviews(Review[] reviewsArray){
        int count = reviewsArray.length;
        for(int i = 0; i < count; i++){
            reviews.add(reviewsArray[i]);
        }
    }
}
