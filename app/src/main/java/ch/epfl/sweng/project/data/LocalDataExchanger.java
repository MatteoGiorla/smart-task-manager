package ch.epfl.sweng.project.data;

import java.util.ArrayList;

import ch.epfl.sweng.project.Task;
import ch.epfl.sweng.project.TaskListAdapter;
import ch.epfl.sweng.project.User;


public class LocalDataExchanger implements DataExchanger {

    private final TaskListAdapter mAdapter;
    private final ArrayList<Task> mTaskList;
    private User mUser;

    public LocalDataExchanger(TaskListAdapter adapter, ArrayList<Task> taskList) {
        mAdapter = adapter;
        mTaskList = taskList;
    }

    @Override
    public void retrieveAllData(User user) {
        //Nothing to retrieve when doing tests
    }

    @Override
    public User retrieveUserInformation(User user) {
        mUser = user;
        return mUser;
    }

    @Override
    public void addNewTask(Task task) {
        mTaskList.add(task);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateTask(Task original, Task updated) {
        deleteTask(original);
        addNewTask(updated);
    }

    @Override
    public void deleteTask(Task task) {
        mAdapter.remove(task);
        mAdapter.notifyDataSetChanged();
    }
}
