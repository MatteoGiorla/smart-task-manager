package ch.epfl.sweng.project;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.Arrays;

/**
 * Class that represents the inflated activity_task under the edit case
 */
public class EditTaskActivity extends TaskActivity {
    public static final String RETURNED_EDITED_TASK = "ch.epfl.sweng.EditTaskActivity.EDITED_TASK";
    public static final String RETURNED_INDEX_EDITED_TASK = "ch.epfl.sweng.EditTaskActivity.RETURNED_INDEX_EDITED_TASK";
    private Task mTaskToBeEdited;
    private int mIndexTaskToBeEdited;

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

        //Get the index and check its validity
        mIndexTaskToBeEdited = intent.getIntExtra(TaskFragment.INDEX_TASK_TO_BE_EDITED_KEY, -1);
        checkTaskToBeEditedIndex();

        //Get the task to be edited
        mTaskToBeEdited = taskList.get(mIndexTaskToBeEdited);

        date = mTaskToBeEdited.getDueDate();
        energy = mTaskToBeEdited.getEnergy();
        duration = mTaskToBeEdited.getDuration();

        //Populate the layout activity_task
        populateLayout();
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

    @Override
    void resultActivity() {
        mTaskToBeEdited.setName(title);
        mTaskToBeEdited.setDescription(description);
        mTaskToBeEdited.setDueDate(date);
        mTaskToBeEdited.setDurationInMinutes(duration);
        mTaskToBeEdited.setLocationName(locationName);
        mTaskToBeEdited.setEnergyNeeded(energy);
        intent.putExtra(RETURNED_EDITED_TASK, mTaskToBeEdited);
        intent.putExtra(RETURNED_INDEX_EDITED_TASK, mIndexTaskToBeEdited);
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

    /**
     * Fill the layout with the old values of the task to be edited.
     */
    private void populateLayout() {
        EditText titleEditText = (EditText) findViewById(R.id.title_task);
        titleEditText.setText(mTaskToBeEdited.getName());

        EditText descriptionEditText = (EditText) findViewById(R.id.description_task);
        descriptionEditText.setText(mTaskToBeEdited.getDescription());

        Spinner durationSpinner = (Spinner) findViewById(R.id.durationSpinner);
        Long duration = mTaskToBeEdited.getDuration();
        populateSpinner(durationSpinner, MainActivity.getDurationTable(),
                MainActivity.DURATION_MAP.get(duration.intValue()));

        Spinner locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        populateSpinner(locationSpinner, MainActivity.getLocationTable(),
                mTaskToBeEdited.getLocationName());

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
                radioGroup.check(R.id.energy_high  );
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
}
