package co.khanal.popularmovies;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 * Created by abhi on 2/20/16.
 */
@RunWith(AndroidJUnit4.class)
public class PopularMoviesBlackBoxTest {

    @Rule public final ActivityRule<MainActivity> main = new ActivityRule<>(MainActivity.class);

//    This will test the main activity / fragment
    @Test
    public void shouldBeAbleToLoadMainScreen(){
        onView(withText("Pop Movies")).check(matches(isDisplayed()));
    }

    @Test
    public void shouldNotSeeTheMovieDetailsScreen(){
        onView(withText("Movie Details")).check(doesNotExist());
    }

    @Test
    public void shouldHaveFilterActionButtonsOnTheTopButNotSettings(){
        onView((withId(R.id.popularity))).check(matches(isDisplayed()));
        onView((withId(R.id.ratings))).check(matches(isDisplayed()));
        onView(withText("Settings")).check(doesNotExist());
    }

    @Test
    public void shouldHave20GridItems(){
        onData(anything()).inAdapterView(withId(R.id.main_fragment)).atPosition(19).perform(click());
        onView(withText("Movie Details")).check(matches(isDisplayed()));
    }

//    This will test the details activity / details fragment on the dual pane view
    @Test
    public void shouldLoadMovieDetails(){
        onView(withId(R.id.main_fragment)).perform(click());
        onView(withText("Movie Details")).check(matches(isDisplayed()));
    }

    @Test
    public void detailsScreenShouldHaveUpActionButtonButNotSettings(){
        shouldLoadMovieDetails();
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.popularity)).check(doesNotExist());
        onView(withId(R.id.ratings)).check(doesNotExist());
        onView(withText("Settings")).check(doesNotExist());
    }

    @Test
    public void detailsScreenShouldHaveAllTheRequiredViews(){
        shouldLoadMovieDetails();
        onView(withId(R.id.movie_title)).check(matches(isDisplayed()));
        onView(withId(R.id.poster)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_year)).check(matches(isDisplayed()));
        onView(withId(R.id.movie_rating)).check(matches(isDisplayed()));
        onView(withId(R.id.add_to_favorite)).check(matches(isDisplayed()));
        onView(withId(R.id.synopsis)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void detailsScreenShouldHaveTrailersAndReviews(){
        shouldLoadMovieDetails();
        onView(withText("Trailers:")).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withText("Reviews:")).perform(scrollTo()).check(matches(isDisplayed()));
    }

//    This will integrate the interaction between main and details
    @Test
    public void shouldLoadTheCorrectMovieOnDetail(){
        onData(anything()).inAdapterView(withId(R.id.main_fragment)).atPosition(2)
                .onChildView(withId(R.id.grid_item_poster)).perform(click());
        onView(withText("Movie Details")).check(matches(isDisplayed()));
        onView(withText("Spectre")).check(matches(isDisplayed()));
    }

}