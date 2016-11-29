package ch.epfl.sweng.project.data;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;

import ch.epfl.sweng.project.Task;
import ch.epfl.sweng.project.TaskListAdapter;
import ch.epfl.sweng.project.User;

/**
 * Local proxy that behave as firebase but does not require
 * any internet connection.
 * Mostly use to mock firebase in order to run tests faster.
 */
public class LocalTaskHelper implements TaskHelper {

    private final TaskListAdapter mAdapter;
    private final ArrayList<Task> mTaskList;

    public LocalTaskHelper(TaskListAdapter adapter, ArrayList<Task> taskList) {
        mAdapter = adapter;
        mTaskList = taskList;
    }

    @Override
    public void retrieveAllData(User user) {
        //Nothing to retrieve when doing tests
    }

    @Override
    public void refreshData(User user) {
        //Nothing to refresh when doing tests
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void addNewTask(Task task) {
        mTaskList.add(task);
        mAdapter.sort(Task.getStaticComparator());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void updateTask(Task original, Task updated) {
        deleteTask(original);
        addNewTask(updated);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void deleteTask(Task task) {
        mAdapter.remove(task);
        mAdapter.sort(Task.getStaticComparator());
    }
}
