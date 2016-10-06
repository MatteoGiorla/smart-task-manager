package ch.epfl.sweng.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Adapter used to display the task list
 */
class TaskListAdapter extends ArrayAdapter<Task> {

    /**
     * Constructor of the class
     *
     * @param context current context contains global information about the application environment.
     * @param resource The resource ID for a layout file containing a layout to use
     *                  when instantiating views
     * @param objects The objects to represent in the ListView
     */
    public TaskListAdapter(Context context, int resource, List<Task> objects) {
        super(context, resource, objects);
    }

    /**
     * Method that return a view to be displayed. The view contains the data at the specified
     * position in the data set.
     *
     * @param position Position of the data in the data set
     * @param convertView Old view to reuse
     * @param parent ViewGroup that this view will eventually be attached to
     * @return the view to be displayed
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View resultView = convertView;

        //There is no recycled view, we need to create a new one
        if(resultView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            resultView = inflater.inflate(R.layout.list_item_task, parent, false);
        }

        //We get the task to be displayed
        Task taskInTheView = getItem(position);
        if(taskInTheView != null) {
            TextView titleView = (TextView) resultView.findViewById(R.id.list_entry_title);
            TextView descriptionView = (TextView) resultView.findViewById(R.id.list_entry_description);

            if(titleView != null) {
                titleView.setText(taskInTheView.getName());
            }
            if(descriptionView != null) {
                descriptionView.setText(taskInTheView.getDescription());
            }
        }
        return resultView;
    }
}