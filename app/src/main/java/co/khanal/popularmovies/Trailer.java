package co.khanal.popularmovies;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhi on 2/22/16.
 */
public class Trailer implements Parcelable {

    public static final String TRAILER_KEY = "trailer_key";
    public static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    private String id;
    private String name;
    private String key;
    private long movie_id;

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String KEY = "key";
    private static final String MOVIE_ID = "movie_id";

    public Trailer() {
    }

    public Trailer(String id, String name, String key, long movie_id) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.movie_id = movie_id;
    }

    protected Trailer(Parcel in) {
        Bundle bundle = in.readBundle();
        id = bundle.getString(ID);
        name = bundle.getString(NAME);
        key = bundle.getString(KEY);
        movie_id = bundle.getLong(MOVIE_ID);
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            Bundle bundle = in.readBundle();
            return new Trailer(
                    bundle.getString(ID),
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        bundle.putString(ID, id);
        bundle.putString(NAME, name);
        bundle.putString(KEY, key);
        bundle.putLong(MOVIE_ID, movie_id);
    }

    public static List<Trailer> parseTrailersJson(JSONObject trailers) throws JSONException {
        List<Trailer> trailerList = new ArrayList<>();
        JSONArray resultsArray = trailers.getJSONArray("results");
        for(int i = 0; i < resultsArray.length(); i++){
            trailerList.add(new Trailer(
                    resultsArray.getJSONObject(i).getString(ID),
                    resultsArray.getJSONObject(i).getString(NAME),
                    YOUTUBE_BASE_URL + resultsArray.getJSONObject(i).getString(KEY),
                    resultsArray.getJSONObject(i).getLong(MOVIE_ID)
            ));
        }
        return trailerList;
    }
}
