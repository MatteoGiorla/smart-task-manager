package ch.epfl.sweng.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

/**
 * Class that represents the inflated activity_new_task
 */
public class NewTaskActivity extends AppCompatActivity {
    public static final String returnedTask = "ch.epfl.sweng.NewTaskActivity.NEW_TASK";

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

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button submitButton = (Button) findViewById(R.id.button_submit_task);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                List<Task> taskList = intent
                        .getParcelableArrayListExtra(TaskFragment.TASKS_LIST_KEY);

                EditText titleEditText = (EditText) findViewById(R.id.input_title);
                String title = titleEditText.getText().toString();

                if(titleAlreadyExist(taskList, title)) {
                    TextInputLayout textInputLayoutTitle = (TextInputLayout) findViewById(R.id.input_layout_title);
                    textInputLayoutTitle.setErrorEnabled(true);
                    textInputLayoutTitle.setError("This title already exists");
                } else {

                    EditText descriptionEditText = (EditText) findViewById(R.id.input_description);
                    String description = descriptionEditText.getText().toString();

                    Task newTask = new Task(title, description);

                    intent.putExtra(returnedTask, newTask);

                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    /**
     *
     * @param taskList
     * @param title
     * @return
     */
    private boolean titleAlreadyExist(List<Task> taskList, String title) {
        boolean result = false;
        for(int i = 0; i < taskList.size(); i++) {
            if(taskList.get(i).getName().equals(title)) {
                result = true;
            }
        }
        return result;
    }
}
