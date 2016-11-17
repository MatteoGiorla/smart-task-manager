package ch.epfl.sweng.project.data;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.project.Location;
import ch.epfl.sweng.project.User;

/**
 * Proxy that does all the work between the app and the firebase real time database.
 * It allows the user to fetch his predefined favorite locations from the
 * database.
 *
 * Note: The queries are done asynchronously
 */
public class FirebaseUserHelper implements UserHelper{

    private final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    public User retrieveUserInformation(User currentUser, Iterable<DataSnapshot> snapshots) {
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
        return currentUser;
    }
}
