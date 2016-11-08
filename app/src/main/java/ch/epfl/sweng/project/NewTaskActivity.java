package ch.epfl.sweng.project;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;

/**
 * Class that represents the inflated activity_new_task
 */
public class NewTaskActivity extends TaskActivity {
    public static final String RETURNED_TASK = "ch.epfl.sweng.NewTaskActivity.NEW_TASK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date = new Date();
        energy = Task.Energy.NORMAL;

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
            if (taskList.get(i).getName().equals(title)) {
                result = true;
            }
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    void resultActivity() {

        //prepare contributors
        listOfContributors = new ArrayList<>();
        String contributor;

        try {
            contributor = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        } catch (NullPointerException e) {
            contributor = User.DEFAULT_EMAIL;
        }

        listOfContributors.add(contributor);
        Task newTask = new Task(title, description, locationName, date, duration, energy.toString(), listOfContributors);

        intent.putExtra(RETURNED_TASK, newTask);
    }
}
