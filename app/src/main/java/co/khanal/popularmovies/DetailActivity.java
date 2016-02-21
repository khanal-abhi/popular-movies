package co.khanal.popularmovies;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class DetailActivity extends AppCompatActivity implements Movie.MovieProvider {

    private static final String DETAIL_FRAG_KEY = "detail_frag_key";
    private Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Keeps prompting me that the statement below may throw an NullPointerException,
//        but it's for up navigation.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        movie = getIntent().getExtras().getParcelable(Movie.MOVIE_KEY);

        if(savedInstanceState != null){
            movie = savedInstanceState.getParcelable(Movie.MOVIE_KEY);
            getSupportFragmentManager().getFragment(savedInstanceState,DETAIL_FRAG_KEY);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

//        outState is ready for consumption

        outState.putParcelable(Movie.MOVIE_KEY, movie);
        FragmentManager fragmentManager = getSupportFragmentManager();
        DetailActivityFragment detailActivityFragment = (DetailActivityFragment)fragmentManager.findFragmentById(R.id.details_fragment);
        if(detailActivityFragment != null){
            fragmentManager.putFragment(outState, DETAIL_FRAG_KEY, detailActivityFragment);
        }
    }

    @Override
    public Movie getMovie() {
        return movie;
    }
}
