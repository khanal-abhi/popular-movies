package co.khanal.popularmovies.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.khanal.popularmovies.Review;

/**
 * Created by abhi on 2/22/16.
 */
public class ReviewModel {

    private PopularMoviesDBHelper dbHelper;
    private SQLiteDatabase db;
    private final String[] COLUMN_FILTERS = {
            Contract.Reviews.ID,
            Contract.Reviews.AUTHOR,
            Contract.Reviews.CONTENT,
            Contract.Reviews.URL,
            Contract.Reviews.MOVIE_ID
    };

    public ReviewModel (Context context){
        dbHelper = new PopularMoviesDBHelper(context);
    }

    public Review getReview(long id){
        db = dbHelper.getReadableDatabase();
        Review review = null;
        Cursor cursor = db.query(Contract.Reviews.TABLE_NAME, COLUMN_FILTERS, Contract.Reviews.ID +
                "=" + id, null, null, null, null);
        if(cursor != null){
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            review = new Review(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getLong(4)
            );
            cursor.close();
        }
        dbHelper.close();
        return review;
    }

    public Review addReview(Review review) throws SQLException {
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = getContentValues(review);
        long id = db.insert(Contract.Reviews.TABLE_NAME, null, contentValues);
        if(id == -1)
            throw new SQLException("returned id was -1");

        Cursor cursor = db.query(Contract.Reviews.TABLE_NAME, COLUMN_FILTERS, Contract.Reviews.ID+"="+id,
                null,
                null,
                null,
                null);

        cursor.close();
        dbHelper.close();
        return getReview(id);
    }

    public void deleteReview(Review review){
        db = dbHelper.getWritableDatabase();
        db.delete(Contract.Reviews.TABLE_NAME, Contract.Reviews.ID + "=" + review.getId(), null);
        db.close();
    }

    public void deleteAll(){
        db = dbHelper.getWritableDatabase();
        db.delete(Contract.Reviews.TABLE_NAME, "1", null);
        db.close();
    }

    public void updateReview(Review review){
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = getContentValues(review);
        db.update(Contract.Reviews.TABLE_NAME, contentValues, Contract.Reviews.ID+"="+review.getId(), null);
        db.close();
    }

    public List<Review> getReviews(){
        List<Review> reviews = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * FROM " + Contract.Reviews.TABLE_NAME, null);
        if(cursor != null){
            cursor.moveToFirst();
            if(cursor.getCount() == 0)
                return reviews;
            do {
                reviews.add(new Review(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getLong(4)
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }
        dbHelper.close();
        return reviews;
    }

    public List<Review> addReviews (List<Review> reviews) throws SQLException{
        List<Review> addedReviews = new ArrayList<>();
        for (Review review : reviews){
            addedReviews.add(addReview(review));
        }
        return addedReviews;
    }


    public static ContentValues getContentValues(Review review){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.Reviews.ID, review.getId());
        contentValues.put(Contract.Reviews.AUTHOR, review.getAuthor());
        contentValues.put(Contract.Reviews.CONTENT, review.getContent());
        contentValues.put(Contract.Reviews.URL, review.getUrl());
        contentValues.put(Contract.Reviews.MOVIE_ID, review.getMovie_id());
        return contentValues;
    }
}
