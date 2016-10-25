package ch.epfl.sweng.project.information;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.Task;

import static ch.epfl.sweng.project.TaskFragment.INDEX_TASK_TO_BE_EDITED_KEY;
import static ch.epfl.sweng.project.TaskFragment.TASKS_LIST_KEY;

public class TaskInformationActivity extends AppCompatActivity {
    private ArrayList<Task> taskList;
    private int position;
    private Task taskToBeDisplayed;
    private ArrayList<InformationItem> informationItemsList;
    private Intent intent;
    private Set<Integer> titles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_task);

        setToolBar();

        //Check the validity of the intent
        intent = getIntent();
        checkIntent();
        taskList = intent.getParcelableArrayListExtra(TASKS_LIST_KEY);
        position = intent.getIntExtra(INDEX_TASK_TO_BE_EDITED_KEY, -1);
        checkIntentExtras();

        //Get the task to be displayed
        taskToBeDisplayed = taskList.get(position);

        //Create the list with the task's information
        createInformationItemList();

        //Instantiate the InformationListAdapter
        InformationListAdapter mInformationAdapter = new InformationListAdapter(
                this,
                R.layout.list_item_information,
                informationItemsList
        );

        //Get the ListView layout
        ListView listView = (ListView) findViewById(R.id.list_view_information);

        listView.setAdapter(mInformationAdapter);

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

    /**
     * Initialise and fill the informationItemsList.
     */
    private void createInformationItemList() {
        informationItemsList = new ArrayList<>();

        //Get the map with the task's information
        SparseArray<String> titleToBody = taskToBeDisplayed.getTitleToBody();
        titles = new HashSet<>();
        constructTitlesSet();

        //Fill the informationItemsList
        for (Integer title : titles) {
            informationItemsList
                    .add(new InformationItem(getString(title), titleToBody.get(title)));

        }
    }

    /**
     * Construct the Set with the InformationItem's titles.
     * Each time a new field is added to the Task class, this method
     * needs to be updated.
     */
    private void constructTitlesSet() {
        titles.add(R.string.title_field);
        titles.add(R.string.description_field);
    }

    /**
     *  Set the tool bar with the return arrow on top left.
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
        if(intent == null) {
            throw new IllegalArgumentException("No intent was passed to TaskInformationActivity !");
        }
    }

    /**
     * Check the intent's extras validity.
     */
    private void checkIntentExtras() {
        if(taskList == null || position == -1) {
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
            finish();
        }
    }
}
