package ch.epfl.sweng.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;



public class EditTaskActivity extends AppCompatActivity {
    public static final String RETURNED_EDITED_TASK = "ch.epfl.sweng.EditTaskActivity.EDITED_TASK";
    public static final String RETURNED_INDEX_EDITED_TASK = "ch.epfl.sweng.EditTaskActivity.RETURNED_INDEX_EDITED_TASK";
    private Task mTaskToBeEdited;
    private ArrayList<Task> mTaskList;
    private int mIndexTaskToBeEdited;
    private boolean isValidTitle = true;

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down then this Bundle contains the data it
     *                           most recently supplied in onSaveInstanceState(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        //Initialize taskToBeEdited and taskList
        final Intent intent = getIntent();
        checkIntent(intent);

        //Initialize toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.editTask_toolbar);
        initializeToolbar(mToolbar);

        //Populate the layout
        populateLayout();

        mToolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final ImageButton doneEditButton = (ImageButton) findViewById(R.id.edit_done_button_toolbar);
        final TextInputLayout title_layout = (TextInputLayout) findViewById(R.id.title_existing_task_layout);
        EditText titleEditText = (EditText) findViewById(R.id.title_existing_task);

        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                title_layout.setErrorEnabled(false);
                isValidTitle = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                isValidTitle = !(titleIsNotUnique(s.toString()));
                if(!isValidTitle){
                    doneEditButton.setVisibility(View.INVISIBLE);
                    title_layout.setErrorEnabled(true);
                    title_layout.setError("This title already exists !");
                }else if (s.toString().isEmpty()){
                    isValidTitle = false;
                    doneEditButton.setVisibility(View.INVISIBLE);
                    title_layout.setErrorEnabled(true);
                    title_layout.setError("The title cannot be empty !");
                }else{
                    doneEditButton.setVisibility(View.VISIBLE);
                    mTaskToBeEdited.setName(s.toString());
                }
            }
        });


        doneEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidTitle) {
                    EditText descriptionEditText = (EditText) findViewById(R.id.description_existing_task);
                    mTaskToBeEdited.setDescription(descriptionEditText.getText().toString());

                    intent.putExtra(RETURNED_EDITED_TASK, mTaskToBeEdited);
                    intent.putExtra(RETURNED_INDEX_EDITED_TASK, mIndexTaskToBeEdited);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    public void initializeToolbar(Toolbar mToolbar) {
        setSupportActionBar(mToolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public void checkIntent(Intent intent) {
        if(intent == null) {
            throw new IllegalArgumentException("No intent was passed to EditTaskActivity !");
        }

        mTaskList = intent.getParcelableArrayListExtra(TaskFragment.TASKS_LIST_KEY);
        mIndexTaskToBeEdited = intent.getIntExtra(TaskFragment.INDEX_TASK_TO_BE_EDITED_KEY, -1);
        if(mIndexTaskToBeEdited == -1 || mTaskList == null) {
            throw new IllegalArgumentException("Error on extras passed to EditTaskActivity !");
        }
        mTaskToBeEdited = mTaskList.get(mIndexTaskToBeEdited);
    }

    public void populateLayout() {
        EditText titleEditText = (EditText) findViewById(R.id.title_existing_task);
        titleEditText.setText(mTaskToBeEdited.getName());

        EditText descriptionEditText = (EditText) findViewById(R.id.description_existing_task);
        descriptionEditText.setText(mTaskToBeEdited.getDescription());
    }

    private boolean titleIsNotUnique(String title) {
        boolean result = false;
        for(int i = 0; i < mTaskList.size(); i++) {
            if(mTaskList.get(i).getName().equals(title) && i != mIndexTaskToBeEdited) {
                result = true;
            }
        }
        return result;
    }
}

