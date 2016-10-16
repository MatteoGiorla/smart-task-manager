package ch.epfl.sweng.project;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.project.data.DatabaseContract;
import ch.epfl.sweng.project.data.DatabaseHelper;

import static android.app.Activity.RESULT_OK;

/**
 * Class that represents the inflated fragment located in the activity_main
 */
public class TaskFragment extends Fragment {
    public static final String INDEX_TASK_TO_BE_EDITED_KEY = "ch.epfl.sweng.TaskFragment._INDEX_TASK_TO_BE_EDITED";
    public static final String TASKS_LIST_KEY = "ch.epfl.sweng.TaskFragment.TASKS_LIST";
    private final int editTaskRequestCode = 2;
    private TaskListAdapter mTaskAdapter;
    private ArrayList<Task> taskList;
    private DatabaseHelper mDatabase;

    /**
     * Method that adds a task in the taskList and in the database.
     *
     * @param task The task to be added
     * @throws IllegalArgumentException If the task to be added is null
     */
    public void addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException();
        }
        boolean newTaskCorrectlyInserted = mDatabase.addData(task);
        if (!newTaskCorrectlyInserted) {
            throw new SQLiteException("An error occurred while inserting " +
                    "the new task in the database");
        }
        taskList.add(task);
        mTaskAdapter.notifyDataSetChanged();
    }

    /**
     * Override the onCreate method. It initialize the database, the list of task
     * and the custom made adapter.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state,
     *                           this is the state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskList = new ArrayList<>();
        mDatabase = new DatabaseHelper(getActivity(), DatabaseContract.DATABASE_NAME);

        mTaskAdapter = new TaskListAdapter(
                getActivity(),
                R.layout.list_item_task,
                taskList
        );

        new FetchTask().execute();
    }

    /**
     * Override the onCreateView method to initialize the adapter of
     * the ListView.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment
     * @param container          Parent view that the fragment's UI should be attached to
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here
     * @return the view of the list to be displayed
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.list_view_tasks);
        listView.setAdapter(mTaskAdapter);

        registerForContextMenu(listView);

        return rootView;
    }

    /**
     * Override the onCreateContextMenu method.
     * This method creates a floating context menu.
     *
     * @param menu     The context menu that is being built.
     * @param v        The view for which the context menu is being built.
     * @param menuInfo Extra information about the item
     *                 for which the context menu should be shown
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.floating_context_menu, menu);
    }

    /**
     * Override the onContextItemSelected.
     * This method decides what to do depending of the context menu's item
     * selected by the user.
     *
     * @param item The context menu item that was selected
     * @return Return false to allow normal context menu processing to proceed,
     * true to consume it here
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo itemInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.floating_delete:
                removeTask(itemInfo);
                return true;
            case R.id.floating_edit:
                startEditTaskActivity(itemInfo);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Method called when an activity launch inside MainActivity,
     * is finished. This method is triggered only if we use
     * startActivityForResult.
     *
     * @param requestCode The integer request code supplied to startActivityForResult
     *                    used as an identifier.
     * @param resultCode  The integer result code returned by the child activity
     * @param data        An intent which can return result data to the caller.
     * @throws IllegalArgumentException if the returned extras from EditTaskActivity are
     * invalid
     * @throws SQLiteException if more that one row was changed when editing a task.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Case when we returned from the EditTaskActivity
        if (requestCode == editTaskRequestCode) {
            if (resultCode == RESULT_OK) {
                // Get result from the result intent.
                Task editedTask = data.getParcelableExtra(EditTaskActivity.RETURNED_EDITED_TASK);
                int indexEditedTask = data.getIntExtra(EditTaskActivity.RETURNED_INDEX_EDITED_TASK, -1);
                if (indexEditedTask == -1 || editedTask == null) {
                    throw new IllegalArgumentException("Invalid extras returned from EditTaskActivity !");
                } else {
                    boolean correctlyEdited = mDatabase.editTask(taskList.get(indexEditedTask), editedTask);
                    if(!correctlyEdited) {
                        throw new SQLiteException("More that was row was edited !");
                    }
                    taskList.set(indexEditedTask, editedTask);
                    mTaskAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity().getApplicationContext(),
                            editedTask.getName() + " has been updated !",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Start the EditTaskActivity for result when the user press the edit button.
     * The task index and the taskList are passed as extras to the intent.
     *
     * @param itemInfo Extra information about the item
     *                 for which the context menu should be shown
     */
    private void startEditTaskActivity(AdapterView.AdapterContextMenuInfo itemInfo) {
        int position = itemInfo.position;
        Intent intent = new Intent(getActivity(), EditTaskActivity.class);

        intent.putExtra(INDEX_TASK_TO_BE_EDITED_KEY, position);
        intent.putParcelableArrayListExtra(TASKS_LIST_KEY, taskList);

        startActivityForResult(intent, editTaskRequestCode);
    }

    /**
     * Remove a task from the database and the taskList.
     *
     * @throw an SQLiteException if an error occurred
     * @param itemInfo Extra information about the item
     *                 for which the context menu should be shown
<<<<<<< HEAD
     * @return true if removed correctly
     */

    private void removeTask(AdapterView.AdapterContextMenuInfo itemInfo) {
        int position = itemInfo.position;
        Task taskToBeDeleted = taskList.get(position);

        //Remove the task from the database
        boolean taskCorrectlyRemoved = mDatabase.removeData(taskToBeDeleted);
        if (!taskCorrectlyRemoved) {
            throw new SQLiteException("An error occurred while deleting " +
                    "the task from the database");
        }

        String taskName = taskToBeDeleted.getName();

        mTaskAdapter.remove(taskToBeDeleted);
        mTaskAdapter.notifyDataSetChanged();

        Context context = getActivity().getApplicationContext();
        String TOAST_MESSAGE = taskName + " deleted";
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, TOAST_MESSAGE, duration).show();
    }

    /**
     * Getter for the taskList
     *
     * @return an immutable copy of taskList
     */
    public List<Task> getTaskList() {
        return new ArrayList<>(taskList);
    }

    /**
     * Fetch the tasks from the database without using the UI thread.
     */
    private class FetchTask extends AsyncTask<Void, Void, Cursor> {

        /**
         * Fetch the content from the database on a background thread.
         *
         * @param params Void parameters
         * @return Cursor The tasks recovered from the database
         */
        @Override
        protected Cursor doInBackground(Void... params) {
            return mDatabase.getAllContents();
        }

        /**
         * Add the recover tasks from the database to the taskList.
         *
         * @param data The recovered tasks from the database
         */
        @Override
        protected void onPostExecute(Cursor data) {
            if (data.getCount() == 0) {
                Toast.makeText(getActivity(), "You don't have any tasks yet !", Toast.LENGTH_SHORT).show();
            } else {
                while (data.moveToNext()) {
                    String taskTitle = data.getString(data.getColumnIndex(DatabaseContract.TaskEntry.COLUMN_TASK_TITLE));
                    String taskDescription = data.getString(data.getColumnIndex(DatabaseContract.TaskEntry.COLUMN_TASK_DESCRIPTION));
                    Task newTask = new Task(taskTitle, taskDescription);
                    taskList.add(newTask);
                }
                mTaskAdapter.notifyDataSetChanged();
            }
        }
    }
}
