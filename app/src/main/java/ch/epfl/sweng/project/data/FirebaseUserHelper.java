package ch.epfl.sweng.project.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.project.Location;
import ch.epfl.sweng.project.User;
import ch.epfl.sweng.project.Utils;

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
    public User retrieveUserInformation() {
        String mail;
        try {
            mail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        } catch (NullPointerException e) {
            mail = User.DEFAULT_EMAIL;
        }
        User currentUser = new User(mail);
        recoverUserLocations(currentUser);
        return currentUser;
    }

    /**
     * Recover the locations set by the user when he
     * first log in.
     *
     * @param user The current user of the app
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
