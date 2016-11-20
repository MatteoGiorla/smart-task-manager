package ch.epfl.sweng.project.synchronization;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.project.Location;
import ch.epfl.sweng.project.MainActivity;
import ch.epfl.sweng.project.User;


public class UserAllOnCompleteListener implements OnCompleteListener<Map<Query, DataSnapshot>> {
    public static final String CURRENT_USER_KEY =
            "ch.epfl.sweng.project.complete_listener.UserAllOnCompleteListener.CURRENT_USER_KEY";

    private Query userRef;
    private User currentUser;
    private Context synchronizationActivityContext;

    public UserAllOnCompleteListener(@NonNull Query userRef, @NonNull User currentUser, @NonNull SynchronizedQueries synchronizedQueries,
                                     @NonNull Context synchronizationActivityContext) {
        super();
        this.userRef = userRef;
        this.currentUser = currentUser;
        SynchronizedQueries synchronizedQueries1 = synchronizedQueries;
        this.synchronizationActivityContext = synchronizationActivityContext;
    }

    @Override
    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Map<Query, DataSnapshot>> task) {
        if (task.isSuccessful()) {
            final Map<Query, DataSnapshot> UserResult = task.getResult();
            retrieveUserInformation(UserResult.get(userRef).getChildren());
//            retrieveUserTask();
            launchNextActivity();
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

    private void launchNextActivity() {
        Intent intent = new Intent(synchronizationActivityContext, MainActivity.class);
        intent.putExtra(UserAllOnCompleteListener.CURRENT_USER_KEY, currentUser);
        synchronizationActivityContext.startActivity(intent);
    }
}
