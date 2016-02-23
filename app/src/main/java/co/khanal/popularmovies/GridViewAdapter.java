package co.khanal.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import co.khanal.popularmovies.DB.MovieModel;

/**
 * Created by abhi on 2/18/16.
 * Custom GridView adapter that extends ArrayAdapter<Movie>. It takes the movies, extracts the
 * image uris and then has Picasso load them in. One key feature it has is that it figures out
 * the optimal width and height for the grid by checking the containers width and dividing it by
 * the number of columns needed and figuring out the height accordingly.
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

//        Get the container width and based on the number of columns needed, divide it up.
        float w = parent.getWidth();
        float h = (277f * w)  /185f;
        int columns = appContext.getResources().getInteger(R.integer.grid_columns);
        ((GridView) parent).setNumColumns(columns);
        placeHolder.imageView.setMinimumWidth((int) (w / columns));
        placeHolder.imageView.setMinimumHeight((int) (h / columns));
        placeHolder.imageView.setMaxWidth((int) (w / columns));
        placeHolder.imageView.setMaxHeight((int) (h / columns));


        Movie movie = movies[position];
        if(movie.getBytesArray() != null){
            placeHolder.imageView.setImageBitmap(MovieModel.getBitmap(movie.getBytesArray()));
        } else {
            Picasso.with(appContext)
                    .load(movie.getImageUri())
                    .placeholder(R.drawable.ic_thumb_up_white_48dp)
                    .error(R.drawable.ic_trending_up_white_48dp)
                    .into(placeHolder.imageView);
        }

        return grid_item;
    }

    public class PlaceHolder{
        public ImageView imageView;
    }
}
