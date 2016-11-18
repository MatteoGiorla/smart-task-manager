package ch.epfl.sweng.project.complete_listener;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.project.Location;
import ch.epfl.sweng.project.SynchronizedQueries;
import ch.epfl.sweng.project.User;
import ch.epfl.sweng.project.Utils;


public class UserAllOnCompleteListener implements OnCompleteListener<Map<Query, DataSnapshot>> {
    public static final String CURRENT_USER_KEY =
            "ch.epfl.sweng.project.complete_listener.UserAllOnCompleteListener.CURRENT_USER_KEY";

    private Query userRef;
    private User currentUser;
    private SynchronizedQueries synchronizedQueries;
    private Context synchronizationActivityContext;

    public UserAllOnCompleteListener(@NonNull Query userRef, @NonNull User currentUser, @NonNull SynchronizedQueries synchronizedQueries,
                                     @NonNull Context synchronizationActivityContext) {
        super();
        this.userRef = userRef;
        this.currentUser = currentUser;
        this.synchronizedQueries = synchronizedQueries;
        this.synchronizationActivityContext = synchronizationActivityContext;
    }

    @Override
    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Map<Query, DataSnapshot>> task) {
        if (task.isSuccessful()) {
            final Map<Query, DataSnapshot> UserResult = task.getResult();
            retrieveUserInformation(UserResult.get(userRef).getChildren());
            retrieveUserTask();
        } else {
            try {
                task.getException();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void retrieveUserInformation(Iterable<DataSnapshot> snapshots) {
        List<Location> listLocations = new ArrayList<>();
        //Construct each user's location
        for (DataSnapshot data : snapshots) {
            String name = (String) data.child("name").getValue();
            Double latitude = data.child("latitude").getValue(Double.class);
            Double longitude = data.child("longitude").getValue(Double.class);
            //Create location
            Location location = new Location(name, latitude, longitude);
            //Add the location to the list
            listLocations.add(location);
        }
        //Set the list with the user's location list
        currentUser.setListLocations(listLocations);
    }

    private void retrieveUserTask() {
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        //Get the taskList reference
        final Query myTasks = mDatabaseRef.child("tasks")
                .child(Utils.encodeMailAsFirebaseKey(currentUser.getEmail())).getRef();

        synchronizedQueries = new SynchronizedQueries(myTasks);
        final com.google.android.gms.tasks
                .Task<Map<Query, DataSnapshot>> readFirebaseTask = synchronizedQueries.start();

        readFirebaseTask
                .addOnCompleteListener(new TaskAllOnCompleteListener(currentUser, synchronizationActivityContext, myTasks));
    }
}
