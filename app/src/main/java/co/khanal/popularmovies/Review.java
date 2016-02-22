package co.khanal.popularmovies;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abhi on 2/22/16.
 */
public class Review implements Parcelable {

    public static final String REVIEW_KEY = "review_key";

    private static String ID = "id";
    private static String AUTHOR = "author";
    private static String CONTENT = "content";
    private static String URL = "url";
    private static String MOVIE_ID = "movie_id";

    private long id;
    private String author;
    private String content;
    private String url;
    private long movie_id;

    public Review(){

    }

    public Review(long id, String author, String content, String url, long movie_id) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
        this.movie_id = movie_id;
    }

    protected Review(Parcel in) {
        id = in.readLong();
        author = in.readString();
        content = in.readString();
        url = in.readString();
        movie_id = in.readLong();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            Bundle bundle = in.readBundle();
            return new Review(
                    bundle.getLong(ID),
                    bundle.getString(AUTHOR),
                    bundle.getString(CONTENT),
                    bundle.getString(URL),
                    bundle.getLong(MOVIE_ID)
            );
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        bundle.putString(AUTHOR, author);
        bundle.putString(CONTENT, content);
        bundle.putString(URL, url);
        bundle.putLong(MOVIE_ID, movie_id);
        dest.writeBundle(bundle);
    }
}
