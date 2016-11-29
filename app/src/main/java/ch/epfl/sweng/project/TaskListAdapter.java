package ch.epfl.sweng.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;

import ch.epfl.sweng.project.information.TaskInformationActivity;

import static ch.epfl.sweng.project.TaskFragment.INDEX_TASK_TO_BE_DISPLAYED;
import static ch.epfl.sweng.project.TaskFragment.TASKS_LIST_KEY;


/**
 * Adapter used to display the task list
 */
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Task> tasksList;

    /**
     * Constructor of the class
     *
     * @param context  Current context contains global information about the application environment.
     * @param taskList The list of tasks
     */
    TaskListAdapter(Context context, ArrayList<Task> taskList) {
        this.tasksList = taskList;
        mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sort(Comparator<Task> comparator) {
        tasksList.sort(comparator);
        notifyDataSetChanged();
    }

    public void remove(Task task) {
        tasksList.remove(task);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

    @Override
    public TaskListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_task, parent, false);

        // create ViewHolder
        return new ViewHolder(itemLayoutView, mContext, tasksList);
    }

    @Override
    public void onBindViewHolder(TaskListAdapter.ViewHolder holder, int position) {
        Task taskInTheView = tasksList.get(position);

        if (taskInTheView != null) {
            holder.taskTitle.setText(taskInTheView.getName());

            Calendar c = Calendar.getInstance();
            int days = taskInTheView.daysBetween(c.getTime(), taskInTheView.getDueDate());
            if(Utils.isDueDateUnfilled(taskInTheView)){
                holder.taskRemainingDays.setText("-");
            }else{
                if (days > 1) {
                    holder.taskRemainingDays.setText(String.format(Locale.UK, "%d" + mContext.getString(R.string.days_left), days));
                } else if (days == 1) {
                    holder.taskRemainingDays.setText(String.format(Locale.UK, "%d" + mContext.getString(R.string.day_left), days));
                } else if (days == 0) {
                    holder.taskRemainingDays.setText(R.string.due_today);
                } else if (days == -1) {
                    int days_value_for_text = days * -1;
                    holder.taskRemainingDays.setText(String.format(Locale.UK, "%d" + mContext.getString(R.string.day_late), days_value_for_text));
                } else if (days < 1) {
                    int days_value_for_text = days * -1;
                    holder.taskRemainingDays.setText(String.format(Locale.UK, "%d" + mContext.getString(R.string.days_late), days_value_for_text));
                }

                if (days < 10 && days >= 1) {
                    holder.taskRemainingDays.setTextColor(ContextCompat.getColor(mContext, R.color.flat_orange));
                } else if (days < 1) {
                    holder.taskRemainingDays.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                } else {
                    holder.taskRemainingDays.setTextColor(ContextCompat.getColor(mContext, R.color.flat_green));
                }
            }

            if(Utils.isLocationUnfilled(taskInTheView, mContext)){
                holder.taskLocation.setText("-");
            }else{
                holder.taskLocation.setText(taskInTheView.getLocationName());
            }

            if(Utils.isDurationUnfilled(taskInTheView)){
                holder.taskDuration.setText("-");
            }else{
                holder.taskDuration.setText(MainActivity.DURATION_MAP.get((int) taskInTheView.getDuration()));
            }

            if (!Utils.isUnfilled(taskInTheView, mContext)) {
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

                holder.colorIndicator.setBackgroundColor(ColorUtils.HSLToColor(hsl));
            }
            if(Utils.isUnfilled(taskInTheView, mContext)){
                holder.colorIndicator.setVisibility(View.INVISIBLE);
            }
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View colorIndicator;
        TextView taskDuration;
        TextView taskLocation;
        TextView taskRemainingDays;
        TextView taskTitle;

        ViewHolder(View v, final Context mContext, final ArrayList<Task> tasksList) {
            super(v);

            colorIndicator = v.findViewById(R.id.list_colored_indicator);
            taskDuration = (TextView) v.findViewById(R.id.list_item_duration);
            taskLocation = (TextView) v.findViewById(R.id.list_item_location);
            taskRemainingDays = (TextView) v.findViewById(R.id.list_remaining_days);
            taskTitle = (TextView) v.findViewById(R.id.list_entry_title);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemPosition = getAdapterPosition();
                    Intent intent = new Intent(mContext, TaskInformationActivity.class);
                    intent.putExtra(INDEX_TASK_TO_BE_DISPLAYED, itemPosition);
                    intent.putParcelableArrayListExtra(TASKS_LIST_KEY, tasksList);
                    ((Activity)mContext).startActivityForResult(intent, TaskFragment.displayTaskRequestCode);
                }
            });
        }
    }
}