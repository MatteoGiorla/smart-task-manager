package ch.epfl.sweng.project.data;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.project.Location;
import ch.epfl.sweng.project.Task;
import ch.epfl.sweng.project.TaskListAdapter;
import ch.epfl.sweng.project.User;
import ch.epfl.sweng.project.Utils;

/**
 * This class is the exchanger between firebase and the app
 * It deals with all necessary operations that must be done
 * on the database.
 */
public class FirebaseDataExchanger implements DataExchanger {

    private static final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private final TaskListAdapter mAdapter;
    private final ArrayList<Task> mTaskList;
    private final Context mContext;

    /**
     * Only constructor of FirebaseDataExchanger for the moment*
     */
    public FirebaseDataExchanger(Context context, TaskListAdapter adapter, ArrayList<Task> taskList) {
        mAdapter = adapter;
        mTaskList = taskList;
        mContext = context;
    }

    @Override
    public User retrieveUserInformation(User user) {
        recoverUserLocations(user);
        return user;
    }

    @Override
    public void retrieveAllData(User user) {
        Query myTasks = mDatabase.child("tasks").child(Utils.encodeMailAsFirebaseKey(user.getEmail())).getRef();

        myTasks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mTaskList.isEmpty() && dataSnapshot.getChildrenCount() == 0) {
                    Toast.makeText(mContext, "You don't have any tasks !", Toast.LENGTH_SHORT).show();
                }
                mTaskList.clear();
                for (DataSnapshot task : dataSnapshot.getChildren()) {
                    if (task != null) {
                        String title = (String) task.child("name").getValue();
                        String description = (String) task.child("description").getValue();
                        Long durationInMinutes = (Long) task.child("durationInMinutes").getValue();
                        String energy = (String) task.child("energy").getValue();
                        List<String> contributors = (List<String>) task.child("listOfContributors").getValue();

                        //Construct Location object
                        String locationName = (String) task.child("locationName").getValue();
                        //Construct the date
                        Long date = (Long) task.child("dueDate").child("time").getValue();
                        Date dueDate = new Date(date);

                        Task newTask = new Task(title, description, locationName, dueDate, durationInMinutes, energy, contributors);
                        mTaskList.add(newTask);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void addNewTask(Task task) {
        for (String mail : task.getListOfContributors()) {
            DatabaseReference taskRef = mDatabase.child("tasks").child(Utils.encodeMailAsFirebaseKey(mail)).child(task.getName()).getRef();
            taskRef.setValue(task);
        }
        mTaskList.add(task);
        mAdapter.notifyDataSetChanged();
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

    /**
     * Recover the locations set by the user when he
     * first sign in.
     *
     * @param user The user
     */
    private void recoverUserLocations(final User user) {
        DatabaseReference userRef = mDatabase.child("users").child(Utils.encodeMailAsFirebaseKey(user.getEmail())).child("listLocations").getRef();
        final List<Location> listLocations = new ArrayList<>();

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String name = (String) data.child("name").getValue();
                    Double latitude = data.child("latitude").getValue(Double.class);
                    Double longitude = data.child("longitude").getValue(Double.class);
                    //Create location
                    Location location = new Location(name, latitude, longitude);
                    listLocations.add(location);
                }
                user.setListLocations(listLocations);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
