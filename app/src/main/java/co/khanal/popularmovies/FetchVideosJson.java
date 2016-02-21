package co.khanal.popularmovies;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by abhi on 2/21/16.
 */
public class FetchVideosJson extends AsyncTask<String, Void, JSONObject> {

    private FetchVideosJsonListener fetchVideosJsonListener;

    public FetchVideosJson(FetchVideosJsonListener listener){
        this.fetchVideosJsonListener = listener;
    }

    @Override
    protected JSONObject doInBackground(String... params) {

        final String STRING_URL = params[0];

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;

        JSONObject videosJson = null;

        try {
            URL url = new URL(STRING_URL);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            if(inputStream == null){
                return null;
            }

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer buffer = new StringBuffer();

            String jsonString = "";

            while ((jsonString = bufferedReader.readLine()) != null){
                buffer.append(jsonString);
            }

            if(buffer == null || buffer.length() == 0){
                return null;
            }

            videosJson = new JSONObject(buffer.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
    protected void onPostExecute(JSONObject videosJson) {
        super.onPostExecute(videosJson);

        fetchVideosJsonListener.onVideosFetched(videosJson);
    }

    public interface FetchVideosJsonListener{
        void onVideosFetched(JSONObject jsonObject);
    }
}
