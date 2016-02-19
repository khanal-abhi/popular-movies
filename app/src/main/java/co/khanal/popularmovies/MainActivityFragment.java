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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public static final String GRID_VIEW_FIRST_VISIBLE_ITEM = "grid_view_first_visible_item";
    private static final String GRID_VIEW_STATE = "grid_view_state";

    private GridView gridView;

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

        if(savedInstanceState != null){
            gridView.onRestoreInstanceState(savedInstanceState.getParcelable(GRID_VIEW_STATE));
            Log.v(GRID_VIEW_STATE, String.valueOf(savedInstanceState.getInt(GRID_VIEW_FIRST_VISIBLE_ITEM)));

//            if(parcelable != null){
//                gridView.onRestoreInstanceState(parcelable);
////                gridView.smoothScrollToPosition(savedInstanceState.getInt(GRID_VIEW_FIRST_VISIBLE_ITEM));
////                gridView.setSelection(savedInstanceState.getInt(GRID_VIEW_FIRST_VISIBLE_ITEM));
//
//            }
        }


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                Movie movie = (Movie) parent.getAdapter().getItem(position);
                intent.putExtra(Movie.MOVIE_KEY, movie);
                startActivity(intent);
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

        new LoadDataFromApi().execute(uri.toString());
    }

    private void updateView(Movie[] movies){
        try {
            gridView.setAdapter(new GridViewAdapter(getContext(), R.layout.grid_item, movies));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        Parcelable parcelable = gridView.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable(GRID_VIEW_STATE, parcelable);
        bundle.putInt(GRID_VIEW_FIRST_VISIBLE_ITEM, gridView.getFirstVisiblePosition());
        Log.v(GRID_VIEW_STATE, String.valueOf(gridView.getFirstVisiblePosition()));
        onSaveInstanceState(bundle);
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public class LoadDataFromApi extends AsyncTask<String, Void, Movie[]>{

        @Override
        protected Movie[] doInBackground(String... params) {

            String jsonMovies = "";
            Movie[] movies;

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try{
                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();

                if(inputStream == null){
                    return null;
                }

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    stringBuffer.append(line);
                }

                if(stringBuffer.length() == 0){
                    stringBuffer = null;
                }

                jsonMovies = stringBuffer.toString();

            } catch (Exception e){
                e.printStackTrace();
            }
            finally {
                if(bufferedReader != null){
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (httpURLConnection != null){
                    httpURLConnection.disconnect();
                }
                movies = new Movie[0];
                try {
                    movies = parseJsonMovies(jsonMovies) ;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return movies;
            }


        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            super.onPostExecute(movies);
            updateView(movies);
        }
    }

    public Movie[] parseJsonMovies(String jsonMovies) throws JSONException{
        Movie[] movies;
        JSONObject moviesJsonObject = new JSONObject(jsonMovies);
        JSONArray moviesArray = moviesJsonObject.getJSONArray("results");
        movies = new Movie[moviesArray.length()];
        for(int i = 0; i < moviesArray.length(); i++){
            movies[i] = new Movie();
            movies[i].setOriginalTitle(moviesArray.getJSONObject(i).getString("original_title"));
            movies[i].setImageUri("http://image.tmdb.org/t/p/w185/" + moviesArray.getJSONObject(i).getString("poster_path"));
            movies[i].setSynopsis(moviesArray.getJSONObject(i).getString("overview"));
            movies[i].setReleaseDate(moviesArray.getJSONObject(i).getString("release_date"));
            movies[i].setUserRating(moviesArray.getJSONObject(i).getDouble("vote_average"));
        }

        return movies;
    }


}
