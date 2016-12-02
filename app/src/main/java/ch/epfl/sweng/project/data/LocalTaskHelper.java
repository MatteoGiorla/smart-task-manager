package ch.epfl.sweng.project.data;

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

    public LocalTaskHelper(TaskListAdapter adapter, ArrayList<Task> taskList) {
        mAdapter = adapter;
        ArrayList<Task> mTaskList = taskList;
    }

    @Override
    public void retrieveAllData(User user) {
        //Nothing to retrieve when doing tests
    }

    @Override
    public void refreshData(User user) {
        //Nothing to refresh when doing tests
    }

    @Override
    public void addNewTask(Task task, int position) {
        mAdapter.add(task, position);
        mAdapter.sort(Task.getStaticComparator());
    }

    @Override
    public void updateTask(Task original, Task updated, int position) {
        deleteTask(original, position);
        addNewTask(updated, position);
    }

    @Override
    public void deleteTask(Task task, int position) {
        mAdapter.remove(position);
        mAdapter.sort(Task.getStaticComparator());
    }
}
