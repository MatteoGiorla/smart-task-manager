package ch.epfl.sweng.project;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Adapter used to display the task list
 */
public class LocationListAdapter extends ArrayAdapter<Location> {

    /**
     * Constructor of the class
     *
     * @param context  Current context contains global information about the application environment.
     * @param resource The resource ID for a layout file containing a layout to use
     *                 when instantiating views
     * @param objects  The objects to represent in the ListView
     */
    LocationListAdapter(Context context, int resource, List<Location> objects) {
        super(context, resource, objects);
    }

    /**
     * Method that return a view to be displayed. The view contains the data at the specified
     * position in the data set.
     *
     * @param position    Position of the data in the data set
     * @param convertView Old view to reuse
     * @param parent      ViewGroup that this view will eventually be attached to
     * @return the view to be displayed
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View resultView = convertView;

        //There is no recycled view, we need to create a new one
        if (resultView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            resultView = inflater.inflate(/*R.layout.list_item_layout*/, parent, false);
        }

        //We get the task to be displayed
        Location locationInTheView = getItem(position);
        if (locationInTheView != null) {
            TextView titleView = (TextView) resultView.findViewById(/*R.id.list_entry_title*/);
            TextView longitudeView = (TextView) resultView.findViewById(/*???*/);
            TextView latitudeView = (TextView) resultView.findViewById(/*???*/);

            if (titleView != null) {
                titleView.setText(locationInTheView.getName());
            }

            if (longitudeView != null) {
                //must set the the longitude in the view ???
            }

            if (titleView != null) {
                //must set the the longitude in the view ???
            }
        }
        return resultView;
    }
}