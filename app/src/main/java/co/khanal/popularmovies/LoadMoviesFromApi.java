package co.khanal.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by abhi on 2/19/16.
 * This is an AsyncTask subclass that takes a MoviesReceiver as a constructor paramter. It takes
 * String[] that contains BASE_URI, SORT_BY_PARAM, SORT_METHOD, API_KEY_PARAM, API_KEY,
 * IMAGE_BASE_URI for background execution and on task completion, calls the MoviesReceiver's
 * OnReceiveMovies method passing in the movies returned.
 */
public class LoadMoviesFromApi extends AsyncTask<String[], Void, Movie[]> {

    private MoviesReceiver receiver;

    public LoadMoviesFromApi(MoviesReceiver receiver){
        this.receiver = receiver;
    }

    @Override
    protected Movie[] doInBackground(String[]... params) {

//        Order is extremely important here!!!
//        BASE_URI, SORT_BY_PARAM, SORT_METHOD, API_KEY_PARAM, API_KEY, IMAGE_BASE_URI

        final String[] PARAMS = params[0];
        final String BASE_URI = PARAMS[0];
        final String SORT_BY_PARAM = PARAMS[1];
        final String SORT_METHOD = PARAMS[2];
        final String API_KEY_PARAM = PARAMS[3];
        final String API_KEY = PARAMS[4];
        final String IMAGE_BASE_URI = PARAMS[5];

        Uri uri = Uri.parse(BASE_URI).buildUpon()
                .appendQueryParameter(SORT_BY_PARAM, SORT_METHOD)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        String jsonMovies = "";
        Movie[] movies;

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;

        try{
            URL url = new URL(uri.toString());
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();

            if(inputStream == null){
                return null;
            }

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line);
            }
            if(stringBuffer.length() == 0){
                stringBuffer = null;
            }
            if(stringBuffer != null) {
                jsonMovies = stringBuffer.toString();
            }

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        finally {
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpURLConnection != null){
                httpURLConnection.disconnect();
            }
            movies = new Movie[0];
            try {
                movies = Movie.parseJsonMovies(jsonMovies, IMAGE_BASE_URI) ;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return movies;
    }


    @Override
    protected void onPostExecute(Movie[] movies) {
        receiver.onReceiveMovies(movies);
        super.onPostExecute(movies);
    }

    public interface MoviesReceiver{
        void onReceiveMovies(Movie[] movies);
    }
}
