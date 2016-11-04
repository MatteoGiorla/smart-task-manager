package ch.epfl.sweng.project.information;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.epfl.sweng.project.EditTaskActivity;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.StateDuration;
import ch.epfl.sweng.project.StateEnergy;
import ch.epfl.sweng.project.Task;

import static ch.epfl.sweng.project.EditTaskActivity.RETURNED_EDITED_TASK;
import static ch.epfl.sweng.project.EditTaskActivity.RETURNED_INDEX_EDITED_TASK;
import static ch.epfl.sweng.project.TaskFragment.INDEX_TASK_TO_BE_DISPLAYED;
import static ch.epfl.sweng.project.TaskFragment.INDEX_TASK_TO_BE_EDITED_KEY;
import static ch.epfl.sweng.project.TaskFragment.TASKS_LIST_KEY;

public class TaskInformationActivity extends AppCompatActivity {
    public static final String TASK_STATUS_KEY = "ch.epfl.sweng.information.TASK_STATUS_KEY";
    public static final String TASK_TO_BE_DELETED_INDEX = "ch.epfl.sweng.information.TASK_TO_BE_DELETED_INDEX";
    public static final int TASK_IS_MODIFIED = 1;
    public static final int TASK_IS_DELETED = 2;
    private final int editTaskRequestCode = 1;
    private ArrayList<Task> taskList;
    private int position;
    private Task taskToBeDisplayed;
    private ArrayList<InformationItem> informationItemsList;
    private Intent intent;
    private TextView taskTitleTextView;
    private InformationListAdapter mInformationAdapter;
    private int taskStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_task);

        setToolBar();

        //Check the validity of the intent
        intent = getIntent();
        checkIntent();
        taskList = intent.getParcelableArrayListExtra(TASKS_LIST_KEY);
        position = intent.getIntExtra(INDEX_TASK_TO_BE_DISPLAYED, -1);
        checkIntentExtras();

        //Get the task to be displayed
        taskList = new ArrayList<>(taskList);
        taskToBeDisplayed = taskList.get(position);

        informationItemsList = new ArrayList<>();

        //Create the list with the task's information
        createInformationItemList();

        //Instantiate the InformationListAdapter
        mInformationAdapter = new InformationListAdapter(
                this,
                R.layout.list_item_information,
                informationItemsList
        );

        //Get the TextView containing the task's title
        taskTitleTextView = (TextView) findViewById(R.id.title_task_information_activity);

        taskTitleTextView.setText(taskToBeDisplayed.getName());

        //Get the ListView layout
        ListView listView = (ListView) findViewById(R.id.list_view_information);

        listView.setAdapter(mInformationAdapter);

    }

    public void openEditTaskActivity(View v) {
        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra(INDEX_TASK_TO_BE_EDITED_KEY, position);
        if (taskList == null)
            throw new IllegalArgumentException("taskList is null before been passed to the intent");
        intent.putParcelableArrayListExtra(TASKS_LIST_KEY, taskList);
        startActivityForResult(intent, editTaskRequestCode);
        taskStatus = TASK_IS_MODIFIED;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.trash_menu :
                taskStatus = TASK_IS_DELETED;
                setResultIntent();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called after editing a task for task information activity.
     *
     * @param requestCode the code of the request
     * @param resultCode the result code
     * @param data the intent with the modified task
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == editTaskRequestCode) {
            if (resultCode == RESULT_OK) {
                Task editedTask = data.getParcelableExtra(RETURNED_EDITED_TASK);
                int editedTaskIndex = data.getIntExtra(RETURNED_INDEX_EDITED_TASK, -1);

                if (editedTask == null || editedTaskIndex == -1)
                    throw new IllegalArgumentException("Invalid extras returned from EditTaskActivity !");

                taskToBeDisplayed = editedTask;
                taskList.set(editedTaskIndex, editedTask);
                taskTitleTextView.setText(taskToBeDisplayed.getName());
                informationItemsList.clear();
                createInformationItemList();
                mInformationAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Called when the press back button is pressed.
     */
    @Override
    public void onBackPressed() {
        setResultIntent();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.trash_menu, menu);
        return true;
    }

    /**
     * Set the result intent.
     */
    private void setResultIntent() {
        if(taskStatus == TASK_IS_MODIFIED) {
            intent.putExtra(TASK_STATUS_KEY, taskStatus);
            intent.putExtra(EditTaskActivity.RETURNED_EDITED_TASK, taskToBeDisplayed);
            intent.putExtra(EditTaskActivity.RETURNED_INDEX_EDITED_TASK, position);
            setResult(RESULT_OK, intent);
        } else if(taskStatus == TASK_IS_DELETED) {
            intent.putExtra(TASK_STATUS_KEY, taskStatus);
            intent.putExtra(TASK_TO_BE_DELETED_INDEX, position);
            setResult(RESULT_OK, intent);
        }
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
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    /**
     * Initialise and fill the informationItemsList.
     */
    private void createInformationItemList() {
        informationItemsList.add(new InformationItem(getString(R.string.due_date_field),
                taskToBeDisplayed.dueDateToString(), R.drawable.calendar_36dp));
        String duration_text = new StateDuration(taskToBeDisplayed.getDurationInMinutes(), getApplicationContext()).toString();
        informationItemsList.add(new InformationItem(getString(R.string.duration_field),
                String.valueOf(duration_text), R.drawable.minutes_needed_36dp));
        informationItemsList.add(new InformationItem(getString(R.string.location_field),
                taskToBeDisplayed.getLocationName(), R.drawable.task_location_36dp));
        String energy_text = new StateEnergy(taskToBeDisplayed.getEnergy(), getApplicationContext()).toString();
        informationItemsList.add(new InformationItem(getString(R.string.energy_field),
                energy_text, R.drawable.thunder_36dp));
        informationItemsList.add(new InformationItem(getString(R
                .string.description_field),
                taskToBeDisplayed.getDescription(), R.drawable.description_36dp));


        for(String contributor : taskToBeDisplayed.getListOfContributors()) {
            informationItemsList.add(new InformationItem(getString(R.string.contributors_field),
                    contributor, R.drawable.author_36dp));
        }
    }

    /**
     * Set the tool bar with the return arrow on top left.
     */
    private void setToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.task_information_toolbar);
        initializeToolbar(mToolbar);
        mToolbar.setNavigationOnClickListener(new OnReturnArrowClickListener());
    }

    /**
     * Check the intent validity.
     */
    private void checkIntent() {
        if (intent == null) {
            throw new IllegalArgumentException("No intent was passed to TaskInformationActivity !");
        }
    }

    /**
     * Check the intent's extras validity.
     */
    private void checkIntentExtras() {
        if (taskList == null || position == -1) {
            throw new IllegalArgumentException("Error on extras passed to TaskInformationActivity !");
        }
    }

    /**
     * OnClickListener on the return arrow.
     */
    private class OnReturnArrowClickListener implements View.OnClickListener {

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            setResultIntent();
            finish();
        }
    }
}
