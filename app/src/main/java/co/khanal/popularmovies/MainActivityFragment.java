package co.khanal.popularmovies;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoadMoviesFromApi.MoviesReciever{

    public static final String GRID_VIEW_FIRST_VISIBLE_ITEM = "grid_view_first_visible_item";
    private static final String GRID_VIEW_STATE = "grid_view_state";
    public static final String CLICKED = "clicked";
    public static final String MOVIES = "movies";

    private int gridview_item = -1;

    private Movie[] movies;
    private GridView gridView;
    private GridViewAdapter gridViewAdapter;

    public MainActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // outState ready for consumption

        outState.putParcelable(GRID_VIEW_STATE, gridView.onSaveInstanceState());
        outState.putInt(GRID_VIEW_FIRST_VISIBLE_ITEM, gridview_item == -1 ? gridView.getFirstVisiblePosition() : gridview_item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){

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
            case R.id.action_settings:
                break;

            case R.id.ratings:
                sortByRatings();
                break;

            case R.id.popularity:
                sortByPopular();
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
        editor.commit();
        setupLoad();
        return;
    }

    private void sortByRatings(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String currentOrder = pref.getString(getString(R.string.pref_sort_by_key), "");
        if(currentOrder.contentEquals(getString(R.string.highest_rated))){
            return;
        }
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(getString(R.string.pref_sort_by_key), getString(R.string.highest_rated));
        editor.commit();
        setupLoad();
        return;
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

                Bundle bundle = new Bundle();
                Movie movie = (Movie) parent.getAdapter().getItem(position);
                bundle.putParcelable(Movie.MOVIE_KEY, movie);
                bundle.putBoolean(CLICKED, true);
                MainActivityFragmentListener activity = (MainActivityFragmentListener) getActivity();
                if(getActivity() != null)
                    ((MainActivityFragmentListener) getActivity()).OnMessageFromMainActivityFragment(bundle);
                gridview_item = position;
            }

        });


        gridView.setNumColumns(getResources().getInteger(R.integer.grid_columns));
        setupLoad();

//        Toast.makeText(getContext(), "Recreated the screen: ", Toast.LENGTH_SHORT).show();

        return rootView;
    }



    private void setupLoad(){
        final String BASE_URI = "http://api.themoviedb.org/3/discover/movie";
        final String SORT_METHOD = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getString(R.string.pref_sort_by_key), getString(R.string.pref_sort_by_default_value));
        final String SORT_BY_PARAM = getString(R.string.sort_by_param);
        final String API_KEY_PARAM = getString(R.string.api_key_param);
        final String API_KEY = getString(R.string.api_key);

        final String[] PARAMS = {
                BASE_URI,
                SORT_BY_PARAM,
                SORT_METHOD,
                API_KEY_PARAM,
                API_KEY
        };

        new LoadMoviesFromApi(getFragmentManager().findFragmentById(R.id.main_fragment)).execute(PARAMS);
    }

    @Override
    public void onRecieveMovies(Movie[] movies) {
        this.movies = movies;

        gridViewAdapter = new GridViewAdapter(getContext(), R.layout.grid_item, movies);
        gridView.setAdapter(gridViewAdapter);

        Bundle bundle = new Bundle();
        bundle.putParcelable(Movie.MOVIE_KEY,movies[0]);
        bundle.putBoolean(CLICKED, false);
        if(getActivity() != null){
            ((MainActivityFragmentListener)getActivity()).OnMessageFromMainActivityFragment(bundle);
        }
    }


    public interface MainActivityFragmentListener{
        public void OnMessageFromMainActivityFragment(Bundle bundle);
    }


}
