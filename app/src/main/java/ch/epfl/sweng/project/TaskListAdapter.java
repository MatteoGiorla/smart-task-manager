package ch.epfl.sweng.project;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Adapter used to display the task list
 */
public class TaskListAdapter extends ArrayAdapter<Task> {

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
    @RequiresApi(api = Build.VERSION_CODES.M)
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
            TextView remainingDays = (TextView) resultView.findViewById(R.id.list_remaining_days);
            TextView taskLocation = (TextView) resultView.findViewById(R.id.list_item_location);
            TextView taskDuration = (TextView) resultView.findViewById(R.id.list_item_duration);
            View coloredIndicator = resultView.findViewById(R.id.list_colored_indicator);

            if (titleView != null) {
                titleView.setText(taskInTheView.getName());
            }
            Calendar c = Calendar.getInstance();
            int days = taskInTheView.daysBetween(c.getTime(), taskInTheView.getDueDate());
            if (remainingDays != null) {
                if(Utils.isDueDateUnfilled(taskInTheView)){
                    remainingDays.setText("—");
                }else{
                    if (days > 1) {
                        remainingDays.setText(String.format(Locale.UK, "%d" + getContext().getString(R.string.days_left), days));
                    } else if (days == 1) {
                        remainingDays.setText(String.format(Locale.UK, "%d" + getContext().getString(R.string.day_left), days));
                    } else if (days == 0) {
                        remainingDays.setText(R.string.due_today);
                    } else if (days == -1) {
                        int days_value_for_text = days * -1;
                        remainingDays.setText(String.format(Locale.UK, "%d" + getContext().getString(R.string.day_late), days_value_for_text));
                    } else if (days < 1) {
                        int days_value_for_text = days * -1;
                        remainingDays.setText(String.format(Locale.UK, "%d" + getContext().getString(R.string.days_late), days_value_for_text));
                    }

                    if (days < 10 && days >= 1) {
                        remainingDays.setTextColor(ContextCompat.getColor(getContext(), R.color.flat_orange));
                    } else if (days < 1) {
                        remainingDays.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    } else {
                        remainingDays.setTextColor(ContextCompat.getColor(getContext(), R.color.flat_green));
                    }
                }
            }
            if (taskLocation!= null) {
                if(Utils.isLocationUnfilled(taskInTheView, getContext())){
                    taskLocation.setText("—");
                }else{
                    taskLocation.setText(taskInTheView.getLocationName());
                }
            }
            if (taskDuration != null) {
                if(Utils.isDurationUnfilled(taskInTheView)){
                    taskDuration.setText("—");
                }else{
                    taskDuration.setText(MainActivity.DURATION_MAP.get((int) taskInTheView.getDuration()));
                }
            }
            if (coloredIndicator != null && !Utils.isUnfilled(taskInTheView, getContext())) {
                double static_sort_value = taskInTheView.getStaticSortValue();
                double urgency_percentage;
                if(days <= 2 || static_sort_value > 100){
                    urgency_percentage = 100;
                } else {
                    urgency_percentage = static_sort_value;
                }
                float[] hsv = new float[3];
                hsv[0]= (float)Math.floor((100 - urgency_percentage) * 120 / 100);
                hsv[1] = 1;
                hsv[2] = 1;

                //flatten the color (formula : Saturation - 37%, lightness + 3.95% (modified))
                float[] hsl = new float[3];
                ColorUtils.colorToHSL(Color.HSVToColor(hsv), hsl);
                hsl[1] = hsl[1] - (float)0.2 * hsl[1];
                hsl[2] = hsl[2] + (float)0.2 * hsl[2];

                coloredIndicator.setBackgroundColor(ColorUtils.HSLToColor(hsl));
            }
            if(Utils.isUnfilled(taskInTheView, getContext())){
                if (coloredIndicator != null) {
                    coloredIndicator.setVisibility(View.INVISIBLE);
                }
            }
        }
        return resultView;
    }
}