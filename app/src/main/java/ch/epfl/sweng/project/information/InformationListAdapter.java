package ch.epfl.sweng.project.information;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.project.R;

/**
 * Adapter used to display task's information
 * in the TaskInformationActivity.
 */
class InformationListAdapter extends ArrayAdapter<InformationItem> {

    /**
     * Constructor of the class.
     *
     * @param context  Current context contains global information about the application environment.
     * @param resource The resource ID for a layout file containing a layout to use
     *                 when instantiating views
     * @param objects  The objects to represent in the ListView
     */
    InformationListAdapter(Context context, int resource, List<InformationItem> objects) {
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
            resultView = inflater.inflate(R.layout.list_item_information, parent, false);
        }

        InformationItem itemToBeDisplayed = getItem(position);
        if (itemToBeDisplayed != null) {
            TextView titleView = (TextView) resultView.findViewById(R.id.title_field_information);
            TextView bodyView = (TextView) resultView.findViewById(R.id.body_field_information);
            ImageView image = (ImageView) resultView.findViewById(R.id.image_field_information);

            if (titleView != null) {
                titleView.setText(itemToBeDisplayed.getTitle());
            }
            if (bodyView != null) {
                bodyView.setText(itemToBeDisplayed.getBody());
            }
            if (image != null) {
                image.setImageResource(itemToBeDisplayed.getImageSrcId());
            }
        }
        return resultView;
    }
}
