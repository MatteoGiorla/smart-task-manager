package ch.epfl.sweng.project;

import android.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.project.information.TaskInformationActivity;

import static android.app.Activity.RESULT_OK;
import static ch.epfl.sweng.project.information.TaskInformationActivity.TASK_IS_DELETED;
import static ch.epfl.sweng.project.information.TaskInformationActivity.TASK_IS_MODIFIED;
import static ch.epfl.sweng.project.information.TaskInformationActivity.TASK_STATUS_KEY;

/**
 * Class that represents the inflated fragment located in the unfilled_task_activity
 */
public class UnfilledTaskFragment extends Fragment {
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
        mTaskAdapter = new TaskListAdapter(getActivity(), unfilledTaskList);
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

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.list_view_tasks);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mTaskAdapter);

        registerForContextMenu(recyclerView);

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
                int position = itemInfo.position;
                unfilledTaskList.remove(position);
                mTaskAdapter.notifyDataSetChanged();
                return true;
            case R.id.floating_task_edit:
                startEditTaskActivity(itemInfo);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Method called when an activity launch inside UnfilledTaskActivity,
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
        mTaskAdapter.notifyDataSetChanged();
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
            //if the task has been fulfilled, we can put it on the temporary list of good tasks.
            if(Utils.isUnfilled(editedTask, this.getActivity().getApplicationContext())){
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

        intent.putExtra(TaskFragment.INDEX_TASK_TO_BE_EDITED_KEY, position);
        intent.putParcelableArrayListExtra(TaskFragment.TASKS_LIST_KEY, unfilledTaskList);

        startActivityForResult(intent, editTaskRequestCode);
    }

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
    public List<Task> getFilledTaskList() {
        if(unfilledTaskList != null){
            return new ArrayList<>(filledTaskList);
        }else{
            return null;
        }
    }
}
