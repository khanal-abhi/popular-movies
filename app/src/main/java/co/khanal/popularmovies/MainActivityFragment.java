package co.khanal.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
public class MainActivityFragment extends Fragment implements LoadDataFromApi.MoviesReciever{

    public static final String GRID_VIEW_FIRST_VISIBLE_ITEM = "grid_view_first_visible_item";
    private static final String GRID_VIEW_STATE = "grid_view_state";

    private MainActivityFragmentListener activity;
    private Movie[] movies;
    private GridView gridView;
    private GridViewAdapter gridViewAdapter;

    public MainActivityFragment() {
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        activity = (MainActivityFragmentListener) getActivity();
        Bundle bundle = new Bundle();
        if(gridViewAdapter != null){
            Movie movie = gridViewAdapter.getItem(0);
            bundle.putParcelable(Movie.MOVIE_KEY, (Movie)gridViewAdapter.getItem(0));
            activity.OnMessageFromMainActivityFragment(bundle);
        }
        super.onActivityCreated(savedInstanceState);
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
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.main_fragment_gridview);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FragmentManager fragmentManager = getFragmentManager();
                Fragment detailsFragment = fragmentManager.findFragmentById(R.id.details_fragment);
                if (detailsFragment != null) {
                    Bundle bundle = new Bundle();
                    Movie movie = (Movie) parent.getAdapter().getItem(position);
                    bundle.putParcelable(Movie.MOVIE_KEY, movie);
                    activity.OnMessageFromMainActivityFragment(bundle);

                } else {
                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    Movie movie = (Movie) parent.getAdapter().getItem(position);
                    intent.putExtra(Movie.MOVIE_KEY, movie);
                    startActivity(intent);
                }
            }
        });

        setupLoad();

        return rootView;
    }



    private void setupLoad(){
        final String SORT_METHOD = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getString(R.string.pref_sort_by_key), getString(R.string.pref_sort_by_default_value));

        Uri uri = Uri.parse("http://api.themoviedb.org/3/discover/movie").buildUpon()
                .appendQueryParameter(getString(R.string.sort_by_param), SORT_METHOD)
                .appendQueryParameter(getString(R.string.api_key_param), getString(R.string.api_key))
                .build();

        Log.v("TAG", uri.toString());

        new LoadDataFromApi(getFragmentManager().findFragmentById(R.id.main_fragment)).execute(uri.toString());
    }

    @Override
    public void onRecieveMovies(Movie[] movies) {
        this.movies = movies;

        gridViewAdapter = new GridViewAdapter(getContext(), R.layout.grid_item, movies);
        gridView.setAdapter(gridViewAdapter);

        Bundle bundle = new Bundle();
        bundle.putParcelable(Movie.MOVIE_KEY,movies[0]);
        activity.OnMessageFromMainActivityFragment(bundle);
    }


    public interface MainActivityFragmentListener{
        public void OnMessageFromMainActivityFragment(Bundle bundle);
    }


}
