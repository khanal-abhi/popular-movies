package co.khanal.popularmovies.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.khanal.popularmovies.Trailer;

/**
 * Created by abhi on 2/22/16.
 */
public class TrailerModel {

    private PopularMoviesDBHelper dbHelper;
    private SQLiteDatabase db;
    private final String[] COLUMN_FILTERS = {
            Contract.Trailers.ID,
            Contract.Trailers.NAME,
            Contract.Trailers.KEY,
            Contract.Trailers.MOVIE_ID
    };

    public TrailerModel (Context context){
        dbHelper = new PopularMoviesDBHelper(context);
    }

    public Trailer getTrailer(String id){
        db = dbHelper.getReadableDatabase();
        Trailer trailer = null;
        Cursor cursor = db.query(Contract.Trailers.TABLE_NAME, COLUMN_FILTERS, Contract.Trailers.ID +
                "=\"" + id + "\"", null, null, null, null);
        if(cursor != null){
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            trailer = new Trailer(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getLong(3)
            );
            cursor.close();
        }
        dbHelper.close();
        return trailer;
    }

    public List<Trailer> getTrailersForMovie(long movie_id){
        List<Trailer> trailers = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(Contract.Trailers.TABLE_NAME, COLUMN_FILTERS, Contract.Reviews.MOVIE_ID + "=" + movie_id,
                null,
                null,
                null,
                null);
        if(cursor != null){
            if(cursor.getCount() != 0){
                cursor.moveToFirst();
                do {
                    trailers.add(new Trailer(
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getLong(3)
                    ));
                } while (cursor.moveToNext());
            }
        }
        return trailers;
    }

    public Trailer addTrailer(Trailer trailer) throws SQLException {
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = getContentValues(trailer);
        long id = db.insert(Contract.Trailers.TABLE_NAME, null, contentValues);
        if(id == -1)
            throw new SQLException("returned id was -1");

        Cursor cursor = db.query(Contract.Trailers.TABLE_NAME, COLUMN_FILTERS, Contract.Trailers.ID+"="+id,
                null,
                null,
                null,
                null);

        cursor.close();
        dbHelper.close();
        return getTrailer(trailer.getId());
    }

    public void deleteTrailer(Trailer trailer){
        db = dbHelper.getWritableDatabase();
        db.delete(Contract.Trailers.TABLE_NAME, Contract.Trailers.ID + "=" + trailer.getId(), null);
        db.close();
    }

    public void deleteAll(){
        db = dbHelper.getWritableDatabase();
        db.delete(Contract.Trailers.TABLE_NAME, "1", null);
        db.close();
    }

    public void updateTrailer(Trailer trailer){
        db = dbHelper.getWritableDatabase();
        ContentValues contentValues = getContentValues(trailer);
        db.update(Contract.Trailers.TABLE_NAME, contentValues, Contract.Trailers.ID+"="+trailer.getId(), null);
        db.close();
    }

    public List<Trailer> getTrailers(){
        List<Trailer> trailers = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * FROM " + Contract.Trailers.TABLE_NAME, null);
        if(cursor != null){
            if(cursor.getCount() == 0)
                return trailers;
            cursor.moveToFirst();
            do {
                trailers.add(new Trailer(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getLong(3)
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }
        dbHelper.close();
        return trailers;
    }

    public List<Trailer> addTrailers (List<Trailer> trailers) throws SQLException{
        List<Trailer> addedTrailers = new ArrayList<>();
        for (Trailer trailer : trailers){
            addedTrailers.add(addTrailer(trailer));
        }
        return addedTrailers;
    }


    public static ContentValues getContentValues(Trailer trailer){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.Trailers.ID, trailer.getId());
        contentValues.put(Contract.Trailers.NAME, trailer.getName());
        contentValues.put(Contract.Trailers.KEY, trailer.getKey());
        contentValues.put(Contract.Trailers.MOVIE_ID, trailer.getMovie_id());
        return contentValues;
    }
}
