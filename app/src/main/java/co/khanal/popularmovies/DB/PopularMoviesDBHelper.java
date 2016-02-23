package co.khanal.popularmovies.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by abhi on 2/21/16.
 */
public class PopularMoviesDBHelper extends SQLiteOpenHelper {


    public PopularMoviesDBHelper(Context context) {
        super(context, Contract.DB_NAME, null, Contract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contract.Movies.CREATE_TABLE_STATEMENT);
        db.execSQL(Contract.Reviews.CREATE_TABLE_STATEMENT);
        db.execSQL(Contract.Trailers.CREATE_TABLE_STATEMENT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Contract.Movies.DROP_TABLE_STATEMENT);
        db.execSQL(Contract.Reviews.DROP_TABLE_STATEMENT);
        db.execSQL(Contract.Trailers.DROP_TABLE_STATEMENT);
    }
}
