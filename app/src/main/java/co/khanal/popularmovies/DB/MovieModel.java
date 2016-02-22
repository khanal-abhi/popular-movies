package co.khanal.popularmovies.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

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

    public void open() throws SQLException{
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public Movie getMovie(long id){
        Cursor cursor = db.query(Contract.Movies.TABLE_NAME, COLUMN_FILTERS, Contract.Movies.ID +
                "=" + id, null, null, null, null);
        if(cursor != null){
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return new Movie(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getBlob(2),
                    cursor.getString(3),
                    cursor.getDouble(4),
                    cursor.getString(5)
            );
        }
        return null;
    }

    public long addMovie(Movie movie){
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        return 0;
    };
}
