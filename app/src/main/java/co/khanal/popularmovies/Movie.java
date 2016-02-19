package co.khanal.popularmovies;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abhi on 2/18/16.
 */
public class Movie implements Parcelable{

    private static final String TITLE = "title";
    private static final String IMAGE_URI = "image_uri";
    private static final String SYNOPSIS = "synopsis";
    private static final String USER_RATING = "user_rating";
    private static final String RELEASE_DATE = "release_date";

    public static final String MOVIE_KEY = "movie";

    private String originalTitle;
    private Uri imageUri;
    private String synopsis;
    private double userRating;
    private String releaseDate;

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {

            Bundle bundle = in.readBundle();

            return new Movie(
                    bundle.getString(TITLE),
                    bundle.getString(IMAGE_URI),
                    bundle.getString(SYNOPSIS),
                    bundle.getDouble(USER_RATING),
                    bundle.getString(RELEASE_DATE)
            );


        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = Uri.parse(imageUri);
    }

    public void setImageUri(Uri uri){
        this.imageUri = uri;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Movie(String originalTitle, Uri imageUri, String synopsis, double userRating, String releaseDate) {
        this.originalTitle = originalTitle;
        this.imageUri = imageUri;
        this.synopsis = synopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    public Movie(String originalTitle, String imageUri, String synopsis, double userRating, String releaseDate) {
        this.originalTitle = originalTitle;
        this.imageUri = Uri.parse(imageUri);
        this.synopsis = synopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    public Movie(){

    }

    @Override
    public String toString() {
        return new String("title:" + originalTitle + " release date:" + releaseDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        Bundle bundle = new Bundle();
        bundle.putString(TITLE, originalTitle);
        bundle.putString(IMAGE_URI, imageUri.toString());
        bundle.putString(SYNOPSIS, synopsis);
        bundle.putDouble(USER_RATING, userRating);
        bundle.putString(RELEASE_DATE, releaseDate);

        dest.writeBundle(bundle);

    }


}
