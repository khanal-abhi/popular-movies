package co.khanal.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.zip.Inflater;

/**
 * Created by abhi on 2/18/16.
 */
public class GridViewAdapter extends ArrayAdapter<Movie> {

    private Context appContext = null;
    private int layoutId;
    private Movie[] movies;

    public GridViewAdapter(Context context, int resource, Movie[] objects) {
        super(context, resource, objects);
        appContext = context;
        layoutId = resource;
        movies = objects;



    }

    public Movie[] getMovies() {
        return movies;
    }

    public void setMovies(Movie[] movies) {
        this.movies = movies;
    }

    @Override
    public Movie getItem(int position) {
        return movies[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (movies == null)
            return null;

        View grid_item = convertView;

        PlaceHolder placeHolder;

        if(grid_item != null){
            placeHolder = (PlaceHolder) grid_item.getTag();
        } else {
            LayoutInflater inflater = LayoutInflater.from(appContext);
            grid_item = inflater.inflate(layoutId, parent, false);

            placeHolder = new PlaceHolder();
            placeHolder.imageView = (ImageView) grid_item.findViewById(R.id.grid_item_poster);

            grid_item.setTag(placeHolder);
        }

        float w = parent.getWidth();
        float h = (277f * w)  /185f;

        placeHolder.imageView.setMinimumWidth((int) w / 2);
        placeHolder.imageView.setMinimumHeight((int) h / 2);

        placeHolder.imageView.setMaxWidth((int) w / 2);
        placeHolder.imageView.setMaxWidth((int)w/2);

        Movie movie = movies[position];
        Picasso.with(appContext).load(movie.getImageUri()).into(placeHolder.imageView);


        return grid_item;
    }

    public class PlaceHolder{
        public ImageView imageView;
    }
}
