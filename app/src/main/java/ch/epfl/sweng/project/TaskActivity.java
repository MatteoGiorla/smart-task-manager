package ch.epfl.sweng.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Class which represents an activity regarding a task
 */
public abstract class TaskActivity extends AppCompatActivity {
    protected TextInputLayout textInputLayoutTitle;
    protected Toolbar mToolbar;
    protected Intent intent;
    protected ArrayList<Task> taskList;
    protected EditText titleEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        //Initialize the toolbar
        mToolbar = (Toolbar) findViewById(R.id.task_toolbar);
        initializeToolbar(mToolbar);

        textInputLayoutTitle = (TextInputLayout) findViewById(R.id.title_task_layout);

        intent = getIntent();
        if (intent == null) {
            throw new IllegalArgumentException("No intent was passed to TaskActivity !");
        }
        taskList = intent
                .getParcelableArrayListExtra(TaskFragment.TASKS_LIST_KEY);

        titleEditText = (EditText) findViewById(R.id.title_task);
    }

    /**
     * Private class that implement TextWatcher.
     * This class is used to check on runtime if the inputs written by the user
     * are valid or not.
     */
    protected class TaskTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after){
            textInputLayoutTitle.setErrorEnabled(false);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s){
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
        }
    }
}
