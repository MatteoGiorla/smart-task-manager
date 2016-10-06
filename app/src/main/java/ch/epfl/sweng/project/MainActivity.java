package ch.epfl.sweng.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

public final class MainActivity extends AppCompatActivity {

    private final int newTaskRequestCode = 1;
    private TaskFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment = new TaskFragment();

        if(savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.tasks_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /* This method is called when the user click on the add button*/
    public void openNewTaskActivity(View v) {
        Intent intent = new Intent(this, NewTaskActivity.class);
        startActivityForResult(intent, newTaskRequestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == newTaskRequestCode) {
            if (resultCode == RESULT_OK) {
                // Get result from the result intent.
                Task newTask = data.getParcelableExtra(NewTaskActivity.returnedTask);
                // Add element to the listTask
                fragment.addTask(newTask);
            }
        }
    }
}