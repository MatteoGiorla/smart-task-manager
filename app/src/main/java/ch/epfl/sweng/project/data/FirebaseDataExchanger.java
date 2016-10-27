package ch.epfl.sweng.project.data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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

/**
 * This class is the exchanger between firebase and the app
 * It deals with all necessary operations that must be done
 * on the database.
 */
public class FirebaseDataExchanger implements DataExchanger {

    private final DatabaseReference mDatabase;
    private final TaskListAdapter mAdapter;
    private final ArrayList<Task> mTaskList;
    private final Context mContext;

    /**
     * Only constructor of FirebaseDataExchanger for the moment*
     */
    public FirebaseDataExchanger(Context context, TaskListAdapter adapter, ArrayList<Task> taskList) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAdapter = adapter;
        mTaskList = taskList;
        mContext = context;
    }

    /**
     * Encode a given mail to be compatible with keys in firebase
     *
     * @param mail The user email
     * @return The encoded email
     */
    private String encodeMailAsFirebaseKey(String mail) {
        return mail.replace('.', ' ');
    }

    @Override
    public User retrieveUserInformation() {
        String mail;
        try {
            mail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        } catch (NullPointerException e) {
            mail = User.DEFAULT_EMAIL;
        }
        Log.e("mail id", mail);
        User user = new User(mail);
        recoverUserLocations(user);
        return user;
    }

    @Override
    public void retrieveAllData(User user) {
        Query myTasks = mDatabase.child("tasks").child(encodeMailAsFirebaseKey(user.getEmail())).getRef();

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
                        Long durationInMinutes = (Long) task.child("duration").getValue();
                        String energy = (String) task.child("energy").getValue();
                        List<String> contributors = (List<String>) task.child("listOfContributors").getValue();

                        //Construct Location object
                        String name = (String) task.child("location").child("name").getValue();
                        String type = (String) task.child("location").child("type").getValue();
                        Double latitude = task.child("location").child("latitude").getValue(Double.class);
                        Double longitude = task.child("location").child("longitude").getValue(Double.class);
                        Location location = new Location(name, type, latitude, longitude);

                        //Construct the date
                        Long date = (Long) task.child("dueDate").child("date").getValue();
                        Date dueDate = new Date(date);

                        Task newTask = new Task(title, description, location, dueDate, durationInMinutes, energy, contributors);
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
            DatabaseReference taskRef = mDatabase.child("tasks").child(encodeMailAsFirebaseKey(mail)).child(task.getName()).getRef();
            taskRef.setValue(task);
        }
        mTaskList.add(task);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteTask(Task task) {
        for (String mail : task.getListOfContributors()) {
            DatabaseReference taskRef = mDatabase.child("tasks").child(encodeMailAsFirebaseKey(mail)).child(task.getName()).getRef();
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

    @Override
    public void addUser(final User user) {
        DatabaseReference userRef = mDatabase.child("users").child(encodeMailAsFirebaseKey(user.getEmail())).getRef();
        userRef.setValue(user);
    }

    @Override
    public void updateUser(User user) {
        deleteUser(user);
        addUser(user);
    }

    /**
     * Deleter a user in the database
     *
     * @param user The user to be deleted
     */
    private void deleteUser(User user) {
        DatabaseReference userRef = mDatabase.child("users").child(encodeMailAsFirebaseKey(user.getEmail())).getRef();
        userRef.removeValue();
    }

    /**
     * Recover the locations set by the user when he
     * first sign in.
     *
     * @param user The user
     */
    private void recoverUserLocations(final User user) {
        DatabaseReference userRef = mDatabase.child("users").child(encodeMailAsFirebaseKey(user.getEmail())).child("listLocations").getRef();
        final List<Location> listLocations = new ArrayList<>();

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String name = (String) data.child("name").getValue();
                    String type = (String) data.child("type").getValue();
                    Double latitude = data.child("latitude").getValue(Double.class);
                    Double longitude = data.child("longitude").getValue(Double.class);
                    //Create location
                    Location location = new Location(name, type, latitude, longitude);
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
