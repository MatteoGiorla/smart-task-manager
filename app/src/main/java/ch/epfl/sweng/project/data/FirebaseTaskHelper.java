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

import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.Task;
import ch.epfl.sweng.project.TaskListAdapter;
import ch.epfl.sweng.project.User;
import ch.epfl.sweng.project.Utils;
import ch.epfl.sweng.project.location_setting.LocationSettingActivity;

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
        Task toAdd = task;
        if(Utils.hasContributors(task)){
            //in this case, it is the duty of newTask/editTaskActivity
            //to give a title with the correct format (title@@email@@email
            for (String mail : task.getListOfContributors()) {
                toAdd = Utils.sharedTaskPreProcessing(task, mail);
                DatabaseReference taskRef = mDatabase.child("tasks").child(Utils.encodeMailAsFirebaseKey(mail)).child(toAdd.getName()).getRef();
                taskRef.setValue(toAdd);
            }
        }else{
            DatabaseReference taskRef = mDatabase.child("tasks").child(Utils.encodeMailAsFirebaseKey(task.getListOfContributors().get(0))).child(task.getName()).getRef();
            taskRef.setValue(toAdd);
        }
        mAdapter.add(toAdd, position);
    }


    @Override
    public void deleteTask(Task task, int position) {
        if(Utils.hasContributors(task)){
            for (String mail : task.getListOfContributors()) {
                String taskTitle = Utils.sharedTaskPreProcessing(task, mail).getName();
                DatabaseReference taskRef = mDatabase.child("tasks").child(Utils.encodeMailAsFirebaseKey(mail)).child(taskTitle).getRef();
                taskRef.removeValue();
            }
        }else{
            DatabaseReference taskRef = mDatabase.child("tasks").child(Utils.encodeMailAsFirebaseKey(task.getListOfContributors().get(0))).child(task.getName()).getRef();
            taskRef.removeValue();
        }
        mAdapter.remove(position);
    }

    @Override
    public void updateTask(final Task original, final Task updated, int position) {
        if(Utils.hasContributors(original)){
            /**
             * This for loop takes care of updating the tasks according to the new
             * contributor list of the updated task.
             * If a new contributor has been added, it will
             * add the updated task to this new contributor.
             */
            for(final String mail : updated.getListOfContributors()){
                if(original.getListOfContributors().contains(mail)){

                    String oldName =  Utils.sharedTaskPreProcessing(original, mail).getName();

                    Task updatedTask = Utils.sharedTaskPreProcessing(updated, mail);
                    //removing the old task
                    DatabaseReference taskRef = mDatabase.child("tasks").child(Utils.encodeMailAsFirebaseKey(mail)).child(oldName).getRef();
                    taskRef.removeValue();

                    //adding the new task.
                    DatabaseReference taskReference = mDatabase.child("tasks").child(Utils.encodeMailAsFirebaseKey(mail)).child(updatedTask.getName()).getRef();
                    taskReference.setValue(updatedTask);

                }else{
                    Task updatedTask = Utils.sharedTaskPreProcessing(updated, mail);
                    DatabaseReference taskRef = mDatabase.child("tasks").child(Utils.encodeMailAsFirebaseKey(mail)).child(updatedTask.getName()).getRef();
                    taskRef.setValue(updatedTask);
                }
            }
            /**
             * Now this for loop takes care of removing the task from the contributor that have been
             * removed from the shared task.
             */
            for(final String mail : original.getListOfContributors()){
                if(!updated.getListOfContributors().contains(mail)){
                    String oldName =  Utils.sharedTaskPreProcessing(original, mail).getName();
                    //removing the old task
                    DatabaseReference taskRef = mDatabase.child("tasks").child(Utils.encodeMailAsFirebaseKey(mail)).child(oldName).getRef();
                    taskRef.removeValue();
                }
            }

            mAdapter.remove(position);
            mAdapter.add(updated, position);

        }else{
            //in either the case of no contributors, or no contributors previously but contributors
            //now, we can simply delete then add the task.
            deleteTask(original, position);
            addNewTask(updated, position);
        }
    }


    /**
     * Reconstruct the tasks from the DataSnapshot given.
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
                long newContributor;
                if(task.child("ifNewContributor").getValue() != null){
                     newContributor  = (long) task.child("ifNewContributor").getValue();
                }else{
                    newContributor = 0;
                }
                Task newTask = new Task(title, description, locationName, dueDate, durationInMinutes, energy, contributors, newContributor);
                mTaskList.add(newTask);
            }
        }
        mAdapter.notifyDataSetChanged();
    }
}