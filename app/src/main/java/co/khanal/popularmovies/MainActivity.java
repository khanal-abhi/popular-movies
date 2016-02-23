package co.khanal.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements Movie.MovieProvider,
        MainActivityFragment.MainActivityFragmentListener {

    private static final String MAIN_FRAG_KEY = "main_frag_key";
    private static final String DETAIL_FRAG_KEY = "detail_frag_key";

    private Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState != null){
            getSupportFragmentManager().getFragment(savedInstanceState, MAIN_FRAG_KEY);
            getSupportFragmentManager().getFragment(savedInstanceState, DETAIL_FRAG_KEY);
            movie = savedInstanceState.getParcelable(Movie.MOVIE_KEY);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

//        Need to check for both the fragments, if the fragment is null then don't need to put it in
        MainActivityFragment mainActivityFragment = (MainActivityFragment)getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        DetailActivityFragment detailActivityFragment = (DetailActivityFragment)getSupportFragmentManager().findFragmentById(R.id.details_fragment);
        if(mainActivityFragment != null)
            getSupportFragmentManager().putFragment(outState, MAIN_FRAG_KEY, mainActivityFragment);
        if(detailActivityFragment != null)
            getSupportFragmentManager().putFragment(outState, DETAIL_FRAG_KEY, detailActivityFragment);

        outState.putParcelable(Movie.MOVIE_KEY, movie);

    }

    @Override
    public Movie getMovie() {
        return movie;
    }

    @Override
    public void OnMessageFromMainActivityFragment(Bundle bundle) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        DetailActivityFragment detailActivityFragment = (DetailActivityFragment)fragmentManager.findFragmentById(R.id.details_fragment);
        movie = bundle.getParcelable(Movie.MOVIE_KEY);
        boolean clicked = bundle.getBoolean(MainActivityFragment.CLICKED);
        if(movie.getBytesArray() != null)
            isFromDb(movie, clicked, detailActivityFragment);
        else isFromUrl(movie, clicked, detailActivityFragment);
    }

    public void isFromDb(Movie movie, boolean clicked, DetailActivityFragment fragment){
        if(fragment != null){
            fragment.loadMovieFromDB(movie);
        } else if (clicked){
            isClicked(movie);
        }
    }

    public void isFromUrl(Movie movie, boolean clicked, DetailActivityFragment fragment){
        if(fragment != null){
            fragment.loadMovie(movie);
        } else if (clicked){
            isClicked(movie);
        }
    }

    public void isClicked(Movie movie){
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        intent.putExtra(Movie.MOVIE_KEY, movie);
        startActivity(intent);
    }

}
