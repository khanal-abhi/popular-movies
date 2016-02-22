package co.khanal.popularmovies;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abhi on 2/22/16.
 */
public class Trailer implements Parcelable {

    public static final String TRAILER_KEY = "trailer_key";

    private long id;
    private String name;
    private String key;
    private long movie_id;

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String KEY = "key";
    private static final String MOVIE_ID = "movie_id";

    public Trailer() {
    }

    public Trailer(long id, String name, String key, long movie_id) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.movie_id = movie_id;
    }

    protected Trailer(Parcel in) {
        id = in.readLong();
        name = in.readString();
        key = in.readString();
        movie_id = in.readLong();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            Bundle bundle = in.readBundle();
            return new Trailer(
                    bundle.getLong(ID),
                    bundle.getString(NAME),
                    bundle.getString(KEY),
                    bundle.getLong(MOVIE_ID)
            );
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(long movie_id) {
        this.movie_id = movie_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putLong(ID, id);
        bundle.putString(NAME, name);
        bundle.putString(KEY, key);
        bundle.putLong(MOVIE_ID, movie_id);
    }
}
