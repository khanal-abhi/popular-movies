package co.khanal.popularmovies.DB;

import android.test.AndroidTestCase;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import co.khanal.popularmovies.Movie;
import co.khanal.popularmovies.Trailer;

/**
 * Created by abhi on 2/22/16.
 */
public class TrailerModelTest extends AndroidTestCase {

    TrailerModel trailerModel;
    MovieModel movieModel;
    Movie movie1;
    Trailer trailer1;
    Trailer trailer2;
    List<Trailer> trailers;

    public void setUp() throws Exception {

        movie1 = new Movie(
                43,
                "hello world",
                new byte[] {'a', 'b', 'c'},
                "",
                7.5,
                "2016"
        );

        trailer1 = new Trailer(
                123,
                "Abhi Khanal",
                "hiyaaaaa",
                43
        );

        trailer2 = new Trailer(
                112323,
                "Munni Baba",
                "Hello Derpette",
                43
        );

        movieModel = new MovieModel(getContext());
        movieModel.addMovie(movie1);
        trailerModel = new TrailerModel(getContext());
        trailers = new ArrayList<>();
    }

    public void tearDown() throws Exception {
        trailerModel.deleteAll();
        movieModel.deleteAll();
    }

    public void testGetTrailer() throws Exception {

    }

    public void testAddTrailer() throws Exception {
        assertEquals(trailer1.getId(), trailerModel.addTrailer(trailer1).getId());
    }

    public void testDeleteTrailer() throws Exception {
        trailerModel.addTrailer(trailer2);
        trailerModel.deleteTrailer(trailer2);
        assertNull(trailerModel.getTrailer(trailer2.getId()));
    }

    public void testDeleteAll() throws Exception {
        trailerModel.addTrailer(trailer2);
        trailerModel.addTrailer(trailer1);
        trailerModel.deleteAll();
        assertEquals(0, trailerModel.getTrailers().size());

    }

    public void testGetTrailers() throws Exception {
        trailers.add(trailer1);
        trailers.add(trailer2);
        trailerModel.addTrailers(trailers);
        Thread.sleep(2000);
        assertEquals(trailers.size(), trailerModel.getTrailers().size());
    }
}