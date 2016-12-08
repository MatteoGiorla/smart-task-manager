package ch.epfl.sweng.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;


public class UnfilledTasksActivity extends AppCompatActivity {

    public final static String FILLED_TASKS = "ch.epfl.sweng.Sweng.UnfilledTasksActivity.FILLED_TASKS";
    private UnfilledTaskFragment unfilledFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_unfilled_tasks);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.unfilled_tasks_toolbar);
        initializeToolbar(mToolbar);
        mToolbar.setNavigationOnClickListener(new OnReturnArrowClickListener());

        ArrayList<Task> unfilledTasks = getIntent().getParcelableArrayListExtra(MainActivity.UNFILLED_TASKS);
        //Add the user to TaskFragments
        unfilledFragment = new UnfilledTaskFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(MainActivity.UNFILLED_TASKS, unfilledTasks);
        unfilledFragment.setArguments(bundle);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.unfilled_tasks_container, unfilledFragment)
                    .commit();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        unfilledFragment.onActivityResult(requestCode,resultCode,data);
    }

    /**
     * OnClickListener on the return arrow.
     */
    private class OnReturnArrowClickListener implements View.OnClickListener {

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            Intent intent = getIntent();
            intent.putParcelableArrayListExtra(MainActivity.UNFILLED_TASKS,(ArrayList<Task>) unfilledFragment.getUnfilledTaskList());
            intent.putParcelableArrayListExtra(FILLED_TASKS,(ArrayList<Task>) unfilledFragment.getFilledTaskList());
            setResult(RESULT_OK, intent);
            finish();
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
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }
}
