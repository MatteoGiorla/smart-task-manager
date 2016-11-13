package ch.epfl.sweng.project;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class which represents an activity regarding a task
 */
public abstract class TaskActivity extends AppCompatActivity {
    Intent intent;
    ArrayList<Task> taskList;
    String title;
    String description;
    long duration;
    String locationName;
    Task.Energy energy;
    List<String> listOfContributors;
    private EditText titleEditText;
    private Spinner mLocation;
    private Spinner mDuration;
    private TextInputLayout textInputLayoutTitle;
    private ImageButton doneEditButton;
    private static Button mButton;
    static Date date;
    private static final DateFormat dateFormat = DateFormat.getDateInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        //Initialize the toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.task_toolbar);
        initializeToolbar(mToolbar);

        textInputLayoutTitle = (TextInputLayout) findViewById(R.id.title_task_layout);

        //Check the validity of the intent
        intent = getIntent();
        checkIntent();
        taskList = intent.getParcelableArrayListExtra(TaskFragment.TASKS_LIST_KEY);
        checkTaskList();

        titleEditText = (EditText) findViewById(R.id.title_task);

        doneEditButton = (ImageButton) findViewById(R.id.edit_done_button_toolbar);

        //Create a listener to check that the user is writing a valid input.
        titleEditText.addTextChangedListener(new TaskTextWatcher());

        mToolbar.setNavigationOnClickListener(new ReturnArrowListener());

        doneEditButton.setOnClickListener(new OnDoneButtonClickListener());

        mButton = (Button)findViewById(R.id.pick_date);

        mLocation = (Spinner) findViewById(R.id.locationSpinner);
        mDuration = (Spinner) findViewById(R.id.durationSpinner);

        /*
         * source: http://stackoverflow.com/questions/1587028/android-configure-spinner-to-use-array
         */
        ArrayAdapter<String> spinnerDuration = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_dropdown_item, MainActivity.getDurationTable());

        mDuration.setAdapter(spinnerDuration);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_energy);
        radioGroup.check(R.id.energy_normal);

        ArrayAdapter<String> spinnerLocation = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, MainActivity.getLocationTable());

        mLocation.setAdapter(spinnerLocation);
    }

    /**
     * Check if the title written is unique or not.
     *
     * @param title The new title of the task
     * @return true if the title is already used or false otherwise.
     */
    abstract boolean titleIsNotUnique(String title);

    abstract void resultActivity();

    /**
     * Check that the intent is valid
     */
    private void checkIntent() {
        if (intent == null) {
            throw new IllegalArgumentException("No intent was passed to TaskActivity !");

        }
    }

    private void checkTaskList() {
        if (taskList == null) {
            throw new IllegalArgumentException("Error on taskList passed with the intent");
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

    /**
     * Private class that implement TextWatcher.
     * This class is used to check on runtime if the inputs written by the user
     * are valid or not.
     */
    private class TaskTextWatcher implements TextWatcher {

        /**
         * Check the input written by the user before it is changed.
         */
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            textInputLayoutTitle.setErrorEnabled(false);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        /**
         * Check the input written by the user after it was changed.
         */
        @Override
        public void afterTextChanged(Editable s) {
            if (titleIsNotUnique(s.toString())) {
                doneEditButton.setVisibility(View.INVISIBLE);
                textInputLayoutTitle.setErrorEnabled(true);
                textInputLayoutTitle.setError(getResources().getText(R.string.error_title_duplicated));
            } else if (s.toString().isEmpty()) {
                doneEditButton.setVisibility(View.INVISIBLE);
                textInputLayoutTitle.setErrorEnabled(true);
                textInputLayoutTitle.setError(getResources().getText(R.string.error_title_empty));
            } else {
                doneEditButton.setVisibility(View.VISIBLE);
                textInputLayoutTitle.setErrorEnabled(false);
            }
        }

    }

    private class OnDoneButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            title = titleEditText.getText().toString();
            if (title.isEmpty()) {
                textInputLayoutTitle.setErrorEnabled(true);
                textInputLayoutTitle.setError(getResources().getText(R.string.error_title_empty));
            } else if (!title.isEmpty() && !titleIsNotUnique(title)) {
                EditText descriptionEditText = (EditText) findViewById(R.id.description_task);
                description = descriptionEditText.getText().toString();
                locationName = mLocation.getSelectedItem().toString();
                duration = MainActivity.REVERSE_DURATION.get(mDuration.getSelectedItem().toString());

                // to set correctly the energy from the radio button
                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_energy);
                int index = radioGroup.indexOfChild(findViewById(radioGroup.getCheckedRadioButtonId()));
                switch(index) {
                    case 0:
                        energy = Task.Energy.LOW;
                        break;
                    case 1:
                        energy = Task.Energy.NORMAL;
                        break;
                    case 2:
                        energy = Task.Energy.HIGH;
                        break;
                    default:
                        energy = Task.Energy.NORMAL;
                        break;
                }

                resultActivity();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    /**
     * Class that implements OnClickListener.
     * It represents a OnClickListener on the return arrow.
     */
    private class ReturnArrowListener implements View.OnClickListener {

        /**
         * Called when the return arrow has been clicked.
         *
         * @param v The view that was clicked, the return arrow.
         */
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    /*
     * Method to hide keyboard when clicking outside the EditTextView
     *
     * source : http://stackoverflow.com/questions/4828636/edittext-clear-focus-on-touch-outside
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    public void showDatePickerDialog(View  v) {
        DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.energy_low:
                if (checked)
                    energy = Task.Energy.LOW;
                break;
            case R.id.energy_normal:
                if (checked)
                    energy = Task.Energy.NORMAL;
                break;
            case R.id.energy_high:
                if (checked)
                    energy = Task.Energy.HIGH;
                break;
            default:
                energy = Task.Energy.NORMAL;
        }
    }


    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @NonNull
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, day);
            date = cal.getTime();
            mButton.setText(dateFormat.format(date.getTime()));

        }

    }
}
