package co.khanal.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements Movie.MovieProvider,
        MainActivityFragment.MainActivityFragmentListener,
        DetailActivityFragment.DetailActivityListener {

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
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        // need to check for both the fragments, if the fragment is null then don't need to put it in
        MainActivityFragment mainActivityFragment = (MainActivityFragment)getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        DetailActivityFragment detailActivityFragment = (DetailActivityFragment)getSupportFragmentManager().findFragmentById(R.id.details_fragment);
        if(mainActivityFragment != null)
            getSupportFragmentManager().putFragment(outState, MAIN_FRAG_KEY, mainActivityFragment);
        if(detailActivityFragment != null)
            getSupportFragmentManager().putFragment(outState, DETAIL_FRAG_KEY, detailActivityFragment);

    }

    @Override
    public Movie getMovie() {
        return movie;
    }

    @Override
    public void OnMessageFromDetailActivityFragment(Bundle bundle) {

    }

    @Override
    public void OnMessageFromMainActivityFragment(Bundle bundle) {
        Toast.makeText(getApplicationContext(), "On Message from main", Toast.LENGTH_SHORT).show();
        movie = bundle.getParcelable(Movie.MOVIE_KEY);
        boolean clicked = bundle.getBoolean(MainActivityFragment.CLICKED);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment detailActivityFragment = fragmentManager.findFragmentById(R.id.details_fragment);
        if(detailActivityFragment != null && movie != null) {
            ((DetailActivityFragment) detailActivityFragment).loadMovie(movie, clicked);
        } else if (movie != null && bundle.getBoolean(MainActivityFragment.CLICKED)){
            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            intent.putExtra(Movie.MOVIE_KEY, movie);
            startActivity(intent);
        }
    }
}
