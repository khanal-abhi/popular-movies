package co.khanal.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

public class DetailActivity extends AppCompatActivity implements Movie.CanGetMovie {

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        this.movie = (Movie) getIntent().getExtras().getParcelable(Movie.MOVIE_KEY);

    }

//    FragmentManager fragmentManager = getSupportFragmentManager();
//    Fragment main = fragmentManager.findFragmentById(R.id.main_fragment);
//    if(main != null){
//        finish();
//    }


    @Override
    public Movie getMovie() {
        return movie;
    }
}
