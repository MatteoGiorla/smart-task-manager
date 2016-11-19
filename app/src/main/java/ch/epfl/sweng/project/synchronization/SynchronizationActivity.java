package ch.epfl.sweng.project.synchronization;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Map;

import ch.epfl.sweng.project.MainActivity;
import ch.epfl.sweng.project.User;
import ch.epfl.sweng.project.Utils;
import ch.epfl.sweng.project.data.UserProvider;

public class SynchronizationActivity extends Activity {

    private SynchronizedQueries synchronizedQueries;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String mail;
        try {
            mail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        } catch (NullPointerException e) {
            mail = User.DEFAULT_EMAIL;
        }

        User currentUser = new User(mail);

        switch (UserProvider.mProvider) {
            case UserProvider.FIREBASE_PROVIDER:
                //Get reference of the database
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                //Get reference of the user
                Query userRef = mDatabase.child("users")
                        .child(Utils.encodeMailAsFirebaseKey(currentUser.getEmail()))
                        .child("listLocations").getRef();

                //This class allows us to get all the user's data before continuing executing the app
                synchronizedQueries = new SynchronizedQueries(userRef);
                final com.google.android.gms.tasks
                        .Task<Map<Query, DataSnapshot>> readFirebaseTask = synchronizedQueries.start();
                //Listener that listen when communications with firebase end
                readFirebaseTask.addOnCompleteListener(this,
                        new UserAllOnCompleteListener(userRef, currentUser, synchronizedQueries, getApplicationContext()));
                break;

            case UserProvider.TEST_PROVIDER:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            default:
                throw new IllegalStateException("UserProvider not in FIREBASE_PROVIDER nor in TEST_PROVIDER");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(UserProvider.mProvider.equals(UserProvider.FIREBASE_PROVIDER)) {
            synchronizedQueries.stop();
        }
    }
}