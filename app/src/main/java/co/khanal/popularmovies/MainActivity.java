package co.khanal.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
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

    private static final String MAIN_FRAG_KEY = "main_frag_key";
    private static final String DETAIL_FRAG_KEY = "detail_frag_key";

    private Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }




    private void showDetail(Movie movie){
        Bundle bundle = new Bundle();
        bundle.putParcelable(Movie.MOVIE_KEY, movie);
        DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
        detailActivityFragment.setArguments(bundle);


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
        } else if (movie != null && bundle.getBoolean(MainActivityFragment.CLICKED)){
            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            intent.putExtra(Movie.MOVIE_KEY, movie);
            startActivity(intent);
        }
    }
}
