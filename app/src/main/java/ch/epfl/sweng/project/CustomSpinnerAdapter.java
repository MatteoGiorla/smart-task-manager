package ch.epfl.sweng.project;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Custom adapter that set the title of the spinners to the empty string
 * in order to hide the text from being written on top of an image.
 *
 * @param <T> The type of the adapter
 */
public class CustomSpinnerAdapter<T> extends ArrayAdapter<T> {

    /**
     * Constructor of the class
     *
     * @param context  Current context contains global information about the application environment.
     * @param textViewResourceId The resource ID for a layout file containing a layout to use
     *                           when instantiating views
     * @param objects  The objects to represent in the ListView
     */
    public CustomSpinnerAdapter(Context context, int textViewResourceId, T[] objects) {
        super(context, textViewResourceId, objects);
    }


    /**
     * Method that return a view containing the empty string so we don't write on top
     * of an image.
     *
     * @param position    Position of the data in the data set
     * @param convertView Old view to reuse
     * @param parent      ViewGroup that this view will eventually be attached to
     * @return the view to be displayed
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        return view;
    }
}