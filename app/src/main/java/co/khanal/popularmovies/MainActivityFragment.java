package co.khanal.popularmovies;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import co.khanal.popularmovies.DB.MovieModel;


/**
 * MainActivity Fragment holds a prominent GridView in addition to (so far) a thumbs up action bar
 * item for sorting movies by rating and a trend up action bar icon for sorting movies by popularity.
 * It saves and restores the states of its GridView, including the current scroll settings.
 */
public class MainActivityFragment extends Fragment implements LoadMoviesFromApi.MoviesReceiver{

    public static final String GRID_VIEW_FIRST_VISIBLE_ITEM = "grid_view_first_visible_item";
    private static final String GRID_VIEW_STATE = "grid_view_state";
    public static final String CLICKED = "clicked";

    private int grid_view_item = -1;

    private GridView gridView;
    private Movie[] movies;

    public MainActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // outState ready for consumption

        outState.putParcelableArray(Movie.MOVIE_KEY, movies);
        outState.putParcelable(GRID_VIEW_STATE, gridView.onSaveInstanceState());
        outState.putInt(GRID_VIEW_FIRST_VISIBLE_ITEM, grid_view_item == -1 ? gridView.getFirstVisiblePosition() : grid_view_item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){
            movies = (Movie[])savedInstanceState.getParcelableArray(Movie.MOVIE_KEY);
            gridView.onRestoreInstanceState(savedInstanceState.getParcelable(GRID_VIEW_STATE));
            gridView.setSelection(savedInstanceState.getInt(GRID_VIEW_FIRST_VISIBLE_ITEM));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){

            case R.id.ratings:
                sortByRatings();
                break;

            case R.id.popularity:
                sortByPopular();
                break;

            case R.id.favorites:
                sortByFavorites();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortByPopular(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String currentOrder = pref.getString(getString(R.string.pref_sort_by_key), "");
        if(currentOrder.contentEquals(getString(R.string.most_popular))){
            return;
        }
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(getString(R.string.pref_sort_by_key), getString(R.string.most_popular));
        editor.apply();
        setupLoad();

    }

    private void sortByRatings(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String currentOrder = pref.getString(getString(R.string.pref_sort_by_key), "");
        if(currentOrder.contentEquals(getString(R.string.highest_rated))){
            return;
        }
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(getString(R.string.pref_sort_by_key), getString(R.string.highest_rated));
        editor.apply();
        setupLoad();

    }

    private void sortByFavorites(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String currentOrder = pref.getString(getString(R.string.pref_sort_by_key), "");
        if(currentOrder.contentEquals(getString(R.string.favorites))){
            return;
        }
        MovieModel movieModel = new MovieModel(getContext());
        if( movieModel.getMovies().size() == 0)
            return;
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(getString(R.string.pref_sort_by_key), getString(R.string.favorites));
        editor.apply();
        loadMoviesFromDB();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        inflater.inflate(R.layout.fragment_detail, container, true);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gridView = (GridView) rootView.findViewById(R.id.main_fragment_gridview);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),"Yo",Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                Movie movie = movies[position];
                bundle.putParcelable(Movie.MOVIE_KEY, movie);
                bundle.putBoolean(CLICKED, true);
                MainActivityFragmentListener activity = (MainActivityFragmentListener) getActivity();
                if (activity != null)
                    activity.OnMessageFromMainActivityFragment(bundle);

//                grid_view_item saves the state of scroll
                grid_view_item = position;
            }

        });


        gridView.setNumColumns(getResources().getInteger(R.integer.grid_columns));
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String currentPref = preferences.getString(getString(R.string.pref_sort_by_key), "");
        if(currentPref.contentEquals(getString(R.string.favorites))){
            loadMoviesFromDB();
        } else {
            setupLoad();
        }

        return rootView;
    }

    private void setupLoad(){
        final String BASE_URI = "http://api.themoviedb.org/3/discover/movie";
        final String SORT_METHOD = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getString(R.string.pref_sort_by_key), getString(R.string.pref_sort_by_default_value));
        final String SORT_BY_PARAM = getString(R.string.sort_by_param);
        final String API_KEY_PARAM = getString(R.string.api_key_param);
        final String API_KEY = getString(R.string.api_key);
        final String IMAGE_BASE_URI = getString(R.string.image_base_uri);

        final String[] PARAMS = {
                BASE_URI,
                SORT_BY_PARAM,
                SORT_METHOD,
                API_KEY_PARAM,
                API_KEY,
                IMAGE_BASE_URI
        };

        new LoadMoviesFromApi((LoadMoviesFromApi.MoviesReceiver)getFragmentManager().findFragmentById(R.id.main_fragment)).execute(PARAMS);
    }

    @Override
    public void onReceiveMovies(Movie[] movies) {
        this.movies = movies;
        if(movies != null){
            gridView.setAdapter(new GridViewAdapter(getContext(), R.layout.grid_item, movies));

            Bundle bundle = new Bundle();
            bundle.putParcelable(Movie.MOVIE_KEY,movies[0]);
            bundle.putBoolean(CLICKED, false);
            if(getActivity() != null){
                ((MainActivityFragmentListener)getActivity()).OnMessageFromMainActivityFragment(bundle);
            }
        } else {
//            I am proud of this one :) If there is no connection, it will eventually try to load the local movies, Offline - First!
            Toast.makeText(getContext(), "Looks like we are having some connections issues. Are you sure you are connected to the internet?", Toast.LENGTH_LONG).show();
        }
    }

    public void loadMoviesFromDB(){
        MovieModel movieModel = new MovieModel(getContext());
        List<Movie> movieList = movieModel.getMovies();
        int i = 0;
        movies = new Movie[movieList.size()];
        if (movies.length == 0)
            return;
        for(Movie movie : movieList){
            movies[i] = movie;
            i++;
        }
        gridView.setAdapter(new GridViewAdapter(getContext(), R.layout.grid_item, movies));
        Bundle bundle = new Bundle();
        bundle.putParcelable(Movie.MOVIE_KEY,movies[0]);
        bundle.putBoolean(CLICKED, false);
        if(getActivity() != null){
            ((MainActivityFragmentListener)getActivity()).OnMessageFromMainActivityFragment(bundle);
        }
    }

    public interface MainActivityFragmentListener{
        void OnMessageFromMainActivityFragment(Bundle bundle);
    }
}
