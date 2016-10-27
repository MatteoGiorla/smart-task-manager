package ch.epfl.sweng.project;

import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.EditText;

import java.util.Date;

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
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getName().equals(title) && i != mIndexTaskToBeEdited) {
                result = true;
            }
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    void resultActivity() {
        mTaskToBeEdited.setName(title);
        mTaskToBeEdited.setDescription(description);

        //prepare date
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, taskYear);
        cal.set(Calendar.MONTH, taskMonth);
        cal.set(Calendar.DAY_OF_MONTH, taskDay);
        Date dateRepresentation = cal.getTime();

        mTaskToBeEdited.setDueDate(dateRepresentation);
        mTaskToBeEdited.setDurationInMinutes(duration);
        mTaskToBeEdited.setLocation(new Location(location, Location.LocationType.HOME.toString(), 0, 0));
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
    }
}
