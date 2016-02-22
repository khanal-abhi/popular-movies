package co.khanal.popularmovies;

import org.json.JSONObject;

import co.khanal.FetchJson;

/**
 * Created by abhi on 2/21/16.
 */
public class FetchJsonReviews extends FetchJson {

    public FetchJsonReviews(Object listener, String url) {
        super(listener, url);
    }

    @Override
    protected void onPostExecute(JSONObject fetchedJson) {
        ((FetchJsonReviewsListener)listener).onFetchedJsonReviews(fetchedJson);
    }

    public interface FetchJsonReviewsListener{
        void onFetchedJsonReviews(JSONObject fetchedJsonReviews);
    }
}
