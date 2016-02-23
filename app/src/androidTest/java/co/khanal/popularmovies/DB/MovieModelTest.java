package co.khanal.popularmovies.DB;

import android.test.AndroidTestCase;

import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.khanal.popularmovies.Movie;

/**
 * Created by abhi on 2/22/16.
 */
public class MovieModelTest extends AndroidTestCase {

    MovieModel movieModel;
    Movie movie1;
    Movie movie2;
    Movie movie3;
    List<Movie> movies;

    public void setUp() throws Exception {
        super.setUp();
        movieModel = new MovieModel(getContext());

        movie1 = new Movie(
                43,
                "hello world",
                new byte[] {'a', 'b', 'c'},
                "",
                7.5,
                "2016"
        );
        movie2 = new Movie(
                27,
                "hello world 2",
                new byte[] {'a', 'b', 'c'},
                "",
                7.5,
                "2016"
        );
        movie3 = new Movie(
                45,
                "hello world 3",
                new byte[] {'a', 'b', 'c'},
                "",
                7.5,
                "2016"
        );

        movies = new ArrayList<>();
    }

    public void tearDown() throws Exception {
        movieModel.deleteAll();
    }

    public void testGetMovie() throws Exception {
        movieModel.addMovie(movie2);
        assertEquals(movie2.getId(), movieModel.getMovie(movie2.getId()).getId());
    }

    public void testAddMovie() throws Exception {
        assertEquals(movie1.getId(), movieModel.addMovie(movie1).getId());
    }

    public void testDeleteMovie() throws Exception {
        movieModel.addMovie(movie3);
        movieModel.deleteMovie(movie3);
        assertEquals(0, movieModel.getMovies().size());
    }

    public void testUpdateMovie() throws Exception {
        movieModel.addMovie(movie1);
        Movie movie1_updated = new Movie(
                movie1.getId(),
                "new title",
                movie1.getImageUri() == null ? "" : movie1.getImageUri().toString(),
                movie1.getSynopsis(),
                movie1.getUserRating(),
                movie1.getReleaseDate(),
                movie1.getBytesArray()
        );
        movieModel.updateMovie(movie1_updated);
        assertEquals(movie1_updated.getOriginalTitle(), movieModel.getMovie(movie1.getId()).getOriginalTitle());
    }

    public void testAddAndGetMovies() throws Exception {
        movies.add(movie1);
        movies.add(movie2);
        movieModel.addMovies(movies);
        Thread.sleep(2000);
        assertEquals(movies.size(), movieModel.getMovies().size());
    }

    public void testUniqueId() throws Exception {
        movieModel.addMovie(movie1);
        try {
            movieModel.addMovie(movie1);
            fail();
        } catch (SQLException e){
            assertTrue(true);
        }
    }

}