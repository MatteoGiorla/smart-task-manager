package ch.epfl.sweng.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

/**
 * Class that represents the inflated activity_task under the edit case
 */
public class EditTaskActivity extends AppCompatActivity {
    public static final String RETURNED_EDITED_TASK = "ch.epfl.sweng.EditTaskActivity.EDITED_TASK";
    public static final String RETURNED_INDEX_EDITED_TASK = "ch.epfl.sweng.EditTaskActivity.RETURNED_INDEX_EDITED_TASK";
    private Task mTaskToBeEdited;
    private ArrayList<Task> mTaskList;
    private int mIndexTaskToBeEdited;
    private boolean isValidTitle = true;

    /**
     * Override the onCreate method
     * Recover the task to be edited and update it, then it puts the
     * edited task in the intent.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down then this Bundle contains the data it
     *                           most recently supplied in onSaveInstanceState(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        //Initialize and check taskToBeEdited and taskList that were passed to the intent.
        final Intent intent = getIntent();
        checkIntent(intent);

        //Initialize the toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.task_toolbar);
        initializeToolbar(mToolbar);

        //set the submit button to non visible
        Button submitButton = (Button) findViewById(R.id.button_submit_task);
        submitButton.setVisibility(View.GONE);

        //Populate the layout activity_task
        populateLayout();

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton doneEditButton = (ImageButton) findViewById(R.id.edit_done_button_toolbar);
        EditText titleEditText = (EditText) findViewById(R.id.title_task);

        //Create a listener to check that the user is writing a valid input.
        titleEditText.addTextChangedListener(new TextWatcher());

        //Terminate the activity if the input written by the user is valid.
        doneEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidTitle) {
                    EditText descriptionEditText = (EditText) findViewById(R.id.description_task);
                    mTaskToBeEdited.setDescription(descriptionEditText.getText().toString());

                    intent.putExtra(RETURNED_EDITED_TASK, mTaskToBeEdited);
                    intent.putExtra(RETURNED_INDEX_EDITED_TASK, mIndexTaskToBeEdited);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
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
     * Check that the intent was correctly passed to the activity, and
     * initialize the global variables of the class.
     *
     * @param intent the intent passed to the activity.
     * @throws IllegalArgumentException If there is an error with the intent passed
     * to the activity.
     */
    private void checkIntent(Intent intent) {
        if (intent == null) {
            throw new IllegalArgumentException("No intent was passed to EditTaskActivity !");
        }

        mTaskList = intent.getParcelableArrayListExtra(TaskFragment.TASKS_LIST_KEY);
        mIndexTaskToBeEdited = intent.getIntExtra(TaskFragment.INDEX_TASK_TO_BE_EDITED_KEY, -1);
        if (mIndexTaskToBeEdited == -1 || mTaskList == null) {
            throw new IllegalArgumentException("Error on extras passed to EditTaskActivity !");
        }
        mTaskToBeEdited = mTaskList.get(mIndexTaskToBeEdited);
    }

    /**
     * Fill the layout with the old values of the task to be edited.
     */
    private void populateLayout() {
        EditText titleEditText = (EditText) findViewById(R.id.title_task);
        titleEditText.setText(mTaskToBeEdited.getName());

        EditText descriptionEditText = (EditText) findViewById(R.id.description_task);
        descriptionEditText.setText(mTaskToBeEdited.getDescription());
    }

    /**
     * Check if the title written is unique or not.
     *
     * @param title The new title of the task
     * @return true if the title is already used or false otherwise.
     */
    private boolean titleIsNotUnique(String title) {
        boolean result = false;
        for (int i = 0; i < mTaskList.size(); i++) {
            if (mTaskList.get(i).getName().equals(title) && i != mIndexTaskToBeEdited) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Private class that implement TextWatcher.
     * This class is used to check on runtime if the inputs written by the user
     * are valid or not.
     */
    private class TextWatcher implements android.text.TextWatcher {
        final ImageButton doneEditButton = (ImageButton) findViewById(R.id.edit_done_button_toolbar);
        final TextInputLayout title_layout = (TextInputLayout) findViewById(R.id.title_task_layout);

        /**
         * Check the input written by the user before it is changed.
         */
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            title_layout.setErrorEnabled(false);
            isValidTitle = false;
        }

        /**
         * Check the input written by the user while it is changed.
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        /**
         * Check the input written by the user after it was changed.
         */
        @Override
        public void afterTextChanged(Editable s) {
            isValidTitle = !(titleIsNotUnique(s.toString()));
            if (!isValidTitle) {
                doneEditButton.setVisibility(View.INVISIBLE);
                title_layout.setErrorEnabled(true);
                title_layout.setError(getResources().getText(R.string.error_title_duplicated));
            } else if (s.toString().isEmpty()) {
                isValidTitle = false;
                doneEditButton.setVisibility(View.INVISIBLE);
                title_layout.setErrorEnabled(true);
                title_layout.setError(getResources().getText(R.string.error_title_empty));
            } else {
                doneEditButton.setVisibility(View.VISIBLE);
                mTaskToBeEdited.setName(s.toString());
            }
        }
    }
}
