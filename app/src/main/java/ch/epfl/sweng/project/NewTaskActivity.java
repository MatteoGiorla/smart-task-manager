package ch.epfl.sweng.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        Button submitButton = (Button) findViewById(R.id.button_submit_task);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText titleEditText = (EditText) findViewById(R.id.input_title);
                String title = titleEditText.getText().toString();

                EditText descriptionEditText = (EditText) findViewById(R.id.input_description);
                String description = descriptionEditText.getText().toString();

                Task newTask = new Task(title, description);

                Intent intent = getIntent();
                intent.putExtra(returnedTask, newTask);

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
