package co.khanal.popularmovies.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.khanal.popularmovies.Movie;

/**
 * Created by abhi on 2/21/16.
 */
public class MovieModel {

    private PopularMoviesDBHelper dbHelper;
    private SQLiteDatabase db;
    private final String[] COLUMN_FILTERS = {
            Contract.Movies.ID,
            Contract.Movies.ORIGINAL_TITLE,
            Contract.Movies.IMAGE,
            Contract.Movies.SYNOPSIS,
            Contract.Movies.USER_RATING,
            Contract.Movies.RELEASE_DATE
    };

    public MovieModel(Context context){
        dbHelper = new PopularMoviesDBHelper(context);
    }

    public Movie getMovie(long id){
        db = dbHelper.getReadableDatabase();
        Movie movie = null;
        Cursor cursor = db.query(Contract.Movies.TABLE_NAME, COLUMN_FILTERS, Contract.Movies.ID +
                "=" + id, null, null, null, null);
        if(cursor != null){
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            movie = new Movie(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getBlob(2),
                    cursor.getString(3),
                    cursor.getDouble(4),
                    cursor.getString(5)
            );
            cursor.close();
        }
        dbHelper.close();
        return movie;
    }

    public Movie addMovie(Movie movie) throws SQLException{
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = getContentValues(movie);
        long id = db.insert(Contract.Movies.TABLE_NAME, null, contentValues);
        if(id == -1)
            throw new SQLException("returned id was -1");

        Cursor cursor = db.query(Contract.Movies.TABLE_NAME, COLUMN_FILTERS, Contract.Movies.ID+"="+id,
                null,
                null,
                null,
                null);

        cursor.close();
        dbHelper.close();
        return getMovie(id);
    }

    public void deleteMovie(Movie movie){
        db = dbHelper.getWritableDatabase();
        db.delete(Contract.Movies.TABLE_NAME, Contract.Movies.ID + "=" + movie.getId(), null);
        db.close();
    }

    public void deleteAll(){
        db = dbHelper.getWritableDatabase();
        db.delete(Contract.Movies.TABLE_NAME, "1", null);
        db.close();
    }

    public void updateMovie(Movie movie){
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = getContentValues(movie);
        db.update(Contract.Movies.TABLE_NAME, contentValues, Contract.Movies.ID+"="+movie.getId(), null);
        db.close();
    }

    public List<Movie> getMovies(){
        List<Movie> movies = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * FROM " + Contract.Movies.TABLE_NAME, null);
        if(cursor != null){
            if(cursor.getCount() == 0)
                return movies;
            cursor.moveToFirst();
            do {
                movies.add(new Movie(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getBlob(2),
                        cursor.getString(3),
                        cursor.getDouble(4),
                        cursor.getString(5)
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }
        dbHelper.close();
        return movies;
    }

    public List<Movie> addMovies (List<Movie> movies) throws SQLException{
        List<Movie> addedMovies = new ArrayList<>();
        for (Movie movie : movies){
            addedMovies.add(addMovie(movie));
        }
        return addedMovies;
    }

    public static byte[] getBytes(Bitmap bitmap){
        ByteBuffer byteBuffer = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(byteBuffer);
        return byteBuffer.array();
    }

    public static Bitmap getBitmap(byte[] bytes){
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static ContentValues getContentValues(Movie movie){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.Movies.ID, movie.getId());
        contentValues.put(Contract.Movies.ORIGINAL_TITLE, movie.getOriginalTitle());
        contentValues.put(Contract.Movies.IMAGE, movie.getBytesArray());
        contentValues.put(Contract.Movies.SYNOPSIS, movie.getSynopsis());
        contentValues.put(Contract.Movies.USER_RATING, movie.getUserRating());
        contentValues.put(Contract.Movies.RELEASE_DATE, movie.getReleaseDate());
        return contentValues;
    }
}
