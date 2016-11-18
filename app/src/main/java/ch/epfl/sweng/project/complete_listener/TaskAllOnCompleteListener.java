package ch.epfl.sweng.project.complete_listener;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.project.MainActivity;
import ch.epfl.sweng.project.Task;
import ch.epfl.sweng.project.User;

public class TaskAllOnCompleteListener implements OnCompleteListener<Map<Query, DataSnapshot>> {
    public static final String TASK_LIST_KEY =
            "ch.epfl.sweng.project.complete_listener.TaskAllOnCompleteListener.TASK_LIST_KEY";

    private ArrayList<Task> taskList;
    private User currentUser;
    private Context synchronizationActivityContext;

    TaskAllOnCompleteListener(@NonNull User currentUser, @NonNull Context synchronizationActivityContext) {
        super();
        taskList = new ArrayList<>();
        this.currentUser = currentUser;
        this.synchronizationActivityContext = synchronizationActivityContext;
    }
    @Override
    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Map<Query, DataSnapshot>> task) {
        if(task.isSuccessful()) {
            final Map<Query, DataSnapshot> taskResult = task.getResult();
            DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
            final Query myTasks = mDatabaseRef.child("tasks");
            retrieveTask(taskResult.get(myTasks).getChildren());
            launchNextActivity();
        } else {
            try {
                task.getException();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void retrieveTask(Iterable<DataSnapshot> snapshots) {
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
                Long startDuration = (Long) data.child("startDuration").getValue();

                Task newTask = new Task(title, description, locationName,
                        dueDate, durationInMinutes, energy, contributors, startDuration);
                taskList.add(newTask);
            }
        }
    }

    private void launchNextActivity() {
        if(currentUser == null) Log.e("in complete", "user null in complete");
        if(taskList == null) Log.e("in complete", "list of task null in complete");
        Intent intent = new Intent(synchronizationActivityContext, MainActivity.class);
        intent.putExtra(UserAllOnCompleteListener.CURRENT_USER_KEY, currentUser);
        intent.putParcelableArrayListExtra(TASK_LIST_KEY, taskList);
        synchronizationActivityContext.startActivity(intent);
    }
}
