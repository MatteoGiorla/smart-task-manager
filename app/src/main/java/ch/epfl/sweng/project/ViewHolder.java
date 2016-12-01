package ch.epfl.sweng.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import static ch.epfl.sweng.project.TaskFragment.INDEX_TASK_TO_BE_EDITED_KEY;
import static ch.epfl.sweng.project.TaskFragment.TASKS_LIST_KEY;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Context mContext;
    private ArrayList<Task> tasksList;
    public View colorIndicator;
    public TextView taskDuration;
    public TextView taskLocation;
    public TextView taskRemainingDays;
    public TextView taskTitle;


    public ViewHolder(View v, Context context, ArrayList<Task> tasksList) {
        super(v);
        this.tasksList = tasksList;
        mContext = context;
        colorIndicator = v.findViewById(R.id.list_colored_indicator);
        taskDuration = (TextView) v.findViewById(R.id.list_item_duration);
        taskLocation = (TextView) v.findViewById(R.id.list_item_location);
        taskRemainingDays = (TextView) v.findViewById(R.id.list_remaining_days);
        taskTitle = (TextView) v.findViewById(R.id.list_entry_title);

        v.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int itemPosition = getAdapterPosition();
        Intent intent = new Intent(mContext, EditTaskActivity.class);
        intent.putExtra(INDEX_TASK_TO_BE_EDITED_KEY, itemPosition);
        intent.putParcelableArrayListExtra(TASKS_LIST_KEY, tasksList);
        ((Activity)mContext).startActivityForResult(intent, TaskFragment.editTaskRequestCode);
    }

}