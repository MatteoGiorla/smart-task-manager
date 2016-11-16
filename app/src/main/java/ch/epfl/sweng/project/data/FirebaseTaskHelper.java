package ch.epfl.sweng.project.data;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.project.Task;
import ch.epfl.sweng.project.TaskListAdapter;
import ch.epfl.sweng.project.User;
import ch.epfl.sweng.project.Utils;

/**
 * Proxy that does all the work between the app and the firebase real time database.
 * It allows the user to fetch task from the database, he can also remove/edit or
 * add tasks in the database through this interface.
 *
 * Note: The queries are done asynchronously
 */
public class FirebaseTaskHelper implements TaskHelper {

    private final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private final TaskListAdapter mAdapter;
    private final ArrayList<Task> mTaskList;
    private final Context mContext;


    public FirebaseTaskHelper(Context context, TaskListAdapter adapter, ArrayList<Task> taskList) {
        mAdapter = adapter;
        mTaskList = taskList;
        mContext = context;
    }

    @Override
    public void retrieveAllData(User user, Iterable<DataSnapshot> snapshots) {
        for (DataSnapshot data : snapshots) {
            if (data != null) {
                String title = (String) data.child("name").getValue();
                String description = (String) data.child("description").getValue();
                Long durationInMinutes = (Long) data.child("durationInMinutes").getValue();
                String energy = (String) data.child("energy").getValue();
                List<String> contributors = (List<String>) data.child("listOfContributors").getValue();

                //Construct Location object
                String locationName = (String) data.child("locationName").getValue();
                //Construct the date
                Long date = (Long) data.child("dueDate").child("time").getValue();
                Date dueDate = new Date(date);

                Task newTask = new Task(title, description, locationName, dueDate, durationInMinutes, energy, contributors);
                mTaskList.add(newTask);
            }
        }
    }

    @Override
    public void addNewTask(Task task) {
        for (String mail : task.getListOfContributors()) {
            DatabaseReference taskRef = mDatabase.child("tasks").child(Utils.encodeMailAsFirebaseKey(mail)).child(task.getName()).getRef();
            taskRef.setValue(task);
        }
        mTaskList.add(task);
        mAdapter.sort(Task.getStaticComparator());
    }

    @Override
    public void deleteTask(Task task) {
        for (String mail : task.getListOfContributors()) {
            DatabaseReference taskRef = mDatabase.child("tasks").child(Utils.encodeMailAsFirebaseKey(mail)).child(task.getName()).getRef();
            taskRef.removeValue();
        }
        mAdapter.remove(task);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateTask(Task original, Task updated) {
        deleteTask(original);
        addNewTask(updated);
    }
}
