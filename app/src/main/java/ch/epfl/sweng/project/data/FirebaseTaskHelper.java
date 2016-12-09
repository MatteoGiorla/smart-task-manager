package ch.epfl.sweng.project.data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import static ch.epfl.sweng.project.Utils.separateTitleAndSuffix;


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
                String[] suffix = Utils.getCreatorAndSharer(Utils.separateTitleAndSuffix(toAdd.getName())[1]);
                if(suffix[0].equals(suffix[1])){
                    toAdd.setIfNewContributor(0L);
                }else{
                    toAdd.setIfNewContributor(1L);
                }
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
                    updatedTask.setIfNewContributor(1L);
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
     *
     *
     * @param mTaskList
     */
    private void warnContributor(List<Task> mTaskList) {
        List<Task> taskAddedAsContributor = new ArrayList<Task>();

        // search task that has been added by someone else:
        for (Task t : mTaskList) {
            if (t.getIfNewContributor() == 1L ) {
                taskAddedAsContributor.add(t);
            }
        }

        if (!taskAddedAsContributor.isEmpty()) {
            // DIALOG
            // Build the Dialog:
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

            builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });

            if (taskAddedAsContributor.size() != 1) {
                builder.setMessage(mContext.getString(R.string.added_as_contributor_on_multiple_tasks)+ taskAddedAsContributor.size()
                        + mContext.getString(R.string.ending_of_added_as_contributor_on_multiple_tasks));
            } else {
                builder.setMessage(mContext.getString(R.string.added_as_contributor_on_one_task)
                        + separateTitleAndSuffix(taskAddedAsContributor.get(0).getName())[0]
                        + mContext.getString(R.string.ending_of_added_as_contributor_on_one_text));
            }

            // Set IfNewContributor on Firebase to 0:
            for (Task t : taskAddedAsContributor) {
                if (mTaskList.indexOf(t) >= 0) {
                    updateTask(t, t.setIfNewContributor(0L), mTaskList.indexOf(t));
                }
            }

            // Create the Dialog:
            AlertDialog dialog = builder.create();
            dialog.show();

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
                long newContributor;
                if(task.child("ifNewContributor").getValue() != null){
                     newContributor  = (long) task.child("ifNewContributor").getValue();
                } else {
                    newContributor = 0;
                }
                //Task newTask = new Task(title, description, locationName, dueDate, durationInMinutes, energy, contributors, newContributor);

                //Define a GenericTypeIndicator to get back properly typed collection
                GenericTypeIndicator<List<Message>> messageListTypeIndicator = new GenericTypeIndicator<List<Message>>() {};
                //Construct list of message
                List<Message> listOfMessages = task.child("listOfMessages").getValue(messageListTypeIndicator);
                Task newTask;

                if(listOfMessages == null) {
                    newTask = new Task(title, description, locationName, dueDate, durationInMinutes, energy, contributors, newContributor);
                }else{
                    newTask = new Task(title, description, locationName, dueDate, durationInMinutes, energy, contributors, newContributor, listOfMessages);
                }

                mTaskList.add(newTask);
            }
        }

        mAdapter.notifyDataSetChanged();
        // Manage the dialog that warn the user that he has been added to a task:
        warnContributor(mTaskList);
        MainActivity.triggerDynamicSort();
    }
}
