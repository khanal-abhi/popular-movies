package co.khanal.popularmovies.JsonHelpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by abhi on 2/21/16.
 */
public class JsonFetcher {

    final static String FETCH_MOVIES_BASED_ON_POPULARITY = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=d4cb1a5611095189dc11c7b9781eac71";
    final static String FETCH_MOVIES_BASED_ON_RATNGS = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=d4cb1a5611095189dc11c7b9781eac71";

    public static JSONArray popularMovies() throws IOException, JSONException{
        JSONArray moviesArray = null;

        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(FETCH_MOVIES_BASED_ON_POPULARITY).openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

        String jsonString = "";
        StringBuffer stringBuffer = new StringBuffer();

        while((jsonString = bufferedReader.readLine()) != null)
            stringBuffer.append(jsonString);

        JSONObject moviesObject = new JSONObject(stringBuffer.toString());
        moviesArray = moviesObject.getJSONArray("results");

        return moviesArray;
    }

    public static JSONArray highlyRatedMovies() throws IOException, JSONException{
        JSONArray moviesArray = null;

        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(FETCH_MOVIES_BASED_ON_RATNGS).openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

        String jsonString = "";
        StringBuffer stringBuffer = new StringBuffer();

        while((jsonString = bufferedReader.readLine()) != null)
            stringBuffer.append(jsonString);

        JSONObject moviesObject = new JSONObject(stringBuffer.toString());
        moviesArray = moviesObject.getJSONArray("results");

        return moviesArray;
    }
}
