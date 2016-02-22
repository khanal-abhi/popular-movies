package co.khanal;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by abhi on 2/21/16.
 * A basic Json Request async task that takes in a FetchJsonListener in its constructor and a
 * String URL in the execute method and returns a JSONObject to the FetchJsonListener via implemented
 * onFetchedJson.
 */

public class FetchJson extends AsyncTask<Void, Void, JSONObject> {

    protected Object listener;
    private String url;

    public FetchJson(Object listener, String url){
        this.listener = listener;
        this.url = url;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;

        JSONObject videosJson = null;

        try {
            URL url = new URL(this.url);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            if(inputStream == null){
                return null;
            }

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer buffer = new StringBuffer();

            String jsonString;

            while ((jsonString = bufferedReader.readLine()) != null){
                buffer.append(jsonString);
            }

            if(buffer.length() == 0){
                return null;
            }

            videosJson = new JSONObject(buffer.toString());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } finally {
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }
        }

        return videosJson;
    }

    @Override
    protected void onPostExecute(JSONObject fetchedJson) {
        super.onPostExecute(fetchedJson);
        ((FetchJsonListener)listener).onFetchedJson(fetchedJson);
    }

    public interface FetchJsonListener {
        void onFetchedJson(JSONObject fetchedJson);
    }
}
