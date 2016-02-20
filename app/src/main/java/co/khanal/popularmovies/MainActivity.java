package co.khanal.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements Movie.CanGetMovie,
        MainActivityFragment.MainActivityFragmentListener,
        DetailActivityFragment.DetailActivityListener {

    private Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        movie = bundle.getParcelable(Movie.MOVIE_KEY);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment detailActivityFragment = fragmentManager.findFragmentById(R.id.details_fragment);
        if(detailActivityFragment != null && movie != null) {
            ((DetailActivityFragment) detailActivityFragment).loadMovie(movie);
        }
    }
}
