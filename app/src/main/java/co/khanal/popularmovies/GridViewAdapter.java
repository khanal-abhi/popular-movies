package co.khanal.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by abhi on 2/18/16.
 */
public class GridViewAdapter extends ArrayAdapter<Movie> {

    Context appContext = null;
    int layoutId;
    Movie[] movies;

    public GridViewAdapter(Context context, int resource, Movie[] objects) {
        super(context, resource, objects);
        appContext = context;
        layoutId = resource;
        movies = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

        Movie movie = movies[position];

        Picasso.with(appContext).load(movie.getImageUri()).into(placeHolder.imageView);


        return grid_item;
    }

    public class PlaceHolder{
        public ImageView imageView;
    }
}
