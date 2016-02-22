package co.khanal.popularmovies;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by abhi on 2/18/16.
 * This is the Movie class that implements Parcelable for data transfer to different activity /
 * fragment life cycle stages. It takes in long id, String originalTitle, String imageUri,
 * String synopsis, double userRating, String releaseDate for its construction. It also defines an
 * implementation MovieProvider that defines getMovie method.
 **/
public class Movie implements Parcelable{

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String IMAGE_URI = "poster_path";
    private static final String SYNOPSIS = "overview";
    private static final String USER_RATING = "vote_average";
    private static final String RELEASE_DATE = "release_date";
    private static final String BYTES_ARRAY = "bytes_Array";


    public static final String MOVIE_KEY = "movie";

    private long id;
    private String originalTitle;
    private Uri imageUri;
    private String synopsis;
    private double userRating;
    private String releaseDate;
    private byte[] bytesArray;

    private List<Review> reviews;
    private List<Trailer> trailers;

    public static String getID() {
        return ID;
    }

    public static String getTITLE() {
        return TITLE;
    }

    public static String getSYNOPSIS() {
        return SYNOPSIS;
    }

    public static String getMovieKey() {
        return MOVIE_KEY;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setBytesArray(byte[] bytesArray) {
        this.bytesArray = bytesArray;
    }

    public static Creator<Movie> getCREATOR() {
        return CREATOR;
    }

    public List<Review> getReviews() {

        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }

    public long getId(){
        return id;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {

            Bundle bundle = in.readBundle();

            return new Movie(
                    bundle.getLong(ID),
                    bundle.getString(TITLE),
                    bundle.getString(IMAGE_URI),
                    bundle.getString(SYNOPSIS),
                    bundle.getDouble(USER_RATING),
                    bundle.getString(RELEASE_DATE),
                    bundle.getByteArray(BYTES_ARRAY)
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

    public Uri getImageUri() {
        return imageUri;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public double getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Movie(long id, String originalTitle, String imageUri, String synopsis, double userRating, String releaseDate) {
        this.id = id;
        this.originalTitle = originalTitle == null ? "UNKNOWN TITLE" : originalTitle;
        this.imageUri = imageUri == null ? Uri.parse("https://image.freepik.com/free-icon/sad-emoticon-square-face_318-58601.png") : Uri.parse(imageUri);
        this.synopsis = synopsis == null ? "No data found." : synopsis;
        this.userRating = userRating == 0 ? 5 : userRating;
        this.releaseDate = releaseDate == null ? "" : releaseDate;
        this.bytesArray = null;
    }

    public Movie(long id, String originalTitle, byte[] image, String synopsis, double userRating, String releaseDate) {
        this.id = id;
        this.originalTitle = originalTitle == null ? "UNKNOWN TITLE" : originalTitle;
        this.synopsis = synopsis == null ? "No data found." : synopsis;
        this.userRating = userRating == 0 ? 5 : userRating;
        this.releaseDate = releaseDate == null ? "" : releaseDate;
        this.bytesArray = image;
        this.imageUri = null;
    }

    public Movie(long id, String originalTitle, String imageUri, String synopsis, double userRating, String releaseDate, byte[] bytesArray) {
        this.id = id;
        this.originalTitle = originalTitle == null ? "UNKNOWN TITLE" : originalTitle;
        this.imageUri = imageUri == null ? Uri.parse("https://image.freepik.com/free-icon/sad-emoticon-square-face_318-58601.png") : Uri.parse(imageUri);
        this.synopsis = synopsis == null ? "No data found." : synopsis;
        this.userRating = userRating == 0 ? 5 : userRating;
        this.releaseDate = releaseDate == null ? "" : releaseDate;
        this.bytesArray = bytesArray;
    }

    @Override
    public String toString() {
        return "id: " + id +
                "\noriginal_title: " + originalTitle +
                "\nimage_uri: " + imageUri +
                "\nsynopsis: " + synopsis +
                "\nuser_rating: " + userRating +
                "\nrelease_date: " + releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        Bundle bundle = new Bundle();
        bundle.putLong(ID, id);
        bundle.putString(TITLE, originalTitle);
        bundle.putString(IMAGE_URI, imageUri.toString());
        bundle.putString(SYNOPSIS, synopsis);
        bundle.putDouble(USER_RATING, userRating);
        bundle.putString(RELEASE_DATE, releaseDate);

        dest.writeBundle(bundle);

    }

    public static Movie[] parseJsonMovies(String jsonMovies, String IMAGE_BASE_URI) throws JSONException {
        Movie[] movies;
        JSONObject moviesJsonObject = new JSONObject(jsonMovies);
        JSONArray moviesArray = moviesJsonObject.getJSONArray("results");
        movies = new Movie[moviesArray.length()];
        for(int i = 0; i < moviesArray.length(); i++){
            JSONObject movie = moviesArray.getJSONObject(i);
            movies[i] = new Movie(
                    movie.getLong(ID),
                    movie.getString(TITLE),
                    IMAGE_BASE_URI + movie.getString(IMAGE_URI),
                    movie.getString(SYNOPSIS),
                    movie.getDouble(USER_RATING),
                    movie.getString(RELEASE_DATE)
            );

        }

        return movies;
    }

    public interface MovieProvider {
        Movie getMovie();
    }

    public byte[] getBytesArray() {
        return bytesArray;
    }

    @Override
    public boolean equals(Object o) {
        return id == ((Movie) o).getId() &&
                originalTitle == ((Movie) o).getOriginalTitle() &&
                bytesArray == ((Movie) o).getBytesArray() &&
                imageUri == ((Movie) o).imageUri &&
                synopsis == ((Movie) o).getSynopsis() &&
                userRating == ((Movie) o).getUserRating() &&
                releaseDate == ((Movie) o).getReleaseDate();
    }
}
