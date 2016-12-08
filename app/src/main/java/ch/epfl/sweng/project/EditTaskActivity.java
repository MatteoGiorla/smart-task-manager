package ch.epfl.sweng.project;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.Arrays;

import ch.epfl.sweng.project.chat.ChatActivity;

import static ch.epfl.sweng.project.chat.ChatActivity.TASK_CHAT_KEY;

/**
 * Class that represents the inflated activity_task under the edit case
 */
public class EditTaskActivity extends TaskActivity {
    public static final int TASK_IS_DELETED = 1;
    public static final int TASK_IS_MODIFIED = 2;
    public static final String RETURNED_EDITED_TASK = "ch.epfl.sweng.EditTaskActivity.EDITED_TASK";
    public static final String RETURNED_INDEX_EDITED_TASK = "ch.epfl.sweng.EditTaskActivity.RETURNED_INDEX_EDITED_TASK";
    public static final String TASK_STATUS_KEY = "ch.epfl.sweng.EditTaskActivity.TASK_STATUS_KEY";
    public static final String TASK_TO_BE_DELETED_INDEX = "ch.epfl.sweng.EditTaskActivity.TASK_TO_BE_DELETED_INDEX";

    private Task mTaskToBeEdited;
    private int mIndexTaskToBeEdited;
    private int taskStatus;

    private static final int TASK_DUE_DATE = 3;
    private static final int TASK_DURATION = 4;
    private static final int TASK_LOCATION = 5;

    /**
     * Override the onCreate method
     * Recover the task to be edited and update it, then it puts the
     * edited task in the intent.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down then this Bundle contains the data it
     *                           most recently supplied in onSaveInstanceState(Bundle)
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get the index and check its validity
        mIndexTaskToBeEdited = intent.getIntExtra(TaskFragment.INDEX_TASK_TO_BE_EDITED_KEY, -1);
        checkTaskToBeEditedIndex();

        //Get the task to be edited
        mTaskToBeEdited = taskList.get(mIndexTaskToBeEdited);

        date = mTaskToBeEdited.getDueDate();
        energy = mTaskToBeEdited.getEnergy();
        duration = mTaskToBeEdited.getDuration();

        final Calendar c = Calendar.getInstance();
        c.setTime(date);

        setSwitchers();

        initialisationFields();
        //Populate the layout activity_task
        populateTextViewInformation();

        populateLayout();

        getDoneEditButton().setVisibility(View.GONE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_task_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.trash_menu :
                taskStatus = TASK_IS_DELETED;
                setResultIntent();
                finish();
                return true;

            case R.id.chat_menu :
                Intent intentToChat = new Intent(this, ChatActivity.class);
                intentToChat.putExtra(TASK_CHAT_KEY, mTaskToBeEdited);
                startActivity(intentToChat);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Check if the title written is unique or not.
     *
     * @param title The new title of the task
     * @return true if the title is already used or false otherwise.
     */
    @Override
    boolean titleIsNotUnique(String title) {
        boolean result = false;
        for (Task task : taskList) {
            if (task.getName().equals(title) && !task.getName().equals(mTaskToBeEdited.getName())) {
                result = true;
            }
        }
        return result;
    }

    /**
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    void resultActivity() {
        Log.e("duration editActivity", "duration to be set : " + duration);
        mTaskToBeEdited.setName(title);
        mTaskToBeEdited.setDescription(description);
        mTaskToBeEdited.setDueDate(date);
        mTaskToBeEdited.setDurationInMinutes(duration);
        mTaskToBeEdited.setLocationName(locationName);
        mTaskToBeEdited.setEnergyNeeded(energy);
        setResultIntent();
    }

    /**
     * Set the result intent.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setResultIntent() {
        if(taskStatus == TASK_IS_MODIFIED) {
            allEditViewToReadView();
            populateTextViewInformation();
            getDoneEditButton().setVisibility(View.GONE);
            intent.putExtra(TASK_STATUS_KEY, taskStatus);
            intent.putExtra(EditTaskActivity.RETURNED_EDITED_TASK, mTaskToBeEdited);
            intent.putExtra(EditTaskActivity.RETURNED_INDEX_EDITED_TASK, mIndexTaskToBeEdited);
            setResult(RESULT_OK, intent);
        } else if(taskStatus == TASK_IS_DELETED) {
            intent.putExtra(TASK_STATUS_KEY, taskStatus);
            intent.putExtra(TASK_TO_BE_DELETED_INDEX, mIndexTaskToBeEdited);
            setResult(RESULT_OK, intent);
        }
    }

    /**
     *
     */
    private void setSwitchers() {
        //Set switch on name
        setSwitcherOnClick((LinearLayout) findViewById(R.id.nameLinearLayout),
                (ViewSwitcher) findViewById(R.id.switcher_name),
                findViewById(R.id.title_task));

        //Set switch on date
        setSwitcherOnClick((LinearLayout) findViewById(R.id.dateLinearLayout),
                (ViewSwitcher) findViewById(R.id.switcher_date),
                findViewById(R.id.pick_date));

        //Set switch on duration
        setSwitcherOnClick((LinearLayout) findViewById(R.id.durationLinearLayout),
                (ViewSwitcher) findViewById(R.id.switcher_duration),
                findViewById(R.id.durationSpinner));

        //Set switch on location
        setSwitcherOnClick((LinearLayout) findViewById(R.id.locationLinearLayout),
                (ViewSwitcher) findViewById(R.id.switcher_location),
                findViewById(R.id.locationSpinner));

        //Set switch on energy
        setSwitcherOnClick((LinearLayout) findViewById(R.id.energyLinearLayout),
                (ViewSwitcher) findViewById(R.id.switcher_energy),
                findViewById(R.id.radio_energy));

        //Set switch on description
        setSwitcherOnClick((LinearLayout) findViewById(R.id.descriptionLinearLayout),
                (ViewSwitcher) findViewById(R.id.switcher_description),
                findViewById(R.id.description_task));
    }

    private void setSwitcherOnClick(LinearLayout container, final ViewSwitcher switcher, final View secondView) {
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switcher.getCurrentView() != secondView) {
                    getDoneEditButton().setVisibility(View.VISIBLE);
                    taskStatus = TASK_IS_MODIFIED;
                    switcher.showNext();
                }
            }
        });
    }

    private void allEditViewToReadView() {
        switchToReadView((ViewSwitcher) findViewById(R.id.switcher_name), findViewById(R.id.text_name));
        switchToReadView((ViewSwitcher) findViewById(R.id.switcher_date), findViewById(R.id.text_date));
        switchToReadView((ViewSwitcher) findViewById(R.id.switcher_duration), findViewById(R.id.text_duration));
        switchToReadView((ViewSwitcher) findViewById(R.id.switcher_location), findViewById(R.id.text_location));
        switchToReadView((ViewSwitcher) findViewById(R.id.switcher_energy), findViewById(R.id.text_energy));
        switchToReadView((ViewSwitcher) findViewById(R.id.switcher_description), findViewById(R.id.text_description));
    }

    private void switchToReadView(final ViewSwitcher switcher, final View firstView) {
        if(switcher.getCurrentView() != firstView) {
            switcher.showPrevious();
        }
    }

    /**
     * Check that the 'task to be edited' 's index is valid
     *
     * @throws IllegalArgumentException If there is an error with the intent passed
     *                                  to the activity.
     */
    private void checkTaskToBeEditedIndex() {
        if (mIndexTaskToBeEdited == -1) {
            throw new IllegalArgumentException("Error on the index passed with the intent !");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initialisationFields() {
        title = mTaskToBeEdited.getName();
        energy = mTaskToBeEdited.getEnergy();
        description = mTaskToBeEdited.getDescription();
        date = mTaskToBeEdited.getDueDate();
        locationName = mTaskToBeEdited.getLocationName();
        duration  = mTaskToBeEdited.getDurationInMinutes();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void populateTextViewInformation() {
        TextView nameTextView = (TextView) findViewById(R.id.text_name);
        nameTextView.setText(title);

        TextView dateTextView = (TextView) findViewById(R.id.text_date);
        dateTextView.setText(safeTaskInformationGetter(TASK_DUE_DATE, mTaskToBeEdited));

        TextView durationTextView = (TextView) findViewById(R.id.text_duration);
        durationTextView.setText(safeTaskInformationGetter(TASK_DURATION, mTaskToBeEdited));

        TextView locationTextView = (TextView) findViewById(R.id.text_location);
        locationTextView.setText(safeTaskInformationGetter(TASK_LOCATION, mTaskToBeEdited));

        TextView energyTextView = (TextView) findViewById(R.id.text_energy);
        energyTextView.setText(MainActivity.ENERGY_MAP.get(energy.ordinal()));

        TextView descriptionTextView = (TextView) findViewById(R.id.text_description);
        descriptionTextView.setText(description);
    }

    /**
     * Fill the layout with the old values of the task to be edited.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void populateLayout() {
        EditText titleEditText = (EditText) findViewById(R.id.title_task);
        titleEditText.setText(title);
        titleEditText.setSelection(titleEditText.getText().length()); //put cursor at the end

        Button mButton = (Button) findViewById(R.id.pick_date);
        mButton.setText(safeTaskInformationGetter(TASK_DUE_DATE, mTaskToBeEdited));

        EditText descriptionEditText = (EditText) findViewById(R.id.description_task);
        descriptionEditText.setText(description);
        descriptionEditText.setSelection(descriptionEditText.getText().length()); //put cursor at the end

        Spinner durationSpinner = (Spinner) findViewById(R.id.durationSpinner);
        populateSpinner(durationSpinner, MainActivity.getDurationTable(), safeTaskInformationGetter(TASK_DURATION, mTaskToBeEdited));

        Spinner locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        populateSpinner(locationSpinner, MainActivity.getLocationTable(), safeTaskInformationGetter(TASK_LOCATION, mTaskToBeEdited));

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_energy);

        // check the right radio button for the energy
        switch(mTaskToBeEdited.getEnergy()) {
            case LOW:
                radioGroup.check(R.id.energy_low);
                break;
            case NORMAL:
                radioGroup.check(R.id.energy_normal);
                break;
            case HIGH:
                radioGroup.check(R.id.energy_high);
                break;
            default:
                radioGroup.check(R.id.energy_normal);
                break;
        }
    }

    private void populateSpinner(Spinner spinner, String[] nameList, String defaultItemName) {
        int position = Arrays.asList(nameList).indexOf(defaultItemName);
        spinner.setSelection(position);
    }

    /**
     * According to which information we're looking at, will retrieve the correct task's information
     * or if the task is unfilled, will give back a message to display on the taskInformationActivity
     * noting this fact. IT is "safe" because it prevents default internal values from appearing
     * on the taskInformationActivity.
     *
     * @param reqCode identifier to decide which information is needed
     * @param task the task to retrieve the information and then transform it to String
     *
     * @return the String corresponding to the information requested
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private String safeTaskInformationGetter(int reqCode, Task task){
        String result;
        switch(reqCode){
            case TASK_DUE_DATE:
                if(Utils.isDueDateUnfilled(task)){
                    result = getString(R.string.enter_due_date_hint);
                }else{
                    result = dateFormat.format(date.getTime());
                }
                return result;
            case TASK_DURATION:
                String duration_text = MainActivity.DURATION_MAP.get(duration.intValue());
                if(Utils.isDurationUnfilled(task)){
                    result = getString(R.string.unfilled_duration);
                }else{
                    result = String.valueOf(duration_text);
                }
                return result;
            case TASK_LOCATION:
                if(Utils.isLocationUnfilled(task, getApplicationContext())){
                    result = getString(R.string.unfilled_location);
                }else{
                    result = locationName;
                }
                return result;
            default:
                throw new IllegalArgumentException("An incorrect code was passed to safely retrieve the task's information.");
        }
    }
}
