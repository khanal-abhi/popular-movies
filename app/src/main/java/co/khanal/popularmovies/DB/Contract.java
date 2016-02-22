package co.khanal.popularmovies.DB;

/**
 * Created by abhi on 2/21/16.
 */
public class Contract {

    public static final String DB_NAME = "pop_movies.db";
    public static final int DB_VERSION = 1;

    public static class Movies{

        public static final String TABLE_NAME = "movies";
        public static final String ID = "id";
        public static final String ORIGINAL_TITLE = "original_title";
        public static final String IMAGE = "image";
        public static final String SYNOPSIS = "synopsis";
        public static final String USER_RATING = "user_rating";
        public static final String RELEASE_DATE = "release_date";

        public static final String CREATE_TABLE_STATEMENT = String.format(
                "CREATE TABLE IF NOT EXISTS %s ( %s INTEGER PRIMARY KEY ," +
                        "%s TEXT," +
                        "%s BLOB," +
                        "%s TEXT," +
                        "%s REAL," +
                        "%s TEXT);",
                TABLE_NAME, ID, ORIGINAL_TITLE, IMAGE, SYNOPSIS, USER_RATING, RELEASE_DATE
        );

        public static final String DROP_TABLE_STATEMENT = String.format(
                "DROP TABLE IF EXITS %s;",
                TABLE_NAME
        );


    }

    public static class Trailers{

        public static final String TABLE_NAME = "trailers";
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String KEY = "key";
        public static final String MOVIE_ID = "movie_id";

        public static final String CREATE_TABLE_STATEMENT = String.format(
                "CREATE TABLE IF NOT EXISTS %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT ," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s INTEGER," +
                        "FOREIGN KEY(%s) REFERENCES %s(%s));",
                TABLE_NAME, ID, NAME, KEY, MOVIE_ID, MOVIE_ID, Movies.TABLE_NAME, Movies.ID
        );

        public static final String DROP_TABLE_STATEMENT = String.format(
                "DROP TABLE IF EXITS %s;",
                TABLE_NAME
        );
    }

    public static class Reviews{

        public static final String TABLE_NAME = "reviews";
        public static final String ID = "id";
        public static final String AUTHOR = "author";
        public static final String CONTENT = "content";
        public static final String URL = "url";
        public static final String MOVIE_ID = "movie_id";

        public static final String CREATE_TABLE_STATEMENT = String.format(
                "CREATE TABLE IF NOT EXISTS %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT ," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s INTEGER," +
                        "FOREIGN KEY(%s) REFERENCES %s(%s));",
                TABLE_NAME, ID, AUTHOR, CONTENT, URL, MOVIE_ID, MOVIE_ID, Movies.TABLE_NAME, Movies.ID
        );

        public static final String DROP_TABLE_STATEMENT = String.format(
                "DROP TABLE IF EXITS %s;",
                TABLE_NAME
        );
    }
}
