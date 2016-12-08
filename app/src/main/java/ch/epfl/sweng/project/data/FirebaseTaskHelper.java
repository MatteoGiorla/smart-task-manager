package ch.epfl.sweng.project.data;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.project.MainActivity;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.Task;
import ch.epfl.sweng.project.TaskListAdapter;
import ch.epfl.sweng.project.User;
import ch.epfl.sweng.project.Utils;
import ch.epfl.sweng.project.chat.Message;

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
    public void retrieveAllData(User user) {
        Query myTasks = mDatabase.child("tasks").child(Utils.encodeMailAsFirebaseKey(user.getEmail())).getRef();

        myTasks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                retrieveTasks(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void refreshData(User user) {
        Query myTasks = mDatabase.child("tasks").child(Utils.encodeMailAsFirebaseKey(user.getEmail())).getRef();

        myTasks.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                retrieveTasks(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void addNewTask(Task task, int position) {
        for (String mail : task.getListOfContributors()) {
            DatabaseReference taskRef = mDatabase.child("tasks").child(Utils.encodeMailAsFirebaseKey(mail)).child(task.getName()).getRef();
            taskRef.setValue(task);
        }
        mAdapter.add(task, position);
    }

    @Override
    public void deleteTask(Task task, int position) {
        for (String mail : task.getListOfContributors()) {
            DatabaseReference taskRef = mDatabase.child("tasks").child(Utils.encodeMailAsFirebaseKey(mail)).child(task.getName()).getRef();
            taskRef.removeValue();
        }
        mAdapter.remove(position);
    }

    @Override
    public void updateTask(Task original, Task updated, int position) {
        deleteTask(original, position);
        addNewTask(updated, position);
    }

    /**
     * Reconstruct the tasks form the DataSnapshot given.
     *
     * @param dataSnapshot Data recovered from the database
     */
    private void retrieveTasks(DataSnapshot dataSnapshot) {
        if (mTaskList.isEmpty() && dataSnapshot.getChildrenCount() == 0) {
            Toast.makeText(mContext, mContext.getText(R.string.info_any_tasks), Toast.LENGTH_SHORT).show();
        }
        mTaskList.clear();
        for (DataSnapshot task : dataSnapshot.getChildren()) {
            if (task != null) {
                String title = task.child("name").getValue(String.class);
                String description = task.child("description").getValue(String.class);
                Long durationInMinutes = task.child("durationInMinutes").getValue(Long.class);
                String energy = task.child("energy").getValue(String.class);

                //Define a GenericTypeIndicator to get back properly typed collection
                GenericTypeIndicator<List<String>> stringListTypeIndicator =
                        new GenericTypeIndicator<List<String>>() {};
                List<String> contributors = task.child("listOfContributors").getValue(stringListTypeIndicator);

                //Construct Location object
                String locationName = task.child("locationName").getValue(String.class);
                //Construct the date
                Long date = task.child("dueDate").child("time").getValue(Long.class);
                Date dueDate = new Date(date);

                //Define a GenericTypeIndicator to get back properly typed collection
                GenericTypeIndicator<List<Message>> messageListTypeIndicator = new GenericTypeIndicator<List<Message>>() {};
                //Construct list of message
                List<Message> listOfMessages = task.child("listOfMessages").getValue(messageListTypeIndicator);
                Task newTask;

                if(listOfMessages == null) {
                    newTask = new Task(title, description, locationName, dueDate, durationInMinutes, energy, contributors);
                }else{
                    newTask = new Task(title, description, locationName, dueDate, durationInMinutes, energy, contributors, listOfMessages);
                }
                mTaskList.add(newTask);
            }
        }
        mAdapter.notifyDataSetChanged();
        MainActivity.triggerDynamicSort();
    }
}