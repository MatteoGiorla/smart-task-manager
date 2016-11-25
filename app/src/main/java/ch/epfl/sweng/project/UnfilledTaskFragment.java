package ch.epfl.sweng.project;


import android.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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

import ch.epfl.sweng.project.information.TaskInformationActivity;

import static android.app.Activity.RESULT_OK;
import static ch.epfl.sweng.project.information.TaskInformationActivity.TASK_IS_DELETED;
import static ch.epfl.sweng.project.information.TaskInformationActivity.TASK_IS_MODIFIED;
import static ch.epfl.sweng.project.information.TaskInformationActivity.TASK_STATUS_KEY;

/**
 * Class that represents the inflated fragment located in the activity_main
 */
public class UnfilledTaskFragment extends Fragment {
    public static final String INDEX_TASK_TO_BE_EDITED_KEY = "ch.epfl.sweng.UnfilledTaskFragment.INDEX_TASK_TO_BE_EDITED";
    public static final String TASKS_LIST_KEY = "ch.epfl.sweng.UnfilledTaskFragment.TASKS_LIST";
    public static final String INDEX_TASK_TO_BE_DISPLAYED = "ch.epfl.sweng.UnfilledTaskFragment.INDEX_TASK_TO_BE_DISPLAYED";
    private final int editTaskRequestCode = 2;
    private final int displayTaskRequestCode = 3;


    private TaskListAdapter mTaskAdapter;
    private ArrayList<Task> unfilledTaskList;
    private ArrayList<Task> filledTaskList;

    /**
     * Override the onCreate method. It retrieves all the task of the user
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state,
     *                           this is the state
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();

        filledTaskList = new ArrayList<>();
        unfilledTaskList = bundle.getParcelableArrayList(MainActivity.UNFILLED_TASKS);
        mTaskAdapter = new TaskListAdapter(
                getActivity(),
                R.layout.list_unfilled_task,
                unfilledTaskList
        );
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), TaskInformationActivity.class);
                intent.putExtra(INDEX_TASK_TO_BE_DISPLAYED, position);
                intent.putParcelableArrayListExtra(TASKS_LIST_KEY, unfilledTaskList);
                startActivityForResult(intent, displayTaskRequestCode);
            }
        });

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
            case R.id.floating_task_delete:
                removeTask(itemInfo, false);
                return true;
            case R.id.floating_task_edit:
                startEditTaskActivity(itemInfo);
                return true;
            case R.id.floating_task_done:
                removeTask(itemInfo, true);
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
     *                                  invalid
     * @throws SQLiteException          if more that one row was changed when editing a task.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Case when we returned from the EditTaskActivity
        if (requestCode == editTaskRequestCode && resultCode == RESULT_OK) {
            onEditTaskActivityResult(data);
        } else if (requestCode == displayTaskRequestCode && resultCode == RESULT_OK) {
            int taskStatus = data.getIntExtra(TASK_STATUS_KEY, -1);
            if (taskStatus == -1)
                throw new IllegalArgumentException("Error with the intent form TaskInformationActivity");

            switch (taskStatus) {
                case TASK_IS_MODIFIED:
                    onEditTaskActivityResult(data);
                    break;
                case TASK_IS_DELETED:
                    int taskIndex = data.getIntExtra(TaskInformationActivity.TASK_TO_BE_DELETED_INDEX, -1);
                    if (taskIndex == -1) {
                        throw new IllegalArgumentException("Error with the task to be deleted index");
                    }
                    unfilledTaskList.remove(taskIndex);
            }
        }
        //sortTaskStatically();
    }

    /**
     * Method called when we return from editing a task inside EditTaskActivity.
     *
     * @param data The returned Intent of EditTaskActivity.
     */
    private void onEditTaskActivityResult(Intent data) {
        // Get result from the result intent.
        Task editedTask = data.getParcelableExtra(EditTaskActivity.RETURNED_EDITED_TASK);
        int indexEditedTask = data.getIntExtra(EditTaskActivity.RETURNED_INDEX_EDITED_TASK, -1);
        if (indexEditedTask == -1 || editedTask == null) {
            throw new IllegalArgumentException("Invalid extras returned from EditTaskActivity !");
        } else {
            if(TaskActivity.isUnfilled(editedTask)){
                unfilledTaskList.set(indexEditedTask, editedTask);
            }else{
                unfilledTaskList.remove(indexEditedTask);
                filledTaskList.add(editedTask);
            }
            mTaskAdapter.notifyDataSetChanged();
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
        intent.putParcelableArrayListExtra(TASKS_LIST_KEY, unfilledTaskList);

        startActivityForResult(intent, editTaskRequestCode);
    }

    /**
     * Remove a task from the taskList of unfilled.
     *
     * @param itemInfo Extra information about the item
     *                 for which the context menu should be shown
     * @param isDone   true if the task is done, otherwise false.
     * @throws SQLiteException if an error occurred
     */
    private void removeTask(AdapterView.AdapterContextMenuInfo itemInfo, Boolean isDone) {
        int position = itemInfo.position;
        unfilledTaskList.remove(position);
    }

    /**
     * Private method executing the actions needed to remove the task.
     * It removes the task from the database.
     *
     * @param position Position of the task to be removed.
     * @param isDone   Boolean indicating if the task is done.
     */
   /* private void removeTaskAction(int position, Boolean isDone) {
        Task taskToBeDeleted = unfilledTaskList.get(position);

        String taskName = taskToBeDeleted.getName();

        mDatabase.deleteTask(taskToBeDeleted);

        Context context = getActivity().getApplicationContext();
        String TOAST_MESSAGE;
        if (isDone) {
            TOAST_MESSAGE = taskName + getString(R.string.info_done);
        } else {
            TOAST_MESSAGE = taskName + getString(R.string.info_deleted);
        }
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, TOAST_MESSAGE, duration).show();
    }*/

    /**
     * Getter for the taskList of unfilled tasks
     *
     * @return an immutable copy of taskList
     */
    public List<Task> getUnfilledTaskList() {
        if(unfilledTaskList != null){
            return new ArrayList<>(unfilledTaskList);
        }else{
            return null;
        }
    }

    /**
     * Getter for the taskList of task that have been filled
     *
     * @return an immutable copy of taskList
     */
    public List<Task> getDoneTaskList() {
        if(unfilledTaskList != null){
            return new ArrayList<>(unfilledTaskList);
        }else{
            return null;
        }
    }
}
