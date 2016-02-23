package co.khanal.popularmovies.DB;

import android.test.AndroidTestCase;

import junit.framework.TestCase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.khanal.popularmovies.Movie;
import co.khanal.popularmovies.Review;

/**
 * Created by abhi on 2/22/16.
 */
public class ReviewModelTest extends AndroidTestCase {

    ReviewModel reviewModel;
    MovieModel movieModel;
    Movie movie1;
    Review review1;
    Review review2;
    List<Review> reviews;

    @Override
    protected void setUp() throws Exception {
        movie1 = new Movie(
                43,
                "hello world",
                new byte[] {'a', 'b', 'c'},
                "",
                7.5,
                "2016"
        );

        review1 = new Review(
                "123",
                "Abhi Khanal",
                "Hello World",
                "Hello.World",
                43
        );

        review2 = new Review(
                "1234",
                "Munni Baba",
                "Hello World",
                "Hello.World",
                43
        );

        movieModel = new MovieModel(getContext());
        movieModel.addMovie(movie1);
        reviewModel = new ReviewModel(getContext());
        reviews = new ArrayList<>();
    }

    @Override
    protected void tearDown() throws Exception {
        reviewModel.deleteAll();
        movieModel.deleteAll();
    }

    public void testGetReview() throws Exception {

    }

    public void testAddReview() throws Exception {
        assertEquals(review1.getId(), reviewModel.addReview(review1).getId());
    }

    public void testDeleteReview() throws Exception {
        reviewModel.addReview(review2);
        reviewModel.deleteReview(review2);
        assertNull(reviewModel.getReview(review2.getId()));
    }

    public void testDeleteAll() throws Exception {
        reviewModel.addReview(review1);
        reviewModel.addReview(review2);
        reviewModel.deleteAll();
        assertEquals(0, reviewModel.getReviews().size());
    }

    public void testUpdateReview() throws Exception {
        reviews.add(review1);
        reviews.add(review2);
        reviewModel.addReviews(reviews);
        Thread.sleep(2000);
        assertEquals(reviews.size(), reviewModel.getReviews().size());
    }

    public void testUniqueId() throws Exception{
        reviewModel.addReview(review1);
        try {
            reviewModel.addReview(review1);
            fail();
        } catch (SQLException e){
            assertTrue(true);
        }
    }
}