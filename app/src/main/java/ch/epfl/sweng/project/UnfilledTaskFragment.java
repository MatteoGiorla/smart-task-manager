package ch.epfl.sweng.project;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.project.data.TaskProvider;

/**
 * Class that represents the inflated fragment located in the unfilled_task_activity
 */
public class UnfilledTaskFragment extends TaskFragment {

    private ArrayList<Task> unfilledTaskList;
    private ArrayList<Task> filledTaskList;
    private TaskListAdapter mTaskAdapter;
    /**
     * Override the onCreate method. It retrieves all the task of the user
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state,
     *                           this is the state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        filledTaskList = new ArrayList<>();
        unfilledTaskList = getBundle().getParcelableArrayList(MainActivity.UNFILLED_TASKS);
        if(unfilledTaskList == null) {
            throw new IllegalArgumentException("unfilledTaskList passed with the intend is null");
        }
        mTaskAdapter = new TaskListAdapter(getActivity(), unfilledTaskList);
    }

    @Override
    void setOnActivityCreated(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setEnabled(false);
    }


    @Override
    void setOnCreateView(RecyclerView recyclerView){
        recyclerView.setAdapter(mTaskAdapter);
        initSwipe();
        TaskProvider provider = new TaskProvider(getActivity(), mTaskAdapter, unfilledTaskList);
        mDatabase = provider.getTaskProvider();
        mDatabase.retrieveAllData(currentUser, true);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    void setOnSwipe(RecyclerView recyclerView, int position, int direction) {
        if (direction == ItemTouchHelper.LEFT){
            createSnackBar(position, false, recyclerView);
        } else {
            startEditTaskActivity(position);
        }
    }

    @Override
    int getIconSwipe() {
        return R.drawable.ic_mode_edit_white_48dp;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    void createSnackBar(final int position, Boolean isDone, final RecyclerView recyclerView) {
        FrameLayout layout = (FrameLayout) getActivity().findViewById(R.id.unfilled_tasks_container);
        final Task mTask = unfilledTaskList.get(position);

        Snackbar snackbar = Snackbar
                .make(layout, Utils.separateTitleAndSuffix(mTask.getName())[0] + getString(R.string.has_been_deleted), Snackbar.LENGTH_LONG)
                .setAction(R.string.undo_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mTaskAdapter.add(mTask, position);
                        recyclerView.scrollToPosition(position);
                    }
                });
        snackbar.setActionTextColor(getResources().getColor(R.color.orange_yellow, null));
        snackbar.show();
        mDatabase.deleteTask(mTask, position);
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTaskAdapter.notifyDataSetChanged();
    }

    /**
     * Method called when we return from editing a task inside EditTaskActivity.
     *
     * @param data The returned Intent of EditTaskActivity.
     */
    void onEditTaskActivityResult(Intent data) {
        // Get result from the result intent.
        Task editedTask = data.getParcelableExtra(EditTaskActivity.RETURNED_EDITED_TASK);
        int indexEditedTask = data.getIntExtra(EditTaskActivity.RETURNED_INDEX_EDITED_TASK, -1);
        if(Utils.hasContributors(editedTask) && Utils.separateTitleAndSuffix(editedTask.getName())[1].isEmpty()){
            String sharedTaskName = Utils.constructSharedTitle(editedTask.getName(), editedTask.getListOfContributors().get(0), editedTask.getListOfContributors().get(0));
            editedTask.setName(sharedTaskName);
        }
        if (indexEditedTask == -1 || editedTask == null) {
            throw new IllegalArgumentException("Invalid extras returned from EditTaskActivity !");
        } else {
            mDatabase.updateTask(unfilledTaskList.get(indexEditedTask), editedTask, indexEditedTask);
            //if the task has been fulfilled, we can put it on the temporary list of good tasks.
            if(Utils.isUnfilled(editedTask)){
                unfilledTaskList.set(indexEditedTask, editedTask);
            }else{
                unfilledTaskList.remove(indexEditedTask);
                filledTaskList.add(editedTask);
            }
            mTaskAdapter.notifyDataSetChanged();
        }
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

    /**
     * Start the EditTaskActivity for result when the user press the edit button.
     * The task index and the taskList are passed as extras to the intent.
     *
     * @param position Position of the task in the list.
     */
    private void startEditTaskActivity(int position) {
        Intent intent = new Intent(getActivity(), EditTaskActivity.class);

        intent.putExtra(INDEX_TASK_TO_BE_EDITED_KEY, position);
        intent.putParcelableArrayListExtra(TASKS_LIST_KEY, unfilledTaskList);

        startActivityForResult(intent, EDIT_TASK_REQUEST_CODE);
    }
}
