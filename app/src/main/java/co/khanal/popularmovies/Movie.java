package co.khanal.popularmovies;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abhi on 2/18/16.
 */
public class Movie implements Parcelable{
    private String originalTitle;
    private Uri imageUri;
    private String synopsis;
    private double userRating;
    private String releaseDate;

    protected Movie(Parcel in) {
        originalTitle = in.readString();
        imageUri = in.readParcelable(Uri.class.getClassLoader());
        synopsis = in.readString();
        userRating = in.readDouble();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
//
//            dest.writeString(originalTitle);
//            dest.writeString(imageUri.toString());
//            dest.writeString(synopsis);
//            dest.writeDouble(userRating);
//            dest.writeString(releaseDate);


            Movie movie = new Movie();
            movie.setOriginalTitle(in.readString());
            movie.setImageUri(in.readString());
            movie.setSynopsis(in.readString());
            movie.setUserRating(in.readDouble());
            movie.setReleaseDate(in.readString());

            return movie;
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

        dest.writeString(originalTitle);
        dest.writeString(imageUri.toString());
        dest.writeString(synopsis);
        dest.writeDouble(userRating);
        dest.writeString(releaseDate);

    }


}
