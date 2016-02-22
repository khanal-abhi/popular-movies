package co.khanal.popularmovies;

import org.json.JSONObject;

import co.khanal.FetchJson;

/**
 * Created by abhi on 2/21/16.
 */
public class FetchJsonTrailers extends FetchJson {

    public FetchJsonTrailers(Object listener, String url) {
        super(listener, url);
    }

    @Override
    protected void onPostExecute(JSONObject fetchedJson) {
        ((FetchJsonTrailersListener)listener).onFetchedJsonTrailers(fetchedJson);
    }

    public interface FetchJsonTrailersListener{
        void onFetchedJsonTrailers(JSONObject fetchedJsonTrailers);
    }
}
