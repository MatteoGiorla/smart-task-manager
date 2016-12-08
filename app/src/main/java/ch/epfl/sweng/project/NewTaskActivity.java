package ch.epfl.sweng.project;

import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * Class that represents the inflated activity_new_task
 */
public class NewTaskActivity extends TaskActivity {
    public static final String RETURNED_NEW_TASK = "ch.epfl.sweng.NewTaskActivity.NEW_TASK";


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set default values
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1899);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        date = cal.getTime();

        energy = Task.Energy.NORMAL;

        //prepare contributors
        listOfContributors = new ArrayList<>();
        String contributor;

        try {
            contributor = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        } catch (NullPointerException e) {
            contributor = User.DEFAULT_EMAIL;
        }

        listOfContributors.add(contributor);
        editContributorButton.setVisibility(View.GONE);

        contributorsListTextView = (TextView) findViewById(R.id.text_contributors);
        setContributorsTextView();

        getEditableView();
    }

    @Override
    void addContributorInTask(String contributor){
        listOfContributors.add(contributor);
    }

    @Override
    void deleteContributorInTask(String contributor){
        listOfContributors.remove(contributor);
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
            if (task.getName().equals(title)) {
                result = true;
            }
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    void resultActivity() {


        //TODO : it is here that the creation of the suffix need to happen.
        Task newTask = new Task(title[0], description, locationName, date, duration, energy.toString(), listOfContributors);
        intent.putExtra(RETURNED_NEW_TASK, newTask);

        if(Utils.isUnfilled(newTask, this.getApplicationContext())){
            intent.putExtra(IS_UNFILLED, true);
        }else{
            intent.putExtra(IS_UNFILLED, false);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    private void getEditableView() {
        ((ViewSwitcher) findViewById(R.id.switcher_name)).showNext();
        ((ViewSwitcher) findViewById(R.id.switcher_description)).showNext();
        ((ViewSwitcher) findViewById(R.id.switcher_energy)).showNext();
        ((ViewSwitcher) findViewById(R.id.switcher_location)).showNext();
        ((ViewSwitcher) findViewById(R.id.switcher_duration)).showNext();
        ((ViewSwitcher) findViewById(R.id.switcher_date)).showNext();
    }
}
