package ch.epfl.sweng.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import static ch.epfl.sweng.project.TaskFragment.INDEX_TASK_TO_BE_EDITED_KEY;
import static ch.epfl.sweng.project.TaskFragment.TASKS_LIST_KEY;

public class TaskInformationActivity extends AppCompatActivity {
    private ArrayList<Task> taskList;
    private int position;
    private Task taskToBeDisplayed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_task);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.task_information_toolbar);
        initializeToolbar(mToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        taskList = intent.getParcelableArrayListExtra(TASKS_LIST_KEY);
        position = intent.getIntExtra(INDEX_TASK_TO_BE_EDITED_KEY, -1);
        if(position == -1 || taskList == null) {
            throw new IllegalArgumentException("Error on extras passed to EditTaskActivity !");
        }
        taskToBeDisplayed = taskList.get(position);

        TextView taskTitle = (TextView) findViewById(R.id.information_title);
        taskTitle.setText(taskToBeDisplayed.getName());
        TextView taskDescription = (TextView) findViewById(R.id.information_description);
        taskDescription.setText(taskToBeDisplayed.getDescription());

    }

    /**
     * Start the toolbar and enable that back button on the toolbar.
     *
     * @param mToolbar the toolbar of the activity
     */
    private void initializeToolbar(Toolbar mToolbar) {
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }
}
