package ch.epfl.sweng.project;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.project.data.TaskHelper;
import ch.epfl.sweng.project.data.TaskProvider;
import ch.epfl.sweng.project.notification.TaskNotification;


/**
 * Class that represents the inflated fragment located in the activity_main
 */
public class FilledTaskFragment extends TaskFragment {

    private static TaskListAdapter mTaskAdapter;
    private static ArrayList<Task> taskList;
    private static TaskHelper mDatabase;

    /**
     * Override the onCreate method. It retrieves all the task of the user
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state,
     *                           this is the state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        taskList = new ArrayList<>();
        mTaskAdapter = new TaskListAdapter(getActivity(), taskList);

        new TaskNotification(taskList, getActivity()).execute(taskList.size(), taskList.size());
    }

    @Override
    void setOnActivityCreated(final SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                mDatabase.refreshData(currentUser, false);
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }, 2500);
            }
        });

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), android.R.color.holo_blue_light),
                ContextCompat.getColor(getActivity(), android.R.color.holo_green_light),
                ContextCompat.getColor(getActivity(), android.R.color.holo_orange_light));
    }

    @Override
    void setOnCreateView(RecyclerView recyclerView) {
        recyclerView.setAdapter(mTaskAdapter);
        initSwipe();

        TaskProvider provider = new TaskProvider(getActivity(), mTaskAdapter, taskList);
        mDatabase = provider.getTaskProvider();
        mDatabase.retrieveAllData(currentUser, false);
    }

    @Override
    int getIconSwipe() {
        return R.drawable.ic_done_white_36dp;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    void createSnackBar(final int position, Boolean isDone, final RecyclerView recyclerView) {
        FloatingActionButton add_button = (FloatingActionButton) getActivity().findViewById(R.id.add_task_button);

        final Task mTask = taskList.get(position);
        String title;
        if(isDone) {
            title = Utils.separateTitleAndSuffix(mTask.getName())[0] + getString(R.string.has_been_done);
        }else{
            title = Utils.separateTitleAndSuffix(mTask.getName())[0] + getString(R.string.has_been_deleted);
        }

        Snackbar snackbar = Snackbar
                .make(add_button, title, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDatabase.addNewTask(mTask, position, false);
                        recyclerView.scrollToPosition(position);
                    }
                });

        snackbar.setActionTextColor(getResources().getColor(R.color.orange_yellow, null));
        snackbar.show();
        mDatabase.deleteTask(mTask, position);
    }

    /**
     * Method called when we return from editing a task inside EditTaskActivity.
     *
     * @param data The returned Intent of EditTaskActivity.
     */
    @Override
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
            mDatabase.updateTask(taskList.get(indexEditedTask), editedTask, indexEditedTask);
            //taskList.set(indexEditedTask, editedTask);
            mTaskAdapter.notifyDataSetChanged();
            Toast.makeText(getActivity().getApplicationContext(),
                    Utils.separateTitleAndSuffix(editedTask.getName())[0] + getString(R.string.info_updated),
                    Toast.LENGTH_SHORT).show();

            //Create a notification
            new TaskNotification(taskList, getActivity()).execute(taskList.size(), taskList.size());
        }
    }

    /**
     * Private method executing the actions needed to remove the task.
     * It removes the task from the database.
     *
     * @param position Position of the task to be removed.
     * @param isDone   Boolean indicating if the task is done.
     */
    @Override
    void removeTaskAction(int position, Boolean isDone) {
        Task taskToBeDeleted = taskList.get(position);

        mDatabase.deleteTask(taskToBeDeleted, position);

        //Update notifications
        new TaskNotification(taskList, getActivity()).execute(taskList.size() + 1, taskList.size());
    }

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
        mDatabase.addNewTask(task, taskList.size(), false);

        //Update notifications
        new TaskNotification(taskList, getActivity()).createUniqueNotification(taskList.size() - 1);
    }

    /**
     * Method that launch the dynamic sort on the tasks.
     *
     * @param currentLocation     User's current location
     * @param currentTimeDisposal User's current disposal time
     */
    public void sortTasksDynamically(String currentLocation, int currentTimeDisposal, String everywhere_location, String select_one_location) {
        mTaskAdapter.sort(Task.getDynamicComparator(currentLocation, currentTimeDisposal, everywhere_location, select_one_location));
    }

    /**
     * Getter for the taskList
     *
     * @return an immutable copy of taskList
     */
    public List<Task> getTaskList() {
        if(taskList != null){
            return new ArrayList<>(taskList);
        }else{
            return null;
        }
    }

    public static void modifyLocationInTaskList(Location editedLocation, Location newLocation) {
        //To avoid concurrent modification
        ArrayList<Task> newTaskList = new ArrayList<>();
        ArrayList<Task> previousTaskList = new ArrayList<>();
        ArrayList<Integer> taskPosition = new ArrayList<>();
        for(int i = 0; i < taskList.size(); ++i) {
            Task task = taskList.get(i);
            if (task.getLocationName().equals(editedLocation.getName())) {
                Task previousTask = new Task(task.getName(), task.getDescription(), task.getLocationName(), task.getDueDate(),
                        task.getDurationInMinutes(), task.getEnergy().toString(), task.getListOfContributors(), task.getIfNewContributor());
                Task newTask = new Task(task.getName(), task.getDescription(), newLocation.getName(), task.getDueDate(),
                        task.getDurationInMinutes(), task.getEnergy().toString(), task.getListOfContributors(), task.getIfNewContributor());
                newTaskList.add(newTask);
                previousTaskList.add(previousTask);
                taskPosition.add(i);
            }
        }
        for(int i = 0; i < newTaskList.size(); ++i) {
            mDatabase.updateTask(previousTaskList.get(i), newTaskList.get(i), taskPosition.get(i));
            mTaskAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Take care of adding an unfilled task in the firebase when newTaskActivity returns
     * an unfilled task.
     *
     * @param task the unfilled task to add in the database.
     */
    public void addUnfilled(Task task){
        mDatabase.addNewTask(task, 0, true);
    }

    public static boolean locationIsUsedByTask(Location locationToCheck) {
        for(Task task : taskList) {
            if (task.getLocationName().equals(locationToCheck.getName())){
                return true;
            }
        }
        return false;
    }
}
