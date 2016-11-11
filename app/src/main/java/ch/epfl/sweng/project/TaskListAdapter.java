package ch.epfl.sweng.project;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Adapter used to display the task list
 */
public class TaskListAdapter extends ArrayAdapter<Task> {
    private List<Task> taskList;

    /**
     * Constructor of the class
     *
     * @param context  Current context contains global information about the application environment.
     * @param resource The resource ID for a layout file containing a layout to use
     *                 when instantiating views
     * @param objects  The objects to represent in the ListView
     */
    TaskListAdapter(Context context, int resource, List<Task> objects) {
        super(context, resource, objects);
        taskList = objects;
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
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View resultView = convertView;

        //There is no recycled view, we need to create a new one
        if (resultView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            resultView = inflater.inflate(R.layout.list_item_task, parent, false);
        }

        //We get the task to be displayed
        final Task taskInTheView = getItem(position);
        if (taskInTheView != null) {
            TextView titleView = (TextView) resultView.findViewById(R.id.list_entry_title);
            // TextView descriptionView = (TextView) resultView.findViewById(R.id.list_entry_description);
            TextView remainingDays = (TextView) resultView.findViewById(R.id.list_remaining_days);
            ImageView energyIconLow = (ImageView) resultView.findViewById(R.id.list_energy_low);
            ImageView energyIconNormal = (ImageView) resultView.findViewById(R.id.list_energy_normal);
            ImageView energyIconHigh = (ImageView) resultView.findViewById(R.id.list_energy_high);
            View coloredIndicator = (View) resultView.findViewById(R.id.list_colored_indicator);

            if (titleView != null) {
                titleView.setText(taskInTheView.getName());
            }
           /* if (descriptionView != null) {
                descriptionView.setText(taskInTheView.getDescription());
            }*/
            if (remainingDays != null) {
                Calendar c = Calendar.getInstance();

                int days = (int)daysBetween(c.getTime(), taskInTheView.getDueDate());
                remainingDays.setText(Integer.toString(days));

                if (days < 10)
                    remainingDays.setTextColor(Color.RED);
            }
            if (energyIconLow != null) {
                Task.Energy e = taskInTheView.getEnergy();
                if (e == Task.Energy.LOW) {
                    energyIconNormal.setVisibility(View.INVISIBLE);
                    energyIconHigh.setVisibility(View.INVISIBLE);
                } else if (e == Task.Energy.NORMAL) {
                    energyIconHigh.setVisibility(View.INVISIBLE);
                }
            }
            /*if (coloredIndicator != null) {

            }*/
        }
        return resultView;
    }

    /**
     * Helper functions to calculate the number of remaining days
     * source: http://stackoverflow.com/questions/3838527/android-java-date-difference-in-days
     */
    private Calendar getDatePart(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    private long daysBetween(Date startDate, Date endDate) {
        Calendar sDate = getDatePart(startDate);
        Calendar eDate = getDatePart(endDate);

        Long millisDifference = eDate.getTimeInMillis() - sDate.getTimeInMillis();
        Long daysDifference =  TimeUnit.MILLISECONDS.toDays(millisDifference);
        return daysDifference.intValue();
    }
}