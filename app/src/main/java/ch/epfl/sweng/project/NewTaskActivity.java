package ch.epfl.sweng.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

/**
 * Class that represents the inflated activity_new_task
 */
public class NewTaskActivity extends AppCompatActivity {
    public static final String RETURNED_TASK = "ch.epfl.sweng.NewTaskActivity.NEW_TASK";
    private Intent intent;
    private List<Task> taskList;
    private EditText titleEditText;
    private TextInputLayout textInputLayoutTitle;

    /**
     * Override the onCreate method
     * Create a new Task and puts in the intent
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down then this Bundle contains the data it
     *                           most recently supplied in onSaveInstanceState(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);


        Toolbar mToolbar = (Toolbar) findViewById(R.id.newTask_toolbar);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Initialisation of the attributes
        intent = getIntent();
        taskList = intent
                .getParcelableArrayListExtra(TaskFragment.TASKS_LIST_KEY);
        titleEditText = (EditText) findViewById(R.id.input_title);
        textInputLayoutTitle = (TextInputLayout) findViewById(R.id.input_layout_title);

        //Control the user's inputs
        titleEditText.addTextChangedListener(new MyTextWatcher());

        Button submitButton = (Button) findViewById(R.id.button_submit_task);

        //When the user clicks on the "Submit" button
        submitButton.setOnClickListener(new MyOnClickListener());
    }

    /**
     * Private method that checks if an existing task in the list already contains
     * the title.
     *
     * @param taskList The task list
     * @param title    The title
     * @return true if the task list already contains an existing task with the same title
     * false otherwise
     */
    private boolean titleAlreadyExist(List<Task> taskList, String title) {
        boolean result = false;
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getName().equals(title)) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Private class that implements TextWatcher.
     * This class is used to check on runtime if the user's
     * inputs are valid or not.
     */
    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            textInputLayoutTitle.setErrorEnabled(false);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (titleAlreadyExist(taskList, s.toString())) {
                textInputLayoutTitle.setErrorEnabled(true);
                textInputLayoutTitle.setError(getResources().getText(R.string.error_title_duplicated));
            } else if (s.toString().isEmpty()) {
                textInputLayoutTitle.setErrorEnabled(true);
                textInputLayoutTitle.setError(getResources().getText(R.string.error_title_empty));
            } else {
                textInputLayoutTitle.setErrorEnabled(false);
            }
        }
    }

    /**
     * Private class that implements OnClickListener.
     * This class is used to do the necessary treatment when the user
     * click on the "Submit" button. It checks that the title is not
     * empty and if not, creates the task and finish.
     */
    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String title = titleEditText.getText().toString();
            if (title.isEmpty()) {
                textInputLayoutTitle.setErrorEnabled(true);
                textInputLayoutTitle.setError(getResources().getText(R.string.error_title_empty));
            } else if (!title.isEmpty() && !titleAlreadyExist(taskList, title)) {
                EditText descriptionEditText = (EditText) findViewById(R.id.input_description);
                String description = descriptionEditText.getText().toString();

                Task newTask = new Task(title, description);

                intent.putExtra(RETURNED_TASK, newTask);


                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
